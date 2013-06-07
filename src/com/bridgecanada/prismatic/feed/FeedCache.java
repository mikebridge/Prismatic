package com.bridgecanada.prismatic.feed;

import com.bridgecanada.prismatic.data.DispatchCreator;
import com.bridgecanada.prismatic.data.IPersistentJsonStore;
import com.bridgecanada.prismatic.data.PersistentJsonStore;
import com.google.inject.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 31/05/13
 * Time: 11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeedCache implements IFeedCache {

    private IPersistentJsonStore _jsonStore;

    @Inject
    public FeedCache(IPersistentJsonStore persistentJsonStore) {
        if (persistentJsonStore == null) {
            throw new NullPointerException("persistentJsonStore is null");
        }
        _jsonStore = persistentJsonStore;

    }

    @Override
    public void saveManyDocs(String feedid, FeedData feedData, String docJson) throws IOException {
        FeedSplitter feedSplitter = new FeedSplitter();
        DispatchCreator creator = new DispatchCreator();
        List<ObjectNode> nodes = feedSplitter.splitArticles(docJson);

        // Act
        final DispatchCreator.ArticleData articleData = getArticleData(feedid, feedData);
        final DispatchCreator.DispatchData disatchData = getDispatchData(feedData);

        for(ObjectNode node: nodes) {
            JsonNode dispatchNode = creator.addData(node, disatchData, articleData);
            String val = new ObjectMapper().writeValueAsString(dispatchNode);
            System.out.println("saving: " + node.get("id").toString());
            _jsonStore.addJson(node.get("id").toString(), val);
        }
    }

    @Override
    public String load(String docid) {
        return _jsonStore.getJson(docid);
    }

    private DispatchCreator.DispatchData getDispatchData(FeedData feedData) {

        // TODO: don't hardcode these
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

    private DispatchCreator.ArticleData getArticleData(String feedid, FeedData feedData) {
        // TODO: don't hardcode these
        DispatchCreator.ArticleData result = new DispatchCreator.ArticleData();
        result.feedId = feedid;
        result.feedIndex = 3;
        result.homeInterests = new ArrayList<String>(); //Arrays.asList("homeinterest1", "homeinterest2", "homeinterest3");
        result.isFavoritedFeed = false;
        result.layoutDisplayText = "";
        result.layoutDisplayTopics = new ArrayList<String>(); //Arrays.asList("disptopic1", "disptopic2", "disptopic3");
        result.layoutFavoritedTopics = new ArrayList<String>();
        return result;

    }


    public static class FeedData {
        String url; // e.g. /news/home
        long first;
        long last;
        long start;
    }


}
