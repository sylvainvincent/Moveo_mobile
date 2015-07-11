package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.PlaceDAO;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by alexMac on 09/04/15.
 */
public class AddPlaceActivity extends Activity{

    // ELEMENTS DE LA VUE
    private TextView exploretitle;
    private TextView category;
    private TextView place;
    private TextView adress;
    private TextView descriptionPlace;
    private TextView cancelAddPlace;

    private EditText editPlace;
    private EditText editAdress;
    private EditText editDescriptionPlace;

    private Button buttonAddPlace;

    private RelativeLayout addplace;

    // AUTRES
    int categoryId;
    int tripId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        this.initialization();

        // Récuperation des variables de l'activity MyTripActivity
        categoryId = getIntent().getIntExtra("categoryId",0);
        tripId = getIntent().getIntExtra("tripId",0);

        if(categoryId == 1) category.setText("Gastronomie");
        if(categoryId == 2) category.setText("Shopping");
        if(categoryId == 3) category.setText("Loisir");

        buttonAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPlace.getText().toString().equals("")){
                    editPlace.setError("Champ obligatoire");
                }else{
                    new ExecuteThread().execute();
                }
            }
        });

    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    private void initialization() {

        exploretitle = (TextView) findViewById(R.id.explore_title);
        category = (TextView) findViewById(R.id.add_place_category);
        place = (TextView) findViewById(R.id.place);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
        editPlace = (EditText) findViewById(R.id.edit_place);
        adress = (TextView) findViewById(R.id.adress);
        editAdress = (EditText) findViewById(R.id.edit_address);
        descriptionPlace = (TextView) findViewById(R.id.descriptionPlace);
        editDescriptionPlace = (EditText) findViewById(R.id.editDescriptionPlace);
        buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        cancelAddPlace = (TextView) findViewById(R.id.cancelAddPlace);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPlaceActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... arg){

            String id = Integer.toString(tripId);
            String name = editPlace.getText().toString();
            String address = editAdress.getText().toString();
            String description = editDescriptionPlace.getText().toString();

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.addPlace(name, address, description, Integer.toString(categoryId), id);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
                    builder.setMessage("Connexion perdu");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }else if(json.getString("success").equals("1")) {
                    Place place = new Place();
                    place.setId(Integer.parseInt(json.getString("place_id")));
                    place.setName(editPlace.getText().toString());
                    place.setAddress(editAdress.getText().toString());
                    if(!editDescriptionPlace.getText().toString().equals(""))place.setDescription(editDescriptionPlace.getText().toString());
                    place.setCategory(categoryId);
                    place.setTripId(tripId);

                    PlaceDAO placeDAO = new PlaceDAO(AddPlaceActivity.this);
                    placeDAO.open();
                    placeDAO.addPlace(place);
                    placeDAO.close();

                    setResult(RESULT_OK,getIntent().putExtra("addPlace",1));
                    finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
                    builder.setMessage("Une erreur s'est produite lors de l'ajout du lieu");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }


}
