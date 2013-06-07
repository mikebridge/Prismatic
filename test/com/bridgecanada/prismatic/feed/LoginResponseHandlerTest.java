package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.JsonResponseHandler;
import com.bridgecanada.net.UnknownResponseException;
import com.bridgecanada.prismatic.LoginFailureResult;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: bridge
 * Date: 19/05/13
 */
public class LoginResponseHandlerTest {

    String passwordError = "Invalid Password";

    private String _json = "{\"form-errors\":{\"password\":\""+passwordError
            +"\"},\"message\":{\"password\":\""+passwordError
            +"\"},\"error-code\":\"error353812\"}";

    @Test
    public void testProcessResponse() throws UnknownResponseException {
        // Arrange
        LoginResponseHandler handler = new LoginResponseHandler();

        // Act
        System.out.println("JSON IS "+_json) ;
        LoginFailureResult result = handler.processResponse(200, _json);

        // Assert
        assertThat(result, notNullValue());
        assertThat(result.getMessages(), notNullValue());
        assertThat(result.getMessages().get(0), equalTo(passwordError));
    }
}
