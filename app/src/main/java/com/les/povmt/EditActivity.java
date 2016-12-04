package com.les.povmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.les.povmt.models.Activity;
import com.les.povmt.models.User;
import com.les.povmt.network.VolleySingleton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cesar on 26/11/16.
 */

public class EditActivity extends AppCompatActivity {

    private final String apiEndpointUrl = "http://povmt.herokuapp.com/activity";
    private EditText title;
    private MaterialBetterSpinner spn;
    private String priority;
    private EditText description;
    private Button button_create;
    private int position;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);


        title = (EditText) findViewById(R.id.title_activity);
        description = (EditText) findViewById(R.id.description_activity);
        button_create = (Button) findViewById(R.id.button_create);
        spn = (MaterialBetterSpinner) findViewById(R.id.priority_activity);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listPriority, android.R.layout.simple_spinner_item);
        spn.setAdapter(adapter);
        activity = getIntent().getExtras().getParcelable("activity");
        title.setText(activity.getTitle());
        description.setText(activity.getDescription());

        priority = activity.getPriority();
        //Log.i(priority, "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//        switch (priority) {
//            case "LOW":
//                position = 0;
//                break;
//            case "MEDIUM":
//                position = 1;
//                break;
//            case "HIGH":
//                position = 2;
//                break;
//
//        }

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

                    if (verifyConditions()) {
                        edit();
                        setResult(RESULT_OK);
                        activity.setTitle(title.getText().toString());
                        //activity.setPriority(priority);
                        activity.setDescription(description.getText().toString());
                       onBackPressed();
                    }else{
                        title.setError("Requerido");
                        description.setError("Requerido");
                    }
                }
            });
    }

    public void edit() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title.getText().toString());
            jsonBody.put("description", description.getText().toString());
            jsonBody.put("creator", User.getCurrentUser().getId());
            //jsonBody.put("priority", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activitiesRequest = new JsonObjectRequest(Request.Method.PUT, apiEndpointUrl+"/"+ activity.getId(), jsonBody,
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplication(), ActivityProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("activity", activity);
        getApplication().startActivity(intent);
    }

    private boolean verifyConditions(){

        return (!title.getText().toString().trim().isEmpty()) && (!description.getText().toString().trim().isEmpty()) &&
                (priority != null && !priority.trim().isEmpty());

    }
}
