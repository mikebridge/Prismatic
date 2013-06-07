package com.bridgecanada.prismatic.di;

import android.app.Application;
import android.content.SharedPreferences;
import com.bridgecanada.net.PersistentCookieStore;
import com.google.inject.Provider;
import roboguice.inject.SharedPreferencesName;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 20/05/13
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */

    public class PersistentCookieStoreProvider implements Provider<PersistentCookieStore> {
        private SharedPreferences _sharedPreferences;

        @Inject
        public PersistentCookieStoreProvider(SharedPreferences sharedPreferences) {
            _sharedPreferences= sharedPreferences;
        }

        @Override
        public PersistentCookieStore get() {
            return new PersistentCookieStore(_sharedPreferences);
        }
    }