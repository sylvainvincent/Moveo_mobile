package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 10/05/15.
 */
public class TripHomeFragment extends Fragment {

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_description, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // ArrayList<Trip> tripArrayList = tripDAO.getTripList();
    }
}
