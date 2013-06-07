package com.bridgecanada.net;



import java.util.List;
import java.util.Map;

/**
 * Information to be used to create the HTTP call.
 *
 * resultClass is required because Java doesn't seem to be able to determine T.class when T is
 * a generic type.
 *
 * User: bridge
 * Date: 24/03/13
 * Time: 10:31 PM
 */
public class RestCall {


    public String getContentType() {
        return _contentType;
    }

    public void setContentType(String contentType) {
        this._contentType = contentType;
    }

    public enum HttpMethod {
        GET,POST,PUT,DELETE
    }

    private String _acceptHeader = "application/json";
    private Map<String, List<String>> _parms;
    private String _url;
    private HttpMethod _httpMethod;
    private IHttpResultCallback _successCallback;
    private IHttpResultCallback _failureCallback;
    private String _content;
    private String _contentType; // todo: set a default "application/json"

    //private Class _resultClass;


    RestCall(String url,
             HttpMethod httpMethod,
             IHttpResultCallback successCallback,
             IHttpResultCallback failureCallback,
             Map<String, List<String>> parms
    ) {
        this._url = url;
        this._httpMethod = httpMethod;
        this._successCallback = successCallback;
        this._failureCallback = failureCallback;
        this._parms = parms;
    }

    // ugh, this is a quick hack; fix this
    RestCall(String url,
             HttpMethod httpMethod,
             IHttpResultCallback successCallback,
             IHttpResultCallback failureCallback,
             String content
    ) {
        this._url = url;
        this._httpMethod = httpMethod;
        this._successCallback = successCallback;
        this._failureCallback = failureCallback;
        this._content = content;
    }

    public static RestCall createPost(String url,
                                        IHttpResultCallback successCallback,
                                        IHttpResultCallback failureCallback,
                                        Map<String, List<String>> parms) {

        return new RestCall(url, HttpMethod.POST, successCallback, failureCallback, parms);
    }

    public static RestCall createPost(String url,
                                      IHttpResultCallback successCallback,
                                      IHttpResultCallback failureCallback,
                                      String content) {

        return new RestCall(url, HttpMethod.POST, successCallback, failureCallback, content);
    }

    public static RestCall createGet(String url,
                                     IHttpResultCallback successCallback,
                                     IHttpResultCallback failureCallback) {

        return new RestCall(url, HttpMethod.GET, successCallback, failureCallback, (String) null);
    }


    public IHttpResultCallback getSuccessCallback() {
        return _successCallback;
    }

    public  IHttpResultCallback getFailureCallback() {
        return _failureCallback;
    }


    public String getUrl() {
        return _url;
    }


    public HttpMethod getHttpMethod() {
        return _httpMethod;
    }

    public String getAcceptHeader() {
        return _acceptHeader;
    }

    /**
     * Post/Put body (may be null)
     * @return
     */
    public Map<String, List<String>> getParms() {
        return _parms;
    }

    public String getContentString()  {
        return _content;
    }
    public void setContentString(String value) {
        _content = value;
    }


}
