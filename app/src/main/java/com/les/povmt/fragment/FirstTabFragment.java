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
import com.les.povmt.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.net.HttpURLConnection.HTTP_OK;

public class FirstTabFragment extends Fragment{
    private static String hostURL = "http://povmt.herokuapp.com/history?startDate=%s&endDate=%s&creator=1";
    String sampleURL = "http://povmt.herokuapp.com/history?startDate=2016-11-01&endDate=2016-11-07&creator=1";
    TextView mTextView;
    StringRequest stringRequest;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String weekSunday = "2016-11-27";
    String today = df.format(new Date());

    public FirstTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostURL = String.format(hostURL,weekSunday,today);

        System.out.println(hostURL);
        System.out.println(weekSunday);
        System.out.println(today);

        /*
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        Date today = Calendar.getInstance().getTime();

        // (2) create a date "formatter" (the date format we want)
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        mTextView = (TextView) view.findViewById(R.id.textview_frag1);
        final ProgressDialog loading = new ProgressDialog(getContext(), R.style.AppThemeDarkDialog);

        loading.setMessage("Carregando...");
        loading.show();

        // Request a string response from the provided hostURL.
        stringRequest = new StringRequest(Request.Method.GET, sampleURL, new Response.Listener<String>() {
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
                    JSONArray jIts = json.getJSONObject("history").optJSONArray("its");

                    String text = "";
                    for (int its = 0; its < jIts.length(); its++) {
                        text = text + jIts.get(its).toString() + "\n\n";
                    }
                    mTextView.setText(text);
                } catch (JSONException e){
                    System.out.println(response);
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
