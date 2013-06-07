package com.bridgecanada.prismatic.feed;

import java.io.IOException;

/**
 * User: bridge
 * Date: 31/05/13
 */
public interface IFeedCache {

    void saveManyDocs(String feedid, FeedCache.FeedData feedData, String docsJson) throws IOException;


    String load(String feedid);

}
