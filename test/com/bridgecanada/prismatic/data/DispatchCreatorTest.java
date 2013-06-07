package com.bridgecanada.prismatic.data;

import com.bridgecanada.prismatic.feed.FeedSplitter;
import com.bridgecanada.prismatic.feed.PrismaticFeedTest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 02/06/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatchCreatorTest {


    @Test
    public void shouldAddDataToArticleJson() throws Exception {

        // Arrange
        FeedSplitter feedSplitter = new FeedSplitter();
        DispatchCreator creator = new DispatchCreator();
        List<ObjectNode> nodes = feedSplitter.splitArticles(PrismaticFeedTest.TEST_DATA);

        // Act
        final DispatchCreator.ArticleData articleData = getArticleData();
        final DispatchCreator.DispatchData disatchData = getDisatchData();
        JsonNode dispatchNode = creator.addData(nodes.get(0), disatchData, articleData);
        System.out.println("PRINTING: " + new ObjectMapper().writeValueAsString(dispatchNode));

        // Assert
        JsonNode dataNode = dispatchNode.get("data");

        assertThat(dataNode, notNullValue());
        JsonNode articleNode = dataNode.get("article");

        assertThat(articleNode, notNullValue() );
        assertThat(articleNode.get("feedID"), notNullValue());
        assertThat(articleNode.get("feedID").getTextValue(), equalTo(articleData.feedId));

        fail("Need to test the rest of this....");


    }

    private DispatchCreator.DispatchData getDisatchData() {
        DispatchCreator.DispatchData result = new DispatchCreator.DispatchData();
        result.feedDes = "Stories from your interests";
        result.feedHas = false;
        result.feedKey = "personalkey";
        result.feedTitle = "home";
        result.feedType = "personal";

        result.op = "prepend";

        result.stack = new ArrayList<String>();
        result.type = "article";

        return result;
    }

    private DispatchCreator.ArticleData getArticleData() {
        DispatchCreator.ArticleData result = new DispatchCreator.ArticleData();
        result.feedId = "G_123_456";
        result.feedIndex = 3;
        result.homeInterests = Arrays.asList("homeinterest1", "homeinterest2", "homeinterest3");
        result.isFavoritedFeed = false;
        result.layoutDisplayText = "Favourite feed";
        result.layoutDisplayTopics = Arrays.asList("disptopic1", "disptopic2", "disptopic3");
        result.layoutFavoritedTopics = new ArrayList<String>();
        return result;

    }

}
