package com.les.povmt.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>UpdateRequest</code>  perform a PUT request to our web service send data to update a resource from server.
 *
 * @author Samuel T. C. Santos
 */

public class UpdateRequest extends StringRequest {

    public UpdateRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
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
