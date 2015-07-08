package fr.moveoteam.moveomobile.model;

/**
 * Classe métier sur les amis
 * Created by Sylvain on 08/05/15.
 */
public class Friend {

    int id;
    String firstName;
    String lastName;
    String birthday;
    String avatarBase64;
    String country;
    String city;
    int tripCount;
    int accessId;
    boolean isFriend;

    public Friend(){}

    public Friend(int id, String firstName, String lastName, String avatarBase64, boolean isFriend){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarBase64 = avatarBase64;
        this.isFriend = isFriend;
    }

    public Friend(int id, String firstName, String lastName, String avatarBase64, int tripCount){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarBase64 = avatarBase64;
        this.tripCount = tripCount;
    }

    public Friend(int id, String firstName, String lastName, String birthday, String avatarBase64, String country, String city, boolean isFriend,int accessId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.avatarBase64 = avatarBase64;
        this.country = country;
        this.city = city;
        this.isFriend = isFriend;
        this.accessId = accessId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", isFriend=" + isFriend + '\'' +
                ", avatarBase64='" + avatarBase64 +
                '}';
    }

    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public int getTripCount() {
        return tripCount;
    }

    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }
}
