package com.bridgecanada.net;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 30/03/13
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
// TODO: Replace this class with a generically typed one
public class HttpClientErrorResult {

    private JSONObject _jsonObject;
    private int _httpStatus;

    public int getHttpStatus() {
        return _httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this._httpStatus = httpStatus;
    }



    public HttpClientErrorResult(String message) {
        setMessage(message);

    }

    public JSONObject getJsonObject() {
        return _jsonObject;
    }

    public void setJsonObject(JSONObject val) {
        _jsonObject = val;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public boolean needsAuthentication() {
       return this._httpStatus == 401 || this._httpStatus == 403;
    }
}
