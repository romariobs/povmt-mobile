package com.les.povmt;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.models.Activity;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;
import com.les.povmt.util.Constants;
import com.les.povmt.util.Messages;
import com.les.povmt.models.Activity;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterTiActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final String URL_ENDPOINT = "http://povmt.herokuapp.com/it";

    private EditText mDurationTi;
    private Calendar mCalendar;
    private Button mBtAdd;
    private final Context mContext = this;

    private int totalTime;
    private boolean editar;

    private InvestedTime InvestedTime;
    private CheckBox mCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ti);
        mDurationTi = (EditText) findViewById(R.id.editx_duration);
        mCalendar = Calendar.getInstance();
        mBtAdd = (Button) findViewById(R.id.bt_add_ti);
        mCheckbox = (CheckBox) findViewById(R.id.checkBox_yesterday);

        InvestedTime = getIntent().getExtras().getParcelable("activity");

        editar = getIntent().getBooleanExtra("editar", false);
        if(!editar) {
            mBtAdd.setText("ADICIONAR");
        } else {
            mDurationTi.setText(String.format("%02d:%02d", getIntent().getIntExtra("time", 0) / 60, getIntent().getIntExtra("time", 0) % 60));
            mBtAdd.setText("EDITAR");
            setTitle(getIntent().getStringExtra("date"));
        }



        mDurationTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = View.inflate(RegisterTiActivity.this, R.layout.dialog_duration, null);

                final AlertDialog alertDialog = new AlertDialog.Builder(RegisterTiActivity.this).create();

                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.duration_picker);
                timePicker.setIs24HourView(true);
                if(editar){
                    timePicker.setCurrentHour(getIntent().getIntExtra("time", 0) / 60);
                    timePicker.setCurrentMinute(getIntent().getIntExtra("time", 0) % 60);
                } else {
                    timePicker.setCurrentHour(00);
                    timePicker.setCurrentMinute(00);
                }

                dialogView.findViewById(R.id.duration_set).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        totalTime = timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute();
                        mDurationTi.setText(String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDurationTi != null && !mDurationTi.getText().toString().equals("Duração") && !mDurationTi.getText().toString().equals("00:00")) {
                    if (!editar)
                        addInvestedTime();
                    else
                        editInvestedTime();
                } else {
                    Toast.makeText(getApplicationContext(), "Duração deve ser maior que 00:00", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void editInvestedTime() {

        String finalRequest = URL_ENDPOINT + "/" + getIntent().getStringExtra("id");

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

                    if (status == RestClient.HTTP_OK) {
                        finish();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTiActivity.this);
                        builder.setMessage(Messages.CREATE_TI_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {

                    Log.e(TAG, e.getMessage());
                }
            }
        };

        Map<String, String> parameters = new HashMap<>();

        parameters.put(Constants.TAG_DURATION, convertToMinutes()+"");
        parameters.put(Constants.TAG_INVESTEDTIMEAT, getIntent().getStringExtra("id"));
        if (mCheckbox.isChecked()) {
            parameters.put(Constants.TAG_CREATED_AT, getYesterday());
        }
        RestClient.put(getApplicationContext(), RestClient.INVESTED_TIME_ENDPOINT_URL+"/" + getIntent().getStringExtra("idTI"), parameters, responseListener );
    }

    private long convertToMinutes() {
        String[] time;
        if (mDurationTi != null) {
            Log.d("RegisterTiAc", "Olha o tempo "+mDurationTi.getText().toString());
            time = mDurationTi.getText().toString().split(":");
        } else {
            time = "00:00".split(":");
        }

        String hour = time[0];
        String minutes = time[1];

        long tempoMili = TimeUnit.HOURS.toMinutes(Integer.parseInt(hour)) + TimeUnit.MINUTES.toMinutes(Integer.parseInt(minutes));
        Log.d("RegisterTiAc", "minutes: "+tempoMili);

        return tempoMili;



    }

    public void addInvestedTime() {

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTiActivity.this);
                        builder.setMessage(Messages.CREATE_TI_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {

                    Log.e(TAG, e.getMessage());
                }
            }
        };

        Map<String, String> parameters = new HashMap<>();

        parameters.put(Constants.TAG_DURATION, convertToMinutes()+"");
        parameters.put(Constants.TAG_INVESTEDTIMEAT, getIntent().getStringExtra("id"));
        if (mCheckbox.isChecked()) {
            parameters.put(Constants.TAG_CREATED_AT, getYesterday());
        }

        RestClient.post(mContext, RestClient.INVESTED_TIME_ENDPOINT_URL, parameters, responseListener);

    }

    private String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        DateFormat dfServer = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        String yesterday = dfServer.format(cal.getTime());

        Log.d("REGISTER TI", "ontem " +yesterday);
        return yesterday;
    }

    private void closeThis() {
        this.finish();
    }

}
