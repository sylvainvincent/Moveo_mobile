package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 03/05/15.
 */
public class TripFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TripDAO tripDAO = new TripDAO(getActivity());
        tripDAO.open();
        String mot[] = new String[5];
        mot[0]= "bonjour";
        ArrayList<Trip> tripArrayList = tripDAO.getTripList();

            setListAdapter(new CustomListAdapter(getActivity(), tripArrayList, false));


    }



}
