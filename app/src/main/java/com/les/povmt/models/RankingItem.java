package com.les.povmt.models;


/**
 * Created by Lucas on 25/11/2016.
 */
public class RankingItem {
    private Activity activity;
    private int timeSpend;
    private int color;

    public RankingItem(Activity activity, int graphColor, int time) {
        this.activity = activity;
        this.timeSpend = time;
        this.color = graphColor;
    }

    public Activity getActivity(){
        return this.activity;
    }

    public int getTimeSpend(){
        return this.timeSpend;
    }

    public int getColor(){
        return this.color;
    }

    public void plusTime(int time){
        timeSpend += time;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
