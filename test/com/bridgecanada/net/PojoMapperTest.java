package com.bridgecanada.net;

import junit.framework.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class PojoMapperTest {

    private String _json = "{\"fieldOne\": \"hello\", \"fieldTwo\":\"hello 2\"}";

    @Test
    public void testConvert() throws Exception {


        // Arrange
        PojoMapper<TestPojo> mapper = new PojoMapper<TestPojo>(TestPojo.class);

        // Act
        TestPojo pojo = mapper.Convert(_json);

        // Assert
        assertThat(pojo.getFieldOne(), equalTo("hello"));
        assertThat(pojo.getFieldTwo(), equalTo("hello 2"));

    }
}
