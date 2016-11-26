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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.adapter.ActivitiesAdapter;
import com.les.povmt.fragment.ReportFragment;
import com.les.povmt.models.Activity;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;

import java.util.ArrayList;
import java.util.List;

public class ListUserActivity extends AppCompatActivity {

    private final String apiEndpointUrl = "http://povmt.herokuapp.com/activity";
    private final String TAG = this.getClass().getSimpleName();

    private List<Activity> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;
    private final Context context = this;
    private ProgressDialog loading;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getString(R.string.app_name));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coodinator_layout);;

        loading = new ProgressDialog(this);
        loading.setMessage("Loading Accounts");
        loading.show();

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
                                int pos = 0;
                                Activity activity = null;

                                for (int position : reverseSortedPositions) {
                                    activity = activities.remove(position);
                                    activitiesAdapter.notifyItemRemoved(position);
                                    pos = position;
                                    break;
                                }

                                final int fPos = pos;
                                final Activity fActivity = activity;

                                Snackbar.make(coordinatorLayout, "Atividade deletada", Snackbar.LENGTH_LONG)
                                        .setCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar snackbar, int event) {
                                                switch (event) {
                                                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                                        activities.add(fPos, fActivity);
                                                        activitiesAdapter.notifyItemInserted(fPos);
                                                        break;
                                                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                                        //TODO Send server delete
                                                        deleteActivity(fActivity.getId());
                                                        break;
                                                }
                                            }
                                        })
                                        .setAction("DESFAZER", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
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

        PopulateList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ListUserActivity.this, "Cesar", Toast.LENGTH_SHORT).show();
                startCreateEditActivity();
            }
        });
    }

    private void PopulateList() {
        StringRequest activitiesRequest = new StringRequest(Request.Method.GET, apiEndpointUrl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                closeDialog();
                ActivityParser dataParser = new ActivityParser();
                activities = dataParser.parse(response);
                activitiesAdapter.update(activities);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeDialog();
                Log.d(TAG, error.toString());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(activitiesRequest);
    }

    private void deleteActivity(String id) {
        StringRequest activitiesRequest = new StringRequest(Request.Method.DELETE, apiEndpointUrl + "/" + id, new Response.Listener<String>(){
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

    private void startCreateEditActivity() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
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
            case R.id.action_options:
                Toast.makeText(ListUserActivity.this, "Options", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startReportFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ReportFragment profileUserFragment = ReportFragment.newInstance();
        profileUserFragment.show(fragmentTransaction, "ReportsType");
    }


}
