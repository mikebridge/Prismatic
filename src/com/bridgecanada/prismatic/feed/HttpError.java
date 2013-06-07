package com.bridgecanada.prismatic.feed;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpError {

    private int status;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean needsAuthentication() {
        return getStatus() == 401 || getStatus() == 403;
    }
}
