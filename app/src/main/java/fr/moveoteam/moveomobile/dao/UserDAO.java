package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.moveoteam.moveomobile.model.User;

/**
 * Created by Sylvain on 18/04/15.
 */
public class UserDAO {

    // Base de données utilisable
    private SQLiteDatabase database;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_LOGIN = "login";

    // NOM DES COLONNES
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_LASTNAME = "user_lastName";
    private static final String KEY_USER_FIRSTNAME = "user_firstName";
    private static final String KEY_USER_BIRTHDAY = "user_birthday";
    private static final String KEY_USER_AVATAR = "user_avatar";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_USER_COUNTRY = "user_country";
    private static final String KEY_USER_CITY = "user_city";
    private static final String KEY_USER_ACCESS = "user_access";

    // POSITION DES COLONNES
    private static final int POSITION_USER_ID = 0;
    private static final int POSITION_USER_LASTNAME = 1;
    private static final int POSITION_USER_FIRSTNAME = 2;
    private static final int POSITION_USER_BIRTHDAY = 3;
    private static final int POSITION_USER_AVATAR = 4;
    private static final int POSITION_USER_EMAIL = 5;
    private static final int POSITION_USER_PASSWORD = 6;
    private static final int POSITION_USER_COUNTRY = 7;
    private static final int POSITION_USER_CITY = 8;
    private static final int POSITION_USER_ACCESS = 9;

    public UserDAO(Context context){
        dbHandler = new DataBaseHandler(context);
    }

    public SQLiteDatabase open() {
        database = dbHandler.getWritableDatabase();
        return database;
    }

    public void close() {
        dbHandler.close();
    }

    /**
     * Stocker les informations du client dans la base de données
     * */
    public void addUser(User user) {

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getId());
        values.put(KEY_USER_LASTNAME, user.getLastName());     // NOM
        values.put(KEY_USER_FIRSTNAME, user.getFirstName());   // PRÉNOM
        values.put(KEY_USER_BIRTHDAY, user.getBirthday());     // DATE DE NAISSANCE
        values.put(KEY_USER_AVATAR, user.getAvatar());         // AVATAR
        values.put(KEY_USER_EMAIL, user.getEmail());           // ADRESSE MAIL
        values.put(KEY_USER_PASSWORD, user.getPassword());     // PASSWORD
        values.put(KEY_USER_COUNTRY, user.getCountry());       // PAYS
        values.put(KEY_USER_CITY, user.getCity());             // VILLE
        values.put(KEY_USER_ACCESS, user.getAccess());         // VILLE
        // Insérer la ligne
        database.insert(TABLE_LOGIN, null, values);
    }
	
	/**
	 *
	 **/
	 public int modifyUser(User user){

		ContentValues values = new ContentValues();

        if(user.getLastName() != null) values.put(KEY_USER_LASTNAME, user.getLastName());     // NOM
        if(user.getFirstName() != null) values.put(KEY_USER_FIRSTNAME, user.getFirstName());   // PRÉNOM
        if(user.getBirthday() != null) values.put(KEY_USER_BIRTHDAY, user.getBirthday());     // DATE DE NAISSANCE
        if(user.getAvatar() != null)values.put(KEY_USER_AVATAR, user.getAvatar());         // AVATAR
        if(user.getCountry() != null)values.put(KEY_USER_COUNTRY, user.getCountry());       // PAYS
        if(user.getCity() != null)values.put(KEY_USER_CITY, user.getCity());             // VILLE
		return database.update(TABLE_LOGIN, values, null, null);
	 }


    public int modifyPassword(String password){
        ContentValues values = new ContentValues();
        values.put(KEY_USER_PASSWORD, password);
        return database.update(TABLE_LOGIN, values, null, null);
    }

    public int modifyAccess(int access){
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ACCESS, access);
        return database.update(TABLE_LOGIN, values, null, null);
    }

    public User getUserDetails(){

        User user = new User();
        String selectQuery = "SELECT * FROM " + TABLE_LOGIN;

        Cursor cursor = database.rawQuery(selectQuery, null);
        // Se déplacer à la premiere ligne
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.setId(cursor.getInt(POSITION_USER_ID)); //0
            user.setFirstName(cursor.getString(POSITION_USER_FIRSTNAME)); //1
            user.setLastName(cursor.getString(POSITION_USER_LASTNAME)); // 2
            user.setBirthday(cursor.getString(POSITION_USER_BIRTHDAY)); //3
            user.setAvatar(cursor.getString(POSITION_USER_AVATAR));
            user.setEmail(cursor.getString(POSITION_USER_EMAIL));
            user.setPassword(cursor.getString(POSITION_USER_PASSWORD));
            user.setCountry(cursor.getString(POSITION_USER_COUNTRY));
            user.setCity(cursor.getString(POSITION_USER_CITY));
            user.setAccess(cursor.getInt(POSITION_USER_ACCESS));
        }
        cursor.close();
        // database.close();

        return user;
    }

    /**
     * Récupérer le statut de l'utilisateur en comptant le nombre d'utilisateur dans la base de données
     * @return vrai s'il y a au moins une ligne, faux si ce n'est pas le cas
     * */
    public boolean getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        Cursor cursor = database.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        // database.close();
        cursor.close();

        return rowCount > 0;
    }

    /**
     * Fonction qui déconnecte l'utilisateur
     * Efface la base de données
     * */
    public void logoutUser(Context context){
        dbHandler.resetTables();
    }
}
