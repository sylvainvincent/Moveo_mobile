package fr.moveoteam.moveomobile.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

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
    private static final String TABLE_TRIP = "trip";

    // NOM DES COLONNES :

        // Login
        public static final String KEY_USER_ID = "user_id";
        public static final String KEY_USER_LASTNAME = "user_lastName";
        public static final String KEY_USER_FIRSTNAME = "user_firstName";
        public static final String KEY_USER_BIRTHDAY = "user_birthday";
        public static final String KEY_USER_EMAIL = "user_email";
        public static final String KEY_USER_COUNTRY = "user_country";
        public static final String KEY_USER_CITY = "user_city";

        // Trip
        public static final String KEY_TRIP_ID = "trip_id";
        public static final String KEY_TRIP_NAME = "trip_name";
        public static final String KEY_TRIP_COUNTRY = "trip_country";
        public static final String KEY_TRIP_DESCRIPTION = "trip_description";
        public static final String KEY_TRIP_CREATED_AT = "trip_created_at";

    // POSITION DES COLONNES

        // Trip
        public static final int POSITION_TRIP_ID = 0;
        public static final int POSITION_TRIP_NAME = 1;
        public static final int POSITION_TRIP_COUNTRY = 2;
        public static final int POSITION_TRIP_DESCRIPTION = 3;
        public static final int POSITION_TRIP_CREATED_AT = 4;

    // CREATION DES TABLES
        private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY ,"
                + KEY_USER_LASTNAME + " TEXT,"
                + KEY_USER_FIRSTNAME + " TEXT,"
                + KEY_USER_BIRTHDAY + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_COUNTRY + " TEXT,"
                + KEY_USER_CITY + " TEXT"+ ")";

        private static final String CREATE_TRIP_TABLE = "CREATE TABLE "+ TABLE_TRIP + "("
                + KEY_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_TRIP_NAME + " TEXT,"
                + KEY_TRIP_COUNTRY + " TEXT,"
                + KEY_TRIP_DESCRIPTION + " TEXT,"
                + KEY_TRIP_CREATED_AT + " TEXT"+ ")";

  public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TRIP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SUPPRIMER l'ANCIENNE TABLE s'il en existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);

        // RECRÉER LA TABLE
        onCreate(db);
    }

    /**
     * Réinitialiser la base de données
     * Supprimer toutes les tables
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Supprimer toutes les lignes
        db.delete(TABLE_LOGIN, null, null);
        db.delete(TABLE_TRIP, null, null);
        db.close();
    }
}
