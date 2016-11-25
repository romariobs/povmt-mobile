package com.les.povmt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.les.povmt.network.LoginRequest;
import com.les.povmt.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *  This class is responsible by Authentication process using the PovMT server.
 *
 *  Auth response example:
 *  {
 *   "status": 200,
 *   "user": {
 *      "email": "samuel.santos@mail.com",
 *       "name": "samuel santos",
 *       "createdAt": "2016-11-20T14:52:02.080Z",
 *       "updatedAt": "2016-11-20T14:52:02.080Z",
 *      "id": 3
 *  }
 } @author Samuel T. C. Santos
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_STATUS = "status";
    private static final String TAG_USER = "user";

    private static final String debugEmail = "samuel.santos@mail.com";
    private static final String debugPassword = "admin#123";

    private static final int HTTP_OK = 200;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.etUserEmail);
        etPassword = (EditText) findViewById(R.id.etUserPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        etEmail.setText(debugEmail);
        etPassword.setText(debugPassword);

        tvRegister = (TextView) findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d(TAG, response);

                        try {
                            JSONObject json = new JSONObject(response);

                            int status = 0;
                            String user = "";

                            if (json.has(TAG_STATUS)){
                                status = json.getInt(TAG_STATUS);
                            }

                            if (json.has(TAG_USER)){
                                user = json.getString(TAG_USER);
                            }

                            if (status == HTTP_OK){
                                Intent accountIntent = new Intent(LoginActivity.this, PovMTActivity.class);
                                LoginActivity.this.startActivity(accountIntent);
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Fail trying authentication with server!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        }
                        catch (JSONException e){
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(email, password,responseListener);
                VolleySingleton.getInstance(mContext).addToRequestQueue(loginRequest);
            }
        });
    }
}
