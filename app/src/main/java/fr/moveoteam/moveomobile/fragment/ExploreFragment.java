package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 29/04/15.
 */
public class ExploreFragment extends Fragment {

    private AdapterView.OnItemSelectedListener listener;

    public  ExploreFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.explore, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listViewExploreTrip);
        return view;
    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        } */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // May also be triggered from the Activity
    public void updateDetail() {
        // create a string, just for testing
        String newTime = String.valueOf(System.currentTimeMillis());

        // Inform the Activity about the change based
        // interface defintion
       // listener.onRssItemSelected(newTime);
    }

}
