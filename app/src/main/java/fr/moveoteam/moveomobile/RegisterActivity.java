package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sylvain on 01/04/15.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONParser;
import fr.moveoteam.moveomobile.webservice.JSONUser;

public class RegisterActivity extends Activity {

    // DECLARATION DES VARIABLES
    Button buttonRegister;
    EditText editMail;
    EditText editPassword;
    EditText editName;
    EditText editFirstName;
    JSONArray user = null;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initialization();
        eventButton();
    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    public void initialization() {
        buttonRegister = (Button) findViewById(R.id.button_register);
        editMail = (EditText) findViewById(R.id.edit_email_register);
        editPassword = (EditText) findViewById(R.id.edit_password_register);
        editName = (EditText) findViewById(R.id.edit_name_register);
        editFirstName = (EditText) findViewById(R.id.edit_firstname_register);
    }

    // Procedure qui permet déclencher un évènement lorsque l'on clique sur un bouton
    public void eventButton() {
        // on sélectionne le bouton créer un compte
        buttonRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //On vérifie la conformité des informations du formulaire
                        if (!Function.isEmailAddress(editMail.getText().toString())) {
                            Toast.makeText(RegisterActivity.this, "Votre adresse mail n'est pas valide.", Toast.LENGTH_LONG).show();
                        } else if (editPassword.getText().toString().length() <= 7) {
                            Toast.makeText(RegisterActivity.this, "Votre mot de passe doit contenir 8 caractères ou plus.", Toast.LENGTH_LONG).show();
                        } else if (!(Function.isString(editName.getText().toString()) && Function.isString(editFirstName.getText().toString()))) {
                            Toast.makeText(RegisterActivity.this, "Votre nom ou prénom ne doit contenir que des lettres.", Toast.LENGTH_LONG).show();
                        } else {
                            // on execute la méthode "execute" de la classe JSONParse si les informations sont conformes
                            new ExecuteThread().execute();
                        }
                    }
                }
        );
    }

    public void linkToLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Classe qui permet de réaliser des tâches de manière asynchrone
    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;


        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Envoi des données ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();
            String name = editName.getText().toString();
            String firstName = editFirstName.getText().toString();

            JSONUser jsonUser = new JSONUser();
            return jsonUser.addUser(email, password, name, firstName);
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                if (json.getString("success").equals("1")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("register", 1);
                    startActivity(intent);
                } else if (json.getString("error").equals("1")) {
                    alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Cette adresse email est déjà prise.");
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}