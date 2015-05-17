package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.DataBaseHandler;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Dialog;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.model.User;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 16/04/15.
 * Le Dashboard est la classe principal, elle va re-diriger l'utilisateur selon son statut de connection
 */
public class DashboardActivity extends Activity {

    private Menu m = null;
	UserDAO userDAO;
    Boolean internet = false;
    Toast toast;
    RelativeLayout layout;
    AlertDialog.Builder alertDialog;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Création de la classe UserDAO pour manipuler la table user de ma BDD
        userDAO = new UserDAO(DashboardActivity.this);
        //On ouvre la base de données pour écrire dedans
        userDAO.open();

        // VERIFIER LE STATUT DU LOGIN DANS LA BASE DE DONNÉES
        if (userDAO.getRowCount()) {
            /** ---------------- TEST
             TripDAO tripDAO = new TripDAO(DashboardActivity.this);
             tripDAO.open();
             Log.e("Test du trip 1:",tripDAO.getTripList().get(0).getName());
             Log.e("Test du trip 1:",tripDAO.getTripList().get(0).getCountry());
             Log.e("Test du trip 2:",tripDAO.getTripList().get(1).getName());
             Log.e("Test du trip 2:",tripDAO.getTripList().get(1).getCountry());
             */
            // VERIFICATION DE LA CONNEXION INTERNET
            if(!Function.beConnectedToInternet(DashboardActivity.this)){
                buildDialog(DashboardActivity.this).show();
            }else {
                new ExecuteThread().execute();
            }
            userDAO.close();

        } else {// Si l'utilisateur n'a pas de session d'ouverte il est renvoyé sur la page Login
            DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
            db.resetTables();
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Fermer le dashboard
            finish();
        }

    }

    // Boite de dialogue d'alerte activé si internet n'est pas activé
    public AlertDialog.Builder buildDialog(Context context) {
        internet = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                System.exit(0);
            }
        });
        builder.setMessage("un accès Internet est requis, Vérifier votre connexion Internet puis réessayez");
        builder.setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Function.beConnectedToInternet(DashboardActivity.this)) {
                    dialog.dismiss();
                    buildDialog(DashboardActivity.this).show();
                }else{
                    Intent explore = new Intent(getApplicationContext(), HomeActivity.class);
                    explore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(explore);
                    finish();
                }
            }
        });

        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.m = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashboardActivity.this);
            pDialog.setMessage("Synchronisation en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONUser jsonUser = new JSONUser();
            userDAO = new UserDAO(DashboardActivity.this);
            userDAO.open();
            password = userDAO.getUserDetails().getPassword();
            Log.d("test",""+userDAO.getUserDetails().getEmail()+" "+password);
            return jsonUser.loginUser(userDAO.getUserDetails().getEmail(), password);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json.getString("error").equals("0")) {
                    DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
                    db.resetTables();
                    // Création de l'objet User
                    User user = new User();
                    user.setId(Integer.parseInt(json.getJSONObject("user").getString("user_id")));
                    user.setLastName(json.getJSONObject("user").getString("user_last_name"));
                    user.setFirstName(json.getJSONObject("user").getString("user_first_name"));
                    user.setBirthday(json.getJSONObject("user").getString("user_birthday"));
                    user.setAvatar(json.getJSONObject("user").getString("avatar"));
                    user.setEmail(json.getJSONObject("user").getString("user_email"));
                    user.setPassword(password);
                    user.setCountry(json.getJSONObject("user").getString("user_country"));
                    user.setCity(json.getJSONObject("user").getString("user_city"));
                    Log.e("Test","test");
                    Log.e("Avatar",user.getAvatar());

                    // Création de l'objet DAO(utilisateur) pour ajouter un utilisateur
                    UserDAO userDAO = new UserDAO(DashboardActivity.this);
                    userDAO.open();
                    userDAO.addUser(user);

                    if(!json.getString("friend").equals("0")) {
                        JSONArray friendList = json.getJSONArray("friend");
                        ArrayList<Friend> friendArrayList = new ArrayList<>(friendList.length());
                        for (int i = 0; i < friendList.length(); i++) {
                            friendArrayList.add(new Friend(
                                    friendList.getJSONObject(i).getInt("friend_id"),
                                    friendList.getJSONObject(i).getString("friend_last_name"),
                                    friendList.getJSONObject(i).getString("friend_first_name"),
                                    friendList.getJSONObject(i).getInt("is_accepted") != 0
                            ));
                        }
                        Log.e("friend", "passage réussi");
                        FriendDAO friendDAO = new FriendDAO(DashboardActivity.this);
                        friendDAO.open();
                        friendDAO.addFriendList(friendArrayList);
                    }


                    if (!json.getString("trip").equals("0")) {
                        JSONArray tripList = json.getJSONArray("trip");
                        ArrayList<Trip> tripArrayList = new ArrayList<>(tripList.length());
                        for (int i = 0; i < tripList.length(); i++) {
                            tripArrayList.add(new Trip(
                                    tripList.getJSONObject(i).getInt("trip_id"),
                                    tripList.getJSONObject(i).getString("trip_name"),
                                    tripList.getJSONObject(i).getString("trip_country"),
                                    tripList.getJSONObject(i).getString("trip_description"),
                                    tripList.getJSONObject(i).getString("trip_created_at"),
                                    tripList.getJSONObject(i).getInt("comment_count"),
                                    tripList.getJSONObject(i).getInt("photo_count")
                            ));
                        }

                        TripDAO tripDAO = new TripDAO(DashboardActivity.this);
                        tripDAO.open();
                        tripDAO.addTripListUser(tripArrayList);
                    }

                    if (!json.getString("inbox").equals("0")) {
                        JSONArray inbox = json.getJSONArray("inbox");
                        ArrayList<Dialog> inboxArrayList = new ArrayList<>(inbox.length());
                        for (int i = 0; i < inbox.length(); i++) {
                            inboxArrayList.add(new Dialog(
                                    inbox.getJSONObject(i).getInt("recipient_id"),
                                    inbox.getJSONObject(i).getString("recipient_last_name"),
                                    inbox.getJSONObject(i).getString("recipient_first_name"),
                                    inbox.getJSONObject(i).getString("message"),
                                    inbox.getJSONObject(i).getBoolean("read_by_recipient"),
                                    inbox.getJSONObject(i).getString("sent_datetime"),
                                    inbox.getJSONObject(i).getBoolean("read_by_recipient")
                            ));
                        }
                        //  TripDAO tripDAO = new TripDAO(DashboardActivity.this);
                        // tripDAO.open();
                        //tripDAO.addTripListUser(tripArrayList);
                    }

                    // L'utilisateur est envoyé vers le DASHBOARDACTIVITY
                    Intent explore = new Intent(getApplicationContext(), HomeActivity.class);
                    explore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(explore);
                    finish();

                } else if (json.getString("error").equals("1")) {
                    Log.d("Erreur dashboard","mot de passe ou adresse email incorrect");
                    DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
                    db.resetTables();
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Fermer le dashboard
                    finish();
                } else if (json.getString("error").equals("3")) {
                    Log.d("Erreur dashboard", "compte non validé");
                    DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
                    db.resetTables();
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Fermer le dashboard
                    finish();
                } else if (json.getString("error").equals("4")) {
                    Log.d("Erreur dashboard", "application en maintenance");
                    DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
                    db.resetTables();
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Fermer le dashboard
                    finish();
                } else if (json.getString("error").equals("5")) {
                    Log.d("Erreur dashboard", "compte est bloqué pour non respect de certaines règles");
                    DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
                    db.resetTables();
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Fermer le dashboard
                    finish();
                } else {
                    Log.d("Erreur dashboard","erreur lors de la recuperation des informations");
                    DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
                    db.resetTables();
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Fermer le dashboard
                    finish();
                }

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
