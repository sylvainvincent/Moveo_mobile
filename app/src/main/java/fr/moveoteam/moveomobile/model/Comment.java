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
