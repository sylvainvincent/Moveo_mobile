package fr.moveoteam.moveomobile.activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;

/**
 * Created by alexMac on 15/04/15.
 */
public class PlaceActivity extends Activity{

    private TextView tripplacecity;
    private TextView tripplacecountry;
    private TextView tripplaceusername;
    private TextView tripplacetitle;
    private TextView tripplaceadress;
    private TextView tripplacedescription;
    private ImageView place_pictures_category;

    private LinearLayout tripplace;

    String placeName;
    String placeAddress;
    String placeDescription;
    private ScrollView placescrollview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);

        initialize();
        placeName = getIntent().getExtras().getString("placeName");
        placeAddress = getIntent().getExtras().getString("placeAddress");
        placeDescription = getIntent().getExtras().getString("placeDescription");
        tripplacetitle.setText(placeName);
        tripplaceadress.setText(placeAddress);
        tripplacedescription.setText(placeDescription);
        tripplaceusername.setVisibility(View.GONE);
        tripplacecountry.setVisibility(View.GONE);
        place_pictures_category.setVisibility(View.GONE);
        placescrollview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.e("test","ok");
            }
        });
        tripplace.setClickable(false);
        placescrollview.setClickable(true);
    }


    private void initialize() {

        tripplacecity = (TextView) findViewById(R.id.trip_place_city);
        tripplacecountry = (TextView) findViewById(R.id.trip_place_country);
        tripplaceusername = (TextView) findViewById(R.id.trip_place_username);
        tripplacetitle = (TextView) findViewById(R.id.trip_place_title);
        tripplaceadress = (TextView) findViewById(R.id.trip_place_adress);
        tripplacedescription = (TextView) findViewById(R.id.trip_place_description);

        tripplace = (LinearLayout) findViewById(R.id.trip_place);

        place_pictures_category = (ImageView) findViewById(R.id.place_pictures_category);
        placescrollview = (ScrollView) findViewById(R.id.place_scroll_view);

    }

}
