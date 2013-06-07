package com.bridgecanada.net;

import android.util.Log;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 18/05/13
 */
public class JsonResponseHandler implements IResponseHandler<JsonNode>  {

    private final String TAG = getClass().getSimpleName();

    public JsonNode processResponse(int statuscode, String rawString) throws UnknownResponseException{

        ObjectMapper mapper = new ObjectMapper();

        //Log.i(TAG, ">> Processing "+rawString);
        try {
           return mapper.readValue(rawString, JsonNode.class);
        }
        catch (JsonMappingException mappingException) {
            throw getUnknownResponseException(mappingException);
        }
        catch (JsonParseException parseException) {
            throw getUnknownResponseException(parseException);
        }
        catch (IOException origException) {
            throw getUnknownResponseException(origException);
        }
        //return rootNode;



//            Log.w(TAG, "ERROR: " + rawString);
//            //System.out.println(">>>>>>ERROR: " + jsonString) ;
//            // TODO: Make this a generic
//            //JSONObject obj = MapToPojo2<JSONObject>(jsonstring, )
//            JSONObject pojo = new JSONObject(rawString);
            //JSONObject pojo = MapToPojo2(call, jsonString);
//            System.out.println("JSON Object IS" + pojo);
//            String message = extractMessage(rawString);
//            System.out.println("RESULT IS" + message);
//            HttpClientErrorResult result = new HttpClientErrorResult(message);
//            // this will be replaced entirely with a generic class.
//            result.setJsonObject(pojo);
//            result.setHttpStatus(statuscode);
//            return new HttpCallResult<JSONObject>(result, call.getCallback());
//
      //} catch (Exception ex) {
//            System.out.println("CAUGHT: " + ex.getMessage());
//            ex.printStackTrace();
//            return new HttpCallResult<JSONObject>(new HttpClientErrorResult(ex.getMessage() + " RESULT WAS " + rawString), call.getCallback());
//
      //}
    }

    private UnknownResponseException getUnknownResponseException(IOException origException) {
        UnknownResponseException ex = new UnknownResponseException();
        ex.initCause(origException);
        return ex;
    }


    /**
     * Parse a json object with "message" key into a string
     * @param jsonString
     * @return
     */
    /*
    private String extractMessage(String jsonString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(jsonString, JsonNode.class);
        String result =  rootNode.get("message").getTextValue();

        if (result==null) {
            return jsonString;
        }
        return result;


    }

    */

}
