package fr.moveoteam.moveomobile;

import android.app.Activity;
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

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editMail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editName = (EditText) findViewById(R.id.editName);
        editFirstName = (EditText) findViewById(R.id.editFirstName);


        // on selectionne le bouton créer un compte
        buttonRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                         // on execute la methode "execute" de la classe JSONParse
                        new JSONParse().execute();
                    }
                }
        );
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerErrorMsg = (TextView) findViewById(R.id.registerErrorMsg);
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();
            String name = editName.getText().toString();
            String firstName = editFirstName.getText().toString();

            UserFunctions userFunction = new UserFunctions();

            JSONParser jParser = new JSONParser();
            UserFunctions userFunctions = new UserFunctions();
            return userFunctions.registerUser(email,password,name,firstName);
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array
                user = json.getJSONArray("error_msg");
                JSONObject c = user.getJSONObject(0);
                // Storing  JSON item in a Variable
                String msg = (String) user.get(0);

                //Set JSON Data in TextView

                registerErrorMsg.setText(msg);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
