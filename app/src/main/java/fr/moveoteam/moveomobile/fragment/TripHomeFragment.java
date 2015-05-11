package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 10/05/15.
 */
public class TripHomeFragment extends Fragment {

    OnInformationListener information;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_description, container,false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            information = (OnInformationListener) activity;
        }catch (Exception e){

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // ArrayList<Trip> tripArrayList = tripDAO.getTripList();
    }

    public interface OnInformationListener{

        public void getInformation(Trip trip);

    }

}
