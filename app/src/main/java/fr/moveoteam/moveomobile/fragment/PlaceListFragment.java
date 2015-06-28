package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.PlaceActivity;
import fr.moveoteam.moveomobile.adapter.PlacesListAdapter;
import fr.moveoteam.moveomobile.model.Place;

/**
 * Listes des lieux du catégories sélectionnés 
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
			// Récupération du la liste des lieux
            placeArrayList = getArguments().getParcelableArrayList("placeList");
            Log.e("array", placeArrayList.toString());
        }
        else
            placeArrayList = null;

        setListAdapter(new PlacesListAdapter(getActivity(), placeArrayList));

    }

    @Override // Ouvre une nouvelle activité "PlaceActivity" avec les informations du lieu
    public void onListItemClick(ListView l, View v, int position, long id) {
		
        super.onListItemClick(l, v, position, id);
        Place place = placeArrayList.get(position); 
        Log.e("Recuperation", ""+place.getId());
        Intent intent = new Intent(getActivity(), PlaceActivity.class);
        intent.putExtra("placeName",place.getName());
        intent.putExtra("placeAddress",place.getAddress());
        intent.putExtra("placeDescription",place.getDescription());
        startActivity(intent);

    }

}
