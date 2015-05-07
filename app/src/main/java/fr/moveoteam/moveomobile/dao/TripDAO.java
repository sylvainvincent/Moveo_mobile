package fr.moveoteam.moveomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 18/04/15.
 */
public class TripDAO {

    // Base de données utilisable
    private SQLiteDatabase database = null;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_TRIP = "trip";

    // LES CHAMPS
    public static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_TRIP_NAME = "trip_name";
    public static final String KEY_TRIP_COUNTRY = "trip_country";
    public static final String KEY_TRIP_DESCRIPTION = "trip_description";
    public static final String KEY_TRIP_CREATED_AT = "trip_created_at";

    public TripDAO(Context context){
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
     * Fonction qui récupère 10 voyages aléatoirement
     * @return une collection contenant 10 voyages
     */
    public ArrayList<Trip> getExploreTripList(){

        ArrayList<Trip> tripList = new ArrayList<>(10);

        String selectQuery = "SELECT  * FROM " + TABLE_TRIP;

        Cursor cursor = database.rawQuery(selectQuery, null);
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            tripList.add(this.cursorToTrip(cursor));
        }
        cursor.close();
        // database.close();

        return tripList;
    }

    public ArrayList<Trip> getTripList(){
        ArrayList<Trip> tripList = null;
        String selectQuery = "SELECT  * FROM " + TABLE_TRIP;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.getCount()>0) {
            tripList = new ArrayList<>(cursor.getCount());
        }
        // Se déplacer à la première ligne
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            assert tripList != null;
            tripList.add(this.cursorToTrip(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        // database.close();
        if(tripList != null) {
            Log.i("Verification taille ", "" + tripList.size());
            Log.i("Verification nom ", "" + tripList.get(0).getName());
            Log.i("Verification nom ", "" + tripList.get(1).getName());
        }
        return tripList;
    }

    public void addTripListUser(ArrayList<Trip> tripList) {
        ContentValues values;
        for(Trip trip : tripList) {
            values = new ContentValues();
            values.put(KEY_TRIP_ID, trip.getId());
            values.put(KEY_TRIP_NAME, trip.getName());     // NOM
            values.put(KEY_TRIP_COUNTRY, trip.getCountry());   // PRÉNOM
            values.put(KEY_TRIP_DESCRIPTION, trip.getDescription());     // DATE DE NAISSANCE
            values.put(KEY_TRIP_CREATED_AT, String.valueOf(trip.getDateInsert()));
            // Insérer la ligne
            database.insert(TABLE_TRIP, null, values);
        }
    }

    /**
     * Récupere les informations du curseur pour les mettre dans la classe Trip
     * @param cursor un curseur
     * @return un voyage (Trip)
     */
    protected Trip cursorToTrip(Cursor cursor){
        Trip trip = new Trip();
        trip.setName(cursor.getString(DataBaseHandler.POSITION_TRIP_NAME));
        trip.setCountry(cursor.getString(DataBaseHandler.POSITION_TRIP_COUNTRY));
        trip.setDescription(cursor.getString(DataBaseHandler.POSITION_TRIP_DESCRIPTION));
        trip.setInsert(cursor.getString(DataBaseHandler.POSITION_TRIP_CREATED_AT));

        return trip;
    }

}
