package com.les.povmt;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class RegisterTiActivity extends AppCompatActivity {

    private static final String URL_ENDPOINT = "http://povmt.herokuapp.com/it";

    private EditText mDurationTi;
    private Calendar mCalendar;
    private Button mBtAdd;

    private int totalTime;
    private boolean editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ti);
        mDurationTi = (EditText) findViewById(R.id.editx_duration);
        mCalendar = Calendar.getInstance();
        mBtAdd = (Button) findViewById(R.id.bt_add_ti);

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
                if(mDurationTi != null && !mDurationTi.getText().toString().equals("00:00")) {
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
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("duration", totalTime);
            jsonBody.put("investedTimeAt", getIntent().getStringExtra("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activitiesRequest = new JsonObjectRequest(Request.Method.PUT, URL_ENDPOINT + "/" + getIntent().getStringExtra("idTI"), jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        closeThis();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddError", error.toString());
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(activitiesRequest);
    }

    public void addInvestedTime() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("duration", totalTime);
            jsonBody.put("investedTimeAt", getIntent().getStringExtra("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activitiesRequest = new JsonObjectRequest(Request.Method.POST, URL_ENDPOINT, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject json = response;
                        closeThis();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddError", error.toString());
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(activitiesRequest);
    }

    private void closeThis() {
        this.finish();

    }


}
