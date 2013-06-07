package com.bridgecanada.prismatic.ui;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: bridge
 * Date: 23/05/13
 */
public class TimeAgoFormatterTest {



    DateTime _now = getTestDateUTC();
    TimeAgoFormatter _formatter = new TimeAgoFormatter(_now);

    @Test
    public void itShouldFormat_One_Hour_Ago() throws Exception {

        // Arrange
        DateTime oneHourAgo = _now.minusMinutes(60);

        // Act
        String result = _formatter.format(oneHourAgo);

        // Assert
        assertThat(result, equalTo("1h ago"));

    }

    @Test
    public void itShouldFormat_65Mins_Ago() throws Exception {

        // Arrange
        DateTime then = _now.minusMinutes(65);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("1h ago"));

    }

    @Test
    public void itShouldFormat_120_Mins() throws Exception {

        // Arrange
        DateTime then = _now.minusMinutes(120);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("2h ago"));

    }

    @Test
    public void itShouldFormat__Over_A_Year() throws Exception {

        // Arrange
        DateTime then = _now.minusDays(379);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("May 2012"));

    }


    @Test
    public void itShouldFormat_23_Hours() throws Exception {

        // Arrange
        DateTime then = _now.minusHours(23);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("23h ago"));

    }

    @Test
    public void itShouldFormat_1_Day_Ago() throws Exception {

        // Arrange
        DateTime then = _now.minusHours(25);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("yesterday"));

    }


    @Test
    public void itShouldFormat_1_Week_Ago() throws Exception {

        // Arrange
        DateTime then = _now.minusHours(7 * 24 + 3);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("1w ago"));

    }

    @Test
    public void itShouldFormat_1_Month_Ago() throws Exception {

        // Arrange
        DateTime then = _now.minusHours(30 * 24 + 3);

        // Act
        String result = _formatter.format(then);

        // Assert
        assertThat(result, equalTo("1 month ago"));

    }

    private DateTime getTestDateUTC() {
       DateTime now = new DateTime(2013, 5, 24, 22, 12, 30, DateTimeZone.UTC);
       return now;
    }
}
