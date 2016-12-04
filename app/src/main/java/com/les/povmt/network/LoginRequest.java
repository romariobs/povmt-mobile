package com.les.povmt.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Request login logic
 *
 * Created by Samuel T. C. Santos on 23/11/2016.
 */

public class LoginRequest extends StringRequest {


    private Map<String, String> parameters;

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    public LoginRequest(String email, String password, Response.Listener<String> listener){
        super(Request.Method.POST, RestClient.AUTH_USER_ENDPOINT_URL, listener, null);

        parameters = new HashMap<>();
        parameters.put(KEY_EMAIL, email);
        parameters.put(KEY_PASSWORD, password);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
