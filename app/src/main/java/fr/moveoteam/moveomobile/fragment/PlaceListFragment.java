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
public class PlaceListFragment extends ListFragment {

    ArrayList<Place> placeArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments().getParcelableArrayList("placeList") != null) {
            placeArrayList = getArguments().getParcelableArrayList("placeList");
            Log.e("array", placeArrayList.toString());
        }
        else
            placeArrayList = null;

        if(placeArrayList != null) {
            setListAdapter(new PlacesListAdapter(getActivity(), placeArrayList));
        }else {
            setListAdapter(null);
        }
    }

}
