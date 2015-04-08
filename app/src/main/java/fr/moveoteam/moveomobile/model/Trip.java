package fr.moveoteam.moveomobile.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by alexMac on 07/04/15.
 */
public class Trip {
    int id;
    String country, city, description;
    Date insert;
    Drawable mainPicture;

    public Trip(String country, String city, Date insert, Drawable mainPicture) {
        this.country = country;
        this.city = city;
        this.insert = insert;
        this.mainPicture = mainPicture;
    }

    public Trip(String country, String city, String description, Date insert, Drawable mainPicture) {
        this.country = country;
        this.city = city;
        this.description = description;
        this.insert = insert;
        this.mainPicture = mainPicture;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;

        Trip trip = (Trip) o;

        if (id != trip.id) return false;
        if (city != null ? !city.equals(trip.city) : trip.city != null) return false;
        if (country != null ? !country.equals(trip.country) : trip.country != null) return false;
        if (description != null ? !description.equals(trip.description) : trip.description != null)
            return false;
        if (insert != null ? !insert.equals(trip.insert) : trip.insert != null) return false;
        if (mainPicture != null ? !mainPicture.equals(trip.mainPicture) : trip.mainPicture != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (insert != null ? insert.hashCode() : 0);
        result = 31 * result + (mainPicture != null ? mainPicture.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", insert=" + insert +
                ", mainPicture=" + mainPicture +
                '}';
    }
}
