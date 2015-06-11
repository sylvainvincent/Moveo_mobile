package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.TripListAdapter;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 11/06/15.
 */
public class AddTrip extends Activity {

    private TextView addtriptitle;
    private TextView city;
    private EditText editCity;
    private TextView country;
    private EditText editCountry;
    private TextView descriptionTrip;
    private EditText editdescriptiontrip;
    private TextView addcover;
    private Button addfile;
    private Button buttonAddPlace;
    private TextView cancelAddPlace;
    private RelativeLayout addplace;


    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trip);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        initialize();

        UserDAO userDAO = new UserDAO(AddTrip.this);
        userDAO.open();
        userId = userDAO.getUserDetails().getId();

        buttonAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExecuteThread().execute();
            }
        });
    }

    private void initialize() {

        addtriptitle = (TextView) findViewById(R.id.add_trip_title);
        city = (TextView) findViewById(R.id.city);
        editCity = (EditText) findViewById(R.id.editCity);
        country = (TextView) findViewById(R.id.country);
        editCountry = (EditText) findViewById(R.id.editCountry);
        descriptionTrip = (TextView) findViewById(R.id.descriptionTrip);
        editdescriptiontrip = (EditText) findViewById(R.id.edit_description_trip);
        addcover = (TextView) findViewById(R.id.add_cover);
        addfile = (Button) findViewById(R.id.add_file);
        buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        cancelAddPlace = (TextView) findViewById(R.id.cancelAddPlace);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
    }


    private class ExecuteThread extends AsyncTask<String, String, JSONObject>{

        private ProgressDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddTrip.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... arg){

            String id = Integer.toString(userId);
            String name = editCity.getText().toString();
            String country = editCountry.getText().toString();
            String cover = null;
            String description = editdescriptiontrip.getText().toString();

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.addTrip(id,name,country,cover,description);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTrip.this);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            System.exit(0);
                        }
                    });
                    builder.setMessage("Connexion perdu");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }

                else if(json.getString("success").equals("1")) {

                    finish();
                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }
}
