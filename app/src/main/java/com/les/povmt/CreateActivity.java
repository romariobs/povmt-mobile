package com.les.povmt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.les.povmt.models.User;
import com.les.povmt.network.VolleySingleton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;


/**
 * Created by cesar on 25/11/16.
 */

public class CreateActivity extends AppCompatActivity {
    private final String apiEndpointUrl = "http://povmt.herokuapp.com/activity";
    private EditText title;
    private MaterialBetterSpinner spn;
    private String priority;
    private EditText description;
    private Button button_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        button_create = (Button) findViewById(R.id.button_create);
        spn = (MaterialBetterSpinner) findViewById(R.id.priority_activity);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listPriority, android.R.layout.simple_spinner_item);

        spn.setAdapter(adapter);


        spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        priority = "LOW";
                        break;
                    case 1:
                        priority = "MEDIUM";
                        break;
                    case 2:
                        priority = "HIGH";
                        break;
                }

            }
        });

        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        title = (EditText) findViewById(R.id.title_activity);
                        description = (EditText) findViewById(R.id.description_activity);

                        if (verifyConditions()) {
                            send();
                            setResult(RESULT_OK);
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
            jsonBody.put("priority", priority);
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

    private boolean verifyConditions(){

        return (!title.getText().toString().trim().isEmpty()) && (!description.getText().toString().trim().isEmpty()) &&
                (priority != null && !priority.trim().isEmpty());

    }
}






