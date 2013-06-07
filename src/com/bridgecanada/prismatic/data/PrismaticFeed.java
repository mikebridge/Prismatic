package com.bridgecanada.prismatic.data;

import java.util.ArrayList;

/**
 * User: bridge
 * Date: 21/04/13
 */
public class PrismaticFeed {

    private ArrayList<Doc> docs = new ArrayList<Doc>();
    private String id;
    //private int start;
    private Next next;

    // private Interest interest;

    @SuppressWarnings("UnusedDeclaration")
    //public void setStart(int start) {
    //    this.start = start;
    //}


    public ArrayList<Doc> getDocs() {
        return docs;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDocs(ArrayList<Doc> docs) {
        this.docs = docs;
    }

    //public int getStart() {
    //    return start;
    //}

    public Next getNext() {
        return next;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setNext(Next next) {
        this.next = next;
    }

    public String getId() {
        return id;
    }
    @SuppressWarnings("UnusedDeclaration")
    public void setId(String id) {
        this.id = id;
    }
}
