package com.bridgecanada.net;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * User: bridge
 * Date: 19/05/13
 */
public class JsonResponseHandlerTest {

    String passwordError = "Incorrect Password";
    private String _json = "{\"form-errors\":{\"password\":\""+passwordError
            +"\"},\"message\":{\"password\":\""+passwordError
            +"\"},\"error-code\":\"error353812\"}";

    @Test
    public void testProcessResponse() throws UnknownResponseException {
        // Arrange
        JsonResponseHandler handler = new JsonResponseHandler();

        // Act
        //System.out.println("JSON IS "+_json) ;
        JsonNode result = handler.processResponse(200, _json);
        //System.out.println(result);

        //
        assertThat(result, notNullValue());
        JsonNode messageNode =  result.get("message");
        assertThat(messageNode, notNullValue());

        String errorMessage = "";
        for(JsonNode node: messageNode) {
            errorMessage += node.getTextValue();
        }
        System.out.println("message is "+messageNode );
        assertThat(errorMessage, equalTo(passwordError));

    }
}
