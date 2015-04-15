package fr.moveoteam.moveomobile.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;

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
}
