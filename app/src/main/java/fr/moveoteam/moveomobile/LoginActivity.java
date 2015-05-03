package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.model.User;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 06/04/15.
 */
public class LoginActivity extends Activity {

    EditText editMail;
    EditText editPassword;
    TextView linkRegistration;
    Button buttonLogin;
    EditText editMailForgetPassword;

    Toast toast;

    AlertDialog.Builder alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        this.initialization();
        int register = getIntent().getIntExtra("register", 0);
        Log.e("register : ", "register" + register);
        if(register == 1) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage("Votre inscription a bien été prise en compte. Veuillez vérifier vos mails pour la confirmation de l'inscription.");
            alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // Create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // Show it
            alertDialog.show();
        }


        buttonLogin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(!Function.beConnectedToInternet(LoginActivity.this)) {
                            toast = Toast.makeText(LoginActivity.this,"un accès Internet est requis, Vérifier votre connexion Internet puis réessayez",
                            Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 15);
                            toast.show();
                        }else if(!Function.isEmailAddress(editMail.getText().toString())){
                            toast = Toast.makeText(LoginActivity.this, "Votre Adresse email est invalide",
                            Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM,0,15);
                            toast.show();
                        } else {
                            new ExecuteThread().execute();
                        }
                    }
                }
        );
    }
	
	// Permet d'initialiser tous les elements du layout
    public void initialization(){

        linkRegistration = (TextView) findViewById(R.id.link_registration);
        buttonLogin = (Button) findViewById(R.id.button_login);
        editMail = (EditText) findViewById(R.id.edit_email_login);
        editPassword = (EditText) findViewById(R.id.edit_password_login);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void lostPasswordSender(View view){
        AlertDialog.Builder lostPassword = new AlertDialog.Builder(this);
        lostPassword.setTitle(R.string.lost_password_label);
        lostPassword.setMessage(R.string.lost_password_description);
        editMailForgetPassword = new EditText(this);
        lostPassword.setView(editMailForgetPassword);
        lostPassword.setPositiveButton(R.string.lost_password_send_email,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Function.beConnectedToInternet(LoginActivity.this)) {
                    toast = Toast.makeText(LoginActivity.this,"un accès Internet est requis, Vérifier votre connexion Internet et réessayez",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM,0,15);
                    toast.show();
                }else if(!Function.isEmailAddress(editMailForgetPassword.getText().toString())){
                    toast = Toast.makeText(LoginActivity.this,"Votre adresse email est invalide",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM,0,15);
                    toast.show();
                }else{
                    new ExecuteThread2().execute();
                }
            }
        });
        lostPassword.setNegativeButton(R.string.lost_password_cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = lostPassword.create();
        alertDialog.show();
    }

	// Thread secondaire pour la connexion
    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Connexion en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();

            JSONUser jsonUser = new JSONUser();
            return jsonUser.loginUser(email, password);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json.getString("success").equals("1") || json.getString("success").equals("2")) {
                    if (json.getJSONObject("user").getString("access_id").equals("2")) {

                        // Création de l'objet User
                        User user = new User();
                        user.setLastName(json.getJSONObject("user").getString("user_last_name"));
                        user.setFirstName(json.getJSONObject("user").getString("user_first_name"));
                        user.setBirthday(json.getJSONObject("user").getString("user_birthday"));
                        user.setEmail(json.getJSONObject("user").getString("user_email"));
                        user.setCountry(json.getJSONObject("user").getString("user_country"));
                        user.setCity(json.getJSONObject("user").getString("user_city"));

                        // Création de l'objet DAO(utilisateur) pour ajouter un utilisateur
                        UserDAO userDAO = new UserDAO(LoginActivity.this);
                        userDAO.open();
                        userDAO.addUser(user);

                        if(json.getString("success").equals("1")){
                            JSONArray tripList = json.getJSONArray("trip");
                            Trip trip;
                            ArrayList<Trip> tripArrayList = new ArrayList<>(tripList.length());
                            for(int i=0; i<tripList.length();i++){
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
                            TripDAO tripDAO = new TripDAO(LoginActivity.this);
                            tripDAO.open();
                            tripDAO.addTripListUser(tripArrayList);
                        }

                        // L'utilisateur est envoyer vers le DASHBOARDACTIVITY
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        Log.e("Passage", "réussi");
                    } else {
                        alertDialog = new AlertDialog.Builder(
                                LoginActivity.this);
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Votre compte n'est pas validé. " +
                                "Veuillez verifier votre boite de réception ou les courriers indésirables de votre boite email.");
                        alertDialog.show();
                    }
                }else if(json.getString("error").equals("1")) {
                    toast = Toast.makeText(LoginActivity.this, "Votre mot de passe ou votre adresse mail est incorrect",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM,0,15);
                    toast.show();
                }else{
                    toast = Toast.makeText(LoginActivity.this, "Un erreur s'est produite lors de la recuperation de vos informations",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM,0,15);
                    toast.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }



	// Thread secondaire pour le mot de passe oublié
    private class ExecuteThread2 extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Envoi en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            String email = editMailForgetPassword.getText().toString();

            JSONUser emailSender = new JSONUser();
            return emailSender.lostPassword(email);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json.getString("success").equals("1")) {
                    alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Un mail avec votre nouveau mot de passe vous a été envoyé");
                    alertDialog.show();
                } else if(json.getString("error").equals("2")){
                    alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Cette adresse email n'existe pas");
                    alertDialog.show();
                }else {
                    alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Erreur lors de l'envoi du mail");
                    alertDialog.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // Fermer l'application lorsque l'on appuie sur le bouton "back"
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }

    // LES ONCLICK
	// Permet d'accéder à l'inscription en sélectionnant le bouton "créer un compte"
    public void linkToRegistration(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

