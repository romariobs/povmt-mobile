package com.les.povmt.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.PieEntry;
import com.les.povmt.DividerItemDecoration;
import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.SwipeableRecyclerViewTouchListener;
import com.les.povmt.adapter.ITListAdapter;
import com.les.povmt.adapter.RankingAdapter;
import com.les.povmt.models.Activity;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.RankingItem;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;
import com.les.povmt.parser.InvestedTimeParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ITListFragment extends Fragment {
    private ITListAdapter itList;
    private List<InvestedTime> investedTimes = new ArrayList<>();
    private RecyclerView recyclerView;
    private String idActivity;

    private final String apiEndpointUrl = "http://povmt.herokuapp.com/it";

    private FloatingActionButton addFab;

    public ITListFragment(String idActivity){
        this.idActivity = idActivity;
        populateList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frament_it_list, container, false);

        this.itList = new ITListAdapter(getContext(), investedTimes, this);

        this.recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        registerForContextMenu(recyclerView);
        this.recyclerView.setAdapter(itList);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) ((android.app.Activity) getContext()).findViewById(R.id.coodinator_layout);

        addFab = (FloatingActionButton) v.findViewById(R.id.addFab);
        itList.update(investedTimes);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itList.getCurrentAction().equals(ITListAdapter.AdapterAction.CREATE)) {
                    Intent intent = new Intent(getContext(), RegisterTiActivity.class);
                    intent.putExtra("editar", false);
                    intent.putExtra("id", idActivity);
                    getContext().startActivity(intent);
                } else {
                    for (String i : itList.getListSelected()){
                        deleteIT(Integer.valueOf(i));
                    }
                }
            }
        });

        return v;
    }

    private void populateList() {
        String finalRequest = apiEndpointUrl + "?timeInvestedAt=" + idActivity;
        StringRequest activitiesRequest = new StringRequest(Request.Method.GET, finalRequest, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("lucas", response);
                InvestedTimeParser dataParser = new InvestedTimeParser();
                investedTimes = dataParser.parse(response);
                Collections.sort(investedTimes, new Comparator<InvestedTime>() {
                    @Override
                    public int compare(InvestedTime it, InvestedTime it1) {
                        if (it.getOriginalDate().after(it1.getOriginalDate())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                itList.update(investedTimes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("lucas", error.toString());
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(activitiesRequest);
    }

    private void deleteIT(Integer i) {
        final int j = i;
        String finalRequest = apiEndpointUrl + "/" + investedTimes.get(i).getId();
        StringRequest activitiesRequest = new StringRequest(Request.Method.DELETE, finalRequest, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("lucas", response);
                investedTimes.remove(j);
                itList.update(investedTimes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("lucas", error.toString());
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(activitiesRequest);

    }
}
