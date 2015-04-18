package fr.moveoteam.moveomobile.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sylvain on 18/04/15.
 */
public class TripDataSource {

    // Base de données utilisable
    private SQLiteDatabase database;

    // Base de données inutilisable
    private DataBaseHandler dbHandler;

    // NOM DE LA TABLE
    private static final String TABLE_TRIP= "trip";

    // NUMÉRO DES COLONNES DE LA TABLE LOGIN
    private static final int POSITION_TRIP_ID = 0;
    private static final int POSITION_TRIP_NAME = 1;
    private static final int POSITION_TRIP_COUNTRY = 2;
    private static final int POSITION_TRIP_DESCRIPTION = 3;
    private static final int POSITION_TRIP_CREATED_AT = 4;

    public TripDataSource(Context context){
        dbHandler = new DataBaseHandler(context);
    }

    public ArrayList<Trip> getExploreTripList(){
        try {
            database = dbHandler.read();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Trip> tripList = new ArrayList<>(10);

        String selectQuery = "SELECT  * FROM " + TABLE_TRIP;

        Cursor cursor = database.rawQuery(selectQuery, null);
        // Se déplacer à la premiere ligne
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            tripList.add(new Trip(
                    cursor.getString(POSITION_TRIP_NAME),
                    cursor.getString(POSITION_TRIP_COUNTRY),
                    cursor.getString(POSITION_TRIP_DESCRIPTION),
                    cursor.getString(POSITION_TRIP_CREATED_AT)
            ));

        }
        cursor.close();
        database.close();

        return tripList;
    }

}
