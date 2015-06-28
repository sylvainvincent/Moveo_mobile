package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.TripActivity;
import fr.moveoteam.moveomobile.adapter.TripListAdapter;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONSearch;

/**
 * Created by Sylvain on 28/06/15.
 */
public class SearchTripListFragments extends ListFragment {

    String query;
    String userId;

    ArrayList<Trip> tripArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        query = getArguments().getString("query");
        Log.e("Test Search",query);
        UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        userId = Integer.toString(userDAO.getUserDetails().getId());
        userDAO.close();
        new ExecuteThread().execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Trip trip = tripArrayList.get(position);
        Log.e("Recuperation",trip.getName());
        Intent intent = new Intent(getActivity(), TripActivity.class);
        intent.putExtra("id",trip.getId());
        startActivity(intent);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Recherche en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONSearch jsonSearch = new JSONSearch();
            return jsonSearch.searchTrip(userId, query);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json == null) {
                    Log.e("test json", "null");
                   Toast.makeText(getActivity(), "Une erreur est survenue lors de la recherche", Toast.LENGTH_SHORT).show();
                } else if (json.getString("success").equals("1")) {
                    JSONArray tripList = json.getJSONArray("trip");
                    tripArrayList = new ArrayList<>(tripList.length());

                    for (int i = 0; i < tripList.length(); i++) {
                        tripArrayList.add(new Trip(
                                tripList.getJSONObject(i).getInt("trip_id"),
                                tripList.getJSONObject(i).getString("trip_name"),
                                tripList.getJSONObject(i).getString("trip_country"),
                                tripList.getJSONObject(i).getString("trip_description"),
                                tripList.getJSONObject(i).getString("trip_created_at"),
                                tripList.getJSONObject(i).getString("trip_cover"),
                                tripList.getJSONObject(i).getString("user_last_name"),
                                tripList.getJSONObject(i).getString("user_first_name"),
                                tripList.getJSONObject(i).getInt("comment_count"),
                                tripList.getJSONObject(i).getInt("photo_count")
                        ));

                        setListAdapter(new TripListAdapter(getActivity(), tripArrayList, true));

                    }

                }else{
                    setListAdapter(null);
                }

            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
