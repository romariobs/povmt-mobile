package com.les.povmt.parser;

import android.util.Log;

import com.les.povmt.models.Activity;
import com.les.povmt.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class is responsible to parse the JSON data retrieve from server and
 * extract the information of Activities and transform into java objects.
 *
 * @author Samuel T. C. Santos
 */
public class ActivityParser {

    private final String TAG = this.getClass().getSimpleName();

    private final String TAG_ID = "id";
    private final String TAG_CREATOR = "creator";
    private final String TAG_TITLE = "title";
    private final String TAG_PRIORITY = "priority";
    private final String TAG_CATEGORY = "category";
    private final String TAG_DESCRIPTION = "description";
    private final String TAG_CREATED_AT = "createdAt";
    private final String TAG_UPDATED_AT = "updatedAt";
    private final String TAG_ROOT = "activities";

    /**
     * Default constructor.
     */
    public ActivityParser(){

    }

    /**
     * Doing the parse of the received information from server.
     *
     * @param json - The data received from server
     * @return - A list of Invested time into the selected activity.
     */
    public List<Activity> parse(String json){

        List<Activity> activities = new ArrayList<Activity>();

        if (json != null){
            try {
                JSONObject dataObject = new JSONObject(json);
                JSONArray itsJsonList = dataObject.getJSONArray(TAG_ROOT);

                for (int i=0; i< itsJsonList.length(); i++){
                    JSONObject it = itsJsonList.getJSONObject(i);

                    String id = "";
                    String creator = "";
                    String title = "";
                    String priority = "";
                    String category = "";
                    String description = "";

                    Calendar createdAt = Calendar.getInstance();
                    Calendar updatedAt = Calendar.getInstance();

                    if (it.has(TAG_ID)){
                        id = it.getString(TAG_ID);
                    }
                    if (it.has(TAG_CREATOR)){
                        creator = it.getString(TAG_CREATOR);
                    }
                    if (it.has(TAG_TITLE)){
                        title = it.getString(TAG_TITLE);
                    }
                    if (it.has(TAG_PRIORITY)){
                        priority = it.getString(TAG_PRIORITY);
                    }
                    if (it.has(TAG_CATEGORY)){
                        category = it.getString(TAG_CATEGORY);
                    }
                    if (it.has(TAG_DESCRIPTION)){
                        description = it.getString(TAG_DESCRIPTION);
                    }
                    if (it.has(TAG_CREATED_AT)){
                        createdAt = Util.parseDateFromUTC(it.getString(TAG_CREATED_AT));
                    }

                    if (it.has(TAG_UPDATED_AT)){
                        updatedAt = Util.parseDateFromUTC(it.getString(TAG_UPDATED_AT));
                    }

                    Activity newActivity = new Activity(id, creator, title, priority, category ,description, createdAt, updatedAt);
                    activities.add(newActivity);
                }

            }
            catch(JSONException e){
                Log.e(TAG, e.getMessage());
            }
        }
        else {
            Log.e(TAG, "Invalid data received from server");
        }

        return activities;
    }

    public List<Activity> parseFromHistory(String json){

        List<Activity> activities = new ArrayList<Activity>();

        if (json != null){
            try {
                JSONObject history = new JSONObject(json);
                JSONArray groupedHistory = history.getJSONArray("groupedHistory");

                for (int i=0; i< groupedHistory.length(); i++){
                    JSONObject it = groupedHistory.getJSONObject(i).getJSONObject("activity");

                    String id = "";
                    String creator = "";
                    String title = "";
                    String priority = "";
                    String category = "";
                    String description = "";

                    Calendar createdAt = Calendar.getInstance();
                    Calendar updatedAt = Calendar.getInstance();

                    if (it.has(TAG_ID)){
                        id = it.getString(TAG_ID);
                    }
                    if (it.has(TAG_CREATOR)){
                        creator = it.getString(TAG_CREATOR);
                    }
                    if (it.has(TAG_TITLE)){
                        title = it.getString(TAG_TITLE);
                    }
                    if (it.has(TAG_PRIORITY)){
                        priority = it.getString(TAG_PRIORITY);
                    }
                    if (it.has(TAG_CATEGORY)){
                        category = it.getString(TAG_CATEGORY);
                    }
                    if (it.has(TAG_DESCRIPTION)){
                        description = it.getString(TAG_DESCRIPTION);
                    }
                    if (it.has(TAG_CREATED_AT)){
                        createdAt = Util.parseDateFromUTC(it.getString(TAG_CREATED_AT));
                    }

                    if (it.has(TAG_UPDATED_AT)){
                        updatedAt = Util.parseDateFromUTC(it.getString(TAG_UPDATED_AT));
                    }

                    Activity newActivity = new Activity(id, creator, title, priority , category, description, createdAt, updatedAt);
                    activities.add(newActivity);
                }

            }
            catch(JSONException e){
                Log.e(TAG, e.getMessage());
            }
        }
        else {
            Log.e(TAG, "Invalid data received from server");
        }

        return activities;
    }
}

