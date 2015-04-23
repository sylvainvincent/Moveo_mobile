package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.User;
import fr.moveoteam.moveomobile.webservice.JSONTrip;
import fr.moveoteam.moveomobile.webservice.JSONUser;


/**
 * Created by alexMac on 07/04/15.
 */
public class ExploreActivity extends Activity {

    private TextView exploreTitle;
    private UserDAO userDAO;
    private TripDAO tripDAO;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        exploreTitle = (TextView) findViewById(R.id.explore_title);
        listView = (ListView) findViewById(R.id.listViewExploreTrip);
        UserDAO userDAO = new UserDAO(ExploreActivity.this);
        userDAO.open();
        exploreTitle.setText("Test Bonjour Mr "+ userDAO.getUserDetails().getFirstName());

        new ExecuteThread().execute();

        ArrayList<Trip> tripStory = getListData();


    }

    //On charge le menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Méthode qui met les données dans une arrayList
    private ArrayList<Trip> getListData() {

        ArrayList<Trip> resultats = new ArrayList<>();
        // Instancie un nouvel item de type Trip
        // ==> Il a 3 valeurs : Nom, Logo et Site
        Trip newsData = new Trip();
        newsData.setName("LAC DE COME");
        newsData.setCountry("ITALIE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("RIO DE JANEIRO");
        newsData.setCountry("BRESIL");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("BERNE");
        newsData.setCountry("SUISSE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("NEW YORK");
        newsData.setCountry("USA");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("TOKYO");
        newsData.setCountry("JAPON");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("BANGKOK");
        newsData.setCountry("THAILANDE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);
        return resultats;
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ExploreActivity.this);
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

                JSONArray tripList = json.getJSONArray("trip");
                ArrayList<Trip> tripArrayList = new ArrayList<>(10);
                if(json.getString("success").equals("1")) {
                        UserDAO userDAO = new UserDAO(ExploreActivity.this);

                        for(int i=0;i<2;i++) {
                            tripArrayList.add(new Trip(
                                    tripList.getJSONObject(i).getInt("trip_id"),
                                    tripList.getJSONObject(i).getString("trip_name"),
                                    tripList.getJSONObject(i).getString("trip_country"),
                                    tripList.getJSONObject(i).getString("trip_description"),
                                 // tripList.getJSONObject(i).getString("trip_created_at"),
                                    tripList.getJSONObject(i).getString("user_last_name"),
                                    tripList.getJSONObject(i).getString("user_first_name"),
                                    tripList.getJSONObject(i).getInt("comment_count"),
                                    tripList.getJSONObject(i).getInt("photo_count")
                            ));
                        }
                        listView.setAdapter(new CustomListAdapter(ExploreActivity.this, tripArrayList));
                        Log.e("Message ",""+tripArrayList.get(0).getName()+""+tripArrayList.get(0).getName());

                } else {
                    Toast.makeText(ExploreActivity.this, "La connexion a échoué",
                            Toast.LENGTH_LONG).show();
                }

                // Storing  JSON item in a Variable
                // String msg = (String) c.getString(msg);
                //Set JSON Data in TextView

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
