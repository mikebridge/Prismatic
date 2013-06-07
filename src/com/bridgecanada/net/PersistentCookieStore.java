package com.bridgecanada.net;

import android.content.SharedPreferences;
import com.google.inject.Inject;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.*;

/**
 * Store cookies (directly) in SharedPreferences.
 *
 * The list of keys are stored as an array under the COOKIE_NAMES key,
 * and the cookies themselves are stored, encoded, under a key generated
 * by getKey().
 *
 * adapted from https://raw.github.com/loopj/android-async-http/master/src/com/loopj/android/http/PersistentCookieStore.java
 *
 * User: bridge
 * Date: 02/04/13
 * Time: 8:49 PM
 */
public class PersistentCookieStore implements CookieStore {

    private SharedPreferences _sharedPreferences;
    private static final String COOKIE_NAMES = "PrismaticAndroid";

    @Inject
    public PersistentCookieStore(SharedPreferences sharedPreferences) {
        _sharedPreferences = sharedPreferences;

    }

    /**
     * Add a cookie to the persistent store, and
     * update the index.
     *
     * the cookie name is also added to the index.
     * @param cookie
     */

    @Override
    public void addCookie(Cookie cookie) {

        addStoredCookieNameToIndex(cookie.getName());
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        String key = getKey(cookie.getName());
        String val = CookieSerializer.serializeCookie(cookie);
        System.out.println("COOKIE IS "+key);
        System.out.println("VALUE IS "+val);
        editor.putString(key, val);
        editor.apply();

    }

    /**
     * Retrieve all cookies
     * @return
     */
    public List<Cookie> getCookies() {

        Set<String> cookieNames = getStoredCookieNames();
        List<Cookie> cookies = new ArrayList<Cookie>();
        for (String name : cookieNames) {
            cookies.add(getCookie(name));
        }
        return cookies;

    }

    @Override
    public boolean clearExpired(Date date) {
        throw new RuntimeException("Not Implemented Yet.");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Not Implemented Yet.");
    }

//    public boolean clearExpired(Date date) {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    public void clear() {
//
//    }

    private Cookie getCookie(String name) {

        String serializedCookieValue = _sharedPreferences.getString(name, null);
        if (serializedCookieValue !=null) {
            try {
               return CookieSerializer.deSerializeCookie(serializedCookieValue);
            }
            catch (Exception ex) {
                System.out.println("Unable to read cookie: "+ex.getMessage());
            }
        }
        return null;

    }

    Set<String> getStoredCookieNames() {
        return _sharedPreferences.getStringSet(COOKIE_NAMES, new HashSet<String>());
    }

    private void saveStoredCookieNames(Set<String> values) {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putStringSet(COOKIE_NAMES, values);
        editor.apply();
    }

    private void addStoredCookieNameToIndex(String name) {

        // TODO: this isn't thread-safe.

        Set<String> storedNames = getStoredCookieNames();

        storedNames.add(getKey(name));

        saveStoredCookieNames(storedNames);
    }

    private String getKey(String name) {
        return  COOKIE_NAMES + "_" + name;
    }

}
