package com.bridgecanada.net;

import android.util.Log;

public class StringResponseHandler implements IResponseHandler<String> {

    private final String TAG = getClass().getSimpleName();

    //private final PojoMapper<T> pojoMapper;

    //public StringResponseHandler(PojoMapper<T> pojoMapper) {
    //    this.pojoMapper = pojoMapper;
    //}

    @Override
    public String processResponse(int status, String stringResult) throws UnknownResponseException {

        System.out.println("processSuccess got status " + status + " with " + stringResult);
        return stringResult;
        //return new HttpCallResult(stringResult, call.getCallback());
        //try {

            //System.out.println("Convert");
            //T pojo = pojoMapper.Convert(call, stringResult);
            //System.out.println("returning new httpcallresult");
            //HttpCallResult<T> result = new HttpCallResult<T>((T) pojo, call.getCallback());



            //System.out.println("created a result");
//            return result;
  //      } catch (Exception ex) {

    //        //System.out.println("ERROR! " + ex.getMessage());
      //      Log.e(TAG, "Couldn't read " + stringResult);
        //    return new HttpCallResult<T>(new HttpClientErrorResult(ex.getMessage()), call.getCallback());

        //}
    }
}