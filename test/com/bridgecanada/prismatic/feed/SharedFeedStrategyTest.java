package com.bridgecanada.prismatic.feed;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SharedFeedStrategyTest {

    @Test
    public void getFeedUrl_With_Zero_Start_Adds_No_Parameter() throws Exception {

        // Arrange
        IFeedStrategy feedStrategy = new SharedFeedStrategy(0, "http://localhost", "1.0") ;

        // Act
        String url = feedStrategy.getFeedUrl();

        // Assert
        assertThat(url, equalTo("http://localhost/news/shared?api-version=1.0"));

    }
    @Test

    public void getFeedUrl_With_Start_Adds_Correct_Parameter() throws Exception {

        // Arrange
        IFeedStrategy feedStrategy = new SharedFeedStrategy(110, "http://localhost", "1.0") ;

        // Act
        String url = feedStrategy.getFeedUrl();

        // Assert
        assertThat(url, equalTo("http://localhost/news/shared?api-version=1.0&start=110"));

    }


}
