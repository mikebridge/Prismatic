package com.bridgecanada.net;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/03/13
 */
public interface IHttpResultCallback<T> {

    void onComplete(T result, String rawResult, int statusCode);


}
