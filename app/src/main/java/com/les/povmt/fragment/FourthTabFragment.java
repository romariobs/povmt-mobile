package com.les.povmt.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.les.povmt.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FourthTabFragment extends Fragment implements OnChartValueSelectedListener {

    //region Binds

    @Bind(R.id.linegraph)
    LineChart mChart;

    //endregion

    private String[] mDays;

    public FourthTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historic, container, false);
        ButterKnife.bind(this,view);

        this.mDays = getResources().getStringArray(R.array.days);

        setGraphic();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //region Private Methods

    /**
     * Configure graphic
     */
    private void setGraphic() {
        setChat();
        setData();
        setAxis();
        setLegends();
    }

    /**
     * Configure style of legends
     */
    private void setLegends() {
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(ColorTemplate.getHoloBlue());
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
    }

    /**
     * Set chat style
     */
    private void setChat() {
        mChart.setOnChartValueSelectedListener(this);
        mChart.setTouchEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setPinchZoom(true);
        mChart.animateX(2500);
    }

    /**
     * Set left, right and x axis
     */
    private void setAxis() {
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(24f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(ColorTemplate.getHoloBlue());
        rightAxis.setAxisMaximum(24f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(true);
        rightAxis.setGranularityEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mDays[(int) value % mDays.length];
            }
        });
    }

    /**
     * Lad and set data
     */
    private void setData() {

        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < mDays.length; i++) {
            yVals1.add(new Entry(i, 2));
        }

        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int i = 0; i < mDays.length; i++) {
            yVals2.add(new Entry(i, 24));
        }

        ArrayList<Entry> yVals3 = new ArrayList<>();
        for (int i = 0; i < mDays.length; i++) {
            yVals3.add(new Entry(i, 4));
        }

        LineDataSet set1, set2, set3;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals1, "Semana Atual");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setCircleColor(Color.BLUE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setColor(Color.BLUE);
            set1.setDrawCircleHole(false);

            set2 = new LineDataSet(yVals2, "Última Semana");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.RED);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setDrawCircleHole(false);

            set3 = new LineDataSet(yVals3, "Semana Anterior");
            set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set3.setColor(Color.GREEN);
            set3.setCircleColor(Color.GREEN);
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setDrawCircleHole(false);

            // create a data object with the datasets
            LineData data = new LineData(set1, set2, set3);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            mChart.setData(data);
        }
    }

    //endregion
}