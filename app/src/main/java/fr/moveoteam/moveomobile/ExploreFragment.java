package fr.moveoteam.moveomobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sylvain on 29/04/15.
 */
public class ExploreFragment extends Fragment {

    public ExploreFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.explore, container, false);

        return rootView;
    }

}
