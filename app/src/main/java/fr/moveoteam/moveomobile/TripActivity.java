package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.fragment.TripHomeFragment;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 10/05/15.
 */
public class TripActivity extends Activity {

    int id;

    RelativeLayout layout;

    Trip trip;
    ArrayList<Place> placeArrayList;
    ArrayList<Comment> commentArrayList;

    AlertDialog.Builder alertDialog;
    private TextView tripName;
    private TextView tripCountry;
    private TextView tripAuthor;
    private TextView tripDate;
    private TextView tripDescription;
    private  LinearLayout tripHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);
        initialize();
        id = getIntent().getExtras().getInt("id",0);
        new ExecuteThread().execute();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.add(R.id.trip_content,new TripHomeFragment());
        ft.commit();

    }

    private void initialize() {

        tripName = (TextView) findViewById(R.id.trip_name);
        tripCountry = (TextView) findViewById(R.id.trip_country);
        tripAuthor = (TextView) findViewById(R.id.trip_author);
        tripDate = (TextView) findViewById(R.id.trip_date);
        tripDescription = (TextView) findViewById(R.id.trip_description);
        tripHome = (LinearLayout) findViewById(R.id.trip_home);
    }


    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TripActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTrip(Integer.toString(id));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json.getString("error").equals("0")) {

                    Trip trip = new Trip();
                    trip.setId(Integer.parseInt(json.getJSONObject("trip").getString("trip_id")));
                    trip.setName(json.getJSONObject("trip").getString("trip_name"));
                    trip.setCountry(json.getJSONObject("trip").getString("trip_country"));
                    trip.setDescription(json.getJSONObject("trip").getString("trip_description"));
                    trip.setDate(json.getJSONObject("trip").getString("trip_created_at"));
                    trip.setAuthor_last_name(json.getJSONObject("trip").getString("user_last_name"));
                    trip.setAuthor_first_name(json.getJSONObject("trip").getString("user_first_name"));
                    trip.setUserId(Integer.parseInt(json.getJSONObject("trip").getString("user_id")));

                    if((json.getString("success").equals("1")) || (json.getString("success").equals("2"))){
                        JSONArray placeList = json.getJSONArray("place");
                        placeArrayList = new ArrayList<>(placeList.length());
                        for (int i = 0; i < placeList.length(); i++) {
                            placeArrayList.add(new Place(
                                    placeList.getJSONObject(i).getInt("place_id"),
                                    placeList.getJSONObject(i).getString("place_name"),
                                    placeList.getJSONObject(i).getString("place_address"),
                                    placeList.getJSONObject(i).getString("place_description"),
                                    placeList.getJSONObject(i).getInt("category_id")
                            ));
                            Log.e("Place", placeArrayList.get(i).toString());
                        }
                    }

                    if((json.getString("success").equals("1")) || (json.getString("success").equals("3"))){
                        JSONArray commentList = json.getJSONArray("comment");
                        commentArrayList = new ArrayList<>(commentList.length());
                        for (int i = 0; i < commentList.length(); i++) {
                            commentArrayList.add(new Comment(
                                    commentList.getJSONObject(i).getInt("comment_id"),
                                    commentList.getJSONObject(i).getString("comment_message"),
                                    commentList.getJSONObject(i).getString("comment_added_datetime"),
                                    commentList.getJSONObject(i).getInt("user_id")
                            ));
                            Log.e("comment", commentArrayList.get(i).toString());
                        }
                    }

                    /*tripName.setText(trip.getName());
                    tripCountry.setText(trip.getCountry());
                    tripDescription.setText(trip.getDescription());
                    tripAuthor.setText(Html.fromHtml("<font color=#000>par</font> <b>"+trip.getAuthor_last_name()+" "+trip.getAuthor_first_name()+" </b>"));
                    tripDate.setText(tripDate.getText()+" "+trip.getDate());
                    tripHome.setVisibility(View.VISIBLE);*/

                } else {

                    alertDialog = new AlertDialog.Builder(
                            TripActivity.this);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Une erreur s'est produite lors de la récupération du voyage");
                    alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
