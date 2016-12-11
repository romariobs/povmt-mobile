package com.les.povmt;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.les.povmt.models.User;
import com.les.povmt.network.LoginRequest;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.notification.NotificationEventReceiver;
import com.les.povmt.util.Constants;
import com.les.povmt.util.Messages;

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

    private static final int HTTP_OK = 200;
    private static final int HTTP_CREATED = 201;

    private final Context mContext = this;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1001;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            loading = new ProgressDialog(LoginActivity.this, R.style.AppThemeDarkDialog);
            loading.setMessage("Autenticando...");
            loading.show();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
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

            LoginRequest loginRequest = new LoginRequest(result.getSignInAccount().getEmail(),responseListener);
            VolleySingleton.getInstance(mContext).addToRequestQueue(loginRequest);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Erro ao obter dados do Google!")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }
    }
}
