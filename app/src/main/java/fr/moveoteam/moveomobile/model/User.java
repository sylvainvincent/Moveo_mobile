package fr.moveoteam.moveomobile.model;

import java.util.Date;

/**
 * Created by alexMac on 07/04/15.
 */
public class User {
    int id, accesAccount;
    String firstname, lastName, email, password, country, city, favoriteCountry, favoriteCity;
    Date birthday, register, lastConnexion;

    public User(String firstname, String lastName, String email, String password) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(int accesAccount, String firstname, String lastName, String email, String password, String country, String city, String favoriteCountry, String favoriteCity, Date birthday, Date register, Date lastConnexion) {
        this.accesAccount = accesAccount;
        this.firstname = firstname;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.favoriteCountry = favoriteCountry;
        this.favoriteCity = favoriteCity;
        this.birthday = birthday;
        this.register = register;
        this.lastConnexion = lastConnexion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (accesAccount != user.accesAccount) return false;
        if (id != user.id) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null)
            return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (country != null ? !country.equals(user.country) : user.country != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (favoriteCity != null ? !favoriteCity.equals(user.favoriteCity) : user.favoriteCity != null)
            return false;
        if (favoriteCountry != null ? !favoriteCountry.equals(user.favoriteCountry) : user.favoriteCountry != null)
            return false;
        if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null)
            return false;
        if (lastConnexion != null ? !lastConnexion.equals(user.lastConnexion) : user.lastConnexion != null)
            return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null)
            return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (register != null ? !register.equals(user.register) : user.register != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + accesAccount;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (favoriteCountry != null ? favoriteCountry.hashCode() : 0);
        result = 31 * result + (favoriteCity != null ? favoriteCity.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (register != null ? register.hashCode() : 0);
        result = 31 * result + (lastConnexion != null ? lastConnexion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", accesAccount=" + accesAccount +
                ", firstname='" + firstname + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", favoriteCountry='" + favoriteCountry + '\'' +
                ", favoriteCity='" + favoriteCity + '\'' +
                ", birthday=" + birthday +
                ", register=" + register +
                ", lastConnexion=" + lastConnexion +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getAccesAccount() {
        return accesAccount;
    }

    public void setAccesAccount(int accesAccount) {
        this.accesAccount = accesAccount;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getFavoriteCountry() {
        return favoriteCountry;
    }

    public void setFavoriteCountry(String favoriteCountry) {
        this.favoriteCountry = favoriteCountry;
    }

    public String getFavoriteCity() {
        return favoriteCity;
    }

    public void setFavoriteCity(String favoriteCity) {
        this.favoriteCity = favoriteCity;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register = register;
    }

    public Date getLastConnexion() {
        return lastConnexion;
    }

    public void setLastConnexion(Date lastConnexion) {
        this.lastConnexion = lastConnexion;
    }
}
