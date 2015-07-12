package fr.moveoteam.moveomobile.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe métier sur les photos
 * Created by alexMac on 08/04/15.
 */
public class Photo implements Parcelable {
    private int id;
    private String photoBase64;
    private String publicationDate;
    private int tripId;

    public Photo() {}

    private Photo(Parcel p){
        super();
        readFromParcel(p);
    }

    public Photo(int id, String photoBase64, String publicationDate, int tripId) {
        this.id = id;
        this.photoBase64 = photoBase64;
        this.publicationDate = publicationDate;
        this.tripId = tripId;
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {

            return new Photo[size];
        }

    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (id != photo.id) return false;
        if (publicationDate != null ? !publicationDate.equals(photo.publicationDate) : photo.publicationDate != null)
            return false;
        if (photoBase64 != null ? !photoBase64.equals(photo.photoBase64) : photo.photoBase64 != null) return false;

        return true;
    }



    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (photoBase64 != null ? photoBase64.hashCode() : 0);
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", photoBase64='" + photoBase64 + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", tripId=" + tripId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.photoBase64);
        dest.writeString(this.publicationDate);
        dest.writeInt(this.tripId);
    }

    void readFromParcel(Parcel in){
        this.id = in.readInt();
        this.photoBase64 = in.readString();
        this.publicationDate = in.readString();
        this.tripId = in.readInt();
    }
}
