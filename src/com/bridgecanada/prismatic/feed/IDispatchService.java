package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IHttpResultCallback;
import org.codehaus.jackson.JsonNode;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 30/05/13
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IDispatchService {

    // TODO: Figure out why there are two calls

    void SendConfirmation1(JsonNode dispatchRequest,
                          IHttpResultCallback<JsonNode> successCallback,
                          IHttpResultCallback<JsonNode> failureCallback);

    void SendConfirmation2(JsonNode dispatchRequest,
                          IHttpResultCallback<JsonNode> successCallback,
                          IHttpResultCallback<JsonNode> failureCallback);

    /**
     * @param sourceUri uri of the source page e.g. /news/home
     * @param targetUri uri of the target eg.g /2013/05/29/blah
     * @param targetUrl  url of target e.g. http://readwrite.com/2013/05/29/blah
     * @return
     */
    JsonNode createDispatchRequest(
            String sourceUri,
            String targetUri,
            String targetUrl);
}
