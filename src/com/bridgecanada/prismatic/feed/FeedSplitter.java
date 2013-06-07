package com.bridgecanada.prismatic.feed;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 02/06/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeedSplitter {

    // split the json list consisting of several documents
    // int an array of JsonData.
    public List<ObjectNode> splitArticles(String docJson) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(docJson);
        //return mapper.readValue(rawString, JsonNode.class);
        ArrayNode array = (ArrayNode) actualObj.get("docs");

        //System.out.println("Got "+)

        List result = new ArrayList();
        Iterator<JsonNode> iterator = array.getElements();
        while (iterator.hasNext()){
            result.add(iterator.next());
        }
        return result;

    }


}
