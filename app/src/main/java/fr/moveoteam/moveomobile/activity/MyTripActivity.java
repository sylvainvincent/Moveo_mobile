package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 17/05/15.
 */
public class MyTripActivity extends Activity {

    TripDAO tripDAO;
    int id;

    AlertDialog.Builder alertDialog;

    ArrayList<Place> placeArrayList;
    ArrayList<Comment> commentArrayList;
    private TextView modifycover;
    private ImageView pictures;
    private TextView mytripcitytitle;
    private TextView mytriptitle;
    private ScrollView mytrip;
    private ListView listView;
    private TextView tripdescription;
    private TextView addtripdate;
    private TextView modifydescription;
    private ImageView deleteapp;
    private TextView deletetrip;
    private ImageView shopping;
    private ImageView hobbies;
    private ImageView fooding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trip);

        id = getIntent().getExtras().getInt("id",0);
        Log.e("id trip",""+id);

        tripDAO = new TripDAO(MyTripActivity.this);

        //Trip = tripDAO.getTripList().get(i)

        new ExecuteThread().execute();
        initialize();
    }

    private void initialize() {

        modifycover = (TextView) findViewById(R.id.modify_cover);
        pictures = (ImageView) findViewById(R.id.pictures);
        mytripcitytitle = (TextView) findViewById(R.id.my_trip_city_title);
        mytrip = (ScrollView) findViewById(R.id.my_trip);
        listView = (ListView) findViewById(R.id.listView);
        tripdescription = (TextView) findViewById(R.id.trip_description);
        addtripdate = (TextView) findViewById(R.id.add_trip_date);
        mytriptitle = (TextView) findViewById(R.id.my_trip_title);
        modifydescription = (TextView) findViewById(R.id.modify_description);
        deleteapp = (ImageView) findViewById(R.id.delete_app);
        deletetrip = (TextView) findViewById(R.id.delete_trip);

    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyTripActivity.this);
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

                    tripDAO = new TripDAO(MyTripActivity.this);
                    tripDAO.open();

                    mytriptitle.setText(json.getJSONObject("trip").getString("trip_name"));
                    mytripcitytitle.setText(json.getJSONObject("trip").getString("trip_country"));
                    tripdescription.setText(json.getJSONObject("trip").getString("trip_description"));

                    addtripdate.setText(addtripdate.getText() + " " + json.getJSONObject("trip").getString("trip_created_at"));


                    if((json.getString("success").equals("1")) || (json.getString("success").equals("2"))){
                        JSONArray placeList = json.getJSONArray("place");
                        placeArrayList = new ArrayList<>(placeList.length());
                        for (int i = 0; i < placeList.length(); i++) {
                            placeArrayList.add(new Place(
                                    placeList.getJSONObject(i).getInt("place_id"),
                                    placeList.getJSONObject(i).getString("place_name"),
                                    placeList.getJSONObject(i).getString("place_address"),
                                    placeList.getJSONObject(i).getString("place_description"),
                                    placeList.getJSONObject(i).getInt("category_id"),
                                    placeList.getJSONObject(i).getInt("trip_id")
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
                                    commentList.getJSONObject(i).getInt("user_id"),
                                    commentList.getJSONObject(i).getString("user_last_name"),
                                    commentList.getJSONObject(i).getString("user_first_name"),
                                    commentList.getJSONObject(i).getString("user_link_avatar")

                            ));
                            Log.e("comment", commentArrayList.get(i).toString());
                        }
                    }
                    mytrip.setAlpha(1);
                    //pictures.setImageBitmap(tripDAO.getTripList().);


                    /*tripName.setText(trip.getName());
                    tripCountry.setText(trip.getCountry());
                    tripDescription.setText(trip.getDescription());
                    tripAuthor.setText(Html.fromHtml("<font color=#000>par</font> <b>"+trip.getAuthor_last_name()+" "+trip.getAuthor_first_name()+" </b>"));
                    tripDate.setText(tripDate.getText()+" "+trip.getDate());
                    tripHome.setVisibility(View.VISIBLE);*/

                } else {

                    alertDialog = new AlertDialog.Builder(
                            MyTripActivity.this);
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
