package com.bridgecanada.prismatic.feed;

import android.util.Log;
import com.bridgecanada.net.*;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.bridgecanada.prismatic.di.UserAgentAnnotation;
import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 05/06/13

 */
public abstract class DispatchServiceBase {

    protected final String TAG = getClass().getSimpleName();
    protected PersistentCookieStore _cookieStore;
    protected String _userAgent;
    protected String _apiVersion;

    public DispatchServiceBase(
            @ApiVersionAnnotation String apiVersion,
            PersistentCookieStore cookieStore,
            @UserAgentAnnotation String userAgent) {

        if (apiVersion == null ) {
            throw new IllegalArgumentException("apiVersion must not be null");
        }
        if (cookieStore == null ) {
            throw new IllegalArgumentException("cookieStore must not be null");
        }
        if (userAgent == null ) {
            throw new IllegalArgumentException("userAgent must not be null");
        }
        _apiVersion = apiVersion;
        _cookieStore = cookieStore;
        _userAgent = userAgent;
    }


    protected HttpAsyncClientTask<JsonNode, JsonNode> getAsyncTask() {

        return new HttpAsyncClientTask<JsonNode, JsonNode>(
                _cookieStore,
                new JsonResponseHandler(),
                new JsonResponseHandler(),
                _userAgent
        );

    }
    protected RestCall getHttpCall(
            String jsonString,
            String url,
            IHttpResultCallback<JsonNode> successCallback,
            IHttpResultCallback<JsonNode> failureCallback) {

        RestCall call= RestCall.createPost(url, successCallback, failureCallback, jsonString);
        call.setContentType("application/json");
        return call;

    }



    /**
     * Confirm a url

     * @throws org.json.JSONException
     */
    public void sendConfirmation(

                IHttpResultCallback<JsonNode> successCallback,
                IHttpResultCallback<JsonNode> failureCallback) {



        HttpAsyncClientTask<JsonNode, JsonNode> task = getAsyncTask();


        try {
            JsonNode node = createBody();
            String jsonRequest =(new ObjectMapper()).writeValueAsString(node);

            //String jsonString = dispatchRequest.getTextValue();
            Log.i(TAG, jsonRequest);
            task.execute(getHttpCall(jsonRequest, getUrl(), successCallback, failureCallback));

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public abstract JsonNode createBody()  throws IOException;

    protected abstract String getUrl();


    public interface IDispatchServiceBaseFactory {

        AuthEventPublicDispatch createDispatch(
                @Assisted("sourceUri") String sourceUri,
                @Assisted("targetUri") String targetUri,
                @Assisted("targetUrl") String targetUrl
        );

        EventDispatchService createEventDispatch(
                @Assisted("articleId") String articleId
        );

    }



}
