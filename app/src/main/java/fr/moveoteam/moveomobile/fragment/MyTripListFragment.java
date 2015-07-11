package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.HomeActivity;
import fr.moveoteam.moveomobile.activity.MyTripActivity;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.adapter.TripListAdapter;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 03/05/15.
 */
public class MyTripListFragment extends ListFragment {

    ArrayList<Trip> tripArrayList;
    TripListAdapter tripListAdapter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TripDAO tripDAO = new TripDAO(getActivity());
        tripDAO.open();
        tripArrayList = tripDAO.getTripList();
        if(tripArrayList != null) {
            if (tripListAdapter != null) tripListAdapter.updateResult(tripArrayList);
            else tripListAdapter = new TripListAdapter(getActivity(), tripArrayList, false);
            setListAdapter(tripListAdapter);
        }else setListAdapter(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Trip trip = tripArrayList.get(position);
        Log.e("Recuperation", trip.getName());
        Intent intent = new Intent(getActivity(), MyTripActivity.class);
        intent.putExtra("id",trip.getId());
        Log.e("id trip frag","" + trip.getId());
        getActivity().startActivityForResult(intent, 2);
    }

}