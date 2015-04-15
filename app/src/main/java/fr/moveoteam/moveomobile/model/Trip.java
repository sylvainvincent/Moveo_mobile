package fr.moveoteam.moveomobile.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by alexMac on 07/04/15.
 */
public class Trip {
    int id;
    String country, name, description;
    Date insert;
    Drawable mainPicture;
    User user;

    public Trip() {
    }

    public Trip(String country, String name, String description, Date insert, Drawable mainPicture, User user) {
        this.country = country;
        this.name = name;
        this.description = description;
        this.insert = insert;
        this.mainPicture = mainPicture;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getInsert() {
        return insert;
    }

    public void setInsert(Date insert) {
        this.insert = insert;
    }

    public Drawable getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(Drawable mainPicture) {
        this.mainPicture = mainPicture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
