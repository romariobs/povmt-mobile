package com.les.povmt.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.les.povmt.DividerItemDecoration;
import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.SwipeableRecyclerViewTouchListener;
import com.les.povmt.adapter.ITListAdapter;
import com.les.povmt.adapter.RankingAdapter;
import com.les.povmt.models.Activity;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.RankingItem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ITListFragment extends Fragment {
    private ITListAdapter itList;
    private List<InvestedTime> investedTimes = new ArrayList<>();
    private RecyclerView recyclerView;

    private List<Integer> listSelectedIndex = new ArrayList<>();

    private FloatingActionButton addFab, deleteFab, editFab;

    public ITListFragment(){
        carregaITs();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void carregaITs() {
        for(int i = 0; i < 15; i++){
            investedTimes.add(new InvestedTime("i","1",(10 + i),new GregorianCalendar(2016,11,12 + i),new GregorianCalendar(2016,11,12)));
        }
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
                    getContext().startActivity(intent);
                }
            }
        });

        return v;
    }
}
