package com.les.povmt.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * <code>PostRequest</code>  perform a request to our web service send data to create a new resource in the server.
 *
 * @author Samuel T. C. Santos
 */
public class PostRequest extends StringRequest {

    private Map<String, String> parameters;

    /**
     * Create a generic method to perform HTTP POST requests to our web service.
     *
     * @param method - the method post identifier
     * @param parameters - the data to transfer to server.
     * @param url - the address in the web service to send data.
     * @param listener - the listener that handle the server response.
     * @param errorListener - the listener thar handle when something is wrong.
     */
    public PostRequest(int method, Map<String, String> parameters, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        this.parameters = parameters;
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}