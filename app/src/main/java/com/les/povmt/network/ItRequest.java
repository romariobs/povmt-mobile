package com.les.povmt.network;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by romario on 11/26/2016.
 */

public class ItRequest {
    private String URL = "http://povmt.herokuapp.com/its";

    public ItRequest() {}

    public void getIt(int duration, int investedTime) {
        try {
            //WIP not working yet
            final JSONObject jsonBody = new JSONObject("{\"duration\": "+duration+", "+ "\"investedTimeAt\": " +investedTime+"}");
            JsonObjectRequest response = new JsonObjectRequest(URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.print("on Response");
                }
            });
        } catch (JSONException e) {
            System.out.print("JSON ERROR");
        }
    }

}
