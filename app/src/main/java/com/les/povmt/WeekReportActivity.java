package com.les.povmt;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class WeekReportActivity extends AppCompatActivity {
    private PieChart mChart;
    // private Typeface tf;
    private static final int[] GRAPH_COLORS = {
            Color.rgb(42, 109, 130),Color.rgb(217, 80, 138),
            Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209),
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_report);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.title_activity_week_report));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_18dp);

        mChart = (PieChart) findViewById(R.id.pieChart);
        mChart.getDescription().setEnabled(false);

       // tf = Typeface.createFromAsset(this.getAssets(), "OpenSans-Light.ttf");
       // mChart.setCenterTextTypeface(tf);
        mChart.setCenterText("15 h\n30 min");
        mChart.setCenterTextSize(18f);
       // mChart.setCenterTextTypeface(tf);

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

        int count = 10;
        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for(int i = 0; i < count; i++) {
            entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), ""));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(GRAPH_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);
        PieData d = new PieData(ds1);
      //  d.setValueTypeface(tf);
        return d;
    }


}
