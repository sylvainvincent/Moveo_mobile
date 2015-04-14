package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;


/**
 * Created by alexMac on 07/04/15.
 */
public class ExploreActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);

        listView = (ListView) findViewById(R.id.listViewExploreTrip);
        ArrayList<Trip> tripStory = getListData();
        /*
        ArrayList<HashMap<String, String>> tripStory = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        map = new HashMap<String, String>();
        map.put("explore_trip_name", "voyage1");
        map.put("explore_country", "country1");
        map.put("explore_username", "jeanjacques");
        map.put("imageViewMainPictureTrip",String.valueOf(R.drawable.journey));
        tripStory.add(map);

        map = new HashMap<String, String>();
        map.put("explore_trip_name", "voyage2");
        map.put("explore_country", "country2");
        map.put("explore_username", "thierry");
        map.put("imageViewMainPictureTrip",String.valueOf(R.drawable.journey));
        tripStory.add(map);

        map = new HashMap<String, String>();
        map.put("explore_trip_name", "voyage3");
        map.put("explore_country", "country3");
        map.put("explore_username", "paul");
        map.put("imageViewMainPictureTrip",String.valueOf(R.drawable.journey));
        tripStory.add(map);

        map = new HashMap<String, String>();
        map.put("explore_trip_name", "voyage4");
        map.put("explore_country", "country4");
        map.put("explore_username", "pierre");
        map.put("imageViewMainPictureTrip",String.valueOf(R.drawable.journey));
        tripStory.add(map);
*/
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

        ArrayList<Trip> resultats = new ArrayList<Trip>();
        // Instancie un nouvel item de type Trip
        // ==> Il a 3 valeurs : Nom, Logo et Site
        Trip newsData = new Trip();
        newsData.setName("LAC DE COME");
        newsData.setCountry("ITALIE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData.setName("RIO DE JANEIRO");
        newsData.setCountry("BRESIL");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData.setName("BERNE");
        newsData.setCountry("SUISSE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData.setName("NEW YORK");
        newsData.setCountry("USA");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData.setName("TOKYO");
        newsData.setCountry("JAPON");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);

        newsData.setName("BANGKOK");
        newsData.setCountry("THAILANDE");
        newsData.setMainPicture(getResources().getDrawable(R.drawable.journey));
        resultats.add(newsData);
        return resultats;
    }
}
