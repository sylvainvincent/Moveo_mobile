package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Friend;

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

    // LES COLONNES
    private String[] allColumns = { DataBaseHandler.KEY_FRIEND_ID,
                                    DataBaseHandler.KEY_FRIEND_FIRSTNAME,
                                    DataBaseHandler.KEY_FRIEND_LASTNAME,
                                    DataBaseHandler.KEY_FRIEND_BIRTHDAY,
                                    DataBaseHandler.KEY_FRIEND_AVATAR,
                                    DataBaseHandler.KEY_FRIEND_COUNTRY,
                                    DataBaseHandler.KEY_FRIEND_CITY,
                                    DataBaseHandler.KEY_FRIEND_IS_ACCEPTED };

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
            values.put(DataBaseHandler.KEY_FRIEND_ID, friend.getId());
            values.put(DataBaseHandler.KEY_FRIEND_FIRSTNAME, friend.getFirstName());   // PRÉNOM
            values.put(DataBaseHandler.KEY_FRIEND_LASTNAME, friend.getLastName());     // NOM
            values.put(DataBaseHandler.KEY_FRIEND_BIRTHDAY, friend.getBirthday());     // DATE D'ANNIVERSAIRE
            values.put(DataBaseHandler.KEY_FRIEND_AVATAR, friend.getAvatarBase64());   // AVATAR
            values.put(DataBaseHandler.KEY_FRIEND_COUNTRY, friend.getCountry());       // PAYS
            values.put(DataBaseHandler.KEY_FRIEND_CITY, friend.getCity());             // VILLE
            values.put(DataBaseHandler.KEY_FRIEND_IS_ACCEPTED, ((friend.isFriend()) ? 1 : 0));     // AMI ?
            // Insérer la ligne
            database.insert(TABLE_FRIEND, null, values);
        }
    }
	
	public boolean acceptFriend(int friendId){

		ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_FRIEND_IS_ACCEPTED, 1); 
		return database.update(TABLE_FRIEND, values, DataBaseHandler.KEY_FRIEND_ID+" = "+friendId, null)>0;
	}
	
	public boolean removeFriend(int friendId){

		ContentValues values = new ContentValues();
		return database.delete(TABLE_FRIEND, DataBaseHandler.KEY_FRIEND_ID+" = "+friendId, null)>0;
	}
	
    public ArrayList<Friend> getFriendList(){
        ArrayList<Friend> friendList = null;

        Cursor cursor = database.query(TABLE_FRIEND,allColumns,DataBaseHandler.KEY_FRIEND_IS_ACCEPTED+" = 1",null,null,null,null);
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
//          Log.i("Verification nom ", "" + friendList.get(1).getLastName());
        }
        return friendList;
    }



    public ArrayList<Friend> getFriendRequestList(){
        ArrayList<Friend> friendList = null;

        Cursor cursor = database.query(TABLE_FRIEND, allColumns, DataBaseHandler.KEY_FRIEND_IS_ACCEPTED+" = 0", null, null, null, null);
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
//            Log.i("Verification nom ", "" + friendList.get(1).getLastName());
        }
        return friendList;
    }

    public Friend getFriend(int id){
        Friend friend = null;

        String selectQuery = "SELECT * FROM " + TABLE_FRIEND;
        Cursor cursor = database.query(TABLE_FRIEND, allColumns, DataBaseHandler.KEY_FRIEND_ID+" = "+id, null, null, null, null);

        // Se déplacer à la premiere ligne
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            friend = cursorToFriend(cursor);
        }
        cursor.close();
        // database.close();

        return friend;
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Trip
     * @param cursor un curseur
     * @return un voyage (Trip)
     */
    Friend cursorToFriend(Cursor cursor){
        Friend friend = new Friend() ;
        friend.setId(cursor.getInt(DataBaseHandler.POSITION_FRIEND_ID));
        friend.setFirstName(cursor.getString(DataBaseHandler.POSITION_FRIEND_FIRSTNAME));
        friend.setLastName(cursor.getString(DataBaseHandler.POSITION_FRIEND_LASTNAME));
        friend.setBirthday(cursor.getString(DataBaseHandler.POSITION_FRIEND_BIRTHDAY));
        friend.setAvatarBase64(cursor.getString(DataBaseHandler.POSITION_FRIEND_AVATAR));
        friend.setCountry(cursor.getString(DataBaseHandler.POSITION_FRIEND_COUNTRY));
        friend.setCity(cursor.getString(DataBaseHandler.POSITION_FRIEND_CITY));
        friend.setFriend(cursor.getInt(DataBaseHandler.POSITION_FRIEND_IS_ACCEPTED) != 0);

        return friend;
    }

}
