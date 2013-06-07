package com.bridgecanada.prismatic.feed;

import android.util.Log;
import com.bridgecanada.net.*;
import com.bridgecanada.prismatic.data.IPersistentJsonStore;
import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.UserAgentAnnotation;
import com.google.inject.Inject;

import java.util.Random;

/**
 * Retrieve the feed, tell others.
 *
 * User: bridge
 * Date: 21/04/13
 */
public class PrismaticFeed implements IPrismaticFeed {

    private final String TAG = getClass().getSimpleName();
    private PersistentCookieStore _cookieStore;
    private String _userAgent;


    @Inject
    public PrismaticFeed(PersistentCookieStore persistentCookieStore,
                         //@ApiBaseUrlAnnotation String baseUrl,
                         @UserAgentAnnotation String userAgent) {


        if (userAgent == null) {
            throw new NullPointerException("userAgent is null");
        }
        if (persistentCookieStore == null) {
            throw new NullPointerException("persistentCookieStore is null");
        }
        _cookieStore = persistentCookieStore;
        _userAgent = userAgent;

    }

    @Override
    public void GetFeed(IFeedStrategy feedStrategy,
                        IHttpResultCallback<com.bridgecanada.prismatic.data.PrismaticFeed> successCallback,
                        IHttpResultCallback<HttpError> failureCallback) {

        HttpAsyncClientTask<com.bridgecanada.prismatic.data.PrismaticFeed, HttpError> task = getAsyncTask();
        String url = feedStrategy.getFeedUrl();


        //Log.i(TAG, "PrismaticFeed from: " + url);
        task.execute(getHttpCall(url, successCallback, failureCallback));

    }

    private RestCall getHttpCall(String url,
                                 IHttpResultCallback<com.bridgecanada.prismatic.data.PrismaticFeed> successCallback,
                                 IHttpResultCallback<HttpError> failureCallback) {
        return RestCall.createGet(url,
                successCallback,
                failureCallback
        );
    }

    private HttpAsyncClientTask<com.bridgecanada.prismatic.data.PrismaticFeed, HttpError> getAsyncTask() {
        return new HttpAsyncClientTask<com.bridgecanada.prismatic.data.PrismaticFeed, HttpError>(
                _cookieStore,
                new TypedResponseHandler<com.bridgecanada.prismatic.data.PrismaticFeed>(com.bridgecanada.prismatic.data.PrismaticFeed.class),
                new HttpErrorResponseHandler(),
                _userAgent
        );
    }



}
