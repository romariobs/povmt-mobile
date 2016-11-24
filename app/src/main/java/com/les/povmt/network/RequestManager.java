package com.les.povmt.network;


import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Request Manager class responsible to handle HTTP request services.
 *
 * @author Samuel T. C. Santos
 */
public class RequestManager extends Application {

    private static final String TAG = RequestManager.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static RequestManager mInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public static synchronized RequestManager getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue =  Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG: tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPedingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
