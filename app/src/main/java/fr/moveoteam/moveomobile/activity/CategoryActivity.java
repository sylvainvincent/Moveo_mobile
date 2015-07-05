package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.PlacesListAdapter;
import fr.moveoteam.moveomobile.model.Place;

/**
 * Created by Sylvain on 05/07/15.
 */
public class CategoryActivity extends Activity {

    ListView listView;
    ArrayList<Place> placeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        listView = (ListView) findViewById(R.id.list_category);
        Place place = new Place();
        place.setName("A");
        place.setAddress("test");
        placeArrayList.add(place);
        listView.setAdapter(new PlacesListAdapter(this,placeArrayList));
    }
}
