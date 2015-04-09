package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.model.Trip;


/**
 * Created by alexMac on 07/04/15.
 */
public class ExploreActivity extends Activity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        listView = (ListView) findViewById(R.id.listViewExploreTrip);
        ArrayList<Trip>tripStory = getListData();
    }

    // méthode qui mettre les données dans un arrayList
    private ArrayList<Trip> getListData() {

        ArrayList<Trip> resultats = new ArrayList<Trip>();
// instancier un nouvel item de type Trip ==> il a 3 valeurs :Nom, Logo,Site
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
