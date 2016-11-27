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
     * @param description
     * @param createAt
     * @param updateAt
     */
    public Activity(String id, String userId, String title, String description, Calendar createAt, Calendar updateAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    protected Activity(Parcel in) {
        id = in.readString();
        userId = in.readString();
        title = in.readString();
        description = in.readString();
    }


    public String getTitle() {
        return this.title;
    }

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
        parcel.writeString(description);
        parcel.writeValue(createAt);
        parcel.writeValue(updateAt);
    }
}

