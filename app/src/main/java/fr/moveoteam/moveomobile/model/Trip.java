package fr.moveoteam.moveomobile.model;

import android.graphics.drawable.Drawable;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by alexMac on 07/04/15.
 */
public class Trip {
    int id;
    String country, name, description;
    String insert;
    Drawable mainPicture;
    User user;
    String author_last_name;
    String author_first_name;
    int commentCount;
    int photoCount;
    String date;
    int userId;

    public Trip() {
    }

    public Trip(String name, String country, String description, String insert) {
        this.country = country;
        this.name = name;
        this.description = description;
        this.insert = insert;
    }

    public Trip(int id, String name, String country, String description, String date, String author_last_name, String author_first_name,int commentCount, int photoCount) throws ParseException {

        this.id = id;
        this.country = country;
        this.name = name;
        this.description = description;
        this.date = date;
        this.author_first_name = author_first_name;
        this.author_last_name =  author_last_name;
        this.commentCount = commentCount;
        this.photoCount = photoCount;
    }

    public Trip(int id, String name, String country, String description, String date,int commentCount, int photoCount) throws ParseException {
        this.id = id;
        this.country = country;
        this.name = name;
        this.description = description;
        this.date = date;
        this.commentCount = commentCount;
        this.photoCount = photoCount;
    }

    public Trip(String name, String country, String description, String insert, Drawable mainPicture, User user) {
        this.name = name;
        this.country = country;
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

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
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

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getAuthor_last_name() {
        return author_last_name;
    }

    public void setAuthor_last_name(String author_last_name) {
        this.author_last_name = author_last_name;
    }

    public String getAuthor_first_name() {
        return author_first_name;
    }

    public void setAuthor_first_name(String author_first_name) {
        this.author_first_name = author_first_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", insert='" + insert + '\'' +
                ", mainPicture=" + mainPicture +
                ", user=" + user +
                ", author_last_name='" + author_last_name + '\'' +
                ", author_first_name='" + author_first_name + '\'' +
                ", commentCount=" + commentCount +
                ", photoCount=" + photoCount +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                '}';
    }
}
