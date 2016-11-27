package com.les.povmt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.adapter.ITListAdapter;
import com.les.povmt.adapter.RankingAdapter;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.RankingItem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

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
        registerForContextMenu(recyclerView);
        this.recyclerView.setAdapter(itList);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);


        addFab = (FloatingActionButton) v.findViewById(R.id.addFab);
        editFab = (FloatingActionButton) v.findViewById(R.id.editFab);
        deleteFab = (FloatingActionButton) v.findViewById(R.id.deleteFab);
        itList.update(investedTimes);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RegisterTiActivity.class);
                getContext().startActivity(intent);
            }
        });

        return v;
    }

    public void itSelect(int index){
        listSelectedIndex.add(index);
        Log.d("sizeS", listSelectedIndex.size() + "");
        if(listSelectedIndex.size() > 1){
            editFab.setVisibility(View.INVISIBLE);
        } else {
            editFab.setVisibility(View.VISIBLE);
            deleteFab.setVisibility(View.VISIBLE);
        }
    }

    public void itDeselect(int index){
        listSelectedIndex.remove(index);
        Log.d("sizeD", listSelectedIndex.size() + "");
        if(listSelectedIndex.size() < 1){
            editFab.setVisibility(View.INVISIBLE);
            deleteFab.setVisibility(View.INVISIBLE);
        } else if (listSelectedIndex.size() == 1) {
            editFab.setVisibility(View.VISIBLE);
        }
    }

}
