package com.les.povmt.models;

import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * The user is the person that access the application to register Activities, Invested Time
 * and see the historical data from your registers.
 *
 * @author Samuel T. C. Santos
 */

public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private Calendar createAt;
    private Calendar updateAt;

    private static User currentUser;

    /**
     * Default constructor
     */
    public User(){
    }

    /**
     * Creating a new User
     *
     * @param id - the user identifier
     * @param name - the user user name
     * @param email - the user user email
     * @param password - the user password
     */
    public User(String id, String name, String email, String password, String role, String pictureUrl){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Calendar updateAt) {
        this.updateAt = updateAt;
    }

    public Calendar getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Calendar createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static User getCurrentUser () {
        return currentUser;
    }

    public static void setCurrentUser (String id, String name, String email) {
        currentUser = new User(id, name, email, null, null, null);
    }
}
