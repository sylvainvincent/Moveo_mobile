package fr.moveoteam.moveomobile.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by alexMac on 15/04/15.
 */
public class Place implements Parcelable{
    int id;
    String name, address, description;
    int category;
    int tripId;

    public Place() {
    }

    public Place(Parcel parcel){
        super();
        readFromParcel(parcel);
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {

            return new Place[size];
        }

    };


    public Place(int id, String name, String address, String description, int category, int tripId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.tripId = tripId;
    }

    public Place(int id, String name, String address, String description, int category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", tripId=" + tripId +
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

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTripId() {return tripId;}

    public void setTripId(int tripId) {this.tripId = tripId;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeInt(this.category);
        dest.writeInt(this.tripId);


    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.category = in.readInt();
        this.tripId = in.readInt();

    }
}
