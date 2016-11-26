package com.les.povmt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.les.povmt.models.User;
import com.les.povmt.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cesar on 26/11/16.
 */

public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Button button_create;
    private final String apiEndpointUrl = "http://povmt.herokuapp.com/activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        title = (EditText) findViewById(R.id.title_activity);
        description = (EditText) findViewById(R.id.description_activity);
        button_create = (Button) findViewById(R.id.button_create);

        button_create.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    edit();
                    finish();
                }
            });
    }

    public void edit() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title.getText().toString());
            jsonBody.put("description", description.getText().toString());
            jsonBody.put("creator", User.getCurrentUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activitiesRequest = new JsonObjectRequest(Request.Method.PUT, apiEndpointUrl+"/"+getIntent().getStringExtra("id"), jsonBody,
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
