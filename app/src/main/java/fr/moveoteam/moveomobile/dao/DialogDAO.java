package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    // LES COLONNES
    private String[] allColumns = {
            DataBaseHandler.KEY_DIALOG_ID,
            DataBaseHandler.KEY_DIALOG_RECIPIENT_ID,
            DataBaseHandler.KEY_DIALOG_RECIPIENT_LASTNAME,
            DataBaseHandler.KEY_DIALOG_RECIPIENT_FIRSTNAME,
            DataBaseHandler.KEY_DIALOG_MESSAGE,
            DataBaseHandler.KEY_DIALOG_DATE,
            DataBaseHandler.KEY_DIALOG_READ,
            DataBaseHandler.KEY_DIALOG_IS_INBOX };

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


    /**
     *
     * @param dialogArrayList ArrayList contenant les messages reçus et envoyés
     */
    public void addDialogList(ArrayList<Dialog> dialogArrayList) {
        ContentValues values;
        for(Dialog dialog : dialogArrayList) {
            values = new ContentValues();
            values.put(DataBaseHandler.KEY_DIALOG_RECIPIENT_ID, dialog.getRecipientId());
            values.put(DataBaseHandler.KEY_DIALOG_RECIPIENT_LASTNAME, dialog.getRecipientLastName());     // NOM
            values.put(DataBaseHandler.KEY_DIALOG_RECIPIENT_FIRSTNAME, dialog.getRecipientFirstName());   // PRÉNOM
            values.put(DataBaseHandler.KEY_DIALOG_MESSAGE, dialog.getMessage());
            values.put(DataBaseHandler.KEY_DIALOG_DATE, dialog.getDate());
            values.put(DataBaseHandler.KEY_DIALOG_READ, dialog.isRead()?1:0);
            values.put(DataBaseHandler.KEY_DIALOG_IS_INBOX, dialog.isInbox()?1:0);
            // Insérer la ligne
            database.insert(TABLE_DIALOG, null, values);
        }
    }

    public ArrayList<Dialog> getInboxList(){
        ArrayList<Dialog> inboxArrayList = null;

        Cursor cursor = database.query(TABLE_DIALOG, allColumns, DataBaseHandler.KEY_DIALOG_IS_INBOX + " = 1", null, null, null, null);
        if(cursor.getCount()>0) {
            inboxArrayList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert inboxArrayList != null;
            inboxArrayList.add(this.cursorToDialog(cursor, true));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(inboxArrayList != null) {
            Log.i("Verification taille ", "" + inboxArrayList.size());
            Log.i("Verification nom ", "" + inboxArrayList.get(0).getRecipientFirstName());
            Log.i("Verification nom ", "" + inboxArrayList.get(0).isRead());
        }
        return inboxArrayList;
    }
	
	// Retourne le nombre de message non lu
	// A COMPLETER !!!!!
	public int getInboxCount(){
		Cursor cursor = database.query(TABLE_DIALOG, allColumns, DataBaseHandler.KEY_DIALOG_IS_INBOX + " = 1", null, null , null, null);
		cursor.close();
		return cursor.getCount();
	}
	

    public ArrayList<Dialog> getSendboxList(){
        ArrayList<Dialog> sendboxArrayList = null;

        Cursor cursor = database.query(TABLE_DIALOG, allColumns, DataBaseHandler.KEY_DIALOG_IS_INBOX+" = 0", null, null, null, null);
        if(cursor.getCount()>0) {
            sendboxArrayList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert sendboxArrayList != null;
            sendboxArrayList.add(this.cursorToDialog(cursor, false));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(sendboxArrayList != null) {
            Log.i("Verification taille ", "" + sendboxArrayList.size());
            Log.i("Verification nom ", "" + sendboxArrayList.get(0).getRecipientFirstName());
            Log.i("Verification nom ", "" + sendboxArrayList.get(0).getRecipientLastName());
        }
        return sendboxArrayList;
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Trip
     * @param cursor un curseur
     * @return un voyage (Trip)
     */
    Dialog cursorToDialog(Cursor cursor, boolean isInbox){

        Dialog dialog = new Dialog() ;
        dialog.setRecipientId(cursor.getInt(DataBaseHandler.POSITION_DIALOG_RECIPIENT_ID));
        dialog.setRecipientLastName(cursor.getString(DataBaseHandler.POSITION_DIALOG_RECIPIENT_LASTNAME));
        dialog.setRecipientFirstName(cursor.getString(DataBaseHandler.POSITION_DIALOG_RECIPIENT_FIRSTNAME));
        dialog.setMessage(cursor.getString(DataBaseHandler.POSITION_DIALOG_MESSAGE));
        dialog.setDate(cursor.getString(DataBaseHandler.POSITION_DIALOG_DATE));
        if(isInbox) dialog.setRead(cursor.getInt(DataBaseHandler.POSITION_DIALOG_READ)==1);
        else dialog.setRead(cursor.getInt(DataBaseHandler.POSITION_DIALOG_READ)==0);
        dialog.setInbox(isInbox);

        return dialog;
    }
}
