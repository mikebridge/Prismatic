package com.bridgecanada.prismatic.data;

import android.content.SharedPreferences;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowPreferenceManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 31/05/13
 */

@RunWith(RobolectricTestRunner.class)
/**
 * STore Json for later by article id:String
 */
public class PersistentJsonStoreTest {

    PersistentJsonStore _store;
    private String _json = "{\"id\":123}";

    @Before
    public void Setup() throws IOException {

        _store = new PersistentJsonStore(getSharedPreferences());

    }




    @Test
    public void addedJsonShouldReturnASavedJsonFragment() throws Exception {

        // Arrange
        String id = String.format("%d", (new Random()).nextInt());
        _store.addJson(id, _json);

        // Act

        String result = _store.getJson(id);

        // Assert
        assertThat(result, equalTo(_json));

    }



    @Test
    public void savingWithSameKeyDoesNotGenerateAnError() throws Exception {
        // Arrange
        String id = String.format("%d", (new Random()).nextInt());
        _store.addJson(id, _json);
        _store.addJson(id, _json);
        // Act

        String result = _store.getJson(id);

        // Assert
        assertThat(result, equalTo(_json));

    }

    @Test
    public void savingWithSameKeyDoesNotCreateDuplicateKey() throws Exception {

        // Arrange
        String id = String.format("%d", (new Random()).nextInt());
        _store.addJson(id, _json);
        _store.addJson(id, _json);
        // Act

        Set<String> ids = _store.getStoredJsonIds();

        // Assert
        assertThat(ids.size(), equalTo(1));

    }

    @Test
    public void clearRemovesAllValues() throws Exception {

        // Arrange
        String id = String.format("%d", (new Random()).nextInt());
        _store.addJson(id, _json);

        // Act
        _store.clear();
        Set<String> ids = _store.getStoredJsonIds();
        String json = _store.getJson(id);
        // Assert

        assertThat(ids.size(), equalTo(0));
        assertThat(json, nullValue());
    }

    @Test
    public void clearRemovesAValue() throws Exception {

        // Arrange
        String id = String.format("%d", (new Random()).nextInt());
        _store.addJson(id, _json);

        // Act
        _store.remove(id);
        Set<String> ids = _store.getStoredJsonIds();
        String json = _store.getJson(id);
        // Assert

        assertThat(ids.size(), equalTo(0));
        assertThat(json, nullValue());
    }



    /* helpers */

    private SharedPreferences getSharedPreferences() {

        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(
                Robolectric.application.getApplicationContext());

        return sharedPreferences;


    }

}
