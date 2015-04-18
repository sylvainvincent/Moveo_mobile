package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.model.TripDataSource;
import fr.moveoteam.moveomobile.model.UserDataSource;


/**
 * Created by alexMac on 07/04/15.
 */
public class ExploreActivity extends Activity {

    private TextView exploreTitle;
    private UserDataSource userDataSource;
    private TripDataSource tripDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        userDataSource = new UserDataSource(ExploreActivity.this);
        exploreTitle = (TextView) findViewById(R.id.explore_title);
        exploreTitle.setText("Test Bonjour Mr "+userDataSource.getUserDetails().getFirstName());

        ListView listView = (ListView) findViewById(R.id.listViewExploreTrip);
        ArrayList<Trip> tripStory = getListData();
        listView.setAdapter(new CustomListAdapter(this, tripStory));
    }

    //On charge le menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Méthode qui met les données dans une arrayList
    private ArrayList<Trip> getListData() {

        ArrayList<Trip> resultats = new ArrayList<>();
        // Instancie un nouvel item de type Trip
        // ==> Il a 3 valeurs : Nom, Logo et Site
        Trip newsData = new Trip();
        newsData.setName("LAC DE COME");
        newsData.setCountry("ITALIE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("RIO DE JANEIRO");
        newsData.setCountry("BRESIL");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("BERNE");
        newsData.setCountry("SUISSE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("NEW YORK");
        newsData.setCountry("USA");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("TOKYO");
        newsData.setCountry("JAPON");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData = new Trip();
        newsData.setName("BANGKOK");
        newsData.setCountry("THAILANDE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);
        return resultats;
    }


}
