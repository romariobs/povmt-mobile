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

import com.android.volley.Response;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.util.Constants;
import com.les.povmt.util.Messages;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Class to create new activities.
 *
 * @author Cesar
 */

public class CreateActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private EditText title;
    private MaterialBetterSpinner spn;
    private String priority;
    private RadioGroup group;
    private String category;
    private EditText description;
    private Button button_create;
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
                        priority = Constants.LOW;
                        break;
                    case 1:
                        priority = Constants.MEDIUM;
                        break;
                    case 2:
                        priority = Constants.HIGH;
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
                //Handle the case where the user don't select any thing
                //In this case no put the parameter to send in the request.
                if (category.equals("Trabalho")){
                    category = Constants.WORK;
                }
                else {
                    category = Constants.LEISURE;
                }
                //Check this better!
            }
        });

        //Rename to this use camelcase ... should be buttonCreate
        button_create = (Button) findViewById(R.id.button_create);

        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        if (verifyConditions()){
                            onBackPressed();
                        }

                    };

                });

    }

    private boolean verifyConditions(){

        return  (!title.getText().toString().trim().isEmpty()) &&
                (!description.getText().toString().trim().isEmpty()) &&
                (priority != null && !priority.trim().isEmpty()) &&
                (category != null && !category.trim().isEmpty());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        createActivity();
        Intent intent = new Intent(this, ListUserActivity.class);
        startActivity(intent);
    }

    private void createActivity(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject json = new JSONObject(response);

                    int status = 0;
                    String activity = "";

                    if (json.has(Constants.TAG_STATUS)) {
                        status = json.getInt(Constants.TAG_STATUS);
                    }

                    if (json.has(Constants.TAG_ACTIVITY)) {
                        activity = json.getString(Constants.TAG_ACTIVITY);
                    }

                    if (status == RestClient.HTTP_CREATED) {
                        finish();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                        builder.setMessage(Messages.CREATE_ACTIVITY_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {

                    Log.e(TAG, e.getMessage());
                }
            }
        };

        Map<String, String> parameters = new HashMap<>();

        parameters.put(Constants.TAG_TITLE, title.getText().toString());
        parameters.put(Constants.TAG_DESCRIPTION, description.getText().toString());
        parameters.put(Constants.TAG_CREATOR, User.getCurrentUser().getId());
        parameters.put(Constants.TAG_PRIORITY, priority);
        parameters.put(Constants.TAG_CATEGORY, category);

        RestClient.post(mContext, RestClient.ACTIVITY_ENDPOINT_URL, parameters, responseListener);

    }
}





