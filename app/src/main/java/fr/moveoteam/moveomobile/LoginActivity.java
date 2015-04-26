package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.model.Function;
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

    AlertDialog.Builder alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        this.initialization();
        int access = getIntent().getIntExtra("access", 0);
        Log.e("access : ", "access" + access);
        if(access == 1) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage("Votre inscription a bien été prise en compte. Veuillez vérifier vos mails pour la confirmation de l'inscription.");
            // Create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // Show it
            alertDialog.show();
        }

        buttonLogin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        /*Intent intent = new Intent(LoginActivity.this, ExploreActivity.class);
                        startActivity(intent);
                        **/

                        if(!Function.isEmailAddress(editMail.getText().toString())){
                            Toast.makeText(LoginActivity.this, "Adresse email invalide",
                            Toast.LENGTH_LONG).show();
                        } else {
                            new ExecuteThread().execute();
                        }

                    }
                }
        );
    }

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
                if(json.getString("success").equals("1")) {
                    if(json.getJSONObject("user").getString("access_id").equals("2")) {
                        UserDAO userDAO = new UserDAO(LoginActivity.this);
                        User user = new User();
                        user.setLastName(json.getJSONObject("user").getString("user_last_name"));
                        user.setFirstName(json.getJSONObject("user").getString("user_first_name"));
                        user.setBirthday(json.getJSONObject("user").getString("user_birthday"));
                        user.setEmail(json.getJSONObject("user").getString("user_email"));
                        user.setCountry(json.getJSONObject("user").getString("user_country"));
                        user.setCity(json.getJSONObject("user").getString("user_city"));

                        userDAO.open();
                        userDAO.addUser(user);

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        Log.e("Passage", "réussi");
                    }else{
                        alertDialog = new AlertDialog.Builder(
                                LoginActivity.this);
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Vous n'avez pas validé votre compte. Veuillez verifier votre boite mail.");
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Votre mot de passe ou votre adresse mail est incorrect",
                            Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                new ExecuteThread2().execute();

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
                    Toast.makeText(LoginActivity.this, "Un email vous a été envoyer",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "blabla",
                            Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }

    public void linkToRegistration(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

