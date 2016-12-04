package com.les.povmt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

                final ProgressDialog loading = new ProgressDialog(LoginActivity.this, R.style.AppThemeDarkDialog);

                loading.setMessage("Autenticando...");
                loading.show();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d(TAG, response);
                        try {
                            JSONObject json = new JSONObject(response);

                            int status = 0;

                            if (json.has(TAG_STATUS)){
                                status = json.getInt(TAG_STATUS);
                            }

                            if (status == RestClient.HTTP_OK){
                                loading.cancel();
                                Intent accountIntent = new Intent(LoginActivity.this, ListUserActivity.class);

                                JSONObject user = json.getJSONObject("user");
                                accountIntent.putExtra("id", user.getString("id") );
                                LoginActivity.this.startActivity(accountIntent);

                                User.setCurrentUser(user.getString("id"), user.getString("name"), user.getString("email"));
                            }
                            else{
                                loading.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Fail trying authentication with server!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        }
                        catch (JSONException e){
                            loading.cancel();
                            Log.d(TAG, e.getMessage());
                        }
                    }
                };

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);
                parameters.put("password", password);
                RestClient.post(mContext, RestClient.AUTH_USER_ENDPOINT_URL, parameters, responseListener);
            }
        });
    }
}
