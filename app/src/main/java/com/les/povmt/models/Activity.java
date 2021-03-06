package com.les.povmt.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * The activity class represent the tasks performed by the user during the week or weekend.
 *
 * @author  Samuel T. C. Santos
 */

public class Activity implements Parcelable {

    private String id;
    private String userId;
    private String title;
    private String priority;
    private String category;
    private String description;
    private Calendar createAt;
    private Calendar updateAt;

    public Activity() {

    }

    /**
     * Create a new activity.
     * @param id - the activity identifier
     * @param userId - the user owner
     * @param title - the activity's title
     * @param priority
     * @param category
     * @param description
     * @param createAt
     * @param updateAt
     */
    public Activity(String id, String userId, String title, String priority, String category ,String description, Calendar createAt, Calendar updateAt) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.description = description;
        this.userId = userId;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    protected Activity(Parcel in) {
        id = in.readString();
        userId = in.readString();
        title = in.readString();
        priority = in.readString();
        category = in.readString();
        description = in.readString();
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setPriority(String priority){
        this.priority = priority;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPriority() { return  this.priority; }

    public String getCategory() { return  this.category; }

    public String getDescription(){
        return this.description;
    }

    public String getId(){
        return this.id;
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeString(title);
        parcel.writeString(priority);
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeValue(createAt);
        parcel.writeValue(updateAt);
    }
}

