package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.LoginFailureResult;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAuthService {

    void Login(String username,
               String password,
               IHttpResultCallback<String> successResultCallback,
               IHttpResultCallback<LoginFailureResult> failureResultCallback);

}
