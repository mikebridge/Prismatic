package com.bridgecanada.prismatic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginFailureResult {

    private List<String> _errorMessages = new ArrayList<String>();

    public List<String> getMessages() {
        return _errorMessages;
    }

    public void addMessage(String message) {
        this._errorMessages.add(message);
    }


}
