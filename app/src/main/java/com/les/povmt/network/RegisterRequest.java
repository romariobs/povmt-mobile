package com.les.povmt.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide a service that build requests to register users on server.
 *
 * Created by Samuel T. C. Santos on 23/11/2016.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_URL = "http://povmt.herokuapp.com/user/";

    private final String KEY_NAME = "name";
    private final String KEY_EMAIL = "email";
    private final String KEY_PASSWORD = "password";

    private Map<String, String> parameters;

    /**
     * Instantiate a new Register Request.
     *
     * @param name - the value to parameter name.
     * @param email - the value to parameter email.
     * @param password - the value to parameter password.
     * @param role - the value to parameter role.
     * @param listener - the Listener to handler server response.
     */
    public RegisterRequest(String name, String email, String password, String role, Response.Listener<String> listener){
        super(Method.POST, REGISTER_URL, listener,null);
        parameters = new HashMap<>();
        parameters.put(KEY_NAME, name);
        parameters.put(KEY_EMAIL, email);
        parameters.put(KEY_PASSWORD, password);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
