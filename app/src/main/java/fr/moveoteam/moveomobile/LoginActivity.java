package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

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

    AlertDialog.Builder alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        linkRegistration = (TextView) findViewById(R.id.link_registration);
        buttonLogin = (Button) findViewById(R.id.button_login);
        editMail = (EditText) findViewById(R.id.edit_email_login);

        linkRegistration.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );

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
            editPassword = (EditText) findViewById(R.id.edit_password_login);
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
                if(json.getString("success").equals("0")) {
                    Intent intent = new Intent(LoginActivity.this, ExploreActivity.class);
                    startActivity(intent);
                }
                if(json.getString("success").equals("1")) {
                    if(json.getJSONObject("user").getString("access_id").equals("1")) {
                        UserDAO userDAO = new UserDAO(LoginActivity.this);
                        User user = new User(
                                json.getJSONObject("user").getString("user_last_name"),
                                json.getJSONObject("user").getString("user_first_name"),
                                json.getJSONObject("user").getString("user_birthday"),
                                json.getJSONObject("user").getString("user_email"),
                                json.getJSONObject("user").getString("user_country"),
                                json.getJSONObject("user").getString("user_city")
                        );
                        userDAO.addUser(user);

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        Log.e("Passage", "réussi");
                    }else{
                        alertDialog = new AlertDialog.Builder(
                                LoginActivity.this);
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Vous n'avez pas valider votre compte.");
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "La connexion a échoué",
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

