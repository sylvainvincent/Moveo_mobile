package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.fragment.TripListFragment;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONFriend;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Amélie on 27/04/2015.
 */
public class FriendProfileActivity extends Activity {

	// Elements de vue
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
	
	// Manipulation de la table friend (Base de données)
    FriendDAO friendDAO;
	
    ArrayList<Trip> tripArrayList;

	// FRAGMENT
    TripListFragment tripListFragment;

	// CLASSE METIER 
    Friend friend;
	
	// AUTRES
    int friendId;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        friendId = getIntent().getExtras().getInt("id",0);

        UserDAO userDAO = new UserDAO(FriendProfileActivity.this);
        userDAO.open();
        id = userDAO.getUserDetails().getId();
        userDAO.close();
        initialize();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        tripListFragment = new TripListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("otherUserId",friendId);
        tripListFragment.setArguments(bundle);
        android.app.FragmentManager fm= getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.trip_list_content,tripListFragment);
        ft.commit();


        friendDAO = new FriendDAO(FriendProfileActivity.this);
        friendDAO.open();

        friend = friendDAO.getFriend(friendId);
        friendDAO.close();
        Log.i("info friend", "" + friendId);
        if (!friend.getAvatarBase64().equals(""))
            useravatar.setImageBitmap(Function.decodeBase64(friend.getAvatarBase64()));

        usernameprofile.setText(friend.getFirstName() + " " + friend.getLastName().toUpperCase());
       // addfriend.setImageDrawable(getResources().getDrawable(R.drawable.refuse_friend));

        // Affichage du lieu de l'utilisateur
        if (!friend.getCity().equals("null") && !friend.getCountry().equals("null"))
            livein.setText(livein.getText() + " " + friend.getCity() + " en " + friend.getCountry());
        else if (friend.getCountry().equals("null") && !friend.getCity().equals("null"))
            livein.setText(livein.getText() + " " + friend.getCity());
        else if (friend.getCity().equals("null") && !friend.getCountry().equals("null"))
            livein.setText(livein.getText() + " " + friend.getCountry());

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendProfileActivity.this,SendMessageActivity.class);
                intent.putExtra("friendId",""+friendId);
                intent.putExtra("name",friend.getFirstName()+" "+friend.getLastName());
                startActivity(intent);
            }
        });

        addfriend.setImageDrawable(getResources().getDrawable(R.drawable.delete_app));
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(FriendProfileActivity.this);
                alert.setMessage("Êtes vous sûr de retirer cette personne de vos amis ?");
                alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ExecuteDeleteFriendThread().execute();
                    }
                });
                alert.setNegativeButton("non", null);
                alert.show();

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
        //placesnumber = (TextView) findViewById(R.id.places_number);
        sendmail = (ImageView) findViewById(R.id.send_mail);
        addfriend = (ImageView) findViewById(R.id.add_friend);

        tripsusertitle = (TextView) findViewById(R.id.trips_user_title);
        userinfos = (LinearLayout) findViewById(R.id.user_infos);
        //userprofile = (RelativeLayout) findViewById(R.id.user_profile);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendProfileActivity.this);
            pDialog.setMessage("Récupération des voyages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTripList(Integer.toString(friendId));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(FriendProfileActivity.this);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setMessage("Connexion perdu");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                     }else if(json.getString("success").equals("1")) {
                     // Si la récupération des voyages a été un succès on affecte les voyages dans un ArrayList

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

                        if(tripArrayList != null){
                            if(tripArrayList.size() == 1)tripsnumber.setText(tripArrayList.size()+" voyage");
                            else tripsnumber.setText(tripArrayList.size()+" voyages");
                        }

                    }
                }

            } catch (ParseException | JSONException e1) {
                e1.printStackTrace();

            }

        }
    }

    private class ExecuteDeleteFriendThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendProfileActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONFriend jsonFriend = new JSONFriend();
            return jsonFriend.removeFriend(Integer.toString(id),Integer.toString(friendId));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            super.onPostExecute(json);

            if (json == null) {
                AlertDialog.Builder alert = new AlertDialog.Builder(FriendProfileActivity.this);
                alert.setMessage("Connexion perdu");
                alert.setPositiveButton("OK",null);
                alert.show();
            } else try {
                if (json.getString("success").equals("1")) {
                    FriendDAO friendDAO = new FriendDAO(FriendProfileActivity.this);
                    friendDAO.open();
                    friendDAO.removeFriend(friendId);
                    friendDAO.close();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(FriendProfileActivity.this);
                    alert.setMessage("Une erreur s'est produite lors de la suppression de l'ami");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
