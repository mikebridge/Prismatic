package com.bridgecanada.net;

import com.bridgecanada.prismatic.LoginFailureResult;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonNode;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 20/05/13
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
//public class HttpResponseHandler<HttpResponseResult<T>> implements IResponseHandler {
public class HttpResponseHandler<HttpResponseResult> implements IResponseHandler {

    private IResponseHandler _innerHandler;

    public HttpResponseHandler(IResponseHandler innerHandler) {

        _innerHandler = innerHandler;

    }

    @Override
    public HttpResponseResult processResponse(int statusCode, String rawString) throws UnknownResponseException {

        throw new RuntimeException("Why doesn't a nested generic work?");
        //HttpResponse result = new LoginFailureResult();

        //T innerResult = _innerHandler.processResponse(statusCode, rawString);
        //int statusCode = response.getStatusLine().getStatusCode();

        //return innerResult;

    }

}
