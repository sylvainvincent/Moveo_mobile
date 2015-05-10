package fr.moveoteam.moveomobile.model;

import android.widget.ImageView;

/**
 * Created by alexMac on 15/04/15.
 */
public class Place {
    int id;
    String name, address, description;
    ImageView picture;
    int category;

    public Place() {
    }

    public Place(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Place(int id, String name, String address, String description, int category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
    }

    public Place(String name, String address, String description, ImageView picture) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", picture=" + picture +
                ", category=" + category +
                '}';
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

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
