package com.les.povmt;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.RankingItem;
import com.les.povmt.models.User;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;
import com.les.povmt.parser.InvestedTimeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class WeekReportActivity extends AppCompatActivity {
    private PieChart mChart;

    private List<RankingItem> activities = new ArrayList<>();
    private RecyclerView recyclerView;
    private RankingAdapter rankingAdapter;

    private Date startDay;
    private Date endDay;
    DateFormat dfServer = new SimpleDateFormat("yyyy-MM-dd");
    String sampleURL = "http://povmt.herokuapp.com/history?startDate=";
    // 2016-11-27&endDate=2016-12-04&creator=1";


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
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        DateFormat df = new SimpleDateFormat("dd/MM");

// get start of this week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDay = cal.getTime();

// start of the next week
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        endDay =  cal.getTime();
        sampleURL += dfServer.format(startDay) + "&endDate=";
        sampleURL += dfServer.format(endDay) + "&creator=";
        sampleURL += User.getCurrentUser().getId();

        generatePieData();

        TextView weekDays = (TextView) findViewById(R.id.weekDays);
        weekDays.setText(df.format(startDay) + " • " + df.format(endDay));

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

    protected void generatePieData() {

        final ProgressDialog loading = new ProgressDialog(WeekReportActivity.this, R.style.AppThemeDarkDialog);

        loading.setMessage("Carregando...");
        loading.show();
        // Request a string response from the provided hostURL.
        Log.d("Lucas", sampleURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, sampleURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json;
                Log.d("Lucas2", response);
                try {
                    json = new JSONObject(response);
                    int status = 0;

                    if (json.has("status")){
                        status = json.getInt("status");
                    }

                    if (status != HTTP_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage(status).setNegativeButton("ok", null)
                                .create().show();
                    }

                    // CAIO AQUI
                    JSONObject group = json.getJSONObject("history").getJSONArray("groupedHistory")
                            .optJSONObject(0);

                    if (group!= null) {
                        JSONArray arrayIts = group.getJSONArray("its");

                        int arraySize = arrayIts != null ? arrayIts.length() : 0;


                        //PARSING ITs FROM HISTORY
                        List<Activity> activitiesList = (new ActivityParser()).parseFromHistory(json.getJSONObject("history").toString());
                        List<InvestedTime> itsList = (new InvestedTimeParser()).parse(group.toString());
                        for(int j = 1; j < json.getJSONObject("history").getJSONArray("groupedHistory").length();j++){
                            group = json.getJSONObject("history").getJSONArray("groupedHistory").optJSONObject(j);
                            List<InvestedTime> varList = (new InvestedTimeParser()).parse(group.toString());
                            itsList.addAll(varList);
                        }

                        activities = new ArrayList<>();
                        ArrayList<PieEntry> entries1 = new ArrayList<>();

                        for (Activity ac : activitiesList){
                            RankingItem rk = new RankingItem(ac, 0, 0);
                            for(InvestedTime it : itsList){
                                Log.d("LucasAQ", it.getDuration() + " - " + ac.getTitle());
                                if(it.getActivityId().equals(ac.getId())){
                                    rk.plusTime(it.getDuration());
                                }
                            }

                            totalTimeInvested += rk.getTimeSpend();
                            entries1.add(new PieEntry(rk.getTimeSpend(), ""));
                            activities.add(rk);
                        }


                        mChart.setCenterText(((int)totalTimeInvested + " min"));
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

                        Collections.sort(activities, new Comparator<RankingItem>() {
                            @Override
                            public int compare(RankingItem rankingItem, RankingItem t1) {
                                if (rankingItem.getTimeSpend() > t1.getTimeSpend()) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        });

                        for(int k = 0; k < activities.size(); k++){
                            activities.get(k).setColor(GRAPH_COLORS[k % GRAPH_COLORS.length]);
                        }

                        loading.cancel();
                        setList();
                        PieDataSet ds1 = new PieDataSet(entries1, "");
                        ds1.setColors(GRAPH_COLORS);
                        ds1.setSliceSpace(2f);
                        ds1.setValueTextColor(Color.WHITE);
                        ds1.setValueTextSize(12f);
                        PieData d = new PieData(ds1);
                        mChart.setData(d);

                        setList();

                    }

                    //

                    //PARSING ITs FROM HISTORY
                } catch (JSONException e){
                    System.out.println(response);
                    Log.e("JSON","FAILED");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Volley Error");
                builder.setMessage(error.toString()).setNegativeButton("OK", null)
                        .create().show();
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

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
