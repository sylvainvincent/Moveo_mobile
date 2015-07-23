package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fr.moveoteam.moveomobile.model.Photo;

/**
 *
 * Created by Sylvain on 31/05/15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Photo> photoArrayList;
    ImageView imageView;

    public ImageAdapter(Context c, ArrayList<Photo> photoArrayList) {
        this.context = c;
        this.photoArrayList = photoArrayList;
    }

    public int getCount() {
        return photoArrayList.size();
    }

    public Object getItem(int position) {
        return photoArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageBitmap(Function.decodeBase64(tripArrayList.get(position).getPhotoBase64()));
        try {
            imageView.setImageBitmap(new DownloadImage().execute(photoArrayList.get(position).getPhotoBase64()).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return imageView;
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
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://moveo.besaba.com/"+url);
            HttpResponse response;
            try {
                    response = (HttpResponse)client.execute(request);
                    HttpEntity entity = response.getEntity();
                    BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
                    InputStream inputStream = bufferedEntity.getContent();
                    bitmap  = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /*@Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                imageView.setImageResource(R.drawable.default_journey);
            } else {
                imageView.setImageBitmap(result);
            }
        }*/
    }

}