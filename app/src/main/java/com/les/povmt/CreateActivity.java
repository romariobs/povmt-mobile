package com.les.povmt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.les.povmt.parser.ActivityParser;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.util.Constants;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by cesar on 25/11/16.
 */

public class CreateActivity extends AppCompatActivity {
    private final String apiEndpointUrl = "http://povmt.herokuapp.com/activity";
    private EditText title;
    private MaterialBetterSpinner spn;
    private String priority;
    private RadioGroup group;
    private String category;
    private EditText description;
    private Button button_create;



    private final static String TAG = ActivityParser.class.getSimpleName();
//    private final static String TAG = CreateActivity.class.getSimpleName();
    private final static int HTTP_CREATED = 201;
    private final static String TAG_STATUS = "status";
    private final static String TAG_ACTIVITY = "activity";

    private final Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        title = (EditText) findViewById(R.id.title_activity);
        description = (EditText) findViewById(R.id.description_activity);
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


        group = (RadioGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                category = button.getText().toString();
            }
        });

        button_create = (Button) findViewById(R.id.button_create);
        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {


//                        final ProgressDialog loading = new ProgressDialog(RegisterActivity.this, R.style.AppThemeDarkDialog);
//                        loading.setMessage("Registrando...");
//                        loading.show();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);

                                try {
                                    JSONObject json = new JSONObject(response);

                                    int status = 0;
                                    String activity = "";

                                    if (json.has(TAG_STATUS)) {
                                        status = json.getInt(TAG_STATUS);
                                    }

                                    if (json.has(TAG_ACTIVITY)) {
                                        activity = json.getString(TAG_ACTIVITY);
                                    }

                                    if (status == HTTP_CREATED) {
                                        //loading.cancel();
                                        backToListActivity();
                                        //Log.d(TAG + " created new user ", activity);
                                    } else {
                                        //loading.cancel();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);

                                        builder.setMessage("Failed to create an activity!")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    //loading.cancel();
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        };

                        Map<String, String> parameters = new HashMap<>();

                        parameters.put(Constants.TAG_TITLE, title.getText().toString());
                        parameters.put(Constants.TAG_DESCRIPTION, description.getText().toString());
                        parameters.put(Constants.TAG_CREATOR, User.getCurrentUser().getId().toString());
                        parameters.put(Constants.TAG_PRIORITY, priority);
                        parameters.put(Constants.TAG_CATEGORY, category);

                        RestClient.post(mContext, RestClient.ACTIVITY_ENDPOINT_URL, parameters, responseListener);
                    };

                });

//                        if (verifyConditions()) {
//                            send();
//                            setResult(RESULT_OK);
//                            finish();
//                        } else {
//                            title.setError("Requerido");
//                            description.setError("Requerido");
//
//                        }

    }

    public void send() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title.getText().toString());
            jsonBody.put("description", description.getText().toString());
            jsonBody.put("creator", User.getCurrentUser().getId());
            jsonBody.put("priority", priority);
            jsonBody.put("category", category);
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
                (priority != null && !priority.trim().isEmpty()) && (category != null && !category.trim().isEmpty());

    }

    private void backToListActivity(){

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent listActivityIntent = new Intent(CreateActivity.this, ListUserActivity.class);
                CreateActivity.this.startActivity(listActivityIntent);
            }
        },500);
    }
}





