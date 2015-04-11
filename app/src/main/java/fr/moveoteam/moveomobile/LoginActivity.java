package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sylvain on 06/04/15.
 */
public class LoginActivity extends Activity {

    EditText editMail;
    EditText editPassword;
    TextView linkRegistration;
    Button buttonLogin;
    Pattern patternMail = Pattern.compile(".+@.+\\.[a-z]+");
    Matcher m;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialization();
        eventButton();
    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    public void initialization(){
        editMail = (EditText) findViewById(R.id.edit_email_login);
        editPassword = (EditText) findViewById(R.id.edit_password_login);
        linkRegistration = (TextView) findViewById(R.id.link_registration);
        buttonLogin = (Button) findViewById(R.id.button_login);
    }

    // Procedure qui permet déclencher un évenement lorsque l'on clique sur un bouton
    public void eventButton(){

        // Lien vers la page d'inscription
        linkRegistration.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );

        // Bouton de connexion
        buttonLogin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        m = patternMail.matcher(editMail.getText().toString());
                        if(!m.matches()){
                            Toast.makeText(LoginActivity.this, "Adresse mail non conforme",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            // new ExecuteThread().execute();
                        }
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
            editMail = (EditText) findViewById(R.id.edit_email_login);
            editPassword = (EditText) findViewById(R.id.edit_password_login);
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Connexion en cours ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();

            UserFunctions userFunction = new UserFunctions();

            JSONParser jParser = new JSONParser();
            UserFunctions userFunctions = new UserFunctions();
            return userFunctions.addUser(email,password);
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
    }
    */

}

