package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Author {

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUrl(String url) {
        this.url = url;
    }



}
