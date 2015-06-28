package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 11/06/15.
 */
public class AddTrip extends Activity {

    private TextView addtriptitle;
    private TextView city;
    private EditText editTripName;
    private TextView country;
    private EditText editCountry;
    private TextView descriptionTrip;
    private EditText editdescriptiontrip;
    private TextView addcover;
    private Button addPhotoButton;
    private Button buttonAddPlace;
    private TextView cancelAddPlace;
    private RelativeLayout addplace;
    private EditText linkPhoto;
    private ImageView image;

    String photoBase64;

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
               int count = 0;
                if(editTripName.getText().toString().equals("")){
                    editTripName.setError("Champ obligatoire");
                }else count++;

                if (editCountry.getText().toString().equals("")) {
                    editCountry.setError("Champ obligatoire");
                }else count++;


                if(count == 2)new ExecuteThread().execute();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });
    }

    private void initialize() {

        addtriptitle = (TextView) findViewById(R.id.add_trip_title);
        city = (TextView) findViewById(R.id.add_trip_name);
        editTripName = (EditText) findViewById(R.id.edit_trip_name);
        country = (TextView) findViewById(R.id.country);
        editCountry = (EditText) findViewById(R.id.editCountry);
        descriptionTrip = (TextView) findViewById(R.id.descriptionTrip);
        editdescriptiontrip = (EditText) findViewById(R.id.edit_description_trip);
        addcover = (TextView) findViewById(R.id.add_cover);
        addPhotoButton = (Button) findViewById(R.id.add_photo_button);
        buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        cancelAddPlace = (TextView) findViewById(R.id.cancelAddPlace);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
        linkPhoto = (EditText) findViewById(R.id.link_photo_trip);
        image = (ImageView) findViewById(R.id.trip_image);
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
            String name = editTripName.getText().toString();
            String country = editCountry.getText().toString();
            String description = editdescriptiontrip.getText().toString();

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.addTrip(id,name,country,description,photoBase64);
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
                    Trip trip = new Trip();
                    trip.setName(editTripName.getText().toString());
                    trip.setCountry(editCountry.getText().toString());
                    if(photoBase64 != null)trip.setCover(photoBase64);
                    if(!editdescriptiontrip.getText().toString().equals(""))trip.setDescription(editdescriptiontrip.getText().toString());
                    trip.setDate(json.getString("date"));
                    TripDAO tripDAO = new TripDAO(AddTrip.this);
                    tripDAO.open();
                    tripDAO.addTrip(trip);
                    tripDAO.close();

                    setResult(RESULT_OK,getIntent().putExtra("addTrip",1));
                    finish();
                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
		
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
			// Récupération des informations d'une photo sélectionné dans l'album
            if (requestCode == 1) {
                Bitmap photo = null;
                // RECUPERATION DE L'ADRESSE DE LA PHOTO
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                // FIN DE LA RECUPERATION
                c.close();

                try{
                    photo = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path de l'image", picturePath + "");
                    // Remplir le champ en dessous de la photo avec le chemin de la nouvelle
                    linkPhoto.setText(picturePath);

                    // Stoker la photo en base64 dans une variable
                    photoBase64 = Function.encodeBase64(photo);

                    // Changer la photo actuel avec la nouvelle
                    image.setImageBitmap(photo);
                }catch (OutOfMemoryError e){
                    e.getMessage();
                    Toast.makeText(AddTrip.this,"Photo trop lourd",Toast.LENGTH_LONG).show();
                }

            }
        }
    }
	
}
