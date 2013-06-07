package com.bridgecanada.testhelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 04/05/13
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponderServlet extends HttpServlet {

    private String _contentType;
    private int _status;
    private String _responseString;


    protected void setContentType(String contentType) {
        this._contentType = contentType;
    }



    protected void setStatus(int status) {
        this._status = status;
    }

    protected void setResponseString(String responseString) {
        _responseString = responseString;
    }

    protected void setResponse(HttpServletResponse response) throws IOException {
        response.setContentType(_contentType);
        response.setStatus(_status);
        response.getWriter().println(_responseString);
    }


}
