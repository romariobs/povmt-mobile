package com.les.povmt.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>DeleteRequest</code>  perform a DELETE request to our web service to remove specific resources from server.
 *
 * @author Samuel T. C. Santos
 */
public class DeleteRequest extends StringRequest {

    public DeleteRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
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
