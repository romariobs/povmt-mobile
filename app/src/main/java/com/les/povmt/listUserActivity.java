package com.les.povmt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.les.povmt.adapter.ActivitiesAdapter;
import com.les.povmt.fragment.ReportFragment;
import com.les.povmt.models.Activity;

import java.util.ArrayList;
import java.util.List;

public class ListUserActivity extends AppCompatActivity {
    private List<Activity> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;

    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getString(R.string.app_name));

        this.activitiesAdapter = new ActivitiesAdapter(getApplicationContext(), activities);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view_trade_in_progress);
        registerForContextMenu(recyclerView);
        this.recyclerView.setAdapter(activitiesAdapter);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        //TODO THE MAGICIAN
        activities.add(new Activity("Title", "description"));
        activities.add(new Activity("Title", "description"));
        activities.add(new Activity("Title", "description"));
        activities.add(new Activity("Title", "description"));
        activities.add(new Activity("Title", "description"));
        activities.add(new Activity("Title", "description"));
        activities.add(new Activity("Title", "description"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ListUserActivity.this, "Cesar", Toast.LENGTH_SHORT).show();
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
            }
        });

        activitiesAdapter.update(activities);
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
