package com.bridgecanada.testhelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 04/05/13
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetResponderServlet  extends ResponderServlet {

    public GetResponderServlet(int status, String responseString) {
        setContentType("application/json");
        setStatus(status);
        setResponseString(responseString);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        setResponse(response);

    }

}
