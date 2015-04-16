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
    // VERSION DE LA BASE DE DONNÉES
    private static final int DATABASE_VERSION = 1;

    // NOM DE LA BASE DE DONNÉES
    private static final String DATABASE_NAME = "moveoMobileDataBase";

    // NOMS DES TABLES
    private static final String TABLE_LOGIN = "login";

    // NOM DES COLONNES DE LA TABLE LOGIN
    private static final String KEY_ID = "id";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";

    // NUMÉRO DES COLONNES

    private static final int POSITION_ID = 1;
    private static final int POSITION_LASTNAME = 2;
    private static final int POSITION_FIRSTNAME = 3;
    private static final int POSITION_BIRTHDAY = 4;
    private static final int POSITION_EMAIL = 5;
    private static final int POSITION_COUNTRY = 6;
    private static final int POSITION_CITY = 7;


    // -----------------TABLE LOGIN --------------------- //
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_LASTNAME + " TEXT,"
            + KEY_FIRSTNAME + " TEXT,"
            + KEY_BIRTHDAY + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_COUNTRY + " TEXT,"
            + KEY_CITY + " TEXT"+ ")";

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

        // RECRÉER LA TABLE
        onCreate(db);
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    /**
     * Stocker les informations du client dans la base de données
     * */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LASTNAME, user.getLastName());             // NOM
        values.put(KEY_FIRSTNAME, user.getFirstName());   // PRÉNOM
        values.put(KEY_BIRTHDAY, user.getBirthday());     // DATE DE NAISSANCE
        values.put(KEY_EMAIL, user.getEmail());           // ADRESSE MAIL
        values.put(KEY_COUNTRY, user.getCountry());       // PAYS
        values.put(KEY_CITY, user.getCity());             // VILLE

        // Insérer la ligne
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Fermer la connexion vers la base de données
    }

    public User getUserDetails(){
        User user = new User();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Se déplacer à la premiere ligne
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.setFirstName(cursor.getString(POSITION_FIRSTNAME));
            user.setLastName(cursor.getString(POSITION_LASTNAME));
            user.setBirthday(cursor.getString(POSITION_BIRTHDAY));
            user.setEmail(cursor.getString(POSITION_EMAIL));
            user.setCountry(cursor.getString(POSITION_COUNTRY));
            user.setCity(cursor.getString(POSITION_CITY));
        }
        cursor.close();
        db.close();

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
     * Réinitialisé la base de données
     * Supprimer toutes les tables
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Supprimer toutes les lignes
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}
