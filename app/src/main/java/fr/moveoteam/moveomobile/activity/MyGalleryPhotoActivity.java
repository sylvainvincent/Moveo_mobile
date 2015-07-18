package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.ImageAdapter;
import fr.moveoteam.moveomobile.dao.PhotoDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Photo;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 16/07/15.
 */
public class MyGalleryPhotoActivity extends Activity {

    private ImageView addPhoto;
    private TextView addPhotoText;
    private GridView gridViewGallery;
    private LinearLayout myGallery;
    int tripId;
    int photoIdSelected;
    String photoDateSelected;
    String photoSelected;

    // Element de la nouvelle vue d'apercu d'image
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_photo_gallery);
        initialize();
        tripId = getIntent().getIntExtra("tripId",0);
        final ArrayList<Photo> photoArrayList;
        PhotoDAO photoDAO = new PhotoDAO(this);
        photoDAO.open();
        photoArrayList = photoDAO.getPhotoList(tripId);
        photoDAO.close();
        if(photoArrayList != null){
            gridViewGallery.setAdapter(new ImageAdapter(this, photoArrayList));
            gridViewGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    photoIdSelected = photoArrayList.get(position).getId();
                    photoDateSelected = photoArrayList.get(position).getPublicationDate();
                    photoSelected = photoArrayList.get(position).getPhotoBase64();
                    openContextMenu(view);
                }
            });
            registerForContextMenu(gridViewGallery);
        }

        addPhotoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGalleryPhotoActivity.this,AddPhotoActivity.class);
                intent.putExtra("tripId",tripId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialize() {
        addPhoto = (ImageView) findViewById(R.id.add_photo);
        addPhotoText = (TextView) findViewById(R.id.add_photo_text);
        gridViewGallery = (GridView) findViewById(R.id.my_grid_gallery);
        myGallery = (LinearLayout) findViewById(R.id.my_gallery);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        menu.setHeaderTitle("Choisir une action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_delete_photo:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MyGalleryPhotoActivity.this);
                builder.setMessage("Êtes vous sûr de supprimer cette photo ?");
                builder.setNegativeButton("non", null);
                builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ExecuteDeletePhotoThread().execute();
                    }
                });

                builder.show();
                break;
            case R.id.option_show_photo:
                AlertDialog.Builder print= new AlertDialog.Builder(MyGalleryPhotoActivity.this);
                LayoutInflater factory = LayoutInflater.from(MyGalleryPhotoActivity.this);
                View photoView = factory.inflate(R.layout.photo, null);
                image = (ImageView) photoView.findViewById(R.id.photo);
                TextView photoDate = (TextView) photoView.findViewById(R.id.photo_publication_date);

                photoDate.setText(photoDate.getText()+" "+ Function.dateSqlToFullDateJava(photoDateSelected));
                new DownloadImage().execute(photoSelected);
                print.setView(photoView);
                //AlertDialog d = print.create();
                print.show();
        }

        return super.onContextItemSelected(item);
    }

    private class ExecuteDeletePhotoThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyGalleryPhotoActivity.this);
            pDialog.setMessage("Suppression en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.deletePhoto(Integer.toString(photoIdSelected));
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MyGalleryPhotoActivity.this);
                    builder.setMessage("Connexion perdue");
                    builder.setPositiveButton("OK", null);
                    builder.show();

                }else if (json.getString("success").equals("1")) {

                    PhotoDAO photoDAO = new PhotoDAO(MyGalleryPhotoActivity.this);
                    photoDAO.open();
                    photoDAO.removePhoto(photoIdSelected);
                    photoDAO.close();

                    startActivity(getIntent());
                    finish();

                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MyGalleryPhotoActivity.this);
                    builder.setMessage("Suppression du commentaire échoué");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                image.setImageResource(R.drawable.default_journey);
            } else {
                image.setImageBitmap(result);
            }
        }
    }


}
