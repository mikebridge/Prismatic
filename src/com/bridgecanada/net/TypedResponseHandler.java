package com.bridgecanada.net;

import android.util.Log;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;


// formerly error handler...

public class TypedResponseHandler<T> implements IResponseHandler<T> {

    private final String TAG = getClass().getSimpleName();

    private final PojoMapper<T> pojoMapper;

    public TypedResponseHandler(PojoMapper<T> pojoMapper) {
        if (pojoMapper == null) {
            throw new NullPointerException("pojoMapper is null");
        }
        this.pojoMapper = pojoMapper;
    }

    public TypedResponseHandler(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("class type is null");
        }
         this.pojoMapper = new PojoMapper<T>(clazz);
    }


    public T processResponse( int statuscode, String rawString) throws UnknownResponseException {

        //try {

        System.out.println("Converting "+rawString);
        T pojo = null;
        try {
            pojo = pojoMapper.Convert(rawString);
        } catch (IOException e) {
            UnknownResponseException ux= new UnknownResponseException();
            ux.initCause(e);
            throw ux;
        }

        //HttpCallResult<T> result = new HttpCallResult<T>((T) pojo, call.getCallback());
        return pojo;
    }


        //System.out.println("created a result");
//            return result;
        //} catch (Exception ex) {

        //        //System.out.println("ERROR! " + ex.getMessage());
        //      Log.e(TAG, "Couldn't read " + stringResult);
        //    return new HttpCallResult<T>(new HttpClientErrorResult(ex.getMessage()), call.getCallback());

        //}


        //throw new RuntimeException("Not Implemented Yet");
//        try {
//            Log.w(TAG, "ERROR: " + rawString);
//            //System.out.println(">>>>>>ERROR: " + jsonString) ;
//            // TODO: Make this a generic
//            //JSONObject obj = MapToPojo2<JSONObject>(jsonstring, )
//            JSONObject pojo = new JSONObject(rawString);
//            //JSONObject pojo = MapToPojo2(call, jsonString);
//            System.out.println("JSON Object IS" + pojo);
//            String message = extractMessage(rawString);
//            System.out.println("RESULT IS" + message);
//            //HttpClientErrorResult result = new HttpClientErrorResult(message);
//            // this will be replaced entirely with a generic class.
//            HttpCallResult<T> result = new HttpCallResult<>(pojo, call.getCallback());
//            //result.setRes(pojo);
//            //result.setHttpStatus(statuscode);
//           result;
//
//        } catch (Exception ex) {
//            System.out.println("CAUGHT: " + ex.getMessage());
//            ex.printStackTrace();
//            return new HttpCallResult<T>(new HttpClientErrorResult(ex.getMessage() + " RESULT WAS " + rawString), call.getCallback());
//
//        }
    //}


    /**
     * Parse a json object with "message" key into a string
     * @param jsonString
     * @return
     */
    private String extractMessage(String jsonString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(jsonString, JsonNode.class);
        String result =  rootNode.get("message").getTextValue();

        if (result==null) {
            return jsonString;
        }
        return result;


    }

}