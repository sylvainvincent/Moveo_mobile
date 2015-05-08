package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 08/05/15.
 */
public class FriendDAO {
    // Base de données utilisable
    private SQLiteDatabase database = null;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_FRIEND = "friend";

    // LES CHAMPS
    public static final String KEY_FRIEND_ID = "friend_id";
    public static final String KEY_FRIEND_LASTNAME = "friend_lastname";
    public static final String KEY_FRIEND_FIRSTNAME = "friend_firstname";
    public static final String KEY_FRIEND_IS_ACCEPTED = "friend_is_accepted";

    public FriendDAO(Context context){
        dbHandler = new DataBaseHandler(context);
    }

    /**
     * Permet à la base de données de faire des ajouts ou des suppressions
     * @return Une base de données modifiable (Écriture + lecture)
     */
    public SQLiteDatabase open() {
        this.database = dbHandler.getWritableDatabase();
        return database;
    }

    /**
     * Permet de fermer la base de données
     */
    public void close() {
        dbHandler.close();
    }

    public void addFriendList(ArrayList<Friend> friendList) {
        ContentValues values;
        for(Friend friend : friendList) {
            values = new ContentValues();
            values.put(KEY_FRIEND_ID, friend.getId());
            values.put(KEY_FRIEND_LASTNAME, friend.getLastName());     // NOM
            values.put(KEY_FRIEND_FIRSTNAME, friend.getFirstName());   // PRÉNOM
            values.put(KEY_FRIEND_IS_ACCEPTED, friend.isFriend());
            // Insérer la ligne
            database.insert(TABLE_FRIEND, null, values);
        }
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Trip
     * @param cursor un curseur
     * @return un voyage (Trip)
     */
    protected Friend cursorToFriend(Cursor cursor){
        Friend friend = new Friend() ;
        /*friend.setId(cursor.getInt(DataBaseHandler.POSITION_FRIEND_ID));
        friend.setLastName(cursor.getString(DataBaseHandler.POSITION_FRIEND_LASTNAME));
        friend.setFirstName(cursor.getString(DataBaseHandler.POSITION_FRIEND_FIRSTNAME));
        friend.setFriend((cursor.getInt(DataBaseHandler.POSITION_FRIEND_IS_ACCEPTED))!=0);
        */
        return friend;
    }

}
