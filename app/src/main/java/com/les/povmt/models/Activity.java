package com.les.povmt.models;

import java.util.Calendar;

/**
 * The activity class represent the tasks performed by the user during the week or weekend.
 *
 * @author  Samuel T. C. Santos
 */

public class Activity {

    private String id;
    private String userId;
    private String title;
    private String description;
    private Calendar createAt;
    private Calendar updateAt;


    public Activity(){

    }

    public Activity(String title, String description){
        this.title = title;
        this.description = description;
    }


}

