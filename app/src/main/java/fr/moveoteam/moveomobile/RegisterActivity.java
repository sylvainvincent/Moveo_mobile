package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Sylvain on 01/04/15.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    final Context context = this;
    private Button dialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initialization();
        eventButton();
    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    public void initialization(){
        buttonRegister = (Button) findViewById(R.id.button_register);
        editMail = (EditText) findViewById(R.id.edit_email_register);
        editPassword = (EditText) findViewById(R.id.edit_password_register);
        editName = (EditText) findViewById(R.id.edit_name_register);
        editFirstName = (EditText) findViewById(R.id.edit_firstname_register);
    }

    // Procedure qui permet déclencher un évènement lorsque l'on clique sur un bouton
    public void eventButton(){
        // on sélectionne le bouton créer un compte
        buttonRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // on execute la méthode "execute" de la classe JSONParse
                        new ExecuteThread().execute();
                    }
                }
        );
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
            return jsonUser.addUser(email,password,name,firstName);
        }
        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {

                Log.e("access : ", json.getString("success"));
                // Storing  JSON item in a Variable
                // String msg = (String) c.getString(msg);

                //Set JSON Data in TextView

                //registerErrorMsg.setText("b"); // A changer

                //Custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);

                //Set the custom dialog : text and button
                TextView dialogText = (TextView) dialog.findViewById(R.id.dialogBoxText);
                dialogText.setText("Votre inscription a bien été prise en compte. Veuillez regarder vos mails pour la confirmation de l'inscription.");

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                //If button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
