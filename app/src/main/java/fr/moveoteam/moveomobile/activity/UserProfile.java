package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.fragment.TripListFragment;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Amélie on 27/04/2015.
 */
public class UserProfile extends Activity {

    private ImageView useravatar;
    private TextView usernameprofile;
    private TextView livein;
    private TextView tripsnumber;
    private TextView placesnumber;
    private ImageView sendmail;
    private ImageView addfriend;
    private ImageView tripsuser;
    private TextView tripsusertitle;
    private LinearLayout userinfos;
    private RelativeLayout userprofile;

    int id;
    boolean isFriend;

    FriendDAO friendDAO;

    ArrayList<Trip> tripArrayList;

    TripListFragment tripListFragment;

    Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        id = getIntent().getExtras().getInt("id",0);
        isFriend = getIntent().getExtras().getInt("friend", 0) == 1;
        initialize();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        tripListFragment = new TripListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("otherUserId",id);
        tripListFragment.setArguments(bundle);
        android.app.FragmentManager fm= getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.trip_list_content,tripListFragment);
        ft.commit();

        if (isFriend) {
            friendDAO = new FriendDAO(UserProfile.this);
            friendDAO.open();
        }



        if (isFriend) {
            friend = friendDAO.getFriend(id);
            Log.i("info friend", "" + id);
            if (!friend.getAvatarBase64().equals(""))
                useravatar.setImageBitmap(Function.decodeBase64(friend.getAvatarBase64()));

            usernameprofile.setText(friend.getFirstName() + " " + friend.getLastName().toUpperCase());
           // addfriend.setImageDrawable(getResources().getDrawable(R.drawable.refuse_friend));

            // Affichage du lieu de l'utilisateur
            if (friend.getCity() != null && friend.getCountry() != null)
                livein.setText(livein.getText() + " " + friend.getCity() + " en " + friend.getCountry());
            else if (friend.getCountry() == null)
                livein.setText(livein.getText() + " " + friend.getCity());
            else
                livein.setText("lieu non renseigné");
        }

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,SendMessageActivity.class);

                if (isFriend) {
                    intent.putExtra("friendId",""+id);
                    intent.putExtra("name",friend.getFirstName()+" "+friend.getLastName());
                }
                startActivity(intent);
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         finish();
        return true;
    }

    private void initialize() {

        useravatar = (ImageView) findViewById(R.id.user_avatar);
        usernameprofile = (TextView) findViewById(R.id.username_profile);
        livein = (TextView) findViewById(R.id.live_in);
        tripsnumber = (TextView) findViewById(R.id.trips_number);
        placesnumber = (TextView) findViewById(R.id.places_number);
        sendmail = (ImageView) findViewById(R.id.send_mail);
        addfriend = (ImageView) findViewById(R.id.add_friend);
        tripsuser = (ImageView) findViewById(R.id.trips_user);
        tripsusertitle = (TextView) findViewById(R.id.trips_user_title);
        userinfos = (LinearLayout) findViewById(R.id.user_infos);
        userprofile = (RelativeLayout) findViewById(R.id.user_profile);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfile.this);
            pDialog.setMessage("Récupération des voyages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTripList(Integer.toString(id));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Log.e("ExploreFragment",json.getString("message"));
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
                                tripList.getJSONObject(i).getString("trip_cover"),
                                tripList.getJSONObject(i).getString("user_last_name"),
                                tripList.getJSONObject(i).getString("user_first_name"),
                                tripList.getJSONObject(i).getInt("comment_count"),
                                tripList.getJSONObject(i).getInt("photo_count")
                        ));
                    }

                    if(tripArrayList != null) {
                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        //      R.layout.element_menu,R.id.title,values);

                        Log.e("afficher list", tripArrayList.get(1).getName());
                        // setListAdapter(new TripListAdapter(getActivity(), tripArrayList, true));
                        Log.e("Message ", "" + tripArrayList.get(0).getName() + "" + tripArrayList.get(0).getName());
                        Log.e("Date ", "" + tripList.getJSONObject(0).getString("trip_created_at") + " java : " + tripArrayList.get(0).getDate());
                    }
                }

            } catch (ParseException | JSONException e1) {
                e1.printStackTrace();

            }

        }
    }



}
