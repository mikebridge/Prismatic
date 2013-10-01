package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.net.PersistentCookieStore;
import com.bridgecanada.prismatic.LoginFailureResult;
import com.bridgecanada.testhelper.JettyLauncher;
import com.bridgecanada.testhelper.MockHelper;
import com.jayway.awaitility.Duration;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import junit.framework.Assert;
import org.apache.http.cookie.Cookie;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: bridge
 * Date: 22/04/13
 */
@RunWith(RobolectricTestRunner.class)
public class AuthServiceTest {

    protected static final String ERROR_MESSAGE = "Invalid Password";

    //private static final String TEST_SERVLET_PATH = "/test" ;
    private JettyLauncher _jettyLauncher;
    String _loginResult = null;
    LoginFailureResult _errorResult = null;
    //TestServiceServlet _testServiceServlet;
    PersistentCookieStore _cookieStore;
    AuthService _service;
    boolean _interceptingHttpRequests;

    @Before
    public void setUp() throws Exception{

        _interceptingHttpRequests = Robolectric.getFakeHttpLayer().isInterceptingHttpRequests();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);

        _cookieStore = MockHelper.MockPersistentCookieStore();

        _loginResult = null;
        _errorResult = null;
        //launchJetty();
        //_persistentCookieStore = MockHelper.MockPersistentCookieStore();
        _jettyLauncher = new JettyLauncher();
        _jettyLauncher.addHandler(AuthService.PATH, TestAuthServlet.class);
        _jettyLauncher.start();
        //_service = new AuthService(_cookieStore, "http://auth.getprismatic.com");
        _service = new AuthService(_cookieStore, _jettyLauncher.getBasePath(), "Test Agent 123");
    }

//    private void launchJetty() throws Exception {
//        //_testServiceServlet = new TestAuthServlet();
//        _jettyLauncher = new JettyLauncher();
//        _jettyLauncher.addHandler(AuthService.PATH, TestAuthServlet.class);
//        //_jettyLauncher.addHandler(TEST_SERVLET_PATH, _testServiceServlet);
//        _jettyLauncher.start();
//    }

    @After
    public void tearDown() throws Exception {

        _jettyLauncher.stop();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(_interceptingHttpRequests);

    }

    @Test
    public void shouldValidateCorrectPassword() throws Exception {

        // Arrange

        Robolectric.getBackgroundScheduler().pause();

        // Act
        _service.Login(
                TestAuthServlet.USERNAME,
                TestAuthServlet.PASSWORD,
                createSuccessLoginCallback(),
                createFailureLoginCallback()
        );

        //assertThat(_reportResult, nullValue()); // internal test: thread hasn't run yet.

        Robolectric.getBackgroundScheduler().runOneTask();

        // Assert
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());
        assertThat(_loginResult, notNullValue());
        assertThat(_loginResult, equalTo(TestAuthServlet.SUCCESS_PAYLOAD));
    }

    @Test
    public void shouldStoreThePayload() throws Exception {

        // Arrange
        Robolectric.getBackgroundScheduler().pause();

        // Act
        _service.Login(
                TestAuthServlet.USERNAME,
                TestAuthServlet.PASSWORD,
                createSuccessLoginCallback(),
                createFailureLoginCallback());

        Robolectric.getBackgroundScheduler().runOneTask();
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());
        // Assert

        assertThat(_loginResult, equalTo(TestAuthServlet.SUCCESS_PAYLOAD));

    }



    @Test
    public void shouldRejectInvalidPassword() throws Exception {
        // Arrange
        //Robolectric.getBackgroundScheduler().pause();


        // Act
        _service.Login(TestAuthServlet.USERNAME,
                "badpassword",
                createSuccessLoginCallback(),
                createFailureLoginCallback());
        Robolectric.getBackgroundScheduler().runOneTask();
        //System.out.println(_errorResult.getMessages());

        //JSONObject msg = _errorResult.getJsonObject().getJSONObject("message");
        String msg = _errorResult.getMessages().get(0);
        //String passwordError = (String) msg.get("password");


        Robolectric.getBackgroundScheduler().runOneTask();
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        // Assert
        assertThat(_errorResult, notNullValue());
        assertThat(_errorResult.getMessages(), notNullValue());
        assertThat(_errorResult.getMessages().get(0), equalTo(TestAuthServlet.ERROR_MESSAGE));

        //assertThat(_errorResult.getMessage(), containsText(ERROR_MESSAGE));

       ///ystem.out.println("MESSAGE: "+_errorResult.getJsonObject().get("message"));

        //assertThat(passwordError, equalTo("Incorrect password"));

    }


    @Test
    public void shouldSetCookieOnSuccess() throws Exception {
        // Arrange
        //Robolectric.getBackgroundScheduler().pause();
        // Act
        _service.Login(
             TestAuthServlet.USERNAME,
             TestAuthServlet.PASSWORD,
             createSuccessLoginCallback(),
             createFailureLoginCallback());

        //Robolectric.getBackgroundScheduler().runOneTask();
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());

        List<Cookie> cookies = _cookieStore.getCookies();
        Cookie authCookie = getAuthCookie(cookies);

        // Assert
        assertThat(authCookie, notNullValue());
        assertThat(authCookie.getValue(), equalTo(TestAuthServlet.AUTHCOOKIE_VALUE));

    }

    @Test
    public void shouldUnsetCookiesOnLogoff() throws Exception {
        // Arrange
        _service.Login(
                TestAuthServlet.USERNAME,
                TestAuthServlet.PASSWORD,
                createSuccessLoginCallback(),
                createFailureLoginCallback());
        //Robolectric.getBackgroundScheduler().runOneTask();
        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());
        List<Cookie> cookies = _cookieStore.getCookies();
        Cookie authCookie = getAuthCookie(cookies);
        assertThat(authCookie, notNullValue()); // internal test

        // Act
        _service.Logoff();
        cookies = _cookieStore.getCookies();
        authCookie = getAuthCookie(cookies);

        // Assert
        assertThat(authCookie, nullValue());
        //fail("Not Implemented yet");

    }

//    @Test
//    public void shouldIncludeAuthCookieOnNextCall() {
//
//        // Act
//        _service.Login(TestAuthServlet.USERNAME, TestAuthServlet.PASSWORD, createSuccessLoginCallback());
//        RestAsyncClientTask<TestReportResult> task = new RestAsyncClientTask<TestReportResult>(_cookieStore);
//
//
//        // Act
//
//        String url = _jettyLauncher.getBasePath() + TEST_SERVLET_PATH;
//        task.execute(RestCall.createGet(url, null, TestReportResult.class));
//
//        await().atMost(Duration.FIVE_SECONDS).until(resultIsNotNull());
//        Cookie cookie = _testServiceServlet.getAuthCookie();
//
//        // Assert
//        assertThat(cookie, notNullValue());
//        assertThat(cookie.getName(), equalTo(TestAuthServlet.AUTHCOOKIE_NAME));
//        assertThat(cookie.getValue(), equalTo(TestAuthServlet.AUTHCOOKIE_VALUE));
//
//    }


    private Callable<Boolean> resultIsNotNull() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return _loginResult != null || _errorResult != null;
            }
        };
    }



    private IHttpResultCallback<String> createSuccessLoginCallback() {
        return new IHttpResultCallback<String>() {

            @Override
            public void onComplete(String loginResult, String raw, int statusCode) {
                //System.out.println("onSuccess! loginresult-> "+loginResult);

                if (loginResult == null) {
                    throw new RuntimeException("This shouldn't be null!");

                }
                _loginResult = loginResult;
            }


        };
    }

    private IHttpResultCallback<LoginFailureResult> createFailureLoginCallback() {
        return new IHttpResultCallback<LoginFailureResult>() {

            @Override
            public void onComplete(LoginFailureResult errorResult, String raw, int statusCode) {
                //System.out.println("GOT FAILURE: "+errorResult.getMessage().get(0));
                _errorResult = errorResult;
            }

        };
    }

    /**
     * Find the auth cookie in the pile.
     */
    public static Cookie getAuthCookie(List<Cookie> cookies) {
        Cookie authCookie = null;

        for (Cookie cookie: cookies) {
            System.out.println("Looking at cookie "+cookie);
            if (TestAuthServlet.AUTHCOOKIE_NAME.equals(cookie.getName())) {
                authCookie = cookie;
            }
        }
        return authCookie;
    }


    public static class TestAuthServlet extends HttpServlet {

        private final String _contentType = "application/json";

        public static final String AUTHCOOKIE_NAME = "TESTAUTHCOOKIE";
        public static final String AUTHCOOKIE_VALUE = "test123";
        public static final String USERNAME = "testuser";
        public static final String PASSWORD = "testpassword";
        public static final String SUCCESS_PAYLOAD = "http://www.example.com/home/test";
        public static final String ERROR_MESSAGE = "Incorrect Password";

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            System.out.println("** RECEIVED POST **");
            response.setContentType(_contentType);
            if (USERNAME.equals(request.getParameter(AuthService.USER_PARM)) &&
                    PASSWORD.equals(request.getParameter(AuthService.PASSWD_PARM))) {

                response.setStatus(HttpServletResponse.SC_OK);
                javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(AUTHCOOKIE_NAME, AUTHCOOKIE_VALUE);

                cookie.setPath(request.getServletPath());
                response.addCookie(cookie);
                //System.out.println("NO RESPONSE");
                response.getWriter().print(SUCCESS_PAYLOAD);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("{\"form-errors\":{\"password\":\""+ERROR_MESSAGE+"\"},\"message\":{\"password\":\""+ERROR_MESSAGE+"\"},\"error-code\":\"error353812\"}");
                //response.getWriter().println("{ \"message\" : \"" + ERROR_MESSAGE + "\"}");
            }
            System.out.println("DONE");
            //response.getWriter().println("session=" + request.getSession(true).getId());
        }


    }


}
