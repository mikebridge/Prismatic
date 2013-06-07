package com.bridgecanada.net;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IObjectMapper {
    Object Convert(String jsonString) throws IOException;
}
