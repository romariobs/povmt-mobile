package com.les.povmt.models;

import java.util.Calendar;

/**
 * Invested Time is any period of time invested by the user in any registered Activity.
 *
 * @author Samuel T. C. Santos
 */

public class InvestedTime {

    private String id;
    private String activityId;
    private int duration;
    private Calendar createAt;
    private Calendar updateAt;

    /**
     * Create a new Invested time reference the matched activity identifier.
     *
     * @param id - the invested time identifier
     * @param activityId - the activity where this it was created.
     * @param duration - the duration time in minutes
     * @param createAt - the creating data in time zulu format
     * @param updateAt - the updated data in time zulu format.
     */
    public InvestedTime(String id, String activityId, int duration , Calendar createAt, Calendar updateAt){
        this.id = id;
        this.activityId = activityId;
        this.duration = duration;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public String getDate(){
        return String.valueOf((""));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "InvestedTime {" +
                "id='" + id + '\'' +
                ", activityId='" + activityId + '\'' +
                ", duration=" + duration +
                '}';
    }
}
