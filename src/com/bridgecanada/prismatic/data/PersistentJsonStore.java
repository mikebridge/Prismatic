package com.bridgecanada.prismatic.data;

import android.content.SharedPreferences;
import com.google.inject.Inject;

import java.util.*;

/**
 * Keys are stored in an array
 * JSON_IDS => id1,id2,id3 where the ids are strings
 *
 * Values are stored:
 * JSON_ID_id1 => value
 * JSON_ID_id2 => value
 *
 * The JSON_ID_X keys are created by getKey()
 *
 * User: bridge
 * Date: 31/05/13
 */
public class PersistentJsonStore implements IPersistentJsonStore {

    private SharedPreferences _sharedPreferences;
    private static final String JSON_IDS = "PrismaticAndroidJson";

    @Inject
    public PersistentJsonStore(SharedPreferences sharedPreferences) {
        _sharedPreferences = sharedPreferences;

    }

    /**
     * Add a json fragment to the persistent store, and
     * update the index with the id.
     *
     * the cookie name is also added to the index.
     * @param id
     * @param json
     */

    @Override
    public void addJson(String id, String json) {

        addStoredCookieIdToIndex(id);
        SharedPreferences.Editor editor = _sharedPreferences.edit();
//

        System.out.println("VALUE IS "+json);
        editor.putString(getKey(id), json);
        System.out.println("KEY IS "+getKey(id));
        editor.apply();

    }

    /**
     * Retrieve all json fragments
     * @return
     */

//    public List<String> getJson() {
//
//        Set<String> jsonNames = getStoredJsonIds();
//        List<String> jsonStrings= new ArrayList<String>();
//        for (String id : jsonStrings) {
//            jsonStrings.add(getJson(id));
//        }
//        return jsonStrings;
//
//    }

//    public boolean clearExpired(Date date) {
//        throw new RuntimeException("Not Implemented Yet.");
//    }

    /**
     * Clear the caceh
     * TODO: Make thread-safe
     */
    @Override
    public void clear() {

        Set<String> ids = getStoredJsonIds();
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        for(String id: ids) {
            System.out.println("DELETING "+id);
            delete(editor, getKey(id));
        }

        editor.remove(JSON_IDS);
        editor.apply();
    }

    @Override
    public void remove(String id) {

        removeStoredCookieIdFromIndex(id);
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        delete(editor, getKey(id));
        editor.apply();
    }

    private void delete(SharedPreferences.Editor editor, String key) {

        editor.remove(key);

    }

//    public boolean clearExpired(Date date) {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    public void clear() {
//
//    }

    @Override
    public String getJson(String id) {

        return _sharedPreferences.getString(getKey(id), null);


    }

    Set<String> getStoredJsonIds() {
        return _sharedPreferences.getStringSet(JSON_IDS, new HashSet<String>());
    }

    private void saveStoredJsonIds(Set<String> values) {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putStringSet(JSON_IDS, values);
        editor.apply();
    }

    /**
     * Add the name to the array of json things stored.
     * @param id
     */
    private void addStoredCookieIdToIndex(String id) {

        // TODO: this isn't thread-safe.

        Set<String> storedNames = getStoredJsonIds();

        storedNames.add(id);

        saveStoredJsonIds(storedNames);
    }

    /**
     * remove name to the array of json things stored.
     * @param id
     */
    private void removeStoredCookieIdFromIndex(String id) {

        // TODO: this isn't thread-safe.

        Set<String> storedNames = getStoredJsonIds();

        storedNames.remove(id);

        saveStoredJsonIds(storedNames);
    }

    private String getKey(String id) {

        return  JSON_IDS + "_" + id;

    }
}
