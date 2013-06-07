package com.bridgecanada.net;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 12/05/13
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PojoMapper<T> implements IObjectMapper {

    private Class _destinationClass;

    public PojoMapper(Class t) {

        _destinationClass = t;
    }

    /**
     * Parse a message and return a Pojo
     * @param jsonString
     * @return
     * @throws java.io.IOException
     */
    @Override
    public T Convert(String jsonString) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (jsonString == null || "".equals(jsonString.trim()))    {
            // return an empty class (i.e. nothing set)
            //return (T) objectMapper.readValue("{}", call.getResultClass());
            @SuppressWarnings("unchecked") // Jackson is mapping this
            T result = (T) objectMapper.readValue("{}", _destinationClass);
            return result;
            //return objectMapper.readValue("{}", new TypeReference(_destinationClass) {}););
        } else {
            //return (T) objectMapper.readValue(jsonString, call.getResultClass());
            @SuppressWarnings("unchecked") // Jackson is mapping this
            T result = (T) objectMapper.readValue(jsonString, _destinationClass);
            return result;
        }

    }

    /*
    private <S> S MapToPojo2(RestCall call, String jsonString) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (jsonString == null || "".equals(jsonString.trim()))    {
            //System.out.println("Returning null");
            //return (T) null;
            // return an empty class (i.e. nothing set)
            System.out.println("NULL OR EMPTY");
            return (S) objectMapper.readValue("{}", JSONObject.class);
        }


        S result = (S) objectMapper.readValue(jsonString, JSONObject.class);
        return result;
    }
    */
}
