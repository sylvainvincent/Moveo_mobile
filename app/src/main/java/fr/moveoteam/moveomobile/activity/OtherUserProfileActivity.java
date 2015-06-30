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
import android.widget.Toast;

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
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 28/06/15.
 */
public class OtherUserProfileActivity extends Activity {
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
    int otherUserId;
    int userId;
    boolean isFriend;
    int tripCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);

        otherUserId = getIntent().getExtras().getInt("id",0);
        UserDAO userDAO = new UserDAO(OtherUserProfileActivity.this);
        userDAO.open();
        userId = userDAO.getUserDetails().getId();
        initialize();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        new ExecuteThread().execute();

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherUserProfileActivity.this,SendMessageActivity.class);
                intent.putExtra("friendId",""+otherUserId);
                intent.putExtra("name", friend.getFirstName() + " " + friend.getLastName());
                startActivity(intent);
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExecuteSendInvitationThread().execute();
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
        //tripsuser = (ImageView) findViewById(R.id.trips_user);
        tripsusertitle = (TextView) findViewById(R.id.trips_user_title);
        userinfos = (LinearLayout) findViewById(R.id.user_infos);
        //userprofile = (RelativeLayout) findViewById(R.id.user_profile);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OtherUserProfileActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONUser jsonUser = new JSONUser();
            return jsonUser.getOtherUser(Integer.toString(userId), Integer.toString(otherUserId));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OtherUserProfileActivity.this);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
                    builder.setMessage("Connexion perdu");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                } else if (json.getString("success").equals("1")) {

                    tripListFragment = new TripListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("otherUserId",otherUserId);
                    tripListFragment.setArguments(bundle);
                    android.app.FragmentManager fm= getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.trip_list_content,tripListFragment);
                    ft.commit();

                    JSONObject jsonFriend = json.getJSONObject("otherUser");
                    friend = new Friend();

                    friend.setFirstName(jsonFriend.getString("first_name"));
                    friend.setLastName(jsonFriend.getString("last_name"));
                    friend.setBirthday(jsonFriend.getString("birthday"));
                    friend.setCountry(jsonFriend.getString("country"));
                    friend.setCity(jsonFriend.getString("city"));
                    friend.setAccessId(Integer.parseInt(jsonFriend.getString("access_id")));
                    friend.setAvatarBase64(jsonFriend.getString("link_avatar"));
                    friend.setFriend(false);
                    tripCount = Integer.parseInt(jsonFriend.getString("trip_count"));

                    if (!friend.getAvatarBase64().equals(""))
                        useravatar.setImageBitmap(Function.decodeBase64(friend.getAvatarBase64()));

                    usernameprofile.setText(friend.getFirstName() + " " + friend.getLastName().toUpperCase());
                    // addfriend.setImageDrawable(getResources().getDrawable(R.drawable.refuse_friend));

                    // Affichage du lieu de l'utilisateur
                    if (friend.getAccessId() == 3) {
                        if (!friend.getCity().equals("null") && !friend.getCountry().equals("null"))
                            livein.setText(livein.getText() + " " + friend.getCity() + " en " + friend.getCountry());
                        else if (friend.getCountry().equals("null") && !friend.getCity().equals("null"))
                            livein.setText(livein.getText() + " " + friend.getCity());
                        else if (friend.getCity().equals("null") && !friend.getCountry().equals("null"))
                            livein.setText(livein.getText() + " " + friend.getCountry());
                    } else {
                        livein.setVisibility(View.INVISIBLE);
                    }


                    if (tripCount == 1)
                        tripsnumber.setText(tripCount + " voyage");
                    else tripsnumber.setText(tripCount + " voyages");

                    friendDAO = new FriendDAO(OtherUserProfileActivity.this);
                    friendDAO.open();

                    // VERIFICATION DU STATUT DE LA PERSONNE (Amis, demande d'amis ou simple personne)
                       Log.e("invitation",json.getString("invitation"));
                    if(json.getString("invitation").equals("1")){
                        addfriend.setImageDrawable(getResources().getDrawable(R.drawable.trips_user));
                        addfriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OtherUserProfileActivity.this,3);
                                alertDialog.setMessage("Demande en attente");
                                alertDialog.setPositiveButton("OK", null);
                                alertDialog.show();
                            }
                        });
                    }else if(friendDAO.getFriendList() != null) {
                       boolean bool = false;
                       int i = 0;
                        Log.e("Test id :","id1 :"+friendDAO.getFriendList().get(i).getId()+" id2 :"+otherUserId);
                       while(i < friendDAO.getFriendList().size() || !bool){
                           Log.e("Test id :","id1 :"+friendDAO.getFriendList().get(i).getId()+" id2 :"+otherUserId);
                           if(friendDAO.getFriendList().get(i).getId() == otherUserId){
                                addfriend.setImageDrawable(getResources().getDrawable(R.drawable.delete_app));
                                addfriend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(OtherUserProfileActivity.this);
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
                                bool = true;
                            }
                           i++;
                       }

                    }else{
                        addfriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new ExecuteSendInvitationThread().execute();
                            }
                        });
                    }

                    friendDAO.close();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private class ExecuteSendInvitationThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OtherUserProfileActivity.this);
            pDialog.setMessage("Envoi de l'invitation...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONFriend jsonFriend = new JSONFriend();
            return jsonFriend.sendInvitation(Integer.toString(userId),Integer.toString(otherUserId));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OtherUserProfileActivity.this);
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
                } else if (json.getString("success").equals("1")) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(OtherUserProfileActivity.this);
                    builder.setMessage("L'invitation a bien été envoyé");
                    builder.setPositiveButton("ok", null);
                    builder.show();

                    addfriend.setImageDrawable(getResources().getDrawable(R.drawable.trips_user));
                    addfriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OtherUserProfileActivity.this,2);
                            alertDialog.setMessage("Demande en attente");
                            alertDialog.setPositiveButton("OK", null);
                            alertDialog.show();
                        }
                    });
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OtherUserProfileActivity.this,2);
                    alertDialog.setMessage("Un erreur s'est produite lors de l'envoi de l'invitation");
                    alertDialog.setPositiveButton("OK", null);
                    alertDialog.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private class ExecuteDeleteFriendThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OtherUserProfileActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONFriend jsonFriend = new JSONFriend();
            return jsonFriend.removeFriend(Integer.toString(userId),Integer.toString(otherUserId));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            super.onPostExecute(json);

            if (json == null) {
                AlertDialog.Builder alert = new AlertDialog.Builder(OtherUserProfileActivity.this);
                alert.setMessage("Connexion perdu");
                alert.setPositiveButton("OK",null);
                alert.show();
            } else try {
                if (json.getString("success").equals("1")) {
                    FriendDAO friendDAO = new FriendDAO(OtherUserProfileActivity.this);
                    friendDAO.open();
                    friendDAO.removeFriend(otherUserId);
                    friendDAO.close();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(OtherUserProfileActivity.this);
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
