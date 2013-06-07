package com.bridgecanada.net;

import org.apache.http.HttpResponse;

/**
 * The result of an http response; includes the HttpResponse plus
 * a handler-generated representation of the payload
 *
 * User: bridge
 * Date: 20/05/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpResponseResult<T> {

    private HttpResponse _httpResponse;
    private T entity;

    public HttpResponse getHttpResponse() {
        return _httpResponse;
    }

    public void setHttpResponse(HttpResponse _httpResponse) {
        this._httpResponse = _httpResponse;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
