package com.bridgecanada.net;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Test;

//import javax.ws.rs.core.NewCookie;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 02/04/13
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CookieSerializerTest {

    String _name = "testCookie";
    String _value = "cookieValue";
    String _path = "/test/path";
    String _domain = "www.example.com";
    int _version = 123;
    String _comment = "This is a comment";
    //Date _expiryDate = new Date(2032, 8, 14, 12, 0, 0);
    boolean _secure = true;

    public Date getExpiryDate() {
        Calendar calendar = getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2032, 4, 1, 0, 0, 0);
        Date date = calendar.getTime();
        //long millis = date.getTime(); // Millis since Unix epoch
        return date;
    }

    @Test
    public void can_Serialize_Cookie() {

        // Arrange
        BasicClientCookie cookie = createBasicClientCookie();

        // Act
        String serializedCookie = CookieSerializer.serializeCookie(cookie);

        // Assert
        assertThat(serializedCookie, not(nullValue()));
    }



    @Test
    public void can_DeSerialize_Cookie() {

        // Arrange

        Cookie origCookie = createBasicClientCookie();
        String serializedCookieString = CookieSerializer.serializeCookie(origCookie);

        // Act
        Cookie deserializedCookie = CookieSerializer.deSerializeCookie(serializedCookieString);


        // Assert
        //assertThat(deserializedCookie, equalTo(origCookie));
        assertThat(deserializedCookie.getExpiryDate(), equalTo(origCookie.getExpiryDate()));
        assertThat(deserializedCookie.getName(), equalTo(origCookie.getName()));
        assertThat(deserializedCookie.getPath(), equalTo(origCookie.getPath()));
        assertThat(deserializedCookie.getComment(), equalTo(origCookie.getComment()));
        assertThat(deserializedCookie.getDomain(), equalTo(origCookie.getDomain()));
        assertThat(deserializedCookie.getValue(), equalTo(origCookie.getValue()));
        assertThat(deserializedCookie.getVersion(), equalTo(origCookie.getVersion()));
    }

    private BasicClientCookie createBasicClientCookie() {
        BasicClientCookie cookie = new BasicClientCookie(_name, _value);
        cookie.setExpiryDate(getExpiryDate());
        cookie.setPath(_path);
        cookie.setDomain(_domain);
        cookie.setVersion(_version);
        cookie.setComment(_comment);
        cookie.setSecure(_secure);
        return cookie;
    }

}
