package fr.moveoteam.moveomobile.thread;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.DashboardActivity;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.model.User;
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
