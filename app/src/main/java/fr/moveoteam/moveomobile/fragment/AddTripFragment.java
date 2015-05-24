package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 07/05/15.
 */
public class AddTripFragment extends Fragment{

    TextView text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_button_add_trip, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text = (TextView) getActivity().findViewById(R.id.add_trip_text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
