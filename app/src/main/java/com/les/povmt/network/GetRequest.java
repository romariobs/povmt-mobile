package com.les.povmt.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>GetRequest</code>  perform a request to our web service retrieving data from a specific endpoint.
 *
 * @author Samuel T. C. Santos
 *
 */
public class GetRequest extends StringRequest {

    /**
     * Perform a GET request to our web service passing authorization token in the request headers.
     *
     * @param method - the method GET
     * @param url - the endpoint to request data
     * @param listener - the listener that handle success response
     * @param errorListener - the listener that handle the wrong responses from server.
     */
    public GetRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String authorization = "Bearer " + RestClient.getToken();
        headers.put(Constants.AUTHORIZATION_HEADER, authorization);
        return headers;
    }

}
