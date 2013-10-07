package com.bridgecanada.prismatic.search;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.net.PersistentCookieStore;
import com.bridgecanada.prismatic.data.SearchActivity;
import com.bridgecanada.prismatic.data.SearchFeed;
import com.bridgecanada.prismatic.data.SearchResults;
import com.bridgecanada.prismatic.data.SearchTopic;
import com.bridgecanada.testhelper.JettyLauncher;
import com.bridgecanada.testhelper.MockHelper;
import com.jayway.awaitility.Duration;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * User: bridge
 * Date: 06/10/13
 */
@RunWith(RobolectricTestRunner.class)
public class SearcherTest {

    private final String TAG = getClass().getSimpleName();

    //private com.bridgecanada.prismatic.data.PrismaticFeed _feed;

    SearchResults _searchResults = null;
    private JettyLauncher _jettyLauncher;

    SearchFailure _errorResult = null;

    private PersistentCookieStore _cookieStore;

    boolean _interceptingHttpRequests;
    //private String _apiKey = "1.0";

    @Before
    public void  setUp() throws Exception{
        System.out.println("Turning off requests");
        _searchResults = null;
        _interceptingHttpRequests = Robolectric.getFakeHttpLayer().isInterceptingHttpRequests();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);

        _cookieStore = MockHelper.MockPersistentCookieStore();
        //_successResult = null;
        //_failureResult = null;
        _jettyLauncher = new JettyLauncher();
        _jettyLauncher.addHandler("/search/explore", TestSearchResultServlet.class);
        _jettyLauncher.start();
    }

    @After
    public void tearDown() throws Exception {
        _jettyLauncher.stop();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(_interceptingHttpRequests);
    }


    @Test
    public void testSearchFindsTenOfEachResult() throws Exception {

        // Arrange
        SimulateSuccessfulSearch("test");

        // Assert
        assertThat(_searchResults, notNullValue());
        assertThat(_searchResults.getActivities(), notNullValue());
        assertThat(_searchResults.getActivities().size(), equalTo(10));
        assertThat(_searchResults.getFeeds(), notNullValue());
        assertThat(_searchResults.getFeeds().size(), equalTo(10));
        assertThat(_searchResults.getTopics(), notNullValue());
        assertThat(_searchResults.getTopics().size(), equalTo(10));
    }

    @Test
    public void testSearchParsesActivities() throws Exception {

        // Arrange
        SimulateSuccessfulSearch("test");

        // Act
        ArrayList<SearchActivity> activities = _searchResults.getActivities();
        SearchActivity firstResult = activities.get(0);

        // Assert
        assertThat(firstResult.getDisplayKey(), equalTo( "test"));
        assertThat(firstResult.getImg(), equalTo( "http://cdn.getprismatic.com/cdn/img/profile/default_avatar1.jpg"));
        assertThat(firstResult.getKey(), equalTo( 39924l ));
        assertThat(firstResult.getName(), equalTo("test"));
        assertThat(firstResult.getSource().getType(), equalTo("search"));
        assertThat(firstResult.getTitle(), equalTo("test"));
        assertThat(firstResult.getType(), equalTo("activity"));

    }

    @Test
    public void testSearchParsesFeeds() throws Exception {

        // Arrange
        SimulateSuccessfulSearch("test");

        // Act
        ArrayList<SearchFeed> feeds = _searchResults.getFeeds();
        SearchFeed firstResult = feeds.get(0);

        // Assert
        assertThat(firstResult.getImg(), equalTo( "http://imagecdn.getprismatic.com/pub/16/1189285808811592695.ico"));
        assertThat(firstResult.getKey(), equalTo( "http://forexblog.oanda.com/20131004/usdjpy-yen-tests-97-as-boj-stands-pat-on-stimulus/OANDA Forex Blog/feed/"));
        assertThat(firstResult.getSource().getType(), equalTo("search"));
        assertThat(firstResult.getTitle(), equalTo("OANDA Forex Blog"));
        assertThat(firstResult.getType(), equalTo("feed"));

    }

    @Test
    public void testSearchParsesTopics() throws Exception {

        // Arrange
        SimulateSuccessfulSearch("test");

        // Act
        ArrayList<SearchTopic> feeds = _searchResults.getTopics();
        SearchTopic firstResult = feeds.get(0);

        // Assert
        assertThat(firstResult.getKey(), equalTo( "Tests and Testing"));
        assertThat(firstResult.getSource().getType(), equalTo("search"));
        assertThat(firstResult.getType(), equalTo("topic"));

    }

    private void SimulateSuccessfulSearch(String searchString) {
        ISearcher searcher = new Searcher(_cookieStore, _jettyLauncher.getBasePath(), "Tester 0.123" );
        Robolectric.getBackgroundScheduler().pause();
        searcher.addSeachResultListener(new TestResultListener());

        // Act
        //searcher.AsyncSearch("test", getSuccessFeedCallback(), getFailureFeedCallback());
        searcher.AsyncSearch(searchString);

        Robolectric.getBackgroundScheduler().runOneTask();
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());
    }

    private  class TestResultListener implements ISearchResultListener {
        @Override
        public void HandleSearchResult(String searchString, SearchResults results) {
            _searchResults = results;
        }
    }
    private Callable<Boolean> resultIsNotNull() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return _searchResults != null || _errorResult != null;
            }
        };
    }

    public static class TestSearchResultServlet extends HttpServlet
    {


        private final String _contentType = "application/json";

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

            System.out.println("CALLING DOGET");
            response.setContentType(_contentType);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(TEST_DATA);
        }

        public static final String TEST_DATA = "{\"activity\":[{\"source\":{\"type\":\"search\"},\"display-key\":\"test\",\"key\":39924,\"name\":\"test\",\"title\":\"test\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar1.jpg\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"fast_test\",\"key\":20736,\"name\":\"Test\",\"title\":\"fast_test\",\"img\":\"http://a0.twimg.com/sticky/default_profile_images/default_profile_0_bigger.png\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"Test24642\",\"key\":47077,\"name\":\"Test\",\"title\":\"Test24642\",\"img\":\"http://a0.twimg.com/sticky/default_profile_images/default_profile_6_bigger.png\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"Tester22\",\"key\":143106,\"name\":\"Anton Johansson\",\"title\":\"Tester22\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar2.jpg\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"testabeta33\",\"key\":173223,\"name\":\"testabeta33\",\"title\":\"testabeta33\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar3.jpg\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"test1\",\"key\":59110,\"name\":\"test1\",\"title\":\"test1\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar3.jpg\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"branchtesthost\",\"key\":7035,\"name\":\"cg tester\",\"title\":\"branchtesthost\",\"img\":\"http://a0.twimg.com/sticky/default_profile_images/default_profile_6_bigger.png\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"ruslantester\",\"key\":14157,\"name\":\"Ruslan Tester\",\"title\":\"ruslantester\",\"img\":\"http://a0.twimg.com/sticky/default_profile_images/default_profile_6_bigger.png\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"prismtester\",\"key\":29770,\"name\":\"prism tester\",\"title\":\"prismtester\",\"img\":\"http://a0.twimg.com/sticky/default_profile_images/default_profile_6_bigger.png\",\"type\":\"activity\"},{\"source\":{\"type\":\"search\"},\"display-key\":\"tester\",\"key\":42428,\"name\":\"Masood Nusratty\",\"title\":\"tester\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar3.jpg\",\"type\":\"activity\"}],\"feed\":[{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://forexblog.oanda.com/20131004/usdjpy-yen-tests-97-as-boj-stands-pat-on-stimulus/OANDA Forex Blog/feed/\",\"title\":\"OANDA Forex Blog\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/1189285808811592695.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://www.androidpit.com/feed/main.xml\",\"title\":\"Android News + App Tests - AndroidPIT\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/6921570278936618797.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"www.mysticsoftwares.com\",\"title\":\"Mystic Softwares, Free, Tarot Reading Software, Runes Reading Software, Psychic Test Software, Numerology Software, Biorhythm Chart Software, Astro Compatibility Test Software, I Ching Software\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/8601574051728582512.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"www.test2.vino-con-vista.us\",\"title\":\"Vino Con Vista\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/-3050511993079895682.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://www.testudotimes.com/rss/current\",\"title\":\"Testudo Times\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/1864357234137347086.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://www.testosteronepit.com/home/atom.xml\",\"title\":\"Testosterone Pit\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/-3924789939852094729.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://www.tested.com/feeds/\",\"title\":\"Tested\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/-8239996732236243880.png\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://testastic.wordpress.com/feed/\",\"title\":\"Testastic\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/-2987084774176192226.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://www.multitestingmommy.com/feeds/posts/default\",\"title\":\"Multi-Testing Mommy\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/7855486825490661032.ico\"},{\"source\":{\"type\":\"search\"},\"type\":\"feed\",\"key\":\"http://www.marketingexperiments.com/blog/feed\",\"title\":\"MarketingExperiments Blog: Research-driven optimization, testing, and marketing ideas\",\"img\":\"http://imagecdn.getprismatic.com/pub/16/-895177068661043192.ico\"}],\"topic\":[{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Tests and Testing\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Testosterone\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Nuclear Tests\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"North Korea Missile Tests\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Testicle\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Testicular cancer\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Testament (band)\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Testicular torsion\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Test cricket\"},{\"source\":{\"type\":\"search\"},\"type\":\"topic\",\"key\":\"Test (assessment)\"}]}";

    }


}

