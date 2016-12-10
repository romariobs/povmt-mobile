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

    private static final String AUTH_URL = "http://povmt.herokuapp.com/user/auth";
    private Map<String, String> parameters;

    private static final String KEY_EMAIL = "email";
    private static final String KEY_APP_ID = "appId";

    private final String clientId = "0068c83491184dbc83539233890424fd30cbfb4b9716493eb68ef3" +
            "f9c6ad818755b32f573aff4ca9857d584d0628f21c0e12b5894d2a430781c0848a31aab51f";

    public LoginRequest(String email, Response.Listener<String> listener){
        super(Request.Method.POST, AUTH_URL, listener, null);

        parameters = new HashMap<>();
        parameters.put(KEY_EMAIL, email);
        parameters.put(KEY_APP_ID, clientId);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
