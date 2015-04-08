package fr.moveoteam.moveomobile.model;

import java.net.URL;
import java.util.Date;

/**
 * Created by alexMac on 08/04/15.
 */
public class Picture {
    int id;
    String url;
    Date publication;
    boolean isMainTripPicture, isProfilPicture;

    public Picture(String url) {
        this.url = url;
    }

    public Picture(String url, Date publication, boolean isMainTripPicture, boolean isProfilPicture) {
        this.url = url;
        this.publication = publication;
        this.isMainTripPicture = isMainTripPicture;
        this.isProfilPicture = isProfilPicture;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", publication=" + publication +
                ", isMainTripPicture=" + isMainTripPicture +
                ", isProfilPicture=" + isProfilPicture +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;

        Picture picture = (Picture) o;

        if (id != picture.id) return false;
        if (isMainTripPicture != picture.isMainTripPicture) return false;
        if (isProfilPicture != picture.isProfilPicture) return false;
        if (publication != null ? !publication.equals(picture.publication) : picture.publication != null)
            return false;
        if (url != null ? !url.equals(picture.url) : picture.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (publication != null ? publication.hashCode() : 0);
        result = 31 * result + (isMainTripPicture ? 1 : 0);
        result = 31 * result + (isProfilPicture ? 1 : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getPublication() {
        return publication;
    }

    public void setPublication(Date publication) {
        this.publication = publication;
    }

    public boolean isMainTripPicture() {
        return isMainTripPicture;
    }

    public void setMainTripPicture(boolean isMainTripPicture) {
        this.isMainTripPicture = isMainTripPicture;
    }

    public boolean isProfilPicture() {
        return isProfilPicture;
    }

    public void setProfilPicture(boolean isProfilPicture) {
        this.isProfilPicture = isProfilPicture;
    }
}
