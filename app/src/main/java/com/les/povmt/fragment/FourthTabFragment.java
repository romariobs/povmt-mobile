package com.les.povmt.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.parser.InvestedTimeParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.net.HttpURLConnection.HTTP_OK;

public class FourthTabFragment extends Fragment implements OnChartValueSelectedListener {

    //region Binds

    @Bind(R.id.linegraph)
    LineChart mChart;

    @Bind(R.id.tv_inf)
    TextView mTvInf;

    @Bind(R.id.tv_work)
    TextView mBtWork;

    @Bind(R.id.tv_recreation)
    TextView mBtRecreation;

    //endregion

    //region Fields

    private String[] mDays;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String mUrlActualWeek = "http://povmt.herokuapp.com/history?startDate=";
    private String mUrlBeforeWeek = "http://povmt.herokuapp.com/history?startDate=";
    private String mUrlLastWeek = "http://povmt.herokuapp.com/history?startDate=";
    private Date mActualWeekStartDay;
    private Date mActualWeekEndDay;
    private Date mBeforeWeekStartDay;
    private Date mBeforeWeekEndDay;
    private Date mLastWeekStartDay;
    private Date mLastWeekEndDay;
    private int mActualWeekTime = 0;
    private int mBeforeWeekTime = 0;
    private int mLastWeekTime = 0;
    private final long SEVEN_DAYS = (long) 6.048e+8;
    private boolean isWorkCategory = false;

    //endregion

    //region Override methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historic, container, false);
        ButterKnife.bind(this, view);

        this.mDays = getResources().getStringArray(R.array.days);

        setDates();
        prepateUrl();
        selectTypeWork();

        return view;
    }

    @OnClick(R.id.tv_work)
    void selectTypeWork() {
        mBtWork.setBackground(getActivity().getResources().getDrawable(R.drawable.borderbg));
        mBtWork.setTextColor(Color.WHITE);

        mBtRecreation.setBackgroundColor(Color.TRANSPARENT);
        mBtRecreation.setTextColor(Color.BLACK);

        isWorkCategory = true;
        callService();
    }

    @OnClick(R.id.tv_recreation)
    void selectTypeRecreation() {
        mBtRecreation.setBackground(getActivity().getResources().getDrawable(R.drawable.borderbg));
        mBtRecreation.setTextColor(Color.WHITE);

        mBtWork.setBackgroundColor(Color.TRANSPARENT);
        mBtWork.setTextColor(Color.BLACK);

        isWorkCategory = false;
        callService();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //endregion

    //region Private Methods

    /**
     * Call service to get historic of time
     */
    private void callService() {
        final ProgressDialog loadingActualWeek = new ProgressDialog(getActivity(), R.style.AppThemeDarkDialog);
        final ProgressDialog loadingBeforeWeek = new ProgressDialog(getActivity(), R.style.AppThemeDarkDialog);
        final ProgressDialog loadingLastWeek = new ProgressDialog(getActivity(), R.style.AppThemeDarkDialog);
        loadingActualWeek.setMessage("Carregando dados...");
        loadingBeforeWeek.setMessage("Carregando dados...");
        loadingLastWeek.setMessage("Carregando dados...");
        loadingActualWeek.show();
        loadingBeforeWeek.show();
        loadingLastWeek.show();

        getActualWeekRequest(loadingActualWeek);
        getBeforeWeekRequest(loadingBeforeWeek);
        getLastWeekRequest(loadingLastWeek);
    }

    /**
     * Response of actual week
     *
     * @param loading ProgressDialog
     * @return StringRequest
     */
    private StringRequest getActualWeekRequest(final ProgressDialog loading) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ON RESP", response);

                JSONObject json;

                try {
                    json = new JSONObject(response);
                    int status = 0;

                    if (json.has("status")) {
                        status = json.getInt("status");
                    }
                    loading.cancel();

                    if (status != HTTP_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(status).setNegativeButton("ok", null)
                                .create().show();
                    }

                    JSONObject group = json.getJSONObject("history").getJSONArray("groupedHistory")
                            .optJSONObject(0);

                    if (group != null) {
                        List<InvestedTime> itsList = (new InvestedTimeParser()).parse(group.toString());
                        for (int j = 1; j < json.getJSONObject("history").getJSONArray("groupedHistory").length(); j++) {
                            group = json.getJSONObject("history").getJSONArray("groupedHistory").optJSONObject(j);
                            List<InvestedTime> varList = (new InvestedTimeParser()).parse(group.toString());
                            itsList.addAll(varList);
                        }

                        for (InvestedTime item :
                                itsList) {
                            //TODO Separar por categoria
                            if (isWorkCategory) {
                                mActualWeekTime += item.getDuration();
                            } else {
                                mActualWeekTime += item.getDuration();
                            }
                        }
                        mActualWeekTime = mActualWeekTime / 7 > 24 ? 24 : mActualWeekTime / 7;
                    }
                    setGraphic();
                    loading.cancel();
                } catch (JSONException e) {
                    Log.e("JSON", "FAILED");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Volley Error");
                builder.setMessage(error.toString()).setNegativeButton("OK", null)
                        .create().show();
            }
        };

        RestClient.get(getContext(), mUrlActualWeek, responseListener, errorListener);

        return null;
    }

    /**
     * Response of before week
     *
     * @param loading ProgressDialog
     * @return StringRequest
     */
    private StringRequest getBeforeWeekRequest(final ProgressDialog loading) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ON RESP", response);

                JSONObject json;

                try {
                    json = new JSONObject(response);
                    int status = 0;

                    if (json.has("status")) {
                        status = json.getInt("status");
                    }
                    loading.cancel();

                    if (status != HTTP_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(status).setNegativeButton("ok", null)
                                .create().show();
                    }

                    JSONObject group = json.getJSONObject("history").getJSONArray("groupedHistory")
                            .optJSONObject(0);

                    if (group != null) {
                        List<InvestedTime> itsList = (new InvestedTimeParser()).parse(group.toString());
                        for (int j = 1; j < json.getJSONObject("history").getJSONArray("groupedHistory").length(); j++) {
                            group = json.getJSONObject("history").getJSONArray("groupedHistory").optJSONObject(j);
                            List<InvestedTime> varList = (new InvestedTimeParser()).parse(group.toString());
                            itsList.addAll(varList);
                        }

                        for (InvestedTime item :
                                itsList) {
                            //TODO Separar por categoria
                            if (isWorkCategory) {
                                mBeforeWeekTime += item.getDuration();
                            } else {
                                mBeforeWeekTime += item.getDuration();
                            }
                        }
                        mBeforeWeekTime = mBeforeWeekTime / 7 > 24 ? 24 : mBeforeWeekTime / 7;
                    }
                    setGraphic();
                    loading.cancel();
                } catch (JSONException e) {
                    Log.e("JSON", "FAILED");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Volley Error");
                builder.setMessage(error.toString()).setNegativeButton("OK", null)
                        .create().show();
            }
        };

        RestClient.get(getContext(), mUrlBeforeWeek, responseListener, errorListener);

        return null;
    }

    /**
     * Response of last week
     *
     * @param loading ProgressDialog
     * @return StringRequest
     */
    private StringRequest getLastWeekRequest(final ProgressDialog loading) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ON RESP", response);

                JSONObject json;

                try {
                    json = new JSONObject(response);
                    int status = 0;

                    if (json.has("status")) {
                        status = json.getInt("status");
                    }
                    loading.cancel();

                    if (status != HTTP_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(status).setNegativeButton("ok", null)
                                .create().show();
                    }

                    JSONObject group = json.getJSONObject("history").getJSONArray("groupedHistory")
                            .optJSONObject(0);

                    if (group != null) {
                        List<InvestedTime> itsList = (new InvestedTimeParser()).parse(group.toString());
                        for (int j = 1; j < json.getJSONObject("history").getJSONArray("groupedHistory").length(); j++) {
                            group = json.getJSONObject("history").getJSONArray("groupedHistory").optJSONObject(j);
                            List<InvestedTime> varList = (new InvestedTimeParser()).parse(group.toString());
                            itsList.addAll(varList);
                        }

                        for (InvestedTime item :
                                itsList) {
                            //TODO Separar por categoria
                            if (isWorkCategory) {
                                mLastWeekTime += item.getDuration();
                            } else {
                                mLastWeekTime += item.getDuration();
                            }
                        }
                        mLastWeekTime = mLastWeekTime / 7 > 24 ? 24 : mLastWeekTime / 7;
                    }
                    setGraphic();
                    loading.cancel();
                } catch (JSONException e) {
                    Log.e("JSON", "FAILED");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Volley Error");
                builder.setMessage(error.toString()).setNegativeButton("OK", null)
                        .create().show();
            }
        };

        RestClient.get(getContext(), mUrlLastWeek, responseListener, errorListener);

        return null;
    }

    /**
     * Prepare url to service
     */
    private void prepateUrl() {
        this.mUrlActualWeek += mDateFormat.format(mActualWeekStartDay) + "&endDate=";
        this.mUrlActualWeek += mDateFormat.format(mActualWeekEndDay) + "&creator=";
        this.mUrlActualWeek += User.getCurrentUser().getId();

        this.mUrlBeforeWeek += mDateFormat.format(mBeforeWeekStartDay) + "&endDate=";
        this.mUrlBeforeWeek += mDateFormat.format(mBeforeWeekEndDay) + "&creator=";
        this.mUrlBeforeWeek += User.getCurrentUser().getId();

        this.mUrlLastWeek += mDateFormat.format(mLastWeekStartDay) + "&endDate=";
        this.mUrlLastWeek += mDateFormat.format(mLastWeekEndDay) + "&creator=";
        this.mUrlLastWeek += User.getCurrentUser().getId();
    }

    /**
     * set dates to weeks
     */
    private void setDates() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        mActualWeekStartDay = cal.getTime();
        mBeforeWeekStartDay = cal.getTime();
        mLastWeekStartDay = cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        mActualWeekEndDay = cal.getTime();
        mBeforeWeekEndDay = cal.getTime();
        mLastWeekEndDay = cal.getTime();

        mBeforeWeekStartDay.setTime(mBeforeWeekStartDay.getTime() - SEVEN_DAYS);
        mBeforeWeekEndDay.setTime(mBeforeWeekEndDay.getTime() - SEVEN_DAYS);
        mLastWeekStartDay.setTime(mLastWeekStartDay.getTime() - 2 * SEVEN_DAYS);
        mLastWeekEndDay.setTime(mLastWeekEndDay.getTime() - 2 * SEVEN_DAYS);
    }

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
            yVals1.add(new Entry(i, mActualWeekTime));
        }

        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int i = 0; i < mDays.length; i++) {
            yVals2.add(new Entry(i, mBeforeWeekTime));
        }

        ArrayList<Entry> yVals3 = new ArrayList<>();
        for (int i = 0; i < mDays.length; i++) {
            yVals3.add(new Entry(i, mLastWeekTime));
        }

        String text = "";
        if (mActualWeekTime > mBeforeWeekTime) {
            text += "Você tem melhorado seu desempenho em relação a útima semana.";
        } else {
            text += "Você tem piorado seu desempenho em relação a útima semana.";
        }

        if (mActualWeekTime > mLastWeekTime) {
            text += "\n\nEm relação a duas semanas atrás, seu desempenho tem melhorado.";
        } else {
            text += "\n\nEm relação a duas semanas atrás, seu desempenho tem piorado.";
        }

        mTvInf.setText(text);

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