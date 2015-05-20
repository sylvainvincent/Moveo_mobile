package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.adapter.PlacesListAdapter;
import fr.moveoteam.moveomobile.model.Place;

/**
 * Created by Amélie on 08/05/2015.
 */
public class TripPlacesListFragment extends ListFragment {

    ArrayList<Place> places;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // placesListView();
        ArrayList<Place> placeArrayList = getArguments().getParcelableArrayList("placeList");

        Log.e("array",placeArrayList.toString());
        if(placeArrayList != null) {
            setListAdapter(new PlacesListAdapter(getActivity(), placeArrayList));
        }else {
            setListAdapter(null);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    /*
    private void placesListView() {
        Place place1 = new Place("N", "A");
        Place place2 = new Place("N", "A");

        places = new ArrayList<>(2);
        places.add(place1);
        places.add(place2);
    } */

}
