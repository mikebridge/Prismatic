package com.bridgecanada.prismatic.feed;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeStrategyTest {

    @Test
    public void getFeedUrl_Should_Return_The_Home_Page_Url() throws Exception {

        // Arrange
        IFeedStrategy feedStrategy = new HomeStrategy("testid",10, 15, "http://localhost", "1.0") ;

        // Act

        String url = feedStrategy.getFeedUrl();

        // Assert
        assertThat(url, equalTo("http://localhost/news/home?last-feed-id=testid"
                + "&first-article-idx=10"
                + "&last-article-idx=15"
                + "&api-version=1.0"));



    }



}
