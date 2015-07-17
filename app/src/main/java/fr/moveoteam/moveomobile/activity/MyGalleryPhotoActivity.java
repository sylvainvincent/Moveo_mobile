package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.ImageAdapter;
import fr.moveoteam.moveomobile.dao.PhotoDAO;
import fr.moveoteam.moveomobile.model.Photo;

/**
 * Created by Sylvain on 16/07/15.
 */
public class MyGalleryPhotoActivity extends Activity {

    private ImageView addPhoto;
    private TextView addPhotoText;
    private GridView gridViewGallery;
    private LinearLayout myGallery;
    int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_photo_gallery);
        initialize();
        tripId = getIntent().getIntExtra("tripId",0);
        ArrayList<Photo> photoArrayList;
        PhotoDAO photoDAO = new PhotoDAO(this);
        photoDAO.open();
        photoArrayList = photoDAO.getPhotoList(tripId);
        photoDAO.close();
        if(photoArrayList != null)
            gridViewGallery.setAdapter(new ImageAdapter(this, photoArrayList));

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
}
