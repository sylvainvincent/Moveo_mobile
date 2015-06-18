package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Photo;

/**
 * Classe permettant d'afficher en taille réel la photo sélectionner 
 * Created by Sylvain on 31/05/15.
 */
public class PhotoActivity extends Activity {
	// CLASSE TEMPORAIRE , A SUPPRIMER!!!!
    private ImageView photo;
    String photoBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        initialize();
        int id = getIntent().getExtras().getInt("id");
        ArrayList<Photo> photos = getIntent().getExtras().getParcelableArrayList("photoArrayList");
        photo.setImageBitmap(Function.decodeBase64(photos.get(id).getPhotoBase64()));
    }

    private void initialize() {

        photo = (ImageView) findViewById(R.id.photo);
    }
}
