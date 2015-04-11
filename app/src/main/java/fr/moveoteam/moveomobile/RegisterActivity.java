package fr.moveoteam.moveomobile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Sylvain on 01/04/15.
 */import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.webservice.JSONParser;
import fr.moveoteam.moveomobile.webservice.UserFunctions;

public class RegisterActivity extends ActionBarActivity {

    // DECLARATION DES VARIABLES
    Button buttonRegister;
    EditText editMail;
    EditText editPassword;
    EditText editName;
    EditText editFirstName;
    TextView registerErrorMsg;
    JSONArray user = null;

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
        @Override //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();
            String name = editName.getText().toString();
            String firstName = editFirstName.getText().toString();

            UserFunctions userFunction = new UserFunctions();

            JSONParser jParser = new JSONParser();
            UserFunctions userFunctions = new UserFunctions();
            return userFunctions.addUser(email,password,name,firstName);
        }
        @Override //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {

                user = json.getJSONArray("error_msg");
                JSONObject a = user.getJSONObject(0);
                // Storing  JSON item in a Variable
                // String msg = (String) c.getString(msg);

                //Set JSON Data in TextView

                //registerErrorMsg.setText("b"); // A changer


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
