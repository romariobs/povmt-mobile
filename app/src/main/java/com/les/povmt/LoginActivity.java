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
import com.les.povmt.util.Constants;
import com.les.povmt.util.Messages;

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
 *   "token" : "TOKEN_STRING"
 *   "user": {
 *      "email": "samuel.santos@mail.com",
 *       "name": "samuel santos",
 *       "createdAt": "2016-11-20T14:52:02.080Z",
 *       "updatedAt": "2016-11-20T14:52:02.080Z",
 *      "id": 3
 *  }
 * }
 * @author Samuel T. C. Santos
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

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

                loading.setMessage(Messages.AUTHENTICATING);
                loading.show();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d(TAG, response);
                        try {
                            JSONObject json = new JSONObject(response);

                            int status = 0;

                            if (json.has(Constants.TAG_STATUS)){
                                status = json.getInt(Constants.TAG_STATUS);
                            }

                            if (status == RestClient.HTTP_OK){

                                if (json.has(Constants.TAG_TOKEN)){
                                    String token = json.getString(Constants.TAG_TOKEN);
                                    RestClient.setToken(token);
                                }

                                loading.cancel();
                                Intent accountIntent = new Intent(LoginActivity.this, ListUserActivity.class);

                                JSONObject user = json.getJSONObject(Constants.TAG_USER);
                                accountIntent.putExtra(Constants.TAG_ID, user.getString(Constants.TAG_ID));
                                LoginActivity.this.startActivity(accountIntent);

                                User.setCurrentUser(user.getString(Constants.TAG_ID), user.getString(Constants.TAG_NAME), user.getString(Constants.TAG_EMAIL));
                            }
                            else{
                                loading.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(Messages.AUTH_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                            }

                        }
                        catch (JSONException e){
                            loading.cancel();
                            Log.d(TAG, e.getMessage());
                        }
                    }
                };

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(Constants.TAG_EMAIL, email);
                parameters.put(Constants.TAG_PASSWORD, password);
                RestClient.post(mContext, RestClient.AUTH_USER_ENDPOINT_URL, parameters, responseListener);
            }
        });
    }
}
