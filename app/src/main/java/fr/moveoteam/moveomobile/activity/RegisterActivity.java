package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sylvain on 01/04/15.
 */
import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONUser;

public class RegisterActivity extends Activity {

    // DECLARATION DES VARIABLES
    Button buttonRegister;
    EditText editMail;
    EditText editPassword;
    EditText editName;
    EditText editFirstName;
    AlertDialog.Builder alertDialog;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        this.initialization();
        if(!Function.beConnectedToInternet(RegisterActivity.this)) {
            toast = Toast.makeText(RegisterActivity.this,"Un accès Internet est requis. Veuillez vérifier votre connexion Internet et réessayez.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM,0,15);
            toast.show();
        }
        this.eventButton();
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
                        if(!Function.beConnectedToInternet(RegisterActivity.this)) {
                            toast = Toast.makeText(RegisterActivity.this, "Un accès Internet est requis. Vérifier votre connexion Internet et réessayez",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 15);
                            toast.show();
                        } else if(editMail.getText().toString().equals("") ||
                                  editPassword.getText().toString().equals("") ||
                                  editName.getText().toString().equals("") ||
                                  editFirstName.getText().toString().equals("")){
                            toast = Toast.makeText(RegisterActivity.this, "Tous les champs sont obligatoires.",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM,0,15);
                            toast.show();
                        } else if (!Function.isEmailAddress(editMail.getText().toString())) {
                            toast = Toast.makeText(RegisterActivity.this, "Votre adresse mail n'est pas valide.",
                            Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM,0,15);
                            toast.show();
                        } else if (editPassword.getText().toString().length() <= 7) {
                            toast = Toast.makeText(RegisterActivity.this, "Votre mot de passe doit contenir 8 caractères ou plus.",
                            Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM,0,15);
                            toast.show();
                        } else if (!(Function.isString(editName.getText().toString()) && Function.isString(editFirstName.getText().toString()))) {
                            toast = Toast.makeText(RegisterActivity.this, "Votre nom ou prénom ne doit contenir que des lettres.",
                            Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM,0,15);
                            toast.show();
                        } else {
                            // Executer la classe ExecuteThread
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
            pDialog.setMessage("Envoi des données...");
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
                if(json == null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
                }else if (json.getString("success").equals("1")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("register", 1);
                    startActivity(intent);
                } else if (json.getString("error").equals("2")) {
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