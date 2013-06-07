package com.bridgecanada.prismatic.feed;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import java.io.Console;
import java.util.List;

/**
 *
 * User: bridge
 * Date: 02/06/13
 *
 */
public class FeedSplitterTest {

    @Test
    public void testSplit() throws Exception {

        // Arrange
        FeedSplitter feedSplitter = new FeedSplitter();

        // Act
        List<ObjectNode> nodes = feedSplitter.splitArticles(PrismaticFeedTest.TEST_DATA);


        // Assert
        ObjectMapper mapper = new ObjectMapper();


        for(JsonNode node: nodes) {
            String str=mapper.writeValueAsString(node);
            System.out.println("====\n"+str);

        }


    }



}
