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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 17/05/15.
 */
public class MyTripActivity extends Activity {

    private TripDAO tripDAO;
    private int id;
    private int userId;

    private AlertDialog.Builder alertDialog;

    private ArrayList<Comment> commentArrayList;
    private TextView modifycover;
    private TextView mytripcitytitle;
    private TextView mytriptitle;
    private ScrollView mytrip;
    private ListView listView;
    private EditText tripdescription;
    private TextView addtripdate;
    private Button modifydescription;
    private TextView deletetrip;
    private ImageView shopping;
    private ImageView hobbies;
    private ImageView fooding;
    private ImageView cover;
    private ImageView comment;

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
            if(!trip.getCover().equals("") && !trip.getCover().equals("null"))
            cover.setImageBitmap(Function.decodeBase64(trip.getCover()));
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
    }

    private void initialize() {

        cover = (ImageView) findViewById(R.id.my_trip_cover);
        modifycover = (TextView) findViewById(R.id.modify_cover);
        mytripcitytitle = (TextView) findViewById(R.id.my_trip_city_title);
        mytrip = (ScrollView) findViewById(R.id.my_trip);
        tripdescription = (EditText) findViewById(R.id.trip_description);
        addtripdate = (TextView) findViewById(R.id.add_trip_date);
        mytriptitle = (TextView) findViewById(R.id.my_trip_title);
        modifydescription = (Button) findViewById(R.id.modify_description);
        deletetrip = (TextView) findViewById(R.id.delete_trip);
        fooding = (ImageView) findViewById(R.id.fooding);
        shopping = (ImageView) findViewById(R.id.shopping);
        hobbies = (ImageView) findViewById(R.id.hobbies);
        comment = (ImageView) findViewById(R.id.comment);

    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        JSONArray placeList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyTripActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTrip(Integer.toString(id));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){
                    finish();
                }
                else if (json.getString("error").equals("0")) {

                    tripDAO = new TripDAO(MyTripActivity.this);
                    tripDAO.open();

                    mytriptitle.setText(json.getJSONObject("trip").getString("trip_name"));
                    mytripcitytitle.setText(json.getJSONObject("trip").getString("trip_country"));
                    tripdescription.setText(json.getJSONObject("trip").getString("trip_description"));

                    if(!json.getJSONObject("trip").getString("trip_cover").equals("null")) {
                        cover.setImageBitmap(Function.decodeBase64(json.getJSONObject("trip").getString("trip_cover")));
                    }

                    addtripdate.setText(addtripdate.getText() + " " + Function.dateSqlToFullDateJava(json.getJSONObject("trip").getString("trip_created_at")));


                    /*if((json.getString("success").equals("1")) || (json.getString("success").equals("2"))){

                        placeArrayList = new ArrayList<>();

                        if(!json.getString("fooding").equals("0")){
                            placeList = json.getJSONArray("fooding");
                            for (int i = 0; i < placeList.length(); i++) {
                                placeArrayList.add(new Place(
                                        placeList.getJSONObject(i).getInt("place_id"),
                                        placeList.getJSONObject(i).getString("place_name"),
                                        placeList.getJSONObject(i).getString("place_address"),
                                        placeList.getJSONObject(i).getString("place_description"),
                                        placeList.getJSONObject(i).getInt("category_id")
                                ));
                                Log.e("fooding", placeArrayList.get(i).toString());
                            }
                        }

                        if(!json.getString("shopping").equals("0")){
                            placeList = json.getJSONArray("shopping");
                            for (int i = 0; i < placeList.length(); i++) {
                                placeArrayList.add(new Place(
                                        placeList.getJSONObject(i).getInt("place_id"),
                                        placeList.getJSONObject(i).getString("place_name"),
                                        placeList.getJSONObject(i).getString("place_address"),
                                        placeList.getJSONObject(i).getString("place_description"),
                                        placeList.getJSONObject(i).getInt("category_id")
                                ));
                                Log.e("shopping", placeArrayList.get(i).toString());
                            }
                        }

                        if(!json.getString("leisure").equals("0")){
                            JSONArray placeList = json.getJSONArray("leisure");
                            placeArrayList = new ArrayList<>(placeList.length());
                            for (int i = 0; i < placeList.length(); i++) {
                                placeArrayList.add(new Place(
                                        placeList.getJSONObject(i).getInt("place_id"),
                                        placeList.getJSONObject(i).getString("place_name"),
                                        placeList.getJSONObject(i).getString("place_address"),
                                        placeList.getJSONObject(i).getString("place_description"),
                                        placeList.getJSONObject(i).getInt("category_id")

                                ));
                                Log.e("leisure", placeArrayList.get(i).toString());
                            }
                        }

                        PlaceDAO placeDAO = new PlaceDAO(MyTripActivity.this);
                        placeDAO.open();
                        placeDAO.addPlaceList(placeArrayList);
                        placeDAO.close();
                    }*/

                    if((json.getString("success").equals("1")) || (json.getString("success").equals("3"))){
                        JSONArray commentList = json.getJSONArray("comment");
                        commentArrayList = new ArrayList<>(commentList.length());
                        for (int i = 0; i < commentList.length(); i++) {
                            commentArrayList.add(new Comment(
                                    commentList.getJSONObject(i).getInt("comment_id"),
                                    commentList.getJSONObject(i).getString("comment_message"),
                                    commentList.getJSONObject(i).getString("comment_added_datetime"),
                                    commentList.getJSONObject(i).getInt("user_id"),
                                    commentList.getJSONObject(i).getString("user_last_name"),
                                    commentList.getJSONObject(i).getString("user_first_name"),
                                    commentList.getJSONObject(i).getString("user_link_avatar")

                            ));
                            Log.e("comment", commentArrayList.get(i).toString());
                        }
                    }
                    mytrip.setAlpha(1);
                    //pictures.setImageBitmap(tripDAO.getTripList().);
                    /*tripName.setText(trip.getName());
                    tripCountry.setText(trip.getCountry());
                    tripDescription.setText(trip.getDescription());
                    tripAuthor.setText(Html.fromHtml("<font color=#000>par</font> <b>"+trip.getAuthor_last_name()+" "+trip.getAuthor_first_name()+" </b>"));
                    tripDate.setText(tripDate.getText()+" "+trip.getDate());
                    tripHome.setVisibility(View.VISIBLE);*/

                } else {

                    alertDialog = new AlertDialog.Builder(
                            MyTripActivity.this);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Une erreur s'est produite lors de la récupération du voyage");
                    alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                        tripDAO.updateCover(id,photoBase64);
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

}
