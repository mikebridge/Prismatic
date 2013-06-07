package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IResponseHandler;
import com.bridgecanada.net.JsonResponseHandler;
import com.bridgecanada.net.UnknownResponseException;
import com.bridgecanada.prismatic.LoginFailureResult;
import org.codehaus.jackson.JsonNode;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 19/05/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginResponseHandler implements IResponseHandler<LoginFailureResult> {
    JsonResponseHandler _jsonHandler = new JsonResponseHandler();

    @Override
    public LoginFailureResult processResponse(int statuscode, String rawString) throws UnknownResponseException {

        LoginFailureResult result = new LoginFailureResult();

        JsonNode jsonNode = _jsonHandler.processResponse(statuscode, rawString);

        for(JsonNode node: jsonNode.get("message")) {
            result.addMessage(node.getTextValue());
        }

        return result;
    }

    /**
     * Parse a json object with "message" key into a string
     * @param jsonString
     * @return
     */
        /*
        private String extractMessage(String jsonString) throws IOException {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readValue(jsonString, JsonNode.class);
            String result =  rootNode.get("message").getTextValue();

            if (result==null) {
                return jsonString;
            }
            return result;


        }
        */
}