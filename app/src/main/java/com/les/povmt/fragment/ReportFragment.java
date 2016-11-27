package com.les.povmt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.les.povmt.HistoricActivity;
import com.les.povmt.R;
import com.les.povmt.WeekReportActivity;

public class ReportFragment extends DialogFragment {
    public void ReportFragment(){

    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reports, container, false);

        Button buttonWeek = (Button) v.findViewById(R.id.buttonWeek);
        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WeekReportActivity.class);
                startActivity(intent);
                dismiss();
            }
        });

        Button buttonHistoric = (Button) v.findViewById(R.id.buttonHistoric);
        buttonHistoric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), HistoricActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return v;
    }

}
