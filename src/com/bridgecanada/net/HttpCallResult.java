package com.bridgecanada.net;

import org.apache.http.HttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 30/03/13
 */
//public class HttpCallResult<S extends IHttpClientSuccessResult> {
public class HttpCallResult<T> {

    private T _result = null;
    private int _statusCode;
    private HttpResponse _httpResponse;

    private String _rawResult;
    IHttpResultCallback<T> _clientCallback;

    public HttpCallResult(T result, String rawResult, IHttpResultCallback<T> clientCallback, int statuscode, HttpResponse response) {

        this._result = result;
        this._clientCallback = clientCallback;
        this._statusCode = statuscode;
        this._httpResponse = response;
        this._rawResult = rawResult;
    }

    public T getResult() {
        return _result;
    }

    public int getStatusCode() {
        return _statusCode;
    }

//    public F getErrorResult() {
//        return _errorResult;
//    }

    public IHttpResultCallback<T> getClientCallback() {
        return _clientCallback;
    }

    public HttpResponse getHttpResponse() {
        return _httpResponse;
    }

    public String getRawResult() {
        return _rawResult;
    }
    public void setRawResult(String result) {
        _rawResult = result;
    }

}
