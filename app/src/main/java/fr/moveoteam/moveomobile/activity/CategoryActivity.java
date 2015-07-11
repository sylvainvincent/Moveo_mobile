package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.PlacesListAdapter;
import fr.moveoteam.moveomobile.dao.PlaceDAO;
import fr.moveoteam.moveomobile.model.Place;

/**
 * Created by Sylvain on 05/07/15.
 */
public class CategoryActivity extends Activity {

    ListView listView;
    ArrayList<Place> placeArrayList;
    int tripId;
    int categoryId;
    TextView addPlaceLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_button_add_place);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(null);
        getActionBar().setDisplayShowTitleEnabled(false);


        tripId = getIntent().getIntExtra("tripId",0);
        categoryId = getIntent().getIntExtra("categoryId",0);

        if(categoryId == 1) setTitle("Gastronomie");
        if(categoryId == 2) setTitle("Shopping");
        if(categoryId == 3) setTitle("Loisir");

        listView = (ListView) findViewById(R.id.list_category);
        addPlaceLink = (TextView) findViewById(R.id.add_place_text);

        PlaceDAO placeDAO = new PlaceDAO(CategoryActivity.this);
        placeDAO.open();
        placeArrayList = placeDAO.getPlaceListByCategory(tripId, categoryId);
        int test = placeDAO.getRowCount();
        placeDAO.close();
        Log.e("CategoryActivity", "Trip : "+tripId+" Categorie : "+categoryId);
        if(placeArrayList != null){
            listView.setAdapter(new PlacesListAdapter(this,placeArrayList));
        }else{
            listView.setAdapter(null);
            Log.e("CategoryActivity", "null");
        }

        addPlaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddPlaceActivity.class);
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("tripId",tripId);
                startActivityForResult(intent,1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, MyPlaceActivity.class);
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("placeId",placeArrayList.get(position).getId());
                startActivityForResult(intent,1);
            }
        });
    }// </onCreate>

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Category", "onActivity");

        if (resultCode == RESULT_OK) {

                startActivity(getIntent());
                finish();

        }
            /*
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
                Log.w("path de l'image", picturePath + "");
                // Remplir le champ en dessous de la photo avec le chemin de la nouvelle
                linkPhoto.setText(picturePath);

                // Stoker la photo en base64 dans une variable
                photoBase64 = Function.encodeBase64(thumbnail2);

                // Changer la photo actuel avec la nouvelle
                thumbnail.setImageBitmap(thumbnail2);
            }
        }*/
    }


}
