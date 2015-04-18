package fr.moveoteam.moveomobile.model;

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
        private static final String KEY_USER_ID = "user_id";
        private static final String KEY_USER_LASTNAME = "user_lastName";
        private static final String KEY_USER_FIRSTNAME = "user_firstName";
        private static final String KEY_USER_BIRTHDAY = "user_birthday";
        private static final String KEY_USER_EMAIL = "user_email";
        private static final String KEY_USER_COUNTRY = "user_country";
        private static final String KEY_USER_CITY = "user_city";

        // Trip
        private static final String KEY_TRIP_ID = "trip_id";
        private static final String KEY_TRIP_NAME = "trip_name";
        private static final String KEY_TRIP_COUNTRY = "trip_country";
        private static final String KEY_TRIP_DESCRIPTION = "trip_description";
        private static final String KEY_TRIP_CREATED_AT = "trip_created_at";

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
     * Cette fonction renvoie une base de données dans lequel on peut faire des ajouts
     *
     * @return SQLiteDatabase modifiable
     * @throws SQLException
     */
    public SQLiteDatabase write() throws SQLException {
        return this.getWritableDatabase();
    }

    /**
     * Cette fonction renvoie une base de données dans lequel on peut seulement lire
     *
     * @return SQLiteDatabase lisible
     * @throws SQLException
     */
    public SQLiteDatabase read() throws SQLException {
        return this.getWritableDatabase();
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
        db.delete(TABLE_TRIP, null, null);
        db.close();
    }
}
