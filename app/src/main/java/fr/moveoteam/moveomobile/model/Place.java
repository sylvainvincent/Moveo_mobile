package fr.moveoteam.moveomobile.model;

import android.widget.ImageView;

/**
 * Created by alexMac on 15/04/15.
 */
public class Place {
    int id;
    String name, address, description;
    ImageView picture;

    public Place() {
    }

    public Place(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }

    public Place(String name, String address, String description, ImageView picture) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }
}
