package com.bridgecanada.net;

import junit.framework.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringResponseHandlerTest {

    @Test
    public void testProcessResponse() throws UnknownResponseException {

        // Arrange
        StringResponseHandler handler = new StringResponseHandler();

        // Act
        String result = handler.processResponse(200, "Test");

        //
        assertThat(result, equalTo("Test"));
    }
}
