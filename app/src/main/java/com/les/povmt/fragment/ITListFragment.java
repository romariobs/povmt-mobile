package com.les.povmt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.les.povmt.DividerItemDecoration;
import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.adapter.ITListAdapter;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.network.RestClient;
import com.les.povmt.parser.InvestedTimeParser;
import com.les.povmt.util.Constants;
import com.les.povmt.util.Messages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ITListFragment extends Fragment {
    private ITListAdapter itList;
    private List<InvestedTime> investedTimes = new ArrayList<>();
    private RecyclerView recyclerView;
    private String idActivity;

    private final String TAG = this.getClass().getSimpleName();

    private final String apiEndpointUrl = "http://povmt.herokuapp.com/it";

    private FloatingActionButton addFab;

    private int delReq = 0;
    private int delReqCompleted = 0;

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
                    int size = itList.getSelected().size();
                    List<String> list = itList.getSelected();

                    delReq = size;
                    delReqCompleted = 0;

                    for(int i = size - 1; i >= 0 ; i--) {
                        deleteIT(list.get(i));

                        for (int j = 0; j < investedTimes.size(); j++) {
                            if (investedTimes.get(j).getId() == list.get(i)) {
                                investedTimes.remove(j);
                                break;
                            }
                        }
                    }
//                    for (String i : itList.clearSelected()){
//                        deleteIT(Integer.valueOf(i));
//                    }
                }
            }
        });

        return v;
    }

    private void populateList() {
        String finalRequest = apiEndpointUrl + "?investedTimeAt=" + idActivity;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, response);

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

                        //finish();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        RestClient.get(getContext(), finalRequest, responseListener, errorListener);
    }

    private void deleteIT(String id) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                if (++delReqCompleted == delReq) {
                    itList.clearSelected();
                    itList.update(investedTimes);
                    Toast.makeText(getContext(), "Ti's removidas", Toast.LENGTH_SHORT).show();
                }

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

                        //finish();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(Messages.DELETE_TI_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {

                    Log.e(TAG, e.getMessage());
                }
            };

        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("error", error.toString());
            }
        };
        RestClient.delete(getContext(), RestClient.INVESTED_TIME_ENDPOINT_URL+"/" + id , responseListener, errorListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        investedTimes = new ArrayList<>();
        populateList();
    }
}
