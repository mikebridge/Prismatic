package com.bridgecanada.testhelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 04/05/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostResponderServlet extends ResponderServlet {

    private String _contentType = "application/json";

    public PostResponderServlet(int status, String responseString) {
        setContentType("application/json");
        setStatus(status);
        setResponseString(responseString);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        setResponse(response);
        /*
        response.setContentType(_contentType);
        response.setStatus(HttpServletResponse.SC_OK);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"one\" : \"post one\", \"two\" : \"post two\"" );

        if (request.getParameterMap() != null) {
            sb.append(", \"parms\" : {");
            boolean firstTime = true;
            for (String s : request.getParameterMap().keySet()) {
                // quickie serialization
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(", ");
                }

                sb.append("\""+s+"\": \"" + request.getParameter(s) + "\"");
            }

            sb.append("}");
        }
        sb.append("}");
        response.getWriter().println(sb.toString());
        //response.getWriter().println("session=" + request.getSession(true).getId());
        */
    }

}
