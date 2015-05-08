package fr.moveoteam.moveomobile;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by alexMac on 15/04/15.
 */
public class PlaceActivity extends Activity{

    private LinearLayout cover;
    private ImageButton homeicon;
    private LinearLayout categoriesmenu;
    private TextView city;
    private TextView country;
    private TextView username;
    private TextView placetitle;
    private TextView adress;
    private TextView placedescription;
    private LinearLayout placeelements;
    private RelativeLayout explore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trip_places_list);
        this.initialization();
    }

    private void initialization() {

        cover = (LinearLayout) findViewById(R.id.cover);
        homeicon = (ImageButton) findViewById(R.id.home_icon);
        categoriesmenu = (LinearLayout) findViewById(R.id.categories_menu);
        city = (TextView) findViewById(R.id.city);
        country = (TextView) findViewById(R.id.country);
        username = (TextView) findViewById(R.id.username);
        placetitle = (TextView) findViewById(R.id.place_title);
        adress = (TextView) findViewById(R.id.adress);
        placedescription = (TextView) findViewById(R.id.place_description);
        placeelements = (LinearLayout) findViewById(R.id.place_elements);
        explore = (RelativeLayout) findViewById(R.id.explore);
    }
}
