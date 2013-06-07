package com.bridgecanada.prismatic.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 05/05/13
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 *
 * SEE also: https://code.google.com/p/roboguice/source/browse/roboguice/src/main/java/roboguice/inject/SharedPreferencesProvider.java?r=f84ac24c03f4651dcaa33ae6915fcef593922e0e
 */
public class SharedPreferencesProvider implements Provider<SharedPreferences> {

    @Inject
    protected Application application;

    @Override
    public SharedPreferences get() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
