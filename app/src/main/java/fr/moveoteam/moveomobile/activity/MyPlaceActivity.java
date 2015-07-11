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

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.PlaceDAO;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 11/07/15.
 */
public class MyPlaceActivity extends Activity {

    private TextView exploretitle;
    private TextView textPlace;
    private EditText editPlace;
    private TextView adress;
    private EditText editaddress;
    private TextView descriptionPlace;
    private EditText editDescriptionPlace;
    private Button buttonAddPlace;
    private TextView cancelAddPlace;
    private TextView addplacecategory;
    private RelativeLayout addplace;

    //AUTRES
    int placeId;
    int categoryId;
    Trip trip;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        initialize();

        categoryId = getIntent().getIntExtra("categoryId",0);
        placeId = getIntent().getIntExtra("placeId",0);

        PlaceDAO placeDAO = new PlaceDAO(MyPlaceActivity.this);
        placeDAO.open();
        place = placeDAO.getPlace(placeId);
        placeDAO.close();


        if(categoryId == 1) addplacecategory.setText("Gastronomie");
        if(categoryId == 2) addplacecategory.setText("Shopping");
        if(categoryId == 3) addplacecategory.setText("Loisir");

        editPlace.setText(place.getName());
        editaddress.setText(place.getAddress());
        editDescriptionPlace.setText(place.getDescription());
        buttonAddPlace.setText("MODIFIER");
        cancelAddPlace.setText("Supprimer");

        buttonAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editPlace.getText().toString().equals("")){
                    new ExecuteUpdatePlaceThread().execute();
                }else {
                    editPlace.setError("Champ obligatoire");
                }
            }
        });

       cancelAddPlace.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
               alert.setMessage("Êtes vous sûr de supprimer le lieu ?");
               alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       new ExecuteDeletePlaceThread().execute();

                   }
               });
               alert.setNegativeButton("non", null);
               alert.show();
           }
       });

    }

    private void initialize() {

        exploretitle = (TextView) findViewById(R.id.explore_title);
        textPlace = (TextView) findViewById(R.id.text_place);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
        editPlace = (EditText) findViewById(R.id.edit_place);
        adress = (TextView) findViewById(R.id.adress);
        editaddress = (EditText) findViewById(R.id.edit_address);
        descriptionPlace = (TextView) findViewById(R.id.descriptionPlace);
        editDescriptionPlace = (EditText) findViewById(R.id.editDescriptionPlace);
        buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        cancelAddPlace = (TextView) findViewById(R.id.cancelAddPlace);
        addplacecategory = (TextView) findViewById(R.id.add_place_category);

    }

    private class ExecuteUpdatePlaceThread extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;
        String name;
        String address;
        String description;

        protected void onPreExecute() {
            dialog = new ProgressDialog(MyPlaceActivity.this);
            dialog.setMessage("Mise à jour...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            name = editPlace.getText().toString();
            address = editaddress.getText().toString();
            description = editDescriptionPlace.getText().toString();
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.updatePlace(Integer.toString(placeId), name, address, description);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dialog.dismiss();
            if(jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equals("1")) {

                        place = new Place();
                        place.setId(placeId);
                        place.setName(name);
                        place.setAddress(address);
                        place.setDescription(description);
                        PlaceDAO placeDAO = new PlaceDAO(MyPlaceActivity.this);
                        placeDAO.open();
                        placeDAO.updatePlace(place);
                        placeDAO.close();
                        setResult(RESULT_OK);
                        AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
                        alert.setMessage("La description a été modifié avec succès");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
                        alert.setMessage("Une erreur s'est produite lors de la mise à jour du lieu");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
                alert.setMessage("Connexion perdu");
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        }
    }

    private class ExecuteDeletePlaceThread extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(MyPlaceActivity.this);
            dialog.setMessage("Suppression en cours...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.deletePlace(Integer.toString(placeId));
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dialog.dismiss();
            if(jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        PlaceDAO placeDAO = new PlaceDAO(MyPlaceActivity.this);
                        placeDAO.open();
                        placeDAO.removePlace(placeId);
                        placeDAO.close();
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
                        alert.setMessage("Une erreur s'est produite lors de la suppression du lieu");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MyPlaceActivity.this);
                    alert.setMessage("Connexion perdu");
                    alert.setPositiveButton("OK",null);
                    alert.show();
            }
        }
    }
}
