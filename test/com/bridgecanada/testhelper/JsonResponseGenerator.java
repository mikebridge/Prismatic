package com.bridgecanada.testhelper;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 */
public class JsonResponseGenerator {

       public static String getError(String message)  {
        // todo: escape quotes, etc.
        return "{\"message\" : \"" +message+ "\"}";

    }


}
