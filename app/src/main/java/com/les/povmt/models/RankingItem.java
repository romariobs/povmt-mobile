package com.les.povmt.models;


/**
 * Created by Lucas on 25/11/2016.
 */
public class RankingItem {
    private Activity activity;
    private float timeSpend;
    private int color;

    public RankingItem(Activity activity, int graphColor, float time) {
        this.activity = activity;
        this.timeSpend = time;
        this.color = graphColor;
    }

    public Activity getActivity(){
        return this.activity;
    }

    public float getTimeSpend(){
        return this.timeSpend;
    }

    public int getColor(){
        return this.color;
    }

}
