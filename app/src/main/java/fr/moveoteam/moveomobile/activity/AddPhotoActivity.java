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
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.PhotoDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Photo;
import fr.moveoteam.moveomobile.model.User;
import fr.moveoteam.moveomobile.webservice.JSONTrip;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 17/07/15.
 */
public class AddPhotoActivity extends Activity {

    private EditText photopathfield;
    private Button selectphotobutton;
    private ImageView photoselected;
    private Button addmyphotobutton;
    private int tripId;
    private String photoBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        initialize();
        tripId = getIntent().getIntExtra("tripId",0);
        selectphotobutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        addmyphotobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoBase64 != null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);

                    builder.setMessage("Confirmer l'ajout ?");
                    builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ExecuteThread().execute();
                        }
                    });
                    builder.setNegativeButton("non", null);
                    builder.show();
                }
            }
        });
    }

    private void initialize() {
        photopathfield = (EditText) findViewById(R.id.photo_path_field);
        selectphotobutton = (Button) findViewById(R.id.select_photo_button);
        photoselected = (ImageView) findViewById(R.id.photo_selected);
        addmyphotobutton = (Button) findViewById(R.id.add_my_photo_button);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Account", "test");
        if (resultCode == Activity.RESULT_OK) {

            // Récupération des informations d'une photo sélectionné dans l'album
            if (requestCode == 1) {

                // RECUPERATION DE L'ADRESSE DE LA PHOTO
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                // FIN DE LA RECUPERATION
                c.close();

                Bitmap thumbnail2 = (BitmapFactory.decodeFile(picturePath));
                photoBase64 = Function.encodeBase64(thumbnail2);
                Log.w("path de l'image", picturePath + "");
                // Remplir le champ en dessous de la photo avec le chemin de la nouvelle
                photopathfield.setText(picturePath);

                // Changer la photo actuel avec la nouvelle
                photoselected.setImageBitmap(thumbnail2);
            }
        }
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPhotoActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.addPhoto(Integer.toString(tripId), photoBase64);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            pDialog.dismiss();
            if(json == null){
                Log.e("test json","null");
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", null);
                builder.show();
            }else try {
                if (json.getString("success").equals("1")) {
                    Photo photo = new Photo();
                    photo.setId(Integer.parseInt(json.getString("id")));
                    photo.setPublicationDate(json.getString("date"));
                    photo.setPhotoBase64(json.getString("image"));
                    photo.setTripId(tripId);
                    PhotoDAO photoDAO = new PhotoDAO(AddPhotoActivity.this);
                    photoDAO.open();
                    photoDAO.addPhoto(photo);
                    photoDAO.close();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);

                    builder.setMessage("La modification a été réalisé avec succès");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(AddPhotoActivity.this, MyGalleryPhotoActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);

                    builder.setMessage("Une erreur est survenue lors de l'enregistrement de la photo");
                    builder.setPositiveButton("ok", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
