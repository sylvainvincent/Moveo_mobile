package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.adapter.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 03/05/15.
 */
public class MyTripListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TripDAO tripDAO = new TripDAO(getActivity());
        tripDAO.open();
        ArrayList<Trip> tripArrayList = tripDAO.getTripList();
        if(tripArrayList != null)
            setListAdapter(new CustomListAdapter(getActivity(), tripArrayList, false));
        else setListAdapter(null);
    }



}
