package com.les.povmt.network;



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.util.Constants;

import java.util.HashMap;
import java.util.Map;



import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cesar on 09/12/16.
 */


public class PutRequest extends StringRequest{

    private Map<String, String> parameters;

    /**
     * Perform a GET request to our web service passing authorization token in the request headers.
     *
     * @param method - the method post identifier
     * @param parameters - the data to transfer to server.
     * @param url - the address in the web service to send data.
     * @param listener - the listener that handle the server response.
     * @param errorListener - the listener thar handle when something is wrong.
     */
    public PutRequest(int method, Map<String, String> parameters, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.parameters = parameters;
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String authorization = "Bearer " + RestClient.getToken();
        headers.put(Constants.AUTHORIZATION_HEADER, authorization);
        return headers;
    }
}
