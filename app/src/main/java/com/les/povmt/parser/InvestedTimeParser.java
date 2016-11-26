package com.les.povmt.parser;

import android.util.Log;

import com.les.povmt.models.InvestedTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.les.povmt.util.Util;

/**
 * This class is responsible to parse the JSON data retrieve from server and
 * extract the information of Invested Time and transform into java objects.
 *
 * @author Samuel T. C. Santos
 */
public class InvestedTimeParser {

    private final String TAG = this.getClass().getSimpleName();

    private final String TAG_ID = "id";
    private final String TAG_DURATION = "duration";
    private final String TAG_CREATED_AT = "createdAt";
    private final String TAG_UPDATED_AT = "updatedAt";
    private final String TAG_INVESTED_TIME_AT = "investedTimeAt";
    private final String TAG_ROOT = "its";

    /**
     * Default constructor.
     */
    public InvestedTimeParser(){

    }

    /**
     * Doing the parse of the received information from server.
     *
     * @param json - The data received from server
     * @return - A list of Invested time into the selected activity.
     */
    public List<InvestedTime> parse(String json){

        List<InvestedTime> its = new ArrayList<InvestedTime>();

        if (json != null){
            try {
                JSONObject dataObject = new JSONObject(json);
                JSONArray itsJsonList = dataObject.getJSONArray(TAG_ROOT);

                for (int i=0; i< itsJsonList.length(); i++){
                    JSONObject it = itsJsonList.getJSONObject(i);
                    String id = "";
                    String investedTimeAt = "";
                    int duration = 0;

                    Calendar createdAt = Calendar.getInstance();
                    Calendar updatedAt = Calendar.getInstance();

                    if (it.has(TAG_ID)){
                        id = it.getString(TAG_ID);
                    }
                    if (it.has(TAG_DURATION)){
                        duration = it.getInt(TAG_DURATION);
                    }
                    if (it.has(TAG_INVESTED_TIME_AT)){
                        investedTimeAt = it.getString(TAG_INVESTED_TIME_AT);
                    }
                    if (it.has(TAG_CREATED_AT)){
                        createdAt = Util.parseDateFromUTC(it.getString(TAG_CREATED_AT));
                    }

                    if (it.has(TAG_UPDATED_AT)){
                        updatedAt = Util.parseDateFromUTC(it.getString(TAG_UPDATED_AT));
                    }

                    InvestedTime newIt = new InvestedTime(id, investedTimeAt, duration, createdAt, updatedAt);
                    its.add(newIt);
                }

            }
            catch(JSONException e){
                Log.e(TAG, e.getMessage());
            }
        }
        else {
            Log.e(TAG, "Invalid data received from server");
        }

        return its;
    }

}
