package com.bridgecanada.prismatic.search;

import android.util.Log;
import com.bridgecanada.net.*;
import com.bridgecanada.prismatic.data.SearchResults;
import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.UserAgentAnnotation;
import com.bridgecanada.prismatic.feed.HttpError;
import com.bridgecanada.prismatic.feed.HttpErrorResponseHandler;
import com.google.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: bridge
 * Date: 06/10/13
 */
public class Searcher implements ISearcher {

    List<ISearchResultListener> listeners = new ArrayList<ISearchResultListener>();


    private String API_VERSION = "1.2"; // different from baseline
    private String PATH = "/search/explore";
    private int limit = 10;
    private final String TAG = getClass().getSimpleName();
    private PersistentCookieStore _cookieStore;
    private String _userAgent;
    private String _baseUrl;

    private String getSearchUrl(String searchString) {
        //http://api.getprismatic.com/search/explore?api-version=1.2&limit=10&query=test

        try {
            return _baseUrl + PATH + "?rand="+ (new Random()).nextInt(Integer.MAX_VALUE)
                    + "&api-version="+API_VERSION
                    + "&limit="+limit
                    + "&query="+ URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {  // this shouldn't ever happen
            e.printStackTrace();
            return null;
        }


    }

    @Inject
    public Searcher(PersistentCookieStore persistentCookieStore,
                         @ApiBaseUrlAnnotation String baseUrl,
                         @UserAgentAnnotation String userAgent) {


        if (userAgent == null) {
            throw new NullPointerException("userAgent is null");
        }
        if (persistentCookieStore == null) {
            throw new NullPointerException("persistentCookieStore is null");
        }
        if (baseUrl == null ) {
            throw new IllegalArgumentException("baseUrl must not be null");
        }

        _baseUrl = baseUrl;
        _cookieStore = persistentCookieStore;
        _userAgent = userAgent;

    }

//    public void AsyncSearch( String searchString,
//                        IHttpResultCallback<SearchResults> successCallback,
//                        IHttpResultCallback<SearchFailure> failureCallback) {

    public void AsyncSearch(String searchString) {
        //HttpAsyncClientTask<com.bridgecanada.prismatic.data.PrismaticFeed, HttpError> task = getAsyncTask();
        if (searchString==null || searchString.trim().equals("")) {
            sendEmptyResult(searchString);
            return;
        }


        final String searchUrl = getSearchUrl(searchString);
        Log.i(TAG, "PrismaticFeed from: " + searchUrl);
        getAsyncTask()
                .execute(getHttpCall(searchUrl, getSuccessFeedCallback(searchString), getFailureFeedCallback()));

    }

    private void sendEmptyResult(String searchString) {


        tellListeners(searchString, SearchResults.createEmptyResult());
    }


    private IHttpResultCallback<SearchResults> getSuccessFeedCallback(final String searchString) {

        return new IHttpResultCallback<SearchResults>() {

            @Override
            public void onComplete(SearchResults result, String raw, int statusCode) {
                //System.out.println(TAG + ".onSuccess: " + result);
                tellListeners(searchString, result);
            }



        };
    }
    private IHttpResultCallback<SearchFailure> getFailureFeedCallback() {

        return new IHttpResultCallback<SearchFailure>() {

            @Override
            public void onComplete(SearchFailure errorResult, String rawResult, int statusCode) {
                //System.out.println("FAILURE!");
                System.out.println("FAILURE: "+errorResult.getErrorMessage());

                // TODO: Do something with the error!
                //_errorResult = errorResult;
            }
        };

    }

    @Override
    public void addSeachResultListener(ISearchResultListener listener) {
        listeners.add(listener);
    }

    private void tellListeners(String searchString, SearchResults results) {
        for (ISearchResultListener listener : listeners) {
            listener.HandleSearchResult(searchString, results);
        }
    }

    private RestCall getHttpCall(String url,
                                 IHttpResultCallback<SearchResults> successCallback,
                                 IHttpResultCallback<SearchFailure> failureCallback) {
        return RestCall.createGet(url,
                successCallback,
                failureCallback
        );
    }

    private HttpAsyncClientTask<SearchResults, HttpError> getAsyncTask() {
        return new HttpAsyncClientTask<SearchResults, HttpError>(
                _cookieStore,
                new TypedResponseHandler<SearchResults>(SearchResults.class),
                new HttpErrorResponseHandler(),
                _userAgent
        );
    }

}
