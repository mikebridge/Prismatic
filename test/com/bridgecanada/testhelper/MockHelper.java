package com.bridgecanada.testhelper;

import android.app.Application;
import android.content.SharedPreferences;
import com.bridgecanada.net.PersistentCookieStore;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowPreferenceManager;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 07/04/13
 */
public class MockHelper {

    public static PersistentCookieStore MockPersistentCookieStore() {
        Application application = Robolectric.application;
        if (application == null) {
            throw new RuntimeException("Robolectric.application is null!");
        }
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(
                application.getApplicationContext());
        return new PersistentCookieStore(sharedPreferences);
    }


}
