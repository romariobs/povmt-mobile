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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.R;
import com.les.povmt.models.Activity;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;
import com.les.povmt.parser.InvestedTimeParser;

import org.json.JSONArray;
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

public class SecondTabFragment extends Fragment {
    private Date startDay, endDay;
    private DateFormat dfServer = new SimpleDateFormat("yyyy-MM-dd");
    private String hostURL = "http://povmt.herokuapp.com/history";
    private StringRequest stringRequest;
    private List<String> dataSource;
    private boolean isWorkCategory = false;

    @Bind(R.id.tv_work)
    TextView mBtWork;

    @Bind(R.id.tv_recreation)
    TextView mBtRecreation;

    ListView lView;

    public SecondTabFragment() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()); // get start of this week
        endDay = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        startDay = cal.getTime();

        hostURL += "?startDate=" + dfServer.format(startDay) + "&endDate=";
        hostURL += dfServer.format(endDay) + "&creator=";
        hostURL += User.getCurrentUser().getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);
        lView = (ListView) view.findViewById(R.id.list2);

        selectTypeWork();

        return view;
    }

    private void callService() {
        dataSource = new ArrayList<>();
        final ProgressDialog loading = new ProgressDialog(getContext(), R.style.AppThemeDarkDialog);
        loading.setMessage("Carregando...");
        loading.show();

        String finalRequest = hostURL;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ON RESP", response);

                JSONObject json;

                try {
                    json = new JSONObject(response);
                    int status = 0;

                    if (json.has("status")){
                        status = json.getInt("status");
                    }
                    loading.cancel();

                    if (status != HTTP_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(status).setNegativeButton("ok", null)
                                .create().show();
                    }
                    String text = "";

                    JSONObject group = json.getJSONObject("history").getJSONArray("groupedHistory")
                            .optJSONObject(0);

                    if (group!= null) {
                        //PARSING ITs FROM HISTORY
                        List<Activity> activities = (new ActivityParser()).parseFromHistory(json.getJSONObject("history").toString());
                        List<InvestedTime> itsList = (new InvestedTimeParser()).parse(group.toString());

                        for(int j = 1; j < json.getJSONObject("history").getJSONArray("groupedHistory").length();j++){
                            group = json.getJSONObject("history").getJSONArray("groupedHistory").optJSONObject(j);
                            List<InvestedTime> varList = (new InvestedTimeParser()).parse(group.toString());
                            itsList.addAll(varList);
                        }

                        for (int it = 0; it < itsList.size(); it++) {
                            InvestedTime invTime = itsList.get(it);
                            String actName = "";

                            for (Activity act : activities) {
                                if((isWorkCategory && act.getCategory().equals("WORK")) || (!isWorkCategory && !act.getCategory().equals("WORK"))) {
                                    if(act.getId().equals(invTime.getActivityId()))
                                        actName = act.getTitle();
                                }
                            }

                            if (!actName.equals("")) {
                                text = "Atividade: " + actName + "\nTempo Investido: " + invTime.getDuration() + " minutos"
                                        + "\nEm " + invTime.getDate();
                                dataSource.add(text);
                            }
                        }
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),R.layout.rowlayout,R.id.txtitem, dataSource);
                    lView.setAdapter(adapter);
                } catch (JSONException e){
                    Log.e("JSON","FAILED");
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

        RestClient.get(getContext(), finalRequest, responseListener, errorListener);
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
}