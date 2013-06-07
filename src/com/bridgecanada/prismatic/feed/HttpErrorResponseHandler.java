package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IResponseHandler;
import com.bridgecanada.net.JsonResponseHandler;
import com.bridgecanada.net.UnknownResponseException;
import com.bridgecanada.prismatic.LoginFailureResult;
import org.codehaus.jackson.JsonNode;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 20/05/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpErrorResponseHandler implements IResponseHandler<HttpError> {

    JsonResponseHandler _jsonHandler = new JsonResponseHandler();

    @Override
    public HttpError processResponse(int statuscode, String rawString) throws UnknownResponseException {

        HttpError result = new HttpError();

        JsonNode jsonNode = _jsonHandler.processResponse(statuscode, rawString);

        // TODO: what are the actual error messages?
        result.setErrorMessage("Unable to access feed");
        result.setStatus(statuscode);

        //for(JsonNode node: jsonNode.get("message")) {
        //    result.addMessage(node.getTextValue());
        //}

        return result;
    }

}
