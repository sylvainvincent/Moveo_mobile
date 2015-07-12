package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.TripActivity;
import fr.moveoteam.moveomobile.adapter.TripListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 16/06/15.
 */
public class TripListFragment extends ListFragment {

    private ArrayList<Trip> tripArrayList;
    private int id;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        id = getArguments().getInt("otherUserId",0);
        Log.e("id : ", "" + id);
        new ExecuteThread().execute();
        /*
        TripDAO tripDAO = new TripDAO(getActivity());
        tripDAO.open();
        tripArrayList = tripDAO.getTripList();
        if(tripArrayList != null)
            setListAdapter(new TripListAdapter(getActivity(), tripArrayList, false));
        else setListAdapter(null);
    */}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Trip trip = tripArrayList.get(position);
        Log.e("Recuperation", trip.getName());
        Intent intent = new Intent(getActivity(), TripActivity.class);
        intent.putExtra("id",trip.getId());
        Log.e("id trip frag",""+trip.getId());
        startActivity(intent);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        /*private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }*/
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTripList(Integer.toString(id));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
          //  pDialog.dismiss();
            try {
                // Log.e("ExploreFragment",json.getString("message"));
                // Si la récupération des voyages a été un succès on affecte les voyages dans un ArrayList
                if(json == null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("Les voyages n'ont pas pu être récupérer");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    setListAdapter(null);
                }else if(json.getString("success").equals("1")) {

                    // Recuperation des voyages sous la forme d'un JSONArray
                    JSONArray tripList = json.getJSONArray("trip");

                    tripArrayList = new ArrayList<>(tripList.length());

                    for (int i = 0; i < tripList.length(); i++) {
                        tripArrayList.add(new Trip(
                                tripList.getJSONObject(i).getInt("trip_id"),
                                tripList.getJSONObject(i).getString("trip_name"),
                                tripList.getJSONObject(i).getString("trip_country"),
                                tripList.getJSONObject(i).getString("trip_cover"),
                                tripList.getJSONObject(i).getInt("comment_count"),
                                tripList.getJSONObject(i).getInt("photo_count")
                        ));
                    }

                    if(tripArrayList != null) {
                        setListAdapter(new TripListAdapter(getActivity(), tripArrayList, false));
                    }else {
                        setListAdapter(null);
                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();

            }

        }
    }

}
