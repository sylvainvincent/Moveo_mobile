package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.fragment.TripListFragment;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONFriend;

/**
 * Created by Amélie on 27/04/2015.
 */
public class FriendProfileActivity extends Activity {

	// Elements de vue
    private ImageView useravatar;
    private TextView usernameprofile;
    private TextView livein;
    private TextView tripsnumber;
    private TextView birthday;
    private ImageView sendmail;
    private ImageView addfriend;
    private ImageView tripsuser;
    private TextView tripsusertitle;
    private LinearLayout userinfos;
    private LinearLayout page;
    private RelativeLayout userprofile;
	
	// Manipulation de la table friend (Base de données)
    private FriendDAO friendDAO;

	// FRAGMENT
    private TripListFragment tripListFragment;

	// CLASSE METIER 
    private Friend friend;
	
	// AUTRES
    private int friendId;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        initialize();
        friendId = getIntent().getExtras().getInt("id",0);

        UserDAO userDAO = new UserDAO(FriendProfileActivity.this);
        userDAO.open();
        id = userDAO.getUserDetails().getId();
        userDAO.close();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        tripListFragment = new TripListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("otherUserId",friendId);
        tripListFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_list_content,tripListFragment);
        ft.commit();

        friendDAO = new FriendDAO(FriendProfileActivity.this);
        friendDAO.open();

        friend = friendDAO.getFriend(friendId);
        friendDAO.close();
        Log.i("info friend", "" + friendId);
        if (!friend.getAvatarBase64().equals("")){
            // useravatar.setImageBitmap(Function.decodeBase64(friend.getAvatarBase64()));
            new DownloadImage().execute(friend.getAvatarBase64());
        }

        if(!friend.getBirthday().equals(""))birthday.setText("née le "+Function.dateSqlToDateJava(friend.getBirthday()));

        usernameprofile.setText(friend.getFirstName() + " " + friend.getLastName().toUpperCase());
        // addfriend.setImageDrawable(getResources().getDrawable(R.drawable.refuse_friend));

        // Affichage du lieu de l'utilisateur
        if ((!friend.getCity().equals("null") && !friend.getCity().equals("")) && (!friend.getCountry().equals("null") && !friend.getCountry().equals("")))
            livein.setText(livein.getText() + " " + friend.getCity() + " en " + friend.getCountry());
        else if ((friend.getCountry().equals("") || friend.getCountry().equals("null"))  && !friend.getCity().equals("") && !friend.getCity().equals("null"))
            livein.setText(livein.getText() + " " + friend.getCity());
        else if ((friend.getCity().equals("") || friend.getCity().equals("null")) && !friend.getCountry().equals("") && !friend.getCountry().equals("null"))
            livein.setText("Habite en " + friend.getCountry());
        else{
            livein.setText("lieu non renseigné");
        }

        tripsnumber.setVisibility(View.GONE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initialize() {

        useravatar = (ImageView) findViewById(R.id.user_avatar);
        usernameprofile = (TextView) findViewById(R.id.username_profile);
        livein = (TextView) findViewById(R.id.live_in);
        tripsnumber = (TextView) findViewById(R.id.trips_number);
        sendmail = (ImageView) findViewById(R.id.send_mail);
        addfriend = (ImageView) findViewById(R.id.add_friend);
        page = (LinearLayout) findViewById(R.id.user_profile);
        tripsusertitle = (TextView) findViewById(R.id.trips_user_title);
        userinfos = (LinearLayout) findViewById(R.id.user_infos);
        birthday = (TextView) findViewById(R.id.birthday_text);
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
                    finish();

                } else {
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

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        String url;
        URL urlImage;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... args) {
            url = args[0];
            try {
                urlImage = new URL("http://moveo.besaba.com/"+url);
                connection = (HttpURLConnection) urlImage.openConnection();
                if (connection != null) {
                    inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                useravatar.setImageResource(R.drawable.default_journey);
            } else {
                useravatar.setImageBitmap(result);
            }
        }
    }

}
