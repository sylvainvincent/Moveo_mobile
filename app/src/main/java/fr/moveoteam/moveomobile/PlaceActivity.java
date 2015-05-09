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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trip_places_list);
        this.initialization();
    }

    private void initialization() {


    }
}
