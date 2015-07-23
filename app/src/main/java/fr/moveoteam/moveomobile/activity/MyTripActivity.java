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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 17/05/15.
 */
public class MyTripActivity extends Activity {

    // Element de vue
    private TextView modifycover;
    private TextView mytripcitytitle;
    private TextView mytriptitle;
    private EditText tripdescription;
    private TextView addtripdate;
    private Button modifydescription;
    private TextView deletetrip;
    private ImageView shopping;
    private ImageView hobbies;
    private ImageView fooding;
    private ImageView cover;
    private ImageView comment;
    private ImageView pictures;

    private TripDAO tripDAO;
    private int id;
    private int userId;
    private String photoBase64;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trip);
        initialize();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        id = getIntent().getExtras().getInt("id",0);

        UserDAO userDAO = new UserDAO(MyTripActivity.this);
        userDAO.open();
        userId = userDAO.getUserDetails().getId();
        userDAO.close();

        tripDAO = new TripDAO(MyTripActivity.this);

        tripDAO.open();
        Trip trip = tripDAO.getTrip(id);
        description = trip.getDescription();

        mytriptitle.setText(trip.getName());
        mytripcitytitle.setText(trip.getCountry());
        tripdescription.setText(description);

        if(trip.getCover() != null ) {
            if(!trip.getCover().equals("") && !trip.getCover().equals("null")){
                new DownloadImage().execute(trip.getCover());
            }
           // cover.setImageBitmap(Function.decodeBase64(trip.getCover()));
        }
        if(trip.getDate() != null)
            addtripdate.setText(addtripdate.getText() + " " + Function.dateSqlToFullDateJava(trip.getDate()));


        modifycover.setVisibility(View.INVISIBLE);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        //Modify Description
        modifydescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!description.equals(tripdescription.getText().toString()))
                    new ExecuteUpdateTripThread().execute();
            }
        });

        // Click Delete Trip
        deletetrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MyTripActivity.this);
                alert.setMessage("Êtes vous sûr de supprimer ce lieu ?");
                alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ExecuteDeleteTripThread().execute();
                    }
                });
                alert.setNegativeButton("non", null);
                alert.show();
            }
        });

        fooding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTripActivity.this,CategoryActivity.class);
                intent.putExtra("tripId",id);
                intent.putExtra("categoryId",1);
                startActivity(intent);
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTripActivity.this,CategoryActivity.class);
                intent.putExtra("tripId",id);
                intent.putExtra("categoryId",2);
                startActivity(intent);
            }
        });

        hobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTripActivity.this,CategoryActivity.class);
                intent.putExtra("tripId",id);
                intent.putExtra("categoryId",3);
                startActivity(intent);
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTripActivity.this,CommentActivity.class);
                intent.putExtra("tripId",id);
                startActivity(intent);
            }
        });

        modifycover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExecuteUpdateCoverThread().execute();

            }
        });

        pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTripActivity.this,MyGalleryPhotoActivity.class);
                intent.putExtra("tripId",id);
                startActivity(intent);
            }
        });

    }

    private void initialize() {

        cover = (ImageView) findViewById(R.id.my_trip_cover);
        modifycover = (TextView) findViewById(R.id.modify_cover);
        mytripcitytitle = (TextView) findViewById(R.id.my_trip_city_title);
        tripdescription = (EditText) findViewById(R.id.trip_description);
        addtripdate = (TextView) findViewById(R.id.add_trip_date);
        mytriptitle = (TextView) findViewById(R.id.my_trip_title);
        modifydescription = (Button) findViewById(R.id.modify_description);
        deletetrip = (TextView) findViewById(R.id.delete_trip);
        fooding = (ImageView) findViewById(R.id.fooding);
        shopping = (ImageView) findViewById(R.id.shopping);
        hobbies = (ImageView) findViewById(R.id.hobbies);
        comment = (ImageView) findViewById(R.id.comment);
        pictures = (ImageView) findViewById(R.id.pictures);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private class ExecuteUpdateTripThread extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(MyTripActivity.this);
            dialog.setMessage("Mise à jour...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.updateDescription(Integer.toString(id), tripdescription.getText().toString());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dialog.dismiss();
            if(jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(MyTripActivity.this);
                        ad.setMessage("La description a été modifié avec succès");
                        ad.create();
                        ad.show();
                        TripDAO tripDAO = new TripDAO(MyTripActivity.this);
                        tripDAO.open();
                        tripDAO.updateTripDescription(id, tripdescription.getText().toString());
                        tripDAO.close();
                        tripdescription.setText(tripdescription.getText().toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyTripActivity.this);
                alert.setMessage("Une erreur s'est produite lors de la mise à jour de la description");
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        }
    }

    private class ExecuteDeleteTripThread extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(MyTripActivity.this);
            dialog.setMessage("Suppression en cours...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.deleteTrip(Integer.toString(id), Integer.toString(userId));
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dialog.dismiss();
            if(jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        TripDAO tripDAO = new TripDAO(MyTripActivity.this);
                        tripDAO.open();
                        tripDAO.removeTrip(id);
                        tripDAO.close();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(MyTripActivity.this);
                        alert.setMessage("Une erreur s'est produite lors de la suppression du voyage");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyTripActivity.this);
                alert.setMessage("Connexion perdue");
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        }
    }

    private class ExecuteUpdateCoverThread extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(MyTripActivity.this);
            dialog.setMessage("Mise à jour...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONTrip jsonTrip = new JSONTrip();
            return  jsonTrip.updateCoverImage(Integer.toString(id),photoBase64) ;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dialog.dismiss();
            if(jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(MyTripActivity.this);
                        ad.setMessage("La photo a été modifié avec succès");
                        ad.create();
                        ad.show();

                        TripDAO tripDAO = new TripDAO(MyTripActivity.this);
                        tripDAO.open();
                        tripDAO.updateCover(id,jsonObject.getString("link_cover"));
                        tripDAO.close();
                        setResult(RESULT_OK);
                    }else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MyTripActivity.this);
                        alert.setMessage("Une erreur s'est produite lors de la mise à jour de la page de couverture");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyTripActivity.this);
                alert.setMessage("Connexion perdue");
                alert.setPositiveButton("OK",null);
                alert.show();
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

                    // Stoker la photo en base64 dans une variable
                    photoBase64 = Function.encodeBase64(photo);

                    // Changer la photo actuel avec la nouvelle
                    cover.setImageBitmap(photo);
                    modifycover.setVisibility(View.VISIBLE);
                }catch (OutOfMemoryError e){
                    e.getMessage();
                    Toast.makeText(MyTripActivity.this, "Photo trop lourd", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        String url;
        URL urlImage;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... args) {
            url = args[0];
            try {
                urlImage = new URL("http://moveo.besaba.com/"+url);
                connection = (HttpURLConnection) urlImage.openConnection();
                if (connection != null) {
                    inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                cover.setImageResource(R.drawable.default_journey);
            } else {
                cover.setImageBitmap(result);
            }
        }
    }

}
