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
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


}
