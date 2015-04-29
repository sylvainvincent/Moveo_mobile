package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Amélie on 27/04/2015.
 */
public class UserProfile extends Activity {

    ListView listViewUserTrips;
    String[] trips = {"Reykjavik", "Londres", "Perth"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

    }

}
