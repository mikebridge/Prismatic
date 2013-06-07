package com.bridgecanada.net;

import junit.framework.Assert;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class TypedResponseHandlerTest {

    @Test
    public void testProcessResponse() throws Exception {

        // Arrange
        PojoMapper<TestPojo> mapper = new PojoMapper<TestPojo>(TestPojo.class);
        TypedResponseHandler<TestPojo> handler = new TypedResponseHandler<TestPojo>(mapper);

        // Act
        TestPojo response = handler.processResponse(200, "{\"fieldOne\": \"hello\", \"fieldTwo\":\"hello 2\"}");

        // Assert
        assertThat(response.getFieldOne(), equalTo("hello"));
        assertThat(response.getFieldTwo(), equalTo("hello 2"));

    }





}
