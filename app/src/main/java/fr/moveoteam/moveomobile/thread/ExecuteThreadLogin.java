package fr.moveoteam.moveomobile.thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 04/05/15.
 */
public class ExecuteThreadLogin extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String email;
        String password;

        public ExecuteThreadLogin(Context context,String email, String password ){
            this.context = context;
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Connexion en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            JSONUser jsonUser = new JSONUser();
            return jsonUser.loginUser(email, password);
        }



}
