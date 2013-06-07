package com.bridgecanada.prismatic.ui;

import com.ocpsoft.pretty.time.PrettyTime;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 23/05/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeAgoFormatter {

    DateTime _now;

    public TimeAgoFormatter(DateTime now) {
        _now = now;
    }


    public String format(DateTime then) {
        Duration duration = new Duration(then, _now);

        if (duration.getStandardDays() > 365) {
            String pattern = "MMM y";

            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            return fmt.print(then);
        } else {

            PrettyTime t = new PrettyTime(_now.toDate());

            String result = t.format(then.toDate());
            // TODO: figure out how to do this properly!!
            result = result.replace(" hours", "h");
            result = result.replace(" hour", "h");
            result = result.replace("1 day ago", "yesterday");
            result = result.replace(" days", "d");
            result = result.replace(" day", "d");
            result = result.replace(" minutes", "m");
            result = result.replace(" minute", "m");
            result = result.replace(" weeks", "w");
            result = result.replace(" week", "w");

            return result;
        }
    }

}
