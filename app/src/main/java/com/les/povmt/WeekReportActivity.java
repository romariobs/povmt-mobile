package com.les.povmt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.les.povmt.adapter.RankingAdapter;
import com.les.povmt.models.Activity;
import com.les.povmt.models.RankingItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WeekReportActivity extends AppCompatActivity {
    private PieChart mChart;

    private List<RankingItem> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private RankingAdapter rankingAdapter;

    private ScrollView scroll;
    private float totalTimeInvested;

    private static final int[] GRAPH_COLORS = {
            Color.rgb(42, 109, 130), Color.rgb(217, 80, 138),
            Color.rgb(254, 149, 7), Color.rgb(254, 247, 50),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209),
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_report);

        scroll = (ScrollView) findViewById(R.id.scrollView);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.title_activity_week_report));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_18dp);

        mChart = (PieChart) findViewById(R.id.pieChart);
        mChart.getDescription().setEnabled(false);

        mChart.setCenterText("15 h\n30 min");
        mChart.setCenterTextSize(18f);

        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(55f);
        mChart.animateY(3000);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        mChart.setData(generatePieData());


        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                scroll.scrollTo(0,(int)(400 + 110 *  (h.getX() + 1)));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case 16908332:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected PieData generatePieData() {
        int count = 15;
        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float value = (float) (Math.random() * 20) + 10 ;
            totalTimeInvested += value;
            entries1.add(new PieEntry((float) value, ""));
        }

        int min =  (int) ((totalTimeInvested % 1) * 100);
        if (min >= 60)
            min = min % 60;

        mChart.setCenterText( (int)(totalTimeInvested/1) + " h\n" + min +" min");
        Collections.sort(entries1, new Comparator<PieEntry>() {
            @Override
            public int compare(PieEntry pieEntry, PieEntry t1) {
                if (pieEntry.getValue() > t1.getValue()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        int i = 0;
        for (PieEntry pie: entries1) {
            activities.add(new RankingItem(new Activity("Title", "Description"), GRAPH_COLORS[i % GRAPH_COLORS.length], pie.getValue()));
            i++;
        }
        setList();

        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(GRAPH_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);
        PieData d = new PieData(ds1);

        return d;
    }

    private void setList() {
        this.rankingAdapter = new RankingAdapter(getApplicationContext(), activities, totalTimeInvested);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        registerForContextMenu(recyclerView);
        this.recyclerView.setAdapter(rankingAdapter);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        rankingAdapter.update(activities);
    }
}
