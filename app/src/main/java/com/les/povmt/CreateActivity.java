package com.les.povmt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.models.Activity;
import com.les.povmt.models.User;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * Created by cesar on 25/11/16.
 */

public class CreateActivity extends AppCompatActivity {

    private final String apiEndpointUrl = "http://povmt.herokuapp.com/activity";
    private EditText title;
    private EditText description;
    private Button button_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);
        button_create = (Button) findViewById(R.id.button_create);


        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        title = (EditText) findViewById(R.id.title_activity);
                        description = (EditText) findViewById(R.id.description_activity);

                        if ((!title.getText().toString().trim().isEmpty()) &&
                                (!description.getText().toString().trim().isEmpty())) {
                            send();
                            finish();
                        }else{
                            title.setError("Requerido");
                            description.setError("Requerido");
                        }
                    }
                });






        }

    public void send() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title.getText().toString());
            jsonBody.put("description", description.getText().toString());
            jsonBody.put("creator", User.getCurrentUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activitiesRequest = new JsonObjectRequest(Request.Method.POST, apiEndpointUrl, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(activitiesRequest);
    }

}






