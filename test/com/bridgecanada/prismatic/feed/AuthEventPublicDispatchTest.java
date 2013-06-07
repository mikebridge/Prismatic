package com.bridgecanada.prismatic.feed;

import android.util.Log;
import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.net.PersistentCookieStore;
import com.bridgecanada.prismatic.data.IPersistentJsonStore;
import com.bridgecanada.testhelper.JettyLauncher;
import com.bridgecanada.testhelper.MockHelper;
import com.jayway.awaitility.Duration;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.runner.RunWith;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: bridge
 * Date: 05/06/13
 */
@RunWith(RobolectricTestRunner.class)
public class AuthEventPublicDispatchTest {


    private final String TAG = getClass().getSimpleName();

    private com.bridgecanada.prismatic.feed.DispatchServiceBase _dispatchService;

    private JettyLauncher _jettyLauncher;



    JsonNode _errorResult = null;
    JsonNode _successResult = null;

    private PersistentCookieStore _cookieStore;


    boolean _interceptingHttpRequests;
    private String _apiKey = "1.0";

    @Before
    public void  setUp() throws Exception{
        System.out.println("Turning off requests");

        _interceptingHttpRequests = Robolectric.getFakeHttpLayer().isInterceptingHttpRequests();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);

        _cookieStore = MockHelper.MockPersistentCookieStore();
        _successResult = null;
        _errorResult = null;
        _jettyLauncher = new JettyLauncher();
        _jettyLauncher.addHandler("/auth/event_public_dispatch", TestDispatchServlet.class);
        _jettyLauncher.start();



        _dispatchService = new AuthEventPublicDispatch(
                "/news/blah", "/2013/my/blog", "http://www.example.com/2013/my/blog",
                _cookieStore, _apiKey, _jettyLauncher.getBasePath(),  "test Agent" );

        //_dispatchService = new AuthEventPublicDispatch(_articleId, _cookieStore, _feedCache, _apiKey, _jettyLauncher.getBasePath(),  "test Agent" );
    }

    @After
    public void tearDown() throws Exception {
        _jettyLauncher.stop();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(_interceptingHttpRequests);
    }

    @Test
    public void shouldCreateAValidDispatchRequest() throws IOException {

        // Arrange/act
        JsonNode node = _dispatchService.createBody();

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(node);

        // Assert

        assertThat(json, StringContains.containsString("\"uri\":\"/2013/my/blog\""));
        assertThat(json, StringContains.containsString("\"type\":\"login\""));
    }

    @Test
    public void sendConfirmation1ShouldReturnSuccess() throws Exception {

        // Arrange
        //JsonNode node = _Event_dispatchService.createBody("/news/blah", "/2013/my/blog", "http://www.example.com/2013/my/blog");
        assertThat(_successResult, nullValue());

        // Act
        _dispatchService.sendConfirmation(

                getSuccessCallback(),
                getFailureCallback());

        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        assertThat(_successResult, notNullValue());

    }



    private Callable<Boolean> resultIsNotNull() {

        return new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return _successResult != null  || _errorResult != null;
            }

        };

    }

    public static class TestDispatchServlet extends HttpServlet {



        //public static final int QUEUED = 123;
        private final String _contentType = "application/json";

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            Log.i("TestDispatchServlet", "POSTING!");
            response.setContentType(_contentType);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{\"ts\":1234567891012345}");
        }

    }

    private IHttpResultCallback<JsonNode> getFailureCallback() {
        return new IHttpResultCallback<JsonNode> () {

            @Override
            public void onComplete(JsonNode node, String raw, int statusCode) {
                System.out.println(TAG + "failure");
                _errorResult = node;
            }
        };
    }

    private IHttpResultCallback<JsonNode>  getSuccessCallback() {
        return new IHttpResultCallback<JsonNode> () {

            @Override
            public void onComplete(JsonNode node, String raw, int statusCode) {
                Log.i(TAG, node.getTextValue());
                System.out.println(TAG + " success");
                _successResult = node;
            }
        };
    }



}
