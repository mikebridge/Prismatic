package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.net.PersistentCookieStore;
import com.bridgecanada.prismatic.data.Doc;
import com.bridgecanada.prismatic.data.Img;
import com.bridgecanada.testhelper.JettyLauncher;
import com.bridgecanada.testhelper.MockHelper;
import com.jayway.awaitility.Duration;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: bridge
 * Date: 21/04/13
 */
@RunWith(RobolectricTestRunner.class)
public class PrismaticFeedTest {


    private final String TAG = getClass().getSimpleName();

    private com.bridgecanada.prismatic.data.PrismaticFeed _feed;

    private JettyLauncher _jettyLauncher;

    HttpError _errorResult = null;

    private PersistentCookieStore _cookieStore;

    boolean _interceptingHttpRequests;
    private String _apiKey = "1.0";

    @Before
    public void  setUp() throws Exception{
        System.out.println("Turning off requests");
        _interceptingHttpRequests = Robolectric.getFakeHttpLayer().isInterceptingHttpRequests();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);

        _cookieStore = MockHelper.MockPersistentCookieStore();
        //_successResult = null;
        //_failureResult = null;
        _jettyLauncher = new JettyLauncher();
        _jettyLauncher.addHandler("/news/home", TestFeedServlet.class);
        _jettyLauncher.addHandler("/news/personal/personalkey", TestFeedServlet.class);
        _jettyLauncher.start();
    }
    @After
    public void tearDown() throws Exception {
        _jettyLauncher.stop();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(_interceptingHttpRequests);
    }

    @Test
    public void shouldFindThePersonalKey() throws JSONException {

        // Arrange
        PrismaticFeed feedService = createPrismaticFeed();

        // Act
        IFeedStrategy strategy = new PersonalKeyStrategy(_jettyLauncher.getBasePath(), _apiKey);
        feedService.GetFeed(strategy, getSuccessFeedCallback(), getFailureFeedCallback());
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        if(_errorResult!=null) {
            System.out.println(_errorResult.getErrorMessage());
        }
        assertThat(_errorResult, nullValue());
        assertThat(_feed.getId(), equalTo("G__210360_29257430"));


    }

    @Test
    public void shouldParseDocsInTheFeed() throws JSONException {

        // Arrange
        com.bridgecanada.prismatic.feed.PrismaticFeed feedService = createPrismaticFeed();

        // Act

        feedService.GetFeed(getHomeFeedStrategy(), getSuccessFeedCallback(), getFailureFeedCallback());
        //feedService.GetFeed("G__210360_29257430", 5, 10, getSuccessFeedCallback(), getFailureFeedCallback());
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        if(_errorResult!=null) {
            System.out.println( _errorResult.getErrorMessage());
        }

        assertThat(_errorResult, nullValue());
        assertThat(_feed.getDocs(), notNullValue());
        assertThat(_feed.getDocs().size(), equalTo(5));
        for (Doc doc: _feed.getDocs()) {
            System.out.println("DOC IS "+doc.getTitle());

        }
        //assertThat(_feed, equalTo(TestFeedServlet.QUEUED));

    }



    @Test
    public void shouldFindAnImage() throws JSONException {

        // Arrange
        com.bridgecanada.prismatic.feed.PrismaticFeed feedService = createPrismaticFeed();

        // Act
        feedService.GetFeed(getHomeFeedStrategy(), getSuccessFeedCallback(), getFailureFeedCallback());
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        assertThat(_feed.getDocs().size(), equalTo(5));

        Img image = _feed.getDocs().get(0).getImg();

        assertThat(image, notNullValue());
        assertThat(image.getUrl(), equalTo("http://tctechcrunch2011.files.wordpress.com/2013/04/cat-facts-on-glass.png?w=846&h=330"));
        assertThat(image.getSize(), notNullValue());
        assertThat(image.getSize().getHeight(), equalTo(330));
        assertThat(image.getSize().getWidth(), equalTo(846));

    }

    @Test
    public void shouldGetNextInfo() throws JSONException {

        // Arrange
        com.bridgecanada.prismatic.feed.PrismaticFeed feedService = createPrismaticFeed();

        // Act
        feedService.GetFeed(getHomeFeedStrategy(), getSuccessFeedCallback(), getFailureFeedCallback());
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        assertThat(_feed.getNext(), notNullValue());
        assertThat(_feed.getNext().getQueryParams(), notNullValue());
        assertThat(_feed.getNext().getQueryParams().getFirstArticleIdx(), equalTo(0L));
        assertThat(_feed.getNext().getQueryParams().getLastArticleIdx(), equalTo(5L));
        assertThat(_feed.getNext().getQueryParams().getStart(), equalTo(123L));
        assertThat(_feed.getNext().getQueryParams().getLastFeedId(), equalTo("G__210360_29257430"));
        assertThat(_feed.getNext().getQueryParams().isSubpage(), is(true));
        assertThat(_feed.getNext().getRemainingCount(), equalTo(495));
    }



    @Test
    public void shouldGetFeedInfo() throws JSONException {

        // Arrange
        com.bridgecanada.prismatic.feed.PrismaticFeed feedService = createPrismaticFeed();

        // Act
        feedService.GetFeed(getHomeFeedStrategy(), getSuccessFeedCallback(), getFailureFeedCallback());
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        assertThat(_feed.getDocs(), notNullValue());
        Doc doc1 = _feed.getDocs().get(0);
        assertThat(doc1.getFeed(), notNullValue());
        assertThat(doc1.getFeed().getUrl(), equalTo("http://techcrunch.com/feed/"));
        assertThat(doc1.getFeed().getHighResImage(), equalTo("http://imagecdn.getprismatic.com/pub/big/-1302688707498574498.png"));
        assertThat(doc1.getFeed().getImage(), equalTo("http://imagecdn.getprismatic.com/pub/16/-1302688707498574498.ico"));
        assertThat(doc1.getFeed().getTitle(), equalTo("TechCrunch"));
    }

    @Test
    public void shouldGetPrismaticActivity() throws JSONException {

        // Arrange
        com.bridgecanada.prismatic.feed.PrismaticFeed feedService = createPrismaticFeed();

        // Act
        feedService.GetFeed(getHomeFeedStrategy(), getSuccessFeedCallback(), getFailureFeedCallback());
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        assertThat(_feed.getDocs(), notNullValue());
        Doc doc1 = _feed.getDocs().get(0);
        //assertThat(doc1.getId(), equalTo(1366435840264L));
        assertThat(doc1.getPrismaticActivity(), notNullValue());
        assertThat(doc1.getPrismaticActivity().getBookmark(), equalTo(5));
        assertThat(doc1.getPrismaticActivity().getClick(), equalTo(158));
        assertThat(doc1.getPrismaticActivity().getEmail(), equalTo(2));
        assertThat(doc1.getPrismaticActivity().getRemove(), equalTo(10));
        assertThat(doc1.getPrismaticActivity().getSave(), equalTo(6));
        assertThat(doc1.getPrismaticActivity().getShare(), equalTo(2));
    }

    @Test
    public void shouldHandleFeedFailure() throws JSONException {
        fail("need to handle feed failure---what does it look like?");
    }

    private Callable<Boolean> resultIsNotNull() {

        return new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return _feed != null  || _errorResult != null;
            }

        };

    }

    private IHttpResultCallback<com.bridgecanada.prismatic.data.PrismaticFeed> getSuccessFeedCallback() {

        return new IHttpResultCallback<com.bridgecanada.prismatic.data.PrismaticFeed>() {

            @Override
            public void onComplete(com.bridgecanada.prismatic.data.PrismaticFeed result, String raw, int statusCode) {
                System.out.println(TAG + ".onSuccess: " + result);
                _feed = result;
            }



        };
    }
    private IHttpResultCallback<HttpError> getFailureFeedCallback() {

        return new IHttpResultCallback<HttpError>() {


            @Override
            public void onComplete(HttpError errorResult, String raw, int statusCode) {
                System.out.println("FAILURE: "+errorResult.getErrorMessage());
                _errorResult = errorResult;
            }

        };

    }


    private PrismaticFeed createPrismaticFeed() {
        return new PrismaticFeed(_cookieStore, "Test Agent 123");
    }


    private IFeedStrategy getHomeFeedStrategy() {
        return new HomeStrategy("G__210360_29257430", 5, 10, _jettyLauncher.getBasePath(), _apiKey);
    }


    public static class TestFeedServlet extends HttpServlet {

        private String readTestData(String filename) throws IOException {

            InputStream is = this.getClass().getResourceAsStream(filename);
            if (is == null) {
                throw new RuntimeException("Unable to read "+filename);
            }

            String line;
            StringBuilder sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }

        //public static final int QUEUED = 123;
        private final String _contentType = "application/json";

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

            //System.out.println("CALLING DOGET");
            response.setContentType(_contentType);
//            if (!validated(request))  {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().println(JsonResponseGenerator.getError("Please Log In First"));
//                return;
//            }

            response.setStatus(HttpServletResponse.SC_OK);
            //response.getWriter().println(readTestData("/news_home.json"));
            //response.getWriter().println("{\"test\" : \"test 123\"}");
            response.getWriter().println(TEST_DATA);
        }

        private boolean validated(HttpServletRequest request) {

            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return false;
            }

            // just check that the auth cookie is set.
            for (Cookie cookie : cookies) {
                if (".ASPNET".equals(cookie.getName()) &&
                        cookie.getValue() != null) {
                    return true;
                }
            }

            return false;

        }
        // todo: figure out how to move this to a resource

        //private String TEST_DATA = "{\"interest\":{\"des\":\"Stories from your interests\",\"key\":\"personalkey\",\"title\":\"Home\",\"type\":\"personal\"},\"next\":{\"remaining-count\":495,\"query-params\":{\"subpage\":\"true\",\"last-feed-id\":\"G__210360_29257430\",\"first-article-idx\":0,\"last-article-idx\":5}},\"removed\":{\"remaining-count\":48},\"related-interests\":{\"interests\":{\"url\":\"/user/interests/interests?api-version=1.0\",\"title\":\"Interests\",\"count\":51},\"following\":{\"url\":\"/user/interests/following?api-version=1.0\",\"title\":\"Following\",\"count\":9}},\"id\":\"G__210360_29257430\",\"start\":0,\"docs\":[{\"text\":\"Last week, Google finally released the developer guides and other necessary documents that will allow developers to write apps for Glass. In some respects, the so-called Mirror API may have been a disappointment to developers who were expecting to run full-blown augmented reality apps, but even in its current form, I'm pretty sure it will allow developers to create new experiences for their new and existing apps that just weren't possible before.\\n\\nOne thing many developers may not have realized before Google published these documents is that the API is essentially an old-school RESTful service. The only way to interact with Glass is through the cloud. The only apps you can build - at least for now - are web-based and despite the fact that Glass runs Android, you can't run any services directly on the hardware.\\n\\nGoogle may have made this choice for a number of reasons. It ensures that Glass' battery life is reasonable (Google says it should last a full day, assuming you don't record a lot...\",\"date\":1366589159298,\"img\":{\"in-div\":1,\"orig-size\":{\"height\":\"330\",\"width\":\"846\"},\"size\":{\"width\":846,\"height\":330},\"url\":\"http://tctechcrunch2011.files.wordpress.com/2013/04/cat-facts-on-glass.png?w=846&h=330\"},\"prismatic-activity\":{\"click\":12},\"author\":{\"url\":null,\"name\":\"Frederic Lardinois\"},\"commerce\":null,\"title\":\"For Developers, Google Glass Looks To Be A Fascinating But Slightly Limited Platform\",\"url\":\"http://techcrunch.com/2013/04/21/for-developers-google-glass-looks-to-be-a-fascinating-but-slightly-limited-platform/\",\"feed\":{\"title\":\"TechCrunch\",\"url\":\"http://techcrunch.com/feed/\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/-1302688707498574498.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/-1302688707498574498.png\"},\"prismatic-shares\":[],\"comments\":[],\"id\":1366589159754,\"home-interests\":[[\"topic\",\"Android\"]],\"num-shares\":283,\"topics\":[[\"Android\",0.9710076369345119,199],[\"iPhone Apps\",0.8166619811628075,190],[\"Gadget\",0.7009678160005709,161]]},{\"text\":\"Better late than never. The TSA finally opened a 90-day period to comment on its controversial full-body scanners last month, following a D.C. court of appeals decision issued in July 2011. The public can now voice its opinion on the Whole Body Imaging scanners, which have been in place for nearly five years.\\n\\nCitizens can express their opinion on Regulation.gov, a government website that has the tagline, \\\"Your Voice in Federal Decision-Making.\\\" SEE ALSO: http://mashable.com/2013/01/18/never-forget-this-high-tech-naked-privacy-violation/\\n\\nThe public-commenting process comes as a result of a lawsuit brought by the Electronic Privacy Information Center (EPIC), a civil-rights group that has long opposed the scanners, calling them overly invasive and a violation against citizen's privacy.\\n\\nSo far, the website has 630 comments, most of which express common complaints about the scanners - from privacy and health concerns, to doubts about whether the technology respects the Fifth Amendment, which...\",\"date\":1366587078580,\"img\":{\"in-div\":1,\"orig-size\":{},\"size\":{\"width\":950,\"height\":534},\"url\":\"http://rack.1.mshcdn.com/media/ZgkyMDEzLzA0LzIxL2VjL3RzYWFpcnBvcnQuZGExMjYuanBnCnAJdGh1bWIJOTUweDUzNCMKZQlqcGc/0f9ca8e7/e06/tsa-airport.jpg\"},\"prismatic-activity\":{\"share\":1,\"click\":6},\"author\":null,\"commerce\":null,\"num-related\":3,\"title\":\"Tell the TSA What You Think About Its Full-Body Scanners\",\"url\":\"http://mashable.com/2013/04/21/tsa-full-body-scanners-public-comment/\",\"feed\":{\"title\":\"Mashable\",\"url\":\"http://feeds.mashable.com/Mashable\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/-8034038011985377761.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/-8034038011985377761.png\"},\"prismatic-shares\":[[{\"topics\":[\"Nuclear Energy\",\"Chemistry\",\"Physics\"],\"display-key\":\"nuclear94\",\"key\":134766,\"name\":\"Jeff Terry\",\"title\":\"nuclear94\",\"img\":\"http://a0.twimg.com/profile_images/3100133844/7589bdacc159b96faf4b25ec4f9b0fcd_bigger.jpeg\",\"type\":\"activity\"},{\"actions\":[\"share\"],\"following?\":false}]],\"comments\":[],\"id\":1366587073733,\"home-interests\":[[\"social\",\"socialkey\"]],\"num-shares\":633,\"topics\":[[\"Airport Security\",0.6665940570832208],[\"Security and Warning Systems\",0.18152502776216142],[\"Transportation Security Administration\",0.17403407724972747]]},{\"text\":\"In 2011, after President Obama used a drone to kill Anwar al-Awlaki, the American citizen who was recruiting jihadists from his perch in Yemen, many hailed     the assassination as a powerful blow against terrorism.\\n\\n\\\"The death of al-Awlaki is the last nail in the coffin of the al Qaeda brand,\\\"wrote Lisa Merriam (a \\\"brand consultant\\\") in a piece for    Forbes. \\\"Yes, bombs are what we think of when we think of al Qaeda, but powerful bombs require a powerful brand. The al Qaeda brand has been the     key to raising awareness, raising an army of recruits, raising money, and raising terror. Now that the brand is dead, all of those goals are out of reach.\\\" Tell that to the people of Boston. The more we learn about the Boston Marathon bombing--and the accused bombers, Tamerlan and Dzhokhar Tsarnaev--the more     reason there is to doubt the wisdom of Obama's drone-heavy approach to fighting terrorism. Not only did his hundreds of drone strikes fail to prevent the     bombing; they've probably...\",\"date\":1366552858905,\"img\":{\"in-div\":null,\"orig-size\":{\"height\":\"234\",\"width\":\"405\"},\"size\":{\"width\":405,\"height\":234},\"url\":\"http://cdn.theatlantic.com/static/newsroom/img/2013/04/19/RTXYSBH-thumb-570x393-119462_1/home-article-curation-405x234.jpg?mlj3tc\"},\"prismatic-activity\":{\"click\":16},\"author\":{\"url\":\"http://www.theatlantic.com/robert-wright/\",\"name\":\"Robert Wright\"},\"commerce\":null,\"title\":\"Drone Strikes and the Boston Marathon Bombing\",\"url\":\"http://www.theatlantic.com/international/archive/2013/04/drone-strikes-and-the-boston-marathon-bombing/275164/\",\"feed\":{\"title\":\"The Atlantic\",\"url\":\"http://feeds.feedburner.com/TheAtlantic\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/4886764575945213146.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/4886764575945213146.png\"},\"prismatic-shares\":[],\"comments\":[],\"id\":1366552860817,\"home-interests\":[[\"feed\",\"http://feeds.feedburner.com/TheAtlantic\"]],\"num-shares\":187,\"topics\":[[\"Military Aircraft\",0.7342122575316106,377]]},{\"text\":\"I am an avowed Apple fan and all my gadgets are from the 'i' family starting from my iMac, to my iPad, Mac Airbook, iPhone and iPod....   What is your latest gadget possession?\\n\\nMy latest Apple iPad.\\n\\nWhat do you not like about technology?\\n\\nIts intrusiveness and the deep dependencies that it develops   Biggest tech success according to you?\\n\\nTwo, according to me. The advent of PCs that brought computing to the 'individual' level, more so now with the form factor shrinking fast. Second, the Walkman - a truly disruptive technology innovation that made music 'mobile' - a concept that has now been taken to new levels by devices like the iPod.\\n\\nWhat is your dream machine?\\n\\nOne that can read thoughts and initiate action for instance, open documents that I am currently thinking of working on, searching emails for the email that I am trying to locate. This must be done without having all kinds of wires hanging around me!\\n\\nOne instance where technology solved your problem.\\n\\nThis one time, I had forgotten...\",\"date\":1366509872248,\"img\":{\"in-div\":null,\"orig-size\":{},\"size\":{\"width\":318,\"height\":419},\"url\":\"http://www.thehindubusinessline.com/multimedia/dynamic/01431/ew19_qt1_JPG_1431287e.jpg\"},\"prismatic-activity\":{\"saveManyDocs\":2,\"remove\":2,\"bookmark\":2,\"click\":105},\"author\":null,\"commerce\":null,\"title\":\"I forgot my flight ticket but Passbook app saved me\",\"url\":\"http://www.thehindubusinessline.com/features/weekend-life/i-forgot-my-flight-ticket-but-passbook-app-saved-me/article4627416.ece\",\"feed\":{\"title\":\"Business Line\",\"url\":\"www.thehindubusinessline.com\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/-453751200142732391.ico\"},\"prismatic-shares\":[[{\"topics\":[\"iPhone Apps\",\"Photography\",\"Cameras\"],\"display-key\":\"tommywdiehl\",\"key\":110372,\"name\":\"Tommy Diehl\",\"title\":\"tommywdiehl\",\"img\":\"http://graph.facebook.com/tommy.diehl.7/picture?type=large\",\"type\":\"activity\"},{\"actions\":[\"bookmark\"],\"following?\":false}],[{\"topics\":[\"iPhone Apps\",\"Gadgets\",\"NASCAR\"],\"display-key\":\"scottsacha\",\"key\":48120,\"name\":\"fssach\",\"title\":\"scottsacha\",\"img\":\"http://s3.amazonaws.com/prismatic-profiles/primary-image-371371581.jpeg\",\"type\":\"activity\"},{\"actions\":[\"bookmark\"],\"following?\":false}],[{\"topics\":[\"iPad\",\"Jogging\",\"Coffee\"],\"display-key\":\"megsaint\",\"key\":55398,\"name\":\"Meg St Clair\",\"title\":\"megsaint\",\"img\":\"http://a0.twimg.com/profile_images/406285198/Photo_3_bigger.jpg\",\"type\":\"activity\"},{\"actions\":[\"click\"],\"following?\":false}],[{\"topics\":[\"iPhone Apps\",\"iPhone\",\"Twitter\"],\"display-key\":\"rickreiko\",\"key\":137388,\"name\":\"Nick\",\"title\":\"rickreiko\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar0.jpg\",\"type\":\"activity\"},{\"actions\":[\"click\"],\"following?\":false}],[{\"topics\":[\"Programming\",\"Arduino\",\"iPad\"],\"display-key\":\"bjartek\",\"key\":6554,\"name\":\"Bjarte  S. Karlsen\",\"title\":\"bjartek\",\"img\":\"http://a0.twimg.com/profile_images/1726352488/bjartek_sinnataggen_bigger.jpg\",\"type\":\"activity\"},{\"actions\":[\"click\"],\"following?\":false}]],\"comments\":[],\"id\":1366509860724,\"home-interests\":[[\"topic\",\"Mobile Applications\"]],\"num-shares\":126,\"topics\":[[\"iPhone Apps\",0.9769467334989813,115],[\"iPad\",0.6926978056775743,115],[\"Android\",0.6931255045264362,115],[\"Gadget\",0.700366355198716,102],[\"Mobile Applications\",0.6867225918961174,110],[\"iPad Apps\",0.6704215760335096,106]]},{\"text\":\"The world's largest Bitcoin exchange Mt. Gox has once again been hit by a distributed denial of service (DDoS) attack. At the time of writing, the site has been down for over three hours, as the company scrambles to fight back.\\n\\nOn Facebook and Twitter, the company first informed its users that it was experiencing an outage and then later followed up to confirm it was indeed yet another DDoS. Since the Facebook posts naturally offer more details, they're the ones worth quoting, and especially so as the site doesn't exactly offer a friendly error message:  Here's the first one, posted at 10:43 AM EST:  We are experiencing some downtime at the moment and are investigating the source. Will update once the issue is clarified or resolved. Apologies for the temporary inconvenience.\\n\\nThe second one was published at 12:49 PM EST:  UPDATE: This again appears to be another strong DDos attack. We are working hard to overcome it and will update when possible. It's currently 2am in Japan so please forgive...\",\"date\":1366568358469,\"img\":{\"in-div\":1,\"orig-size\":{\"height\":\"251\",\"width\":\"683\"},\"size\":{\"width\":683,\"height\":251},\"url\":\"http://cdn.thenextweb.com/wp-content/blogs.dir/1/files/2013/04/mtgox_down1.png\"},\"prismatic-activity\":{\"remove\":1,\"bookmark\":1,\"click\":10},\"author\":{\"url\":null,\"name\":\"Emil Protalinski\"},\"commerce\":null,\"num-related\":2,\"title\":\"Bitcoin Exchange Mt. Gox Taken Down for Hours by DDoS Attack\",\"url\":\"http://thenextweb.com/insider/2013/04/21/here-we-go-again-top-bitcoin-exchange-mt-gox-taken-down-for-hours-by-another-strong-ddos-attack/\",\"feed\":{\"title\":\"The Next Web\",\"url\":\"http://feeds2.feedburner.com/thenextweb\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/5302663484565409349.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/5302663484565409349.png\"},\"prismatic-shares\":[[{\"topics\":[\"Programming\",\"NoSQL\",\"Ruby\"],\"display-key\":\"rhoml\",\"key\":11292,\"name\":\"Rhommel Lamas\",\"title\":\"rhoml\",\"img\":\"http://a0.twimg.com/profile_images/3307090807/519f91281d1cc98f606321abb6702d7c_bigger.png\",\"type\":\"activity\"},{\"actions\":[\"bookmark\"],\"following?\":false}]],\"comments\":[],\"id\":1366568359823,\"home-interests\":[[\"feed\",\"http://feeds2.feedburner.com/thenextweb\"]],\"num-shares\":243,\"topics\":[[\"Twitter\",0.680528252506726],[\"Boston Marathon\",0.29373155320822514]]}]}";


    }

    public static final String TEST_DATA = "{\"interest\":{\"des\":\"Stories from your interests\",\"key\":\"personalkey\",\"title\":\"Home\",\"type\":\"personal\"},"+
            "\"next\":{\"remaining-count\":495,\"query-params\":{\"start\":123,\"subpage\":\"true\",\"last-feed-id\":\"G__210360_29257430\",\"first-article-idx\":0,\"last-article-idx\":5}},\"removed\":{\"remaining-count\":48},\"related-interests\":{\"interests\":{\"url\":\"/user/interests/interests?api-version=1.0\",\"title\":\"Interests\",\"count\":51},\"following\":{\"url\":\"/user/interests/following?api-version=1.0\",\"title\":\"Following\",\"count\":9}},\"id\":\"G__210360_29257430\",\"start\":0,\"docs\":[{\"text\":\"Last week, Google finally released the developer guides and other necessary documents that will allow developers to write apps for Glass. In some respects, the so-called Mirror API may have been a disappointment to developers who were expecting to run full-blown augmented reality apps, but even in its current form, I'm pretty sure it will allow developers to create new experiences for their new and existing apps that just weren't possible before.\\n\\nOne thing many developers may not have realized before Google published these documents is that the API is essentially an old-school RESTful service. The only way to interact with Glass is through the cloud. The only apps you can build - at least for now - are web-based and despite the fact that Glass runs Android, you can't run any services directly on the hardware.\\n\\nGoogle may have made this choice for a number of reasons. It ensures that Glass' battery life is reasonable (Google says it should last a full day, assuming you don't record a lot...\",\"date\":1366589159298,\"img\":{\"in-div\":1,\"orig-size\":{\"height\":\"330\",\"width\":\"846\"},\"size\":{\"width\":846,\"height\":330},\"url\":\"http://tctechcrunch2011.files.wordpress.com/2013/04/cat-facts-on-glass.png?w=846&h=330\"},\"prismatic-activity\":{\"bookmark\":5,\"click\":158,\"email\":2,\"remove\":10,\"saveManyDocs\":6,\"share\":2},\"author\":{\"url\":null,\"name\":\"Frederic Lardinois\"},\"commerce\":null,\"title\":\"For Developers, Google Glass Looks To Be A Fascinating But Slightly Limited Platform\",\"url\":\"http://techcrunch.com/2013/04/21/for-developers-google-glass-looks-to-be-a-fascinating-but-slightly-limited-platform/\",\"feed\":{\"title\":\"TechCrunch\",\"url\":\"http://techcrunch.com/feed/\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/-1302688707498574498.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/-1302688707498574498.png\"},\"prismatic-shares\":[],\"comments\":[],\"id\":1366589159754,\"home-interests\":[[\"topic\",\"Android\"]],\"num-shares\":283,\"topics\":[[\"Android\",0.9710076369345119,199],[\"iPhone Apps\",0.8166619811628075,190],[\"Gadget\",0.7009678160005709,161]]},{\"text\":\"Better late than never. The TSA finally opened a 90-day period to comment on its controversial full-body scanners last month, following a D.C. court of appeals decision issued in July 2011. The public can now voice its opinion on the Whole Body Imaging scanners, which have been in place for nearly five years.\\n\\nCitizens can express their opinion on Regulation.gov, a government website that has the tagline, \\\"Your Voice in Federal Decision-Making.\\\" SEE ALSO: http://mashable.com/2013/01/18/never-forget-this-high-tech-naked-privacy-violation/\\n\\nThe public-commenting process comes as a result of a lawsuit brought by the Electronic Privacy Information Center (EPIC), a civil-rights group that has long opposed the scanners, calling them overly invasive and a violation against citizen's privacy.\\n\\nSo far, the website has 630 comments, most of which express common complaints about the scanners - from privacy and health concerns, to doubts about whether the technology respects the Fifth Amendment, which...\",\"date\":1366587078580,\"img\":{\"in-div\":1,\"orig-size\":{},\"size\":{\"width\":950,\"height\":534},\"url\":\"http://rack.1.mshcdn.com/media/ZgkyMDEzLzA0LzIxL2VjL3RzYWFpcnBvcnQuZGExMjYuanBnCnAJdGh1bWIJOTUweDUzNCMKZQlqcGc/0f9ca8e7/e06/tsa-airport.jpg\"},\"prismatic-activity\":{\"share\":1,\"click\":6},\"author\":null,\"commerce\":null,\"num-related\":3,\"title\":\"Tell the TSA What You Think About Its Full-Body Scanners\",\"url\":\"http://mashable.com/2013/04/21/tsa-full-body-scanners-public-comment/\",\"feed\":{\"title\":\"Mashable\",\"url\":\"http://feeds.mashable.com/Mashable\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/-8034038011985377761.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/-8034038011985377761.png\"},\"prismatic-shares\":[[{\"topics\":[\"Nuclear Energy\",\"Chemistry\",\"Physics\"],\"display-key\":\"nuclear94\",\"key\":134766,\"name\":\"Jeff Terry\",\"title\":\"nuclear94\",\"img\":\"http://a0.twimg.com/profile_images/3100133844/7589bdacc159b96faf4b25ec4f9b0fcd_bigger.jpeg\",\"type\":\"activity\"},{\"actions\":[\"share\"],\"following?\":false}]],\"comments\":[],\"id\":1366587073733,\"home-interests\":[[\"social\",\"socialkey\"]],\"num-shares\":633,\"topics\":[[\"Airport Security\",0.6665940570832208],[\"Security and Warning Systems\",0.18152502776216142],[\"Transportation Security Administration\",0.17403407724972747]]},{\"text\":\"In 2011, after President Obama used a drone to kill Anwar al-Awlaki, the American citizen who was recruiting jihadists from his perch in Yemen, many hailed     the assassination as a powerful blow against terrorism.\\n\\n\\\"The death of al-Awlaki is the last nail in the coffin of the al Qaeda brand,\\\"wrote Lisa Merriam (a \\\"brand consultant\\\") in a piece for    Forbes. \\\"Yes, bombs are what we think of when we think of al Qaeda, but powerful bombs require a powerful brand. The al Qaeda brand has been the     key to raising awareness, raising an army of recruits, raising money, and raising terror. Now that the brand is dead, all of those goals are out of reach.\\\" Tell that to the people of Boston. The more we learn about the Boston Marathon bombing--and the accused bombers, Tamerlan and Dzhokhar Tsarnaev--the more     reason there is to doubt the wisdom of Obama's drone-heavy approach to fighting terrorism. Not only did his hundreds of drone strikes fail to prevent the     bombing; they've probably...\",\"date\":1366552858905,\"img\":{\"in-div\":null,\"orig-size\":{\"height\":\"234\",\"width\":\"405\"},\"size\":{\"width\":405,\"height\":234},\"url\":\"http://cdn.theatlantic.com/static/newsroom/img/2013/04/19/RTXYSBH-thumb-570x393-119462_1/home-article-curation-405x234.jpg?mlj3tc\"},\"prismatic-activity\":{\"click\":16},\"author\":{\"url\":\"http://www.theatlantic.com/robert-wright/\",\"name\":\"Robert Wright\"},\"commerce\":null,\"title\":\"Drone Strikes and the Boston Marathon Bombing\",\"url\":\"http://www.theatlantic.com/international/archive/2013/04/drone-strikes-and-the-boston-marathon-bombing/275164/\",\"feed\":{\"title\":\"The Atlantic\",\"url\":\"http://feeds.feedburner.com/TheAtlantic\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/4886764575945213146.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/4886764575945213146.png\"},\"prismatic-shares\":[],\"comments\":[],\"id\":1366552860817,\"home-interests\":[[\"feed\",\"http://feeds.feedburner.com/TheAtlantic\"]],\"num-shares\":187,\"topics\":[[\"Military Aircraft\",0.7342122575316106,377]]},{\"text\":\"I am an avowed Apple fan and all my gadgets are from the 'i' family starting from my iMac, to my iPad, Mac Airbook, iPhone and iPod....   What is your latest gadget possession?\\n\\nMy latest Apple iPad.\\n\\nWhat do you not like about technology?\\n\\nIts intrusiveness and the deep dependencies that it develops   Biggest tech success according to you?\\n\\nTwo, according to me. The advent of PCs that brought computing to the 'individual' level, more so now with the form factor shrinking fast. Second, the Walkman - a truly disruptive technology innovation that made music 'mobile' - a concept that has now been taken to new levels by devices like the iPod.\\n\\nWhat is your dream machine?\\n\\nOne that can read thoughts and initiate action for instance, open documents that I am currently thinking of working on, searching emails for the email that I am trying to locate. This must be done without having all kinds of wires hanging around me!\\n\\nOne instance where technology solved your problem.\\n\\nThis one time, I had forgotten...\",\"date\":1366509872248,\"img\":{\"in-div\":null,\"orig-size\":{},\"size\":{\"width\":318,\"height\":419},\"url\":\"http://www.thehindubusinessline.com/multimedia/dynamic/01431/ew19_qt1_JPG_1431287e.jpg\"},\"prismatic-activity\":{\"saveManyDocs\":2,\"remove\":2,\"bookmark\":2,\"click\":105},\"author\":null,\"commerce\":null,\"title\":\"I forgot my flight ticket but Passbook app saved me\",\"url\":\"http://www.thehindubusinessline.com/features/weekend-life/i-forgot-my-flight-ticket-but-passbook-app-saved-me/article4627416.ece\",\"feed\":{\"title\":\"Business Line\",\"url\":\"www.thehindubusinessline.com\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/-453751200142732391.ico\"},\"prismatic-shares\":[[{\"topics\":[\"iPhone Apps\",\"Photography\",\"Cameras\"],\"display-key\":\"tommywdiehl\",\"key\":110372,\"name\":\"Tommy Diehl\",\"title\":\"tommywdiehl\",\"img\":\"http://graph.facebook.com/tommy.diehl.7/picture?type=large\",\"type\":\"activity\"},{\"actions\":[\"bookmark\"],\"following?\":false}],[{\"topics\":[\"iPhone Apps\",\"Gadgets\",\"NASCAR\"],\"display-key\":\"scottsacha\",\"key\":48120,\"name\":\"fssach\",\"title\":\"scottsacha\",\"img\":\"http://s3.amazonaws.com/prismatic-profiles/primary-image-371371581.jpeg\",\"type\":\"activity\"},{\"actions\":[\"bookmark\"],\"following?\":false}],[{\"topics\":[\"iPad\",\"Jogging\",\"Coffee\"],\"display-key\":\"megsaint\",\"key\":55398,\"name\":\"Meg St Clair\",\"title\":\"megsaint\",\"img\":\"http://a0.twimg.com/profile_images/406285198/Photo_3_bigger.jpg\",\"type\":\"activity\"},{\"actions\":[\"click\"],\"following?\":false}],[{\"topics\":[\"iPhone Apps\",\"iPhone\",\"Twitter\"],\"display-key\":\"rickreiko\",\"key\":137388,\"name\":\"Nick\",\"title\":\"rickreiko\",\"img\":\"http://cdn.getprismatic.com/cdn/img/profile/default_avatar0.jpg\",\"type\":\"activity\"},{\"actions\":[\"click\"],\"following?\":false}],[{\"topics\":[\"Programming\",\"Arduino\",\"iPad\"],\"display-key\":\"bjartek\",\"key\":6554,\"name\":\"Bjarte  S. Karlsen\",\"title\":\"bjartek\",\"img\":\"http://a0.twimg.com/profile_images/1726352488/bjartek_sinnataggen_bigger.jpg\",\"type\":\"activity\"},{\"actions\":[\"click\"],\"following?\":false}]],\"comments\":[],\"id\":1366509860724,\"home-interests\":[[\"topic\",\"Mobile Applications\"]],\"num-shares\":126,\"topics\":[[\"iPhone Apps\",0.9769467334989813,115],[\"iPad\",0.6926978056775743,115],[\"Android\",0.6931255045264362,115],[\"Gadget\",0.700366355198716,102],[\"Mobile Applications\",0.6867225918961174,110],[\"iPad Apps\",0.6704215760335096,106]]},{\"text\":\"The world's largest Bitcoin exchange Mt. Gox has once again been hit by a distributed denial of service (DDoS) attack. At the time of writing, the site has been down for over three hours, as the company scrambles to fight back.\\n\\nOn Facebook and Twitter, the company first informed its users that it was experiencing an outage and then later followed up to confirm it was indeed yet another DDoS. Since the Facebook posts naturally offer more details, they're the ones worth quoting, and especially so as the site doesn't exactly offer a friendly error message:  Here's the first one, posted at 10:43 AM EST:  We are experiencing some downtime at the moment and are investigating the source. Will update once the issue is clarified or resolved. Apologies for the temporary inconvenience.\\n\\nThe second one was published at 12:49 PM EST:  UPDATE: This again appears to be another strong DDos attack. We are working hard to overcome it and will update when possible. It's currently 2am in Japan so please forgive...\",\"date\":1366568358469,\"img\":{\"in-div\":1,\"orig-size\":{\"height\":\"251\",\"width\":\"683\"},\"size\":{\"width\":683,\"height\":251},\"url\":\"http://cdn.thenextweb.com/wp-content/blogs.dir/1/files/2013/04/mtgox_down1.png\"},\"prismatic-activity\":{\"remove\":1,\"bookmark\":1,\"click\":10},\"author\":{\"url\":null,\"name\":\"Emil Protalinski\"},\"commerce\":null,\"num-related\":2,\"title\":\"Bitcoin Exchange Mt. Gox Taken Down for Hours by DDoS Attack\",\"url\":\"http://thenextweb.com/insider/2013/04/21/here-we-go-again-top-bitcoin-exchange-mt-gox-taken-down-for-hours-by-another-strong-ddos-attack/\",\"feed\":{\"title\":\"The Next Web\",\"url\":\"http://feeds2.feedburner.com/thenextweb\",\"image\":\"http://imagecdn.getprismatic.com/pub/16/5302663484565409349.ico\",\"highres-image\":\"http://imagecdn.getprismatic.com/pub/big/5302663484565409349.png\"},\"prismatic-shares\":[[{\"topics\":[\"Programming\",\"NoSQL\",\"Ruby\"],\"display-key\":\"rhoml\",\"key\":11292,\"name\":\"Rhommel Lamas\",\"title\":\"rhoml\",\"img\":\"http://a0.twimg.com/profile_images/3307090807/519f91281d1cc98f606321abb6702d7c_bigger.png\",\"type\":\"activity\"},{\"actions\":[\"bookmark\"],\"following?\":false}]],\"comments\":[],\"id\":1366568359823,\"home-interests\":[[\"feed\",\"http://feeds2.feedburner.com/thenextweb\"]],\"num-shares\":243,\"topics\":[[\"Twitter\",0.680528252506726],[\"Boston Marathon\",0.29373155320822514]]}]}";

}
