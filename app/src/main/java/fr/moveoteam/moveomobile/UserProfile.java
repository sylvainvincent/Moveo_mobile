package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
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
        setContentView(R.layout.fragment_user_profile);

    }

}
