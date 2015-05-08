package fr.moveoteam.moveomobile.model;

/**
 * Created by Sylvain on 08/05/15.
 */
public class Friend {
    int id;
    String firstName, lastName;
    boolean isFriend;

    public Friend(){};

    public Friend(int id, String firstName, String lastName, boolean isFriend){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isFriend = isFriend;
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
}
