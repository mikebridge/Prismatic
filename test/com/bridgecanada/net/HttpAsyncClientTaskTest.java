package com.bridgecanada.net;

import com.bridgecanada.testhelper.GetResponderServlet;
import com.bridgecanada.testhelper.JettyLauncher;
import com.bridgecanada.testhelper.PostResponderServlet;
import com.jayway.awaitility.Duration;
import com.bridgecanada.testhelper.MockHelper;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.codehaus.jackson.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 *

 *
 * NOTE: Could recreate this using RoboElectric's http stubbing:
 * https://github.com/pivotal/RobolectricSample/blob/master/src/test/java/com/pivotallabs/api/HttpTest.java
 *
 * User: bridge
 * Date: 23/03/13
 * Time: 6:36 PM
 */
@RunWith(RobolectricTestRunner.class)
public class HttpAsyncClientTaskTest {

    private JettyLauncher _jettyLauncher;
    private TestReportResult _successResult;
    private HttpClientErrorResult _failureResult;

    PersistentCookieStore _persistentCookieStore;
    boolean _interceptingHttpRequests;

    @Before
    public void setUp() throws Exception{

        // use real http requests
        System.out.println("Turning off requests");
        _interceptingHttpRequests = Robolectric.getFakeHttpLayer().isInterceptingHttpRequests();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
        _persistentCookieStore = MockHelper.MockPersistentCookieStore();
        _successResult = null;
        _failureResult = null;
        _jettyLauncher = new JettyLauncher();
        //_jettyLauncher.addHandler("/getSuccess", TestSuccessServlet.class);
        //_jettyLauncher.addHandler("/getFailure", TestFailureServlet.class);
        //_jettyLauncher.start();

    }

    @After
    public void tearDown() throws Exception {
        _jettyLauncher.stop();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(_interceptingHttpRequests);
    }

    @Test
    public void shouldParseAFailureMessageViaGet() {

        // Act
        String path = "/doFailureGetWithMessage";
        String failureMessage = "Failed To Do Something";
        _jettyLauncher.addHandler(path,
                new GetResponderServlet(HttpServletResponse.SC_UNAUTHORIZED,
                        "{\"message\" : \""+failureMessage+"\"}"));
        _jettyLauncher.start();

        sendGetRequest(path);

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertThat(_failureResult, notNullValue());
        assertThat(_failureResult.getMessage(), equalTo(failureMessage));

    }

    @Test
    public void shouldSetTheHttpStatusOnFailure() {

        // Act
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        String path = "/getErrorCode";
        String failureMessage = "Failed To Do Something";
        _jettyLauncher.addHandler(path,
                new GetResponderServlet(statusCode,
                        "{\"message\" : \""+failureMessage+"\"}"));
        _jettyLauncher.start();

        sendGetRequest(path);

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertThat(_failureResult, notNullValue());
        assertThat(_failureResult.getHttpStatus(), equalTo(statusCode));
        assertThat(_failureResult.needsAuthentication(), is(true));

    }


    @Test
    public void shouldParseAFailureMessageViaPost() {

        // Act
        String path = "/doFailurePostWithMessage";
        String failureMessage = "Failed To Do Something";
        _jettyLauncher.addHandler(path,
                new PostResponderServlet(HttpServletResponse.SC_UNAUTHORIZED,
                        "{\"message\" : \""+failureMessage+"\"}"));
        _jettyLauncher.start();

        Map<String,List<String>> parms = new HashMap<String, List<String>>();
        parms.put("parm1", Arrays.asList("test1"));
        parms.put("parm2", Arrays.asList("test2"));

        // Act
        sendPostRequest(path, parms);

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertThat(_failureResult, notNullValue());
        assertThat(_failureResult.getMessage(), equalTo(failureMessage));

    }

    @Test
    public void shouldParseAFailureMessageViaPostWithStringEntirty() {

        // Act
        String path = "/doFailurePostWithMessage";
        String failureMessage = "Failed To Do Something";
        _jettyLauncher.addHandler(path,
                new PostResponderServlet(HttpServletResponse.SC_UNAUTHORIZED,
                        "{\"message\" : \""+failureMessage+"\"}"));
        _jettyLauncher.start();

       String content = "{\"hello\":\"world\"}";

        // Act
        sendPostRequest(path, content);

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertThat(_failureResult, notNullValue());
        assertThat(_failureResult.getMessage(), equalTo(failureMessage));

    }


    @Test
    public void shouldParseASuccessfullyReturnedObjectViaGet() {

        // Arrange
        String path = "/doSuccessGetWithContent";

        System.out.println("is intercepting: "+Robolectric.getFakeHttpLayer().isInterceptingHttpRequests());
        _jettyLauncher.addHandler(path,
                new GetResponderServlet(200,
                        "{\"one\" : \"get one\", \"two\" : \"get two\"}"));
        _jettyLauncher.start();


        // Act
        sendGetRequest(path);

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertThat(_successResult.getOne(), equalTo("get one"));
        assertThat(_successResult.getTwo(), equalTo("get two"));
    }


    @Test
    public void shouldParseASuccessfullyReturnedObjectViaPost() throws Exception {


        // Arrange
        String path = "/doSuccessPostWithContent";
        _jettyLauncher.addHandler(path,
                new PostResponderServlet(200,
                        "{\"one\" : \"post one\", \"two\" : \"post two\"}"));
        _jettyLauncher.start();

        Map<String,List<String>> parms = new HashMap<String, List<String>>();
        parms.put("parm1", Arrays.asList("test1"));
        parms.put("parm2", Arrays.asList("test2"));

        //Act
        sendPostRequest(path, parms);


        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertThat(_successResult.getOne(), equalTo("post one"));
        assertThat(_successResult.getTwo(), equalTo("post two"));
        //assertNotNull(_successResult.getParms());
        //assertThat(_successResult.getParms().get("parm1"), equalTo("test1"));
    }

    @Test
    public void shouldParseAnEmptyObjectViaPost() throws Exception {

        // Arrange
        String path = "/doSuccessPostWithNoContent";
        _jettyLauncher.addHandler(path,
                new PostResponderServlet(200, ""));
        _jettyLauncher.start();

        Map<String,List<String>> parms = new HashMap<String, List<String>>();
        parms.put("parm1", Arrays.asList("test1"));
        parms.put("parm2", Arrays.asList("test2"));

        //Act
        sendPostRequest(path, parms);

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(reportIsNotNull());
        assertNotNull(_successResult);
        assertNull(_failureResult);
    }


    /* Helpers */

    private void sendGetRequest(String path) {
        // Arrange
        String url = _jettyLauncher.getBasePath() + path;

        //Robolectric.addPendingHttpResponse(200,
        //        "{\"one\" : \"get one\", \"two\" : \"get two\"}");

        HttpAsyncClientTask<TestReportResult, JsonNode> task = getTestResportTask();


        // Act

        //System.out.println("Thread in testDoCall is " + Thread.currentThread().getName());
        task.execute(RestCall.createGet(
                url,
                createSuccessfulTestResportCallback(),
                createFailureTestReportCallback()
                //TestReportResult.class
                ));

        //assertThat(_successResult, nullValue()); // internal test: thread hasn't run yet.


    }

    private HttpAsyncClientTask<TestReportResult, JsonNode> getTestResportTask() {


        return new HttpAsyncClientTask<TestReportResult, JsonNode>(
            _persistentCookieStore,
            new TypedResponseHandler<TestReportResult>(
                    TestReportResult.class
            ),
            new JsonResponseHandler()  , "Test Agent 123"
        );
    }

    private void sendPostRequest(String path, Map<String, List<String>> parms) {


        String url = _jettyLauncher.getBasePath() + path;
        HttpAsyncClientTask<TestReportResult, JsonNode> task = getTestResportTask();

        task.execute(RestCall.createPost(
                url,
                createSuccessfulTestResportCallback(),
                createFailureTestReportCallback(),
                //TestReportResult.class,
                parms));
    }

    private void sendPostRequest(String path, String content) {


        String url = _jettyLauncher.getBasePath() + path;
        HttpAsyncClientTask<TestReportResult, JsonNode> task = getTestResportTask();

        task.execute(RestCall.createPost(
                url,
                createSuccessfulTestResportCallback(),
                createFailureTestReportCallback(),
                //TestReportResult.class,
                content));
    }


    private IHttpResultCallback<TestReportResult> createSuccessfulTestResportCallback() {
        return new IHttpResultCallback<TestReportResult>() {
            @Override
            public void onComplete(TestReportResult reportResult, String raw, int statusCode) {

                if (reportResult == null) {
                    // this shouldn't happen
                    throw new InvalidParameterException("reportResult is null");
                }

                System.out.println("GOT SUCCESS");
                //System.out.println("Thread in callback is " + Thread.currentThread().getName());
                //reportResult.one("WEOFIJWEF");
                _successResult = reportResult;
            }

//            @Override
//            public void onFailure(HttpClientErrorResult errorResult) {
//                System.out.println("GOT FAILURE: "+errorResult.getMessage());
//                System.out.println("JSON is : "+errorResult.getJsonObject());
//                _failureResult = errorResult;
//            }

        };
    }

    private IHttpResultCallback<JsonNode> createFailureTestReportCallback() {
        return new IHttpResultCallback<JsonNode>() {
            @Override
            public void onComplete(JsonNode jsonErrorNode, String raw, int statusCode) {

                if (jsonErrorNode == null) {
                    // this shouldn't happen
                    throw new InvalidParameterException("Failure result is null");
                }

                System.out.println("GOT Failure");
                //System.out.println("Thread in callback is " + Thread.currentThread().getName());
                //reportResult.one("WEOFIJWEF");
                _failureResult = new HttpClientErrorResult(jsonErrorNode.get("message").getTextValue());
                _failureResult.setHttpStatus(statusCode);
            }


        };
    }

    private Callable<Boolean> reportIsNotNull() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return (_successResult != null || _failureResult !=null);
            }
        };
    }



    //
    // NOTE: jackson expects static inner class:
    // http://cowtowncoder.com/blog/archives/2010/08/entry_411.html
    //
    public static class TestReportResult implements IHttpClientSuccessResult {

        public TestReportResult() {
        }

        private String _one;

        public String getOne() {
            return _one;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setOne(String val) {
            this._one = val;
        }

        private String _two;

        public String getTwo() {
            return _two;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setTwo(String val) {
            this._two = val;
        }

        private HashMap<String, String> _parms;

        @SuppressWarnings("UnusedDeclaration")
        public HashMap<String, String> getParms() {
            return _parms;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setParms(HashMap<String, String> val) {
            this._parms = val;
        }


    }


}
