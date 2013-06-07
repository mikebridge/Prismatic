package com.bridgecanada.prismatic.feed;

import com.bridgecanada.prismatic.data.IPersistentJsonStore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: bridge
 * Date: 31/05/13
 */
public class FeedCacheTest {


    private String _testUrl = "/news/home";

    @Test
    public void cacheShouldSaveTheDocument() throws Exception {

        // Arrange

        IPersistentJsonStore mockPersistentJsonStore = mock(IPersistentJsonStore.class);
        IFeedCache feedCache = new FeedCache(mockPersistentJsonStore);
        String id = String.format("%d", (new Random()).nextInt());

        // Act
        FeedCache.FeedData feedData = new FeedCache.FeedData();
        feedCache.saveManyDocs(id, feedData, PrismaticFeedTest.TEST_DATA);

        // Assert
        verify(mockPersistentJsonStore).addJson(eq("1366587073733"), any(String.class));

    }

    @Test
    public void cacheShouldRetrieveTheDocument() throws Exception {

        // Arrange

        IPersistentJsonStore mockPersistentJsonStore = mock(IPersistentJsonStore.class);
        IFeedCache feedCache = new FeedCache(mockPersistentJsonStore);
        String id = String.format("%d", (new Random()).nextInt());

        // Act
        FeedCache.FeedData feedData = new FeedCache.FeedData();
        feedCache.load("1366587073733");

        // Assert
        verify(mockPersistentJsonStore).getJson(eq("1366587073733"));

    }



}
