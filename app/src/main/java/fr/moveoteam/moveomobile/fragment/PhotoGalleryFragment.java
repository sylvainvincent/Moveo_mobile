package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.ImageAdapter;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Photo;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Catégorie galerie photo d'un voyage avec vue en gridView
 * Created by Sylvain on 31/05/15.
 */
public class PhotoGalleryFragment  extends Fragment{

    private GridView gridview;
    private ArrayList<Photo> photoArrayList;
    private int tripId;
    ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_gallery, container, false);
        gridview = (GridView) view.findViewById(R.id.gridView_gallery);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tripId = getArguments().getInt("tripId");
        ExecuteThread executeThread = new ExecuteThread();
        executeThread.execute();

    }


    // Classe qui permet de réaliser des tâches de manière asynchrone
    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Recuperation des photos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getPhotoGallery(Integer.toString(tripId));
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                if (json.getString("success").equals("1")) {
                    final JSONArray photoList = json.getJSONArray("photo");
                    photoArrayList = new ArrayList<>(photoList.length());

                    for (int i = 0; i < photoList.length(); i++){
                        photoArrayList.add(new Photo(
                                Integer.parseInt(photoList.getJSONObject(i).getString("photo_id")),
                                photoList.getJSONObject(i).getString("photo_link"),
                                photoList.getJSONObject(i).getString("photo_added_date"),
                                tripId
                        ));
                    }

                    gridview.setAdapter(new ImageAdapter(getActivity(),photoArrayList));

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            AlertDialog.Builder print= new AlertDialog.Builder(getActivity());
                            LayoutInflater factory = LayoutInflater.from(getActivity());
                            View photoView = factory.inflate(R.layout.photo, null);
                            image = (ImageView) photoView.findViewById(R.id.photo);
                            TextView photoDate = (TextView) photoView.findViewById(R.id.photo_publication_date);

                            photoDate.setText(photoDate.getText()+" "+photoArrayList.get(position).getPublicationDate());
                            new DownloadImage().execute(photoArrayList.get(position).getPhotoBase64());
                            print.setView(photoView);
                            //AlertDialog d = print.create();
                            print.show();
                        }
                    });
                } else {
                    /*
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("La galerie d'images est vide");
                    alertDialog.show();
                    */
                    gridview.setEmptyView(null);
                    gridview.setAdapter(null);
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
