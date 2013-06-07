package com.bridgecanada.net;

import org.apache.http.HttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 */

public interface IResponseHandler<T> {
    public T processResponse(int statusCode, String rawString) throws UnknownResponseException;
}
