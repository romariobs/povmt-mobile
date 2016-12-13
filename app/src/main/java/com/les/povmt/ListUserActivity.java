package com.les.povmt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.les.povmt.adapter.ActivitiesAdapter;
import com.les.povmt.fragment.ReportFragment;
import com.les.povmt.models.Activity;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.parser.ActivityParser;
import com.les.povmt.parser.InvestedTimeParser;
import com.les.povmt.util.Constants;
import com.les.povmt.util.ImageUtils;
import com.les.povmt.util.Messages;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListUserActivity extends AppCompatActivity {

    private static final String POVMT_PREFS = "POVMT_PREFS";
    public static final String NOTIFY_TI = "NOTIFY_TI";
    private static final int TWENTY_HOURS_IN_MILI = 86400000;
    private final int CREATE_ATIVITY = 1;

    private static String userId;
    private final String TAG = this.getClass().getSimpleName();

    private List<Activity> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;
    private final Context context = this;
    private ProgressDialog loading;
    private CoordinatorLayout coordinatorLayout;

    private final Context mContext = this;

    private Map<Integer, Activity> map;
    private boolean notTiYesterday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            userId = (String) bd.get("id");
        }

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getString(R.string.app_name));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coodinator_layout);

        loading = new ProgressDialog(this);
        loading.setMessage("Loading Accounts");
        loading.show();

        map = new HashMap<>();
        this.activitiesAdapter = new ActivitiesAdapter(getApplicationContext(), activities);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view_trade_in_progress);
        registerForContextMenu(recyclerView);
        this.recyclerView.setAdapter(activitiesAdapter);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        this.recyclerView.setLayoutManager(linearLayoutManager);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return false;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                clear();

                                for (int i = 0; i < reverseSortedPositions.length; i++) {
                                    map.put(reverseSortedPositions[i], activities.remove(reverseSortedPositions[i]));
                                    activitiesAdapter.notifyItemRemoved(reverseSortedPositions[i]);
                                }

                                final int lastPos = reverseSortedPositions[0];

                                Snackbar.make(coordinatorLayout, "Atividade deletada", Snackbar.LENGTH_LONG)
                                        .setCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar snackbar, int event) {
                                                switch (event) {
                                                    // Warning
                                                    // Doesn't change order!!
                                                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                                        for(Iterator<Map.Entry<Integer, Activity>> it = map.entrySet().iterator(); it.hasNext(); ) {
                                                            Map.Entry<Integer, Activity> entry = it.next();

                                                            if (entry.getValue() != null && !entry.getValue().getId().equals(lastPos)) {
                                                                deleteActivity(entry.getValue().getId());
                                                                it.remove();
                                                            }
                                                        }
                                                        break;
                                                }
                                            }
                                        })
                                        .setAction("DESFAZER", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                activities.add(lastPos, map.get(lastPos));
                                                activitiesAdapter.notifyItemInserted(lastPos);
                                                map.remove(lastPos);

                                                clear();

                                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Atividade restaurada", Snackbar.LENGTH_SHORT);
                                                snackbar.show();
                                            }
                                        }).show();

                                activitiesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                return;
                            }
                        });

        if (canNotify() && !hasAlarm() && checkYesterday()) {

            generateNotification();
        }


        recyclerView.addOnItemTouchListener(swipeTouchListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditActivity();
            }
        });
    }

    private void generateNotification() {
        Intent intent = new Intent("BIRL");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 669, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTimeMilliNotification(), TWENTY_HOURS_IN_MILI, pendingIntent);
    }

    private boolean hasAlarm () {
        return PendingIntent.getBroadcast(this, 669, new Intent("BIRL"), PendingIntent.FLAG_NO_CREATE) != null; //Se for null é por que não existe alarme ativo.
    }

    public void ouvindo() {
        Intent intent = new Intent(NOTIFY_TI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendBroadcast(intent);
    }

    private boolean canNotify() {
        SharedPreferences prefs = getSharedPreferences(POVMT_PREFS, MODE_PRIVATE);
        return prefs.getBoolean("habilitaNoti", true);
    }

    public void refresh () {
        PopulateList();
    }

    public long getTimeMilliNotification() {

        SharedPreferences prefs = getSharedPreferences(POVMT_PREFS, MODE_PRIVATE);
        int timeset_hour = prefs.getInt("TimeScheduleHour", 00);
        int timeset_min = prefs.getInt("TimeScheduleMin", 00);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeset_hour);
        calendar.set(Calendar.MINUTE, timeset_min);
        calendar.set(Calendar.SECOND, 00);

        calendar.add(Calendar.SECOND, 3);

        return calendar.getTimeInMillis();
    }

    private boolean checkYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        DateFormat dfServer = new SimpleDateFormat("yyyy-MM-dd");
        String today = dfServer.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String yesterday = dfServer.format(cal.getTime());

        String sampleURL = RestClient.HISTORY_ENDPOINT_URL+"?startDate=";

        sampleURL += yesterday + "&endDate=";
        sampleURL += today + "&creator=";
        sampleURL += User.getCurrentUser().getId() + "&token=";
        sampleURL += RestClient.getToken();


        Response.Listener<String> successListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                InvestedTimeParser investedTimeParser = new InvestedTimeParser();
                List<InvestedTime> lista = investedTimeParser.parse(response);
                notTiYesterday = lista.isEmpty();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notTiYesterday = true;
            }
        };


        RestClient.get(this, sampleURL, successListener, errorListener);

        return notTiYesterday;
    }

    public void clear () {
        for(Iterator<Map.Entry<Integer, Activity>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Activity> entry = it.next();

            if (entry.getValue() != null) {
                deleteActivity(entry.getValue().getId());
                it.remove();
            }
        }
    }

    private void PopulateList() {

        Response.Listener<String> successListner = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                closeDialog();
                ActivityParser dataParser = new ActivityParser();
                activities = dataParser.parse(response);
                activitiesAdapter.update(activities);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeDialog();
                Log.d(TAG, error.toString());
            }
        };

        String url = RestClient.ACTIVITY_ENDPOINT_URL + "?creator=" + userId;
        RestClient.get(context, url, successListner, errorListener);
    }

    private void deleteActivity(final String id) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject json = new JSONObject(response);

                    int status = 0;

                    if (json.has(Constants.TAG_STATUS)) {
                        status = json.getInt(Constants.TAG_STATUS);
                    }

                    ImageUtils.deleteFile(Environment.getExternalStorageDirectory() + File.separator + id +".jpg");

                    if (status != RestClient.HTTP_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListUserActivity.this);
                        builder.setMessage(Messages.DELETE_ACTIVITY_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {

                    Log.e(TAG, e.getMessage());
                }
            };

        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        RestClient.delete(mContext, RestClient.ACTIVITY_ENDPOINT_URL+"/"+ id , responseListener, errorListener);
    }

    private void startEditActivity() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivityForResult(intent, CREATE_ATIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CREATE_ATIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                refresh();
            }
        }
    }

    private void closeDialog() {
        if (loading != null) {
            loading.dismiss();
            loading = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
        closeDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        final View dialogView;
        final AlertDialog alertDialog;
        switch (itemId) {
            case 16908332:
                onBackPressed();
                finish();
                return true;
            case R.id.action_report:
                startReportFragment();
                return true;
            case R.id.action_options:
                return true;
            case R.id.action_set_schedule:
                dialogView = View.inflate(this, R.layout.dialog_notification_schedule, null);
                alertDialog = new AlertDialog.Builder(this).create();

                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.tp_new_time);
                timePicker.setIs24HourView(true);
                SharedPreferences prefs = getSharedPreferences(POVMT_PREFS, MODE_PRIVATE);
                int timeset_hour = prefs.getInt("TimeScheduleHour", 00);
                int timeset_min = prefs.getInt("TimeScheduleMin", 00);

                timePicker.setCurrentHour(timeset_hour);
                timePicker.setCurrentMinute(timeset_min);

                dialogView.findViewById(R.id.bt_dp_set).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String time = String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                        SharedPreferences.Editor editor = getSharedPreferences(POVMT_PREFS, MODE_PRIVATE).edit();
                        editor.putInt("TimeScheduleHour", timePicker.getCurrentHour());
                        editor.putInt("TimeScheduleMin", timePicker.getCurrentMinute());
                        editor.commit();

                        if (canNotify() && checkYesterday()) {
                            cancelAlarm();

                            generateNotification();
                        }
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();

                return true;
            case R.id.action_disable_noti:
                dialogView = View.inflate(this, R.layout.dialog_notification_disable, null);
                alertDialog = new AlertDialog.Builder(this).create();

                dialogView.findViewById(R.id.bt_notification_disable).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = getSharedPreferences(POVMT_PREFS, MODE_PRIVATE).edit();
                        editor.putBoolean("habilitaNoti", false);
                        cancelAlarm();
                        alertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.bt_notification_enable).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = getSharedPreferences(POVMT_PREFS, MODE_PRIVATE).edit();
                        editor.putBoolean("habilitaNoti", true);
                        if (checkYesterday()) {
                            cancelAlarm();

                            generateNotification();
                        }
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelAlarm() {
        Intent intent = new Intent("BIRL");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 669, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void startReportFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ReportFragment profileUserFragment = ReportFragment.newInstance();
        profileUserFragment.show(fragmentTransaction, "ReportsType");
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
}
