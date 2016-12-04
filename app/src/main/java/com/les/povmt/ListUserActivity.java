package com.les.povmt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.adapter.ActivitiesAdapter;
import com.les.povmt.fragment.ReportFragment;
import com.les.povmt.models.Activity;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListUserActivity extends AppCompatActivity {

    private final int CREATE_ATIVITY = 1;

    private static String userId;
    private final String TAG = this.getClass().getSimpleName();

    private List<Activity> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;
    private final Context context = this;
    private ProgressDialog loading;
    private CoordinatorLayout coordinatorLayout;

    private Map<Integer, Activity> map;

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

        recyclerView.addOnItemTouchListener(swipeTouchListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditActivity();
            }
        });
    }

    public void refresh () {
        PopulateList();
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

    private void deleteActivity(String id) {
        StringRequest activitiesRequest = new StringRequest(Request.Method.DELETE, RestClient.ACTIVITY_ENDPOINT_URL + "/" + id, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(activitiesRequest);
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
        switch (itemId) {
            case 16908332:
                onBackPressed();
                finish();
                return true;
            case R.id.action_report:
                startReportFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
