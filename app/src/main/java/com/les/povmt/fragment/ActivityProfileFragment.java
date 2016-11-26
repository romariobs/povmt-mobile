package com.les.povmt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.les.povmt.EditActivity;
import com.les.povmt.R;
import com.les.povmt.models.Activity;

public class ActivityProfileFragment extends Fragment {
    private Activity activity;

    public ActivityProfileFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_activity, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ListUserActivity.this, "Cesar", Toast.LENGTH_SHORT).show();
                startEditActivity();
            }
        });
        return v;
    }


    private void startEditActivity() {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra("activity", activity);
        startActivity(intent);
    }

}
