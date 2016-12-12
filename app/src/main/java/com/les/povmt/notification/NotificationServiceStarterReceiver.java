package com.les.povmt.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by felipe on 12/11/16.
 */

public final class NotificationServiceStarterReceiver extends BroadcastReceiver {
    private static Context context;

    public static void setupAlarm(Context applicationContext) {
        context = applicationContext;
        // TODO VERIFICAR SE ESTÁ HABILITADO A NOTIFICAÇÃO
        // TODO VERIFICAR A HORA DA NOTIFICAÇÃO
        verifyRegisterYesterday();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    private static void showNotification(boolean addYesterday) {
        if(addYesterday){
            Intent intent = new Intent();
            intent.putExtra("time", getTimeMilliNotification());
            NotificationEventReceiver.setupAlarm(context, intent);
        }
    }

    static void verifyRegisterYesterday(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        DateFormat dfServer = new SimpleDateFormat("yyyy-MM-dd");
        String today = dfServer.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String yesterday = dfServer.format(cal.getTime());

        serverRequest(yesterday, today);
    }

    static void serverRequest(String yesterday, String today){
        String sampleURL = "http://povmt.herokuapp.com/history?startDate=";

        sampleURL += yesterday + "&endDate=";
        sampleURL += today + "&creator=";
        sampleURL += User.getCurrentUser().getId() + "&token=";
        sampleURL += RestClient.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sampleURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json;
                try {
                    json = new JSONObject(response);
                    int status = 0;

                    if (json.has("status")){
                        status = json.getInt("status");
                    }

                    if (status != HTTP_OK) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage(status).setNegativeButton("ok", null)
                                .create().show();
                    }

                    JSONObject group = json.getJSONObject("history").getJSONArray("groupedHistory")
                            .optJSONObject(0);

                    if (group!= null) {
                        JSONArray arrayIts = group.getJSONArray("its");

                        int arraySize = arrayIts != null ? arrayIts.length() : 0;
                        if(arraySize > 0)
                            showNotification(false);
                    } else
                        showNotification(true);

                } catch (JSONException e){
                    System.out.println(response);
                    Log.e("JSON","FAILED");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showNotification(true);
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public static long getTimeMilliNotification() {
        String POVMT_PREFS = "POVMT_PREFS";
        SharedPreferences prefs = context.getSharedPreferences(POVMT_PREFS, context.MODE_PRIVATE);
        int timeset_hour = prefs.getInt("TimeScheduleHour", 00);
        int timeset_min = prefs.getInt("TimeScheduleMin", 00);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeset_hour);
        calendar.set(Calendar.MINUTE, timeset_min);
        calendar.set(Calendar.SECOND, 00);

        return calendar.getTimeInMillis();


    }
}
