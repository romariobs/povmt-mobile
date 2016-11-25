package com.les.povmt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import com.android.volley.Response;
import com.les.povmt.network.RegisterRequest;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = RegisterActivity.class.getSimpleName();
    private Context context = null;
    private final static int HTTP_CREATED = 201;
    private final static String TAG_STATUS = "status";
    private final static String TAG_USER = "user";

    private final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword  = (EditText) findViewById(R.id.etPassword);

        final Button btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String name = etName.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        try {
                            JSONObject json = new JSONObject(response);

                            int status = 0;
                            String user = "";

                            if (json.has(TAG_STATUS)) {
                                status = json.getInt(TAG_STATUS);
                            }

                            if (json.has(TAG_USER)) {
                                user = json.getString(TAG_USER);
                            }

                            if (status == HTTP_CREATED) {
                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(loginIntent);
                                Log.d(TAG + " created new user ", user);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                                builder.setMessage("Fail registering user account!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, email, password, responseListener);
                VolleySingleton.getInstance(mContext).addToRequestQueue(registerRequest);

            };

        });
    }
}
