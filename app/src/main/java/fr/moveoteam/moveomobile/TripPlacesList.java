package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Place;

/**
 * Created by Amélie on 08/05/2015.
 */
public class TripPlacesList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_places_list);

        placesListView();
    }

    private void placesListView() {
        Place place1 = new Place("N", "A");
        Place place2 = new Place("N", "A");

        ArrayList<Place> places = new ArrayList<>(2);
    }

}
