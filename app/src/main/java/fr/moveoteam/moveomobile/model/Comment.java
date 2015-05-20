package fr.moveoteam.moveomobile.model;

/**
 * Created by Sylvain on 10/05/15.
 */
public class Comment {

    int id;
    String message;
    String date;
    int idUser;

    public Comment(){}

    public Comment(int id, String message, String date, int idUser) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.idUser = idUser;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", idUser=" + idUser +
                '}';
    }
}
