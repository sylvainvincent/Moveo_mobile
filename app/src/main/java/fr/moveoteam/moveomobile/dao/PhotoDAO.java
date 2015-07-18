package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Photo;

/**
 * Created by Sylvain on 18/04/15.
 */
public class PhotoDAO {

    // Base de données utilisable
    private SQLiteDatabase database = null;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_PHOTO = "photo";

    // LES COLONNES
    private String[] allColumns = { DataBaseHandler.KEY_PHOTO_ID,
                                    DataBaseHandler.KEY_PHOTO_BASE64,
                                    DataBaseHandler.KEY_PHOTO_DATE,
                                    DataBaseHandler.KEY_PHOTO_TRIP_ID};

    public PhotoDAO(Context context){
        dbHandler = new DataBaseHandler(context);
    }

    /**
     * Permet à la base de données de faire des ajouts ou des suppressions
     * @return Une base de données modifiable (Écriture + lecture)
     */
    public SQLiteDatabase open() {
        database = dbHandler.getWritableDatabase();
        return database;
    }

    /**
     * Permet de fermer la base de données
     */
    public void close() {
        dbHandler.close();
    }
    
    /**
     * Supprime une photo de la base de données SQLite
     *
     */
    public boolean removePhoto(int photoId){

		return database.delete(TABLE_PHOTO, DataBaseHandler.KEY_PHOTO_ID+" = "+photoId, null)>0;
	}
    
    /**
     * Ajouter une photo
     *
     */
    public void addPhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_PHOTO_ID, photo.getId());                   // ID
        values.put(DataBaseHandler.KEY_PHOTO_BASE64, photo.getPhotoBase64());      // PHOTO EN BASE 64
        values.put(DataBaseHandler.KEY_PHOTO_DATE, photo.getPublicationDate());    // DATE DE PUBLICATION
        values.put(DataBaseHandler.KEY_PHOTO_TRIP_ID, photo.getTripId());          // ID D'UN VOYAGE
        // Insérer la ligne
        database.insert(TABLE_PHOTO, null, values);
        
    }
    
    /**
     * Recupere la liste des photos d'un voyage
     * @param tripId l'identifiant du lieu
     * @return une arrayList de photo
     */
    public ArrayList<Photo> getPhotoList(int tripId){
        ArrayList<Photo> photoList = null;

        Cursor cursor = database.query(TABLE_PHOTO, allColumns, DataBaseHandler.KEY_PHOTO_TRIP_ID + " = " + tripId, null, null, null, null);
        if(cursor.getCount()>0) {
            photoList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert photoList != null;
            photoList.add(this.cursorToPhoto(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(photoList != null) {
            Log.i("PhotoDAO size", "" + photoList.size());
        }
        return photoList;
    }

    public void addPhotoList(ArrayList<Photo> photoList) {
        ContentValues values;
        for(Photo photo : photoList) {
            values = new ContentValues();
            values.put(DataBaseHandler.KEY_PHOTO_ID, photo.getId());                   // ID
            values.put(DataBaseHandler.KEY_PHOTO_BASE64, photo.getPhotoBase64());        // PHOTO EN BASE 64
            values.put(DataBaseHandler.KEY_PHOTO_DATE, photo.getPublicationDate()); // DATE DE PUBLICATION
            values.put(DataBaseHandler.KEY_PHOTO_TRIP_ID, photo.getTripId());          // ID D'UN VOYAGE
            // Insérer la ligne
            database.insert(TABLE_PHOTO, null, values);
        }
    }

    /**
     * Récupère les informations du curseur pour les mettre dans la classe Photo
     * @param cursor un curseur
     * @return une photo (Photo)
     */
    Photo cursorToPhoto(Cursor cursor){

        Photo photo = new Photo();
        photo.setId(cursor.getInt(DataBaseHandler.POSITION_PHOTO_ID));
        photo.setPhotoBase64(cursor.getString(DataBaseHandler.POSITION_PHOTO_BASE64));
        photo.setPublicationDate(cursor.getString(DataBaseHandler.POSITION_PHOTO_DATE));
        photo.setTripId(cursor.getInt(DataBaseHandler.POSITION_PHOTO_TRIP_ID));

        return photo;
    }

}
