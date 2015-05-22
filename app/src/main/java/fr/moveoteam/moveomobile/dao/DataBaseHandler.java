package fr.moveoteam.moveomobile.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String TABLE_PLACE = "place";
    private static final String TABLE_COMMENT = "comment";
    private static final String TABLE_PHOTO = "photo";
    private static final String TABLE_FRIEND = "friend";
    private static final String TABLE_DIALOG = "dialog";

    // NOM DES COLONNES :

        // Login
        public static final String KEY_USER_ID = "user_id";
        public static final String KEY_USER_LASTNAME = "user_lastName";
        public static final String KEY_USER_FIRSTNAME = "user_firstName";
        public static final String KEY_USER_BIRTHDAY = "user_birthday";
        public static final String KEY_USER_AVATAR = "user_avatar";
        public static final String KEY_USER_EMAIL = "user_email";
        public static final String KEY_USER_PASSWORD = "user_password";
        public static final String KEY_USER_COUNTRY = "user_country";
        public static final String KEY_USER_CITY = "user_city";

        // Trip
        public static final String KEY_TRIP_ID = "trip_id";
        public static final String KEY_TRIP_NAME = "trip_name";
        public static final String KEY_TRIP_COUNTRY = "trip_country";
        public static final String KEY_TRIP_DESCRIPTION = "trip_description";
        public static final String KEY_TRIP_CREATED_AT = "trip_created_at";
        public static final String KEY_TRIP_COVER = "trip_cover";
        public static final String KEY_TRIP_USER = "trip_user";

        // Place
        public static final String KEY_PLACE_ID = "place_id";
        public static final String KEY_PLACE_NAME = "place_name";
        public static final String KEY_PLACE_ADDRESS = "place_address";
        public static final String KEY_PLACE_DESCRIPTION = "place_description";
        public static final String KEY_PLACE_CATEGORY= "place_category";
        public static final String KEY_PLACE_TRIP_ID = "trip_id";

        // Comment
        public static final String KEY_COMMENT_ID = "comment_id";
        public static final String KEY_COMMENT_MESSAGE = "comment_message";
        public static final String KEY_COMMENT_DATE = "comment_date";
        public static final String KEY_COMMENT_AUTHOR = "author_id";
        public static final String KEY_COMMENT_TRIP_ID = "trip_id";

        // Photo
        public static final String KEY_PHOTO_ID = "photo_id";
        public static final String KEY_PHOTO_BASE64 = "photo_base64";
        public static final String KEY_PHOTO_DATE = "photo_date";
        public static final String KEY_PHOTO_TRIP_ID = "trip_id";

        // Friend
        public static final String KEY_FRIEND_ID = "friend_id";
        public static final String KEY_FRIEND_LASTNAME = "friend_lastname";
        public static final String KEY_FRIEND_FIRSTNAME = "friend_firstname";
        public static final String KEY_FRIEND_IS_ACCEPTED = "friend_is_accepted";

        // Dialog
        public static final String KEY_DIALOG_RECIPIENT_ID = "dialog_recipient_id";
        public static final String KEY_DIALOG_RECIPIENT_LASTNAME = "dialog_recipient_lastname";
        public static final String KEY_DIALOG_RECIPIENT_FIRSTNAME = "dialog_recipient_firstname";
        public static final String KEY_DIALOG_MESSAGE = "dialog_message";
        public static final String KEY_DIALOG_DATE = "dialog_date";
        public static final String KEY_DIALOG_READ = "dialog_read";
        public static final String KEY_DIALOG_IS_INBOX = "dialog_is_inbox";

    // POSITION DES COLONNES

        // Trip
        public static final int POSITION_TRIP_ID = 0;
        public static final int POSITION_TRIP_NAME = 1;
        public static final int POSITION_TRIP_COUNTRY = 2;
        public static final int POSITION_TRIP_DESCRIPTION = 3;
        public static final int POSITION_TRIP_CREATED_AT = 4;
        public static final int POSITION_TRIP_COVER= 5;
        public static final int POSITION_TRIP_USER= 6;

        // Place
        public static final int POSITION_PLACE_ID = 0;
        public static final int POSITION_PLACE_NAME = 1;
        public static final int POSITION_PLACE_ADDRESS = 2;
        public static final int POSITION_PLACE_DESCRIPTION = 3;
        public static final int POSITION_PLACE_CATEGORY = 4;
        public static final int POSITION_PLACE_TRIP_ID = 5;


        // Comment
        public static final int POSITION_COMMENT_ID = 0;
        public static final int POSITION_COMMENT_MESSAGE = 1;
        public static final int POSITION_COMMENT_DATE = 2;
        public static final int POSITION_COMMENT_AUTHOR = 3;
        public static final int POSITION_COMMENT_ID_TRIP = 4;

        // Photo
        public static final int POSITION_PHOTO_ID = 0;
        public static final int POSITION_PHOTO_BASE64 = 1;
        public static final int POSITION_PHOTO_DATE = 2;
        public static final int POSITION_PHOTO_TRIP_ID = 3;

        // Friend
        public static final int POSITION_FRIEND_ID = 0;
        public static final int POSITION_FRIEND_LASTNAME = 1;
        public static final int POSITION_FRIEND_FIRSTNAME = 2;
        public static final int POSITION_FRIEND_IS_ACCEPTED = 3;

        // Dialog
        public static final int POSITION_DIALOG_RECIPIENT_ID = 0;
        public static final int POSITION_DIALOG_RECIPIENT_LASTNAME = 1;
        public static final int POSITION_DIALOG_RECIPIENT_FIRSTNAME = 2;
        public static final int POSITION_DIALOG_MESSAGE = 3;
        public static final int POSITION_DIALOG_DATE = 4;
        public static final int POSITION_DIALOG_READ = 5;
        public static final int POSITION_DIALOG_IS_INBOX = 6;

    // CREATION DES TABLES

        //USER
        private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY ,"
                + KEY_USER_LASTNAME + " TEXT,"
                + KEY_USER_FIRSTNAME + " TEXT,"
                + KEY_USER_BIRTHDAY + " TEXT,"
                + KEY_USER_AVATAR + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT,"
                + KEY_USER_COUNTRY + " TEXT,"
                + KEY_USER_CITY + " TEXT" + ")";

        // TRIP
        private static final String CREATE_TRIP_TABLE = "CREATE TABLE "+ TABLE_TRIP + "("
                + KEY_TRIP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TRIP_NAME + " TEXT,"
                + KEY_TRIP_COUNTRY + " TEXT,"
                + KEY_TRIP_DESCRIPTION + " TEXT,"
                + KEY_TRIP_CREATED_AT + " TEXT,"
                + KEY_TRIP_COVER + " TEXT,"
                + KEY_TRIP_USER + " INTEGER" + ")";

        // TRIP
        private static final String CREATE_PLACE_TABLE = "CREATE TABLE "+ TABLE_PLACE + "("
                + KEY_PLACE_ID + " INTEGER PRIMARY KEY,"
                + KEY_PLACE_NAME + " TEXT,"
                + KEY_PLACE_ADDRESS + " TEXT,"
                + KEY_PLACE_DESCRIPTION + " TEXT"
                + KEY_PLACE_CATEGORY + " INTEGER"
                + KEY_PLACE_TRIP_ID + " INTEGER" + ")";

        // Comment
        private static final String CREATE_COMMENT_TABLE = "CREATE TABLE "+ TABLE_COMMENT + "("
                + KEY_COMMENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_COMMENT_MESSAGE + " TEXT,"
                + KEY_COMMENT_DATE + " TEXT,"
                + KEY_COMMENT_AUTHOR + " TEXT,"
                + KEY_COMMENT_TRIP_ID + " INTEGER" + ")";

        // Comment
        private static final String CREATE_PHOTO_TABLE = "CREATE TABLE "+ TABLE_PHOTO + "("
                + KEY_PHOTO_ID + " INTEGER PRIMARY KEY,"
                + KEY_PHOTO_BASE64 + " TEXT,"
                + KEY_PHOTO_DATE + " TEXT,"
                + KEY_PHOTO_TRIP_ID + " INTEGER" + ")";

        // FRIEND
        private static final String CREATE_FRIEND_TABLE = "CREATE TABLE "+ TABLE_FRIEND + "("
                + KEY_FRIEND_ID + " INTEGER PRIMARY KEY,"
                + KEY_FRIEND_LASTNAME + " TEXT,"
                + KEY_FRIEND_FIRSTNAME + " TEXT,"
                + KEY_FRIEND_IS_ACCEPTED + " INTEGER" + ")";

        // Dialog
        private static final String CREATE_DIALOG_TABLE = "CREATE TABLE "+ TABLE_DIALOG + "("
                + KEY_DIALOG_RECIPIENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_DIALOG_RECIPIENT_LASTNAME + " TEXT,"
                + KEY_DIALOG_RECIPIENT_FIRSTNAME + " TEXT,"
                + KEY_DIALOG_MESSAGE + " TEXT,"
                + KEY_DIALOG_DATE + " TEXT,"
                + KEY_DIALOG_READ + " INTEGER,"
                + KEY_DIALOG_IS_INBOX + " INTEGER" + ")";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TRIP_TABLE);
        db.execSQL(CREATE_PLACE_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_PHOTO_TABLE);
        db.execSQL(CREATE_FRIEND_TABLE);
        db.execSQL(CREATE_DIALOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SUPPRIMER l'ANCIENNE TABLE s'il en existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOG);
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
        db.delete(TABLE_PLACE, null, null);
        db.delete(TABLE_COMMENT, null, null);
        db.delete(TABLE_PHOTO, null, null);
        db.delete(TABLE_FRIEND, null, null);
        db.delete(TABLE_DIALOG, null, null);
        db.close();
    }
}
