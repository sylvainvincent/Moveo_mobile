package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import java.util.ArrayList;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.PlacesListAdapter;

/**
 * Created by Amélie on 08/05/2015.
 */
public class TripPlacesListFragment extends ListFragment {

    ArrayList<Place> places;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        placesListView();
        if(places != null) {
            setListAdapter(new PlacesListAdapter(getActivity(), places));
        }
        else {
            setListAdapter(null);
        }
    }

    private void placesListView() {
        Place place1 = new Place("N", "A");
        Place place2 = new Place("N", "A");

        places = new ArrayList<>(2);
        places.add(place1);
        places.add(place2);
    }

}
