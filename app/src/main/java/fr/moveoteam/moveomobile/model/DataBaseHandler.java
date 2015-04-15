package fr.moveoteam.moveomobile.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;
import java.util.HashMap;

/**
 * Created by Sylvain on 14/04/15.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    // TOUT LES VARIABLES STATIC
    // VERSION DE LA BASE DE DONNEES
    private static final int DATABASE_VERSION = 1;

    // NOM DE LA BASE DE DONNEE
    private static final String DATABASE_NAME = "moveoMobileDataBase";

    // NOMS DES TABLES
    private static final String TABLE_LOGIN = "login";

    // NOM DES COLONNES DE LA TABLE LOGIN
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";

    // -----------------TABLE LOGIN --------------------- //
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_FIRSTNAME + " TEXT,"
            + KEY_BIRTHDAY + " DATE,"
            + KEY_EMAIL + " TEXT,"
            + KEY_COUNTRY + " TEXT,"
            + KEY_CITY + " TEXT,"+ ")";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SUPPRIMER l'ANCIENNE TABLE s'il en existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // RECRÉER LES TABLE
        onCreate(db);
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    /**
     * Stocker les informations du client dans la base de données
     * */
    public void addUser(String name, String firstname, String birthday, String email, String country, String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);             // NOM
        values.put(KEY_FIRSTNAME, firstname);   // PRÉNOM
        values.put(KEY_BIRTHDAY, birthday);     // DATE DE NAISSANCE
        values.put(KEY_EMAIL, email);           // ADRESSE MAIL
        values.put(KEY_COUNTRY, country);       // PAYS
        values.put(KEY_CITY, city);             // VILLE

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Fermer la connexion vers la base de données
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Se déplacer à la premiere ligne
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("id", cursor.getString(0));
            user.put("civilite", cursor.getString(1));
            user.put("nom", cursor.getString(2));
            user.put("prenom", cursor.getString(3));
            user.put("ddn", cursor.getString(4));
            user.put("telephone", cursor.getString(5));
            user.put("email", cursor.getString(6));
            user.put("adresse", cursor.getString(7));
            user.put("codePostal", cursor.getString(8));
            user.put("ville", cursor.getString(9));
            user.put("dateInscription", cursor.getString(10));

        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Récupérer le statut de l'utilisateur
     * @return vrai si
     * */
    public boolean getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount > 0;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Supprimer toutes les lignes
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}
