package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.webservice.JSONParser;
import fr.moveoteam.moveomobile.webservice.UserFunctions;

/**
 * Created by Sylvain on 06/04/15.
 */
public class LoginActivity extends Activity {

    EditText editMail;
    EditText editPassword;
    TextView linkRegistration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        linkRegistration = (TextView) findViewById(R.id.link_registration);

        linkRegistration.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    /*
    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerErrorMsg = (TextView) findViewById(R.id.registerErrorMsg);
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Envoi des données ...");
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
            return userFunctions.addUser(email,password,name,firstName);
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {

                user = json.getJSONArray("error_msg");
                JSONObject a = user.getJSONObject(0);
                // Storing  JSON item in a Variable
                // String msg = (String) c.getString(msg);

                //Set JSON Data in TextView

                registerErrorMsg.setText("b"); // A changer


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } */


}
