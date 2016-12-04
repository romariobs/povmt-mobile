package com.les.povmt.network;

/**
 * Rest Client perform asynchronous communication with our web service
 * abstract basic rest interface to methods GET, POST, UPDATE and DELETE and keep
 * the current authorization token between requests.
 *
 * We're declaring here each endpoint to call from web service.
 *
 * @author Samuel T. C. Santos
 */

public class RestClient {

    /**
     * Define our web service address for production (external deploy)
     */
    public final String SERVER_URL = "https://povmt.herokuapp.com";

    /**
     * API endpoint to access user services
     */
    public final String USER_ENDPOINT_URL = SERVER_URL + "/user";
    /**
     * API endpoint to access activity services.
     */
    public final String ACTIVITY_ENDPOINT_URL = SERVER_URL + "/activity";

    /**
     * API endpoint to access invested time services .
     */
    public final String INVESTED_TIME_ENDPOINT_URL = SERVER_URL + "/its";

    /**
     * Getting date from out web service.
     *
     * @param url - the address to call in the server
     */
    public void get(String url){

    }

    /**
     * Sending data to our web service.
     *
     * @param url - the address to call in the server
     */
    public void post(String url){

    }

    /**
     * Updating resources in our web services
     *
     * @param url - the address to call in the server
     */
    public void put(String url){

    }

    /**
     * Deleting resouces into our web services.
     *
     * @param url - the address to call in the server
     */
    public void delete(String url){

    }
}
