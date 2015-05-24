package fr.moveoteam.moveomobile.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sylvain on 10/05/15.
 */
public class Comment implements Parcelable{

    int id;
    String message;
    String date;
    int idUser;
    String userLastName;
    String userFirstName;
    String userAvatarBase64;



    public Comment(){}

    public Comment(Parcel parcel){
        super();
        readFromParcel(parcel);
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {

            return new Comment[size];
        }

    };

    public Comment(int id, String message, String date, int idUser, String userLastName, String userFirstName, String userAvatarBase64) {
        this.id = id;
        this.message = message;
        this.userAvatarBase64 = userAvatarBase64;
        this.date = date;
        this.idUser = idUser;
        this.userLastName = userLastName;
        this.userFirstName = userFirstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUserAvatarBase64() {
        return userAvatarBase64;
    }

    public void setUserAvatarBase64(String userAvatarBase64) {
        this.userAvatarBase64 = userAvatarBase64;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", idUser=" + idUser +
                ", userLastName='" + userLastName + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userAvatarBase64='" + userAvatarBase64 + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.message = in.readString();
        this.date = in.readString();
        this.idUser = in.readInt();
        this.userFirstName = in.readString();
        this.userLastName = in.readString();
        this.userAvatarBase64 = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.message);
        dest.writeString(this.date);
        dest.writeInt(this.idUser);
        dest.writeString(this.userFirstName);
        dest.writeString(this.userLastName);
        dest.writeString(this.userAvatarBase64);
    }
}
