package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.*;
import com.bridgecanada.prismatic.LoginFailureResult;
import com.bridgecanada.prismatic.di.AuthBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.UserAgentAnnotation;
import com.google.inject.Inject;
import org.codehaus.jackson.JsonNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: bridge
 * Date: 21/04/13
 */
public class AuthService implements IAuthService {

    //"/Api/Auth/Login";
    public static final String PATH = "/auth/login";
    public static final String QS = "?api-version=1.0&ignore=true&whitelist_url=http%3A%2F%2Fwww.getprismatic.com%2Fnews%2Fhome&soon_url=http%3A%2F%2Fwww.getprismatic.com%2Fwelcome&create_url=http%3A%2F%2Fwww.getprismatic.com%2Fcreateaccount&resetpassword_url=http%3A%2F%2Fwww.getprismatic.com%2Fresetpassword";
    public static final String USER_PARM = "handle";
    public static final String PASSWD_PARM = "password";
    private String _baseUrl;
    private PersistentCookieStore _cookieStore;
    private String _userAgent;

    @Inject
    public AuthService(
            PersistentCookieStore cookieStore,
            @AuthBaseUrlAnnotation String baseUrl,
            @UserAgentAnnotation String userAgent) {

        if (cookieStore == null ) {
            throw new IllegalArgumentException("cookieStore must not be null");
        }
        if (baseUrl == null ) {
            throw new IllegalArgumentException("baseUrl must not be null");
        }
        if (userAgent == null ) {
            throw new IllegalArgumentException("userAgent must not be null");
        }
        _cookieStore = cookieStore;
        _baseUrl = baseUrl;
        _userAgent = userAgent;
    }

    @Override
    public void Login(
            String username,
            String password,
            IHttpResultCallback<String> successCallback,
            IHttpResultCallback<LoginFailureResult> failureCallback) {

        HttpAsyncClientTask<String, LoginFailureResult> task = createLoginTask();

        task.execute(RestCall.createPost(
                getLoginUrl(),
                successCallback,
                failureCallback,
                createPostParms(username, password)
        ));
    }

    @Override
    public void Logoff() {
        _cookieStore.clear();

    }

    private Map<String, List<String>> createPostParms(String username, String password) {

        Map<String, List<String>> parms = new HashMap<String, List<String>>();
        parms.put(USER_PARM, Arrays.asList(username));
        parms.put(PASSWD_PARM, Arrays.asList(password));
        return parms;

    }

    private HttpAsyncClientTask<String, LoginFailureResult> createLoginTask() {
        return new HttpAsyncClientTask<String, LoginFailureResult> (
                _cookieStore,
                new StringResponseHandler(), // this returns the home url as its body
                new LoginResponseHandler(),
                _userAgent
                );
    }

    public String getLoginUrl() {

        return _baseUrl + PATH + QS;

    }



}
