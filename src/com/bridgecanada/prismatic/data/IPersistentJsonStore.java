package com.bridgecanada.prismatic.data;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 31/05/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPersistentJsonStore {

    void addJson(String id, String json);

    void clear();

    void remove(String id);

    String getJson(String id);
}
