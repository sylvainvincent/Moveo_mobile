package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Dialog;

/**
 * Created by Sylvain on 12/05/15.
 */
public class DialogDAO {

    // Base de données utilisable
    private SQLiteDatabase database = null;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_DIALOG = "dialog";

    // Dialog
    public static final String KEY_DIALOG_RECIPIENT_ID = "friend_id";
    public static final String KEY_DIALOG_RECIPIENT_LASTNAME = "dialog_recipient_lastname";
    public static final String KEY_DIALOG_RECIPIENT_FIRSTNAME = "dialog_recipient_firstname";
    public static final String KEY_DIALOG_MESSAGE = "dialog_message";
    public static final String KEY_DIALOG_DATE = "dialog_date";
    public static final String KEY_DIALOG_READ = "read";

    private String[] allColumns = { DataBaseHandler.KEY_DIALOG_RECIPIENT_ID,
            DataBaseHandler.KEY_DIALOG_RECIPIENT_LASTNAME,
            DataBaseHandler.KEY_DIALOG_RECIPIENT_FIRSTNAME,
            DataBaseHandler.KEY_DIALOG_MESSAGE,
            DataBaseHandler.KEY_DIALOG_DATE,
            DataBaseHandler.KEY_DIALOG_READ};

    public DialogDAO(Context context){
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

    public void addInboxList(ArrayList<Dialog> inboxList) {
        ContentValues values;
        for(Dialog dialog : inboxList) {
            values = new ContentValues();
            values.put(KEY_DIALOG_RECIPIENT_ID, dialog.getRecipientId());
            values.put(KEY_DIALOG_RECIPIENT_LASTNAME, dialog.getRecipientLastName());     // NOM
            values.put(KEY_DIALOG_RECIPIENT_FIRSTNAME, dialog.getRecipientFirstName());   // PRÉNOM
            values.put(KEY_DIALOG_MESSAGE, dialog.getMessage());
            values.put(KEY_DIALOG_DATE, dialog.getDate());
            values.put(KEY_DIALOG_READ, dialog.isRead());
            // Insérer la ligne
            database.insert(TABLE_DIALOG, null, values);
        }
    }
/*
    public ArrayList<Friend> getFriendList(){
        ArrayList<Friend> friendList = null;
        String selectQuery = "SELECT  * FROM " + TABLE_FRIEND;

        Cursor cursor = database.query(TABLE_FRIEND,allColumns,KEY_FRIEND_IS_ACCEPTED+" = 1",null,null,null,null);
        if(cursor.getCount()>0) {
            friendList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert friendList != null;
            friendList.add(this.cursorToFriend(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(friendList != null) {
            Log.i("Verification taille ", "" + friendList.size());
            Log.i("Verification nom ", "" + friendList.get(0).getLastName());
            Log.i("Verification nom ", "" + friendList.get(1).getLastName());
        }
        return friendList;
    }

    public ArrayList<Friend> getFriendRequestList(){
        ArrayList<Friend> friendList = null;
        // String selectQuery = "SELECT  * FROM " + TABLE_FRIEND+ "WHERE "+ KEY_FRIEND_IS_ACCEPTED+" = 1";

        Cursor cursor = database.query(TABLE_FRIEND,allColumns,KEY_FRIEND_IS_ACCEPTED+" = 0",null,null,null,null);
        if(cursor.getCount()>0) {
            friendList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert friendList != null;
            friendList.add(this.cursorToFriend(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(friendList != null) {
            Log.i("Verification taille ", "" + friendList.size());
            Log.i("Verification nom ", "" + friendList.get(0).getLastName());
            Log.i("Verification nom ", "" + friendList.get(1).getLastName());
        }
        return friendList;
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Trip
     * @param cursor un curseur
     * @return un voyage (Trip)
     */
    /*protected Message Friend cursorToMessage(Cursor cursor){
        Message message = new Message() ;
        message.setId(cursor.getInt(DataBaseHandler.POSITION_FRIEND_ID));
        friend.setLastName(cursor.getString(DataBaseHandler.POSITION_FRIEND_LASTNAME));
        friend.setFirstName(cursor.getString(DataBaseHandler.POSITION_FRIEND_FIRSTNAME));
        friend.setFriend((cursor.getInt(DataBaseHandler.POSITION_FRIEND_IS_ACCEPTED))!=0);

        return message;
    }/*/
}
