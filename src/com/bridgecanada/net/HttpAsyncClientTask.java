package com.bridgecanada.net;

import android.os.AsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;




/**
User: bridge
 * Date: 28/04/13
 * Time: 9:25 PM
 * S = success result object
 * F = failure result object
 */
public class HttpAsyncClientTask<S, F> extends AsyncTask<RestCall, Integer, List<HttpCallResult<?>>> {

    //private final String TAG = getClass().getSimpleName();

    private final IResponseHandler<S> _successHandler;
    private final IResponseHandler<F> _failureHandler;

    PersistentCookieStore _persistentCookieStore;
    String _userAgent;

    public HttpAsyncClientTask(
            PersistentCookieStore persistentCookieStore,
            IResponseHandler<S> successHandler,
            IResponseHandler<F> failureHandler,
            String userAgent
    ) {

        if (successHandler == null ) {

            throw new NullPointerException("successHandler is null");

        }
        if (failureHandler == null ) {

            throw new NullPointerException("failureHandler is null");

        }
        if (userAgent == null) {

            throw new NullPointerException("userAgent is null");

        }

        _persistentCookieStore = persistentCookieStore;
        _successHandler = successHandler;
        _failureHandler = failureHandler;
        _userAgent = userAgent;
    }

    @Override
    /**
     * Execute the calls described by RestCall objects, convert a successful
     * message to a result class, or else call "failure" with a String message.
     */
    protected List<HttpCallResult<?>> doInBackground(RestCall... calls) {

        List<HttpCallResult<?>> result = new ArrayList<HttpCallResult<?>>();
        // System.out.println("doInBackground");


        for (RestCall call: calls) {

            try {
                //System.out.println(">> DO CALL");
                HttpCallResult<?> httpCallResult = doCall(call);
                //HttpCallResult<?> httpCallResult = new HttpCallResult("Test Result", call.getSuccessCallback());
                //System.out.println(">> DONE CALL, got "+httpCallResult.getResult());
                result.add(httpCallResult);

            }
            catch(Exception ex) {
                System.out.println("ERROR: "+ex.getMessage());
                //System.out.println(ex.getStackTrace());
                ex.printStackTrace();
            }
        }
        //System.out.println("DONE httpAsyncClientTask doInBackground");
        //return new HttpCallResult<String>("Test", null);
        return result;

    }


    protected HttpCallResult<?> doCall(RestCall call) {

        System.out.println("Doing call to " + call.getUrl());

        // TODO: Move to AndroidHttpClient?

        DefaultHttpClient httpClient = createHttpClient();

        HttpRequestBase httpRequest = createHttpRequest(call);

        return executeHttpCall(call, httpClient, httpRequest);

    }

    private DefaultHttpClient createHttpClient() {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setCookieStore(_persistentCookieStore);
        return httpClient;

    }

    private HttpCallResult<?> executeHttpCall(RestCall call, DefaultHttpClient httpClient, HttpRequestBase httpRequest) {

        String textResponse;
        HttpContext localContext = new BasicHttpContext();

        try {

            HttpResponse response = httpClient.execute(httpRequest, localContext);
            textResponse = getBodyFromResponse(response);

            int statuscode = response.getStatusLine().getStatusCode();

            if (statuscode >= 200 && statuscode < 300) {

                //if (_successHandler!=null) {
                    S successResult = _successHandler.processResponse(statuscode, textResponse);
                    return new HttpCallResult<S>(successResult, textResponse, call.getSuccessCallback(), statuscode, response);
                //}

            } else {

                //if (_failureHandler != null ) {
                    F failureResult = _failureHandler.processResponse(statuscode, textResponse);
                    return new HttpCallResult<F>(failureResult, textResponse, call.getFailureCallback(), statuscode, response);
                //}
            }

        } catch (Exception e) {

            //return processResponse(call, jsonString);
            e.printStackTrace();
            throw new RuntimeException("Unhandled exception: "+e.getMessage());
            //    F exceptionResult =  _failureHandler.processResponse(0, e.getMessage());
                //  TODO: Should this be a separate callback type?
            //    return new HttpCallResult<F>(exceptionResult, call.getFailureCallback());
            //}
            //return e.getLocalizedMessage();
        }
    }

    private String getBodyFromResponse(HttpResponse response) throws IOException {

        String text;HttpEntity entity = response.getEntity();
        text = getASCIIContentFromEntity(entity);
        return text;

    }

    private HttpRequestBase createHttpRequest(RestCall call) {

        HttpRequestBase httpRequest;

        if (call.getHttpMethod() == RestCall.HttpMethod.GET) {

            httpRequest = new HttpGet(call.getUrl());

        } else if (call.getHttpMethod() == RestCall.HttpMethod.POST) {

            httpRequest = doPostRequest(call);

        } else {
            throw new RuntimeException("Unknown HttpMethod "+call.getHttpMethod());

        }
        if (_userAgent != null && !_userAgent.equals("")) {
            httpRequest.setHeader(CoreProtocolPNames.USER_AGENT, _userAgent);
        }
        if (call.getContentType() != null && !call.getContentType().equals("")) {
            httpRequest.setHeader("Content-Type", call.getContentType());
        }
        if (call.getAcceptHeader() != null && !call.getAcceptHeader().equals("")) {
            httpRequest.setHeader("Accept", call.getAcceptHeader());
        }
        return httpRequest;
    }


    private HttpRequestBase doPostRequest(RestCall call) {

        HttpRequestBase httpRequest;
        HttpPost httpPost = new HttpPost(call.getUrl());
        if (call.getContentString() != null) {
            try {
                httpPost.setEntity(new StringEntity(call.getContentString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } else {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key: call.getParms().keySet()) {
                List<String> vals = call.getParms().get(key);
                for(String val: vals) {
                    // mb: is this the way to add multi-valued
                    // key/value pairs??
                    nameValuePairs.add(new BasicNameValuePair(key, val));
                }
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        httpRequest = httpPost;
        return httpRequest;
    }

    // A temporary message from here:
    // http://www.techrepublic.com/blog/app-builder/calling-restful-services-from-your-android-app/1076
    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    // TODO: Extract into result handler




    @Override
    protected void onProgressUpdate(Integer... progress) {
        System.out.println("Progress: "+progress);
    }


    /**
     * Note: this is executed on the UI thread after it's done.
     */
    @Override
    protected void onPostExecute(List<HttpCallResult<?>> results) {

        for (HttpCallResult result: results) {

            //System.out.println("onPostExecute called");

            if (result.getClientCallback() == null) {
                System.out.println("No callback---ignoring");

            } else {

//                if (result == null) {
//                    throw new RuntimeException("Error: onPostExecute result is null");
//                }

                result.getClientCallback().onComplete(result.getResult(), result.getRawResult(), result.getStatusCode());

            }
        }

    }
}
