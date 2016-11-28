package com.les.povmt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.les.povmt.HistoryTabsActivity;
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
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_Dialog);
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
                // Conteúdo de HistoricActivity deve ser colocado no FourthTabFragment
                // que é a última tab com titulo "TODOS" na HistoryTabsActivity
                Intent intent = new Intent(getContext(), HistoryTabsActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return v;
    }

}
