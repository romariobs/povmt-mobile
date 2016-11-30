package com.les.povmt.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.R;
import com.les.povmt.models.Activity;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.User;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.parser.ActivityParser;
import com.les.povmt.parser.InvestedTimeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class FirstTabFragment extends Fragment{
    private Date startDay;
    private Date endDay;
    DateFormat dfServer = new SimpleDateFormat("yyyy-MM-dd");
    private String hostURL = "http://povmt.herokuapp.com/history?startDate=";
    TextView mTextView;
    StringRequest stringRequest;

    public FirstTabFragment() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of this week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDay = cal.getTime();

        // start of the next week
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        endDay =  cal.getTime();
        hostURL += dfServer.format(startDay) + "&endDate=";
        hostURL += dfServer.format(endDay) + "&creator=";
        hostURL += User.getCurrentUser().getId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(User.getCurrentUser().getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        mTextView = (TextView) view.findViewById(R.id.textview_frag1);
        final ProgressDialog loading = new ProgressDialog(getContext(), R.style.AppThemeDarkDialog);

        loading.setMessage("Carregando...");
        loading.show();

        stringRequest = new StringRequest(Request.Method.GET, hostURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                //mTextView.setText("Response is: "+ response.substring(0,500));

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

                    System.out.println(response);

                    if (group!= null) {
                        JSONArray arrayIts = group.getJSONArray("its");

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
                                if(act.getId().equals(invTime.getActivityId()))
                                    actName = act.getTitle();
                            }
                            Calendar cal = invTime.getOriginalDate();

                            text = text + "Atividade: " + actName + "\nTempo Investido: " + invTime.getDuration() + " minutos"
                                    + "\nEm " + invTime.getDate() + "\n\n";
                        }
                    }
                    mTextView.setText(text);
                } catch (JSONException e){
                    Log.e("JSON","FAILED");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Volley Error");
                builder.setMessage(error.toString()).setNegativeButton("OK", null)
                        .create().show();
                mTextView.setText(error.toString());
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        return view;
    }
}
