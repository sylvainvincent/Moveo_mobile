package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.HomeActivity;
import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 29/04/15.
 */
public class ExploreFragment extends ListFragment {

    private AdapterView.OnItemSelectedListener listener;
    ArrayList<Trip> tripArrayList;

    public  ExploreFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ExecuteThread().execute();
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"};


        //setListAdapter(new CustomListAdapter(getActivity(),tripArrayList));
    }

    /*
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // L'inflater permet de deserializer un fichier XML
        View view = inflater.inflate(R.layout.explore, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listViewExploreTrip);
        return view;
    }*/

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

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Récupération des voyages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getExploreTrips();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                Log.e("ExploreFragment",json.getString("message"));
                // Si la récupération des voyages a été un succès on affecte les voyages dans un ArrayList
                if(json.getString("success").equals("1")) {
                    // Recuperation des voyages sous la forme d'un JSONArray
                    JSONArray tripList = json.getJSONArray("trip");

                    tripArrayList = new ArrayList<>(tripList.length());

                    for (int i = 0; i < tripList.length(); i++) {
                        tripArrayList.add(new Trip(
                                tripList.getJSONObject(i).getInt("trip_id"),
                                tripList.getJSONObject(i).getString("trip_name"),
                                tripList.getJSONObject(i).getString("trip_country"),
                                tripList.getJSONObject(i).getString("trip_description"),
                                tripList.getJSONObject(i).getString("trip_created_at"),
                                tripList.getJSONObject(i).getString("user_last_name"),
                                tripList.getJSONObject(i).getString("user_first_name"),
                                tripList.getJSONObject(i).getInt("comment_count"),
                                tripList.getJSONObject(i).getInt("photo_count")
                        ));
                    }
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                      //      R.layout.element_menu,R.id.title,values);
                    Log.e("afficher list",tripArrayList.get(1).getName());
                    setListAdapter(new CustomListAdapter(getActivity(),tripArrayList));
                    Log.e("Message ", "" + tripArrayList.get(0).getName() + "" + tripArrayList.get(0).getName());
                    Log.e("Date ", "" + tripList.getJSONObject(0).getString("trip_created_at") + " java : " + tripArrayList.get(0).getDateInsert());
                }

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

}