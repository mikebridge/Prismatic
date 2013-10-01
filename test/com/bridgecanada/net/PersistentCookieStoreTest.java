package com.bridgecanada.net;

import android.content.Context;
import android.content.SharedPreferences;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowPreferenceManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.util.Calendar.getInstance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 02/04/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(RobolectricTestRunner.class)
public class PersistentCookieStoreTest {

    String _name = "testCookie";
    String _value = "cookieValue";
    String _path = "/test/path";
    String _domain = "www.example.com";
    int _version = 123;
    String _comment = "This is a comment";
    int _maxAge = 345;
    boolean _secure = true;
    PersistentCookieStore _store;

    @Before
    public void Setup() throws IOException {

        _store = new PersistentCookieStore(getSharedPreferences());

    }

    private SharedPreferences getSharedPreferences() {

        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(
                Robolectric.application.getApplicationContext());

        return sharedPreferences;

    }
    @Test
    public void shouldReturnAnAddedCookie() throws Exception {

        // Arrange
        Cookie cookie = createBasicClientCookie(_name, _value);

        // Act
        _store.addCookie(cookie);
        Set<String> names = _store.getStoredCookieNames();
        List<Cookie> cookies = _store.getCookies();

        // Assert


        //assertThat(cookies.get(0), samePropertyValuesAs(cookie));
        //assertThat(cookies, hasItem(cookie));
        //assertThat(cookies,.contains(cookie), is(true));

    }

    @Test
    public void shouldReturnMultipleAddedCookies() throws Exception {

        // Arrange
        Cookie cookie1 = createBasicClientCookie("cookie1", _value);
        Cookie cookie2 = createBasicClientCookie("cookie2", _value);

        // Act
        _store.addCookie(cookie1);
        _store.addCookie(cookie2);

        List<Cookie> cookies = _store.getCookies();

        // Assert
        //assertThat(cookies, hasItem(cookie1));
        //assertThat(cookies, hasItem(cookie2));

        assertThat(2, equalTo(cookies.size()));

    }

    @Test
    public void shouldOverwriteOldCookieWhenNewCookieAdded() {

        // Arrange
        String newValue = "value 2";
        Cookie cookie1 = createBasicClientCookie(_name, "value 1");
        Cookie cookie2 = createBasicClientCookie(_name, newValue);

        // Act
        _store.addCookie(cookie1);
        _store.addCookie(cookie2);

        List<Cookie> cookies = _store.getCookies();

        // Assert
        assertThat(cookies.size(), equalTo(1));
        assertThat(cookies.get(0).getValue(), equalTo(newValue));

    }

    @Test
    public void shouldClearOldCookies() {

        // Arrange
        String newValue = "value 2";
        Cookie cookie1 = createBasicClientCookie(_name, "value 1");
        Cookie cookie2 = createBasicClientCookie(_name, newValue);
        _store.addCookie(cookie1);
        _store.addCookie(cookie2);

        List<Cookie> cookies = _store.getCookies();
        assertThat(cookies.size(), equalTo(1));    // internal
        assertThat(cookies.get(0).getValue(), equalTo(newValue));

        // Act
        _store.clear();


        //
        cookies = _store.getCookies();
        assertThat(cookies.size(), equalTo(0));    // internal


    }




    private BasicClientCookie createBasicClientCookie(String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(getExpiryDate());
        cookie.setPath(_path);
        cookie.setDomain(_domain);
        cookie.setVersion(_version);
        cookie.setComment(_comment);
        cookie.setSecure(_secure);
        return cookie;
    }

    public Date getExpiryDate() {
        Calendar calendar = getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2032, 4, 1, 0, 0, 0);
        Date date = calendar.getTime();
        //long millis = date.getTime(); // Millis since Unix epoch
        return date;
    }



}
