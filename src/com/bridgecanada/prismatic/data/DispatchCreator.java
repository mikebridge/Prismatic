package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: bridge
 * Date: 02/06/13
 */
public class DispatchCreator {

    /**
     * Take a doc node (from the prismatic feed, i.e. docs/*),
     * put it in a /data node suitable for a call to "dispatch"
     *
     *
     * 1) DOCNODE has the following added:
     *
     *   "feedID": "G__..."
     *   "feedIndex": 1
     *   "home-interests" :
     *      [
     *           "topic",
     *           "Software Engineering"
     *      ]
     *   "layout" {
     *      "displayText": "",
     *      "displayTopics": [
     *         "Robots",
     *         "Artificial Intelligence",
     *      ],
     *      "favoritedTopics": [],
     *      "imgSpecs": null,
     *      "isFavoritedFeed": false
     *   }
     *
     * 2) it gets wrapped liek this:
     *
     * {
     *    "data": {
     *       "article": DOCNODE
     *       "feed": {
     *          "des": "Stories from your interests",
     *          "has": false,
     *          "key": "personalkey",
     *          "title": "Home",
     *          "type": "personal"
     *        },
     *        "category": "click"
     *        "data": {
     *          "new_tab": false,
     *          "raw_article":
     *             DOCNODE
     *
     *        },
     *        "op": "prepend",
     *        "stack": [],
     *        "type": "article"
     *    }
     * }
     *
     *
     *
     * @param articleNode
     * @return
     */
    public ObjectNode addData(ObjectNode articleNode, DispatchData data, ArticleData articleData) throws IOException {

        // TODO: Get more information on this.

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode dataNode = mapper.createObjectNode();


        ObjectNode articlePlusNode =copyNode((ObjectNode) articleNode);
        addDataToArticle(mapper, articlePlusNode, articleData);
        dataNode.put("article", articlePlusNode);

        dataNode.put("feed", addFeedNode(data, mapper));



        dataNode.put("category", data.category);

        ObjectNode data2Node = mapper.createObjectNode();
        data2Node.put("new_tab", data.dataNewTab );
        data2Node.put("raw_article", articlePlusNode);
        dataNode.put("data", data2Node);

        dataNode.put("op", data.op);
        ArrayNode stack = createArrayNodeFromStringArray(mapper, data.stack);
        dataNode.put("stack", stack);
        dataNode.put("type", data.type);

        rootNode.put("data", dataNode);
        return rootNode;
    }

    private <T extends JsonNode> T copyNode(T node) throws IOException {
       return (T) new ObjectMapper().readTree(node.traverse());
    }


    private ArrayNode createArrayNodeFromStringArray(ObjectMapper mapper, List<String> array) {
        ArrayNode arrayNode = mapper.createArrayNode();
        if (array !=null) {
            for(String str: array) {
                arrayNode.add(str);
            }
        }
        return arrayNode;
    }

    private void addDataToArticle(ObjectMapper mapper, ObjectNode articleNode, ArticleData articleData) {

        articleNode.put("feedID", articleData.feedId);
        articleNode.put("feedIndex", articleData.feedIndex);
        articleNode.put("home-interests", createArrayNodeFromStringArray(mapper, articleData.homeInterests));

        ObjectNode layoutNode = mapper.createObjectNode();
        layoutNode.put("displayText", articleData.layoutDisplayText);
        layoutNode.put("displayTopics", createArrayNodeFromStringArray(mapper, articleData.layoutDisplayTopics));
        layoutNode.put("favoritedTopics", createArrayNodeFromStringArray(mapper, articleData.layoutFavoritedTopics));
        layoutNode.put("imgSpecs", articleData.imgSpecs);
        layoutNode.put("isFavoritedFeed", articleData.isFavoritedFeed);

        articleNode.put("layout", layoutNode);

    };

    private ObjectNode addFeedNode(DispatchData data, ObjectMapper mapper) {

        ObjectNode feedNode = mapper.createObjectNode();
        feedNode.put("des", data.feedDes);
        feedNode.put("has", data.feedHas);
        feedNode.put("key", data.feedKey);
        feedNode.put("title", data.feedTitle);
        feedNode.put("type", data.feedType);
        return feedNode;
    }


    public static final class DispatchData {
        public String feedDes = "";
        public boolean feedHas = false;
        public String feedKey = "personalKey";
        public String feedTitle = "home";
        public String feedType ="personal";
        public String op ="prepend";
        public List<String> stack = new ArrayList<String>();
        public String category = "click";
        public boolean dataNewTab = false;
        public String type = "article";
    }

    public static final class ArticleData {

        public String feedId = null;
        public int feedIndex = -1;
        public List<String> homeInterests = new ArrayList<String>();
        public String layoutDisplayText = "";
        public List<String> layoutDisplayTopics = new ArrayList<String>();
        public List<String> layoutFavoritedTopics = new ArrayList<String>();
        public String imgSpecs = null;
        public boolean isFavoritedFeed = false;


    }
}

