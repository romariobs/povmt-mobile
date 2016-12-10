package com.les.povmt.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Rest Client perform asynchronous communication with our web service
 * abstract basic rest interface to methods GET, POST, UPDATE and DELETE and keep
 * the current authorization token between requests.
 *
 * We're declaring here each endpoint to call from web service and HTTP response status code.
 *
 * @author Samuel T. C. Santos
 */

public class RestClient {

    /**
     * Define our web service address for production (external deploy)
     */
    public static final String SERVER_URL = "https://povmt.herokuapp.com";

    /**
     * API endpoint to access user services
     */
    public static final String USER_ENDPOINT_URL = SERVER_URL + "/user";

    /**
     * API endpoint for request the authentication token and authorize the next api calls.
     */
    public static final String AUTH_ENDPOINT_URL = USER_ENDPOINT_URL + "/auth";

    /**
     * API endpoint to access activity services.
     */
    public static final String ACTIVITY_ENDPOINT_URL = SERVER_URL + "/activity";

    /**
     * API endpoint to access invested time services .
     */
    public static final String INVESTED_TIME_ENDPOINT_URL = SERVER_URL + "/its";

    /**
     * Authorization token used to authenticate api calls.
     */
    public static String AUTHORIZATION_TOKEN = "";

    /**
     * HTTP status code received after server respond the request successfully.
     */
    public static final int HTTP_OK = 200;

    public static final int HTTP_CREATED = 201;

    /**
     * Getting date from out web service.
     *
     * @param ctx - the current activity context.
     * @param url - the address to call in the server
     * @param successListener - the listener to handle the success response
     * @param errorListener - the listener to handle the fail response
     */
    public static void get(Context ctx,String url, Response.Listener successListener, Response.ErrorListener errorListener){
        GetRequest request = new GetRequest(Request.Method.GET, url, successListener, errorListener);
        VolleySingleton.getInstance(ctx).addToRequestQueue(request);
    }

    /**
     * Sending data to our web service.
     *
     * @param ctx - the current activity context.
     * @param url - the address to call in the server.
     * @param parameters - the parameters to send.
     * @param listener - the listener to handle the server response.
     */
    public static void post(Context ctx, String url, Map<String,String> parameters, Response.Listener<String> listener){
        PostRequest request = new PostRequest(Request.Method.POST, parameters, url, listener, null);
        VolleySingleton.getInstance(ctx).addToRequestQueue(request);
    }

    /**
     * Updating resources in our web services
     *
     * @param ctx - the current activity context.
     * @param url - the address to call in the server
     */
    public static void put(Context ctx, String url, Map<String,String> parameters, Response.Listener<String> listener){
        PutRequest request = new PutRequest(Request.Method.PUT, parameters, url, listener, null);
        VolleySingleton.getInstance(ctx).addToRequestQueue(request);
    }

    /**
     * Deleting resouces into our web services.
     *
     * @param ctx - the current activity context.
     * @param url - the address to call in the server
     */
    public static void delete(Context ctx, String url){

    }

    /**
     * Retrieve the current authorization token.
     *
     * @return the authorization token.
     */
    public static String getToken(){
        return AUTHORIZATION_TOKEN;
    }

    /**
     * Change the current authorization token.
     *
     * @param token - The current authorization token.
     */
    public static void setToken(String token){
        RestClient.AUTHORIZATION_TOKEN = token;
    }

}
