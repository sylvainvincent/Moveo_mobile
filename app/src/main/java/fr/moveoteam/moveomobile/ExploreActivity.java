package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.menu.MenuAdapter;
import fr.moveoteam.moveomobile.menu.MenuItems;
import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.webservice.JSONTrip;


/**
 * Created by alexMac on 07/04/15.
 */
public class ExploreActivity extends Activity {

    private TextView exploreTitle;
    private ListView listView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<MenuItems> listMenuItems;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        this.initialization();


        mTitle = mDrawerTitle = getTitle();

        // Récupérer le nom des éléments de la liste
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        listMenuItems = new ArrayList<>();

        listMenuItems.add(new MenuItems(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        listMenuItems.add(new MenuItems(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        listMenuItems.add(new MenuItems(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        listMenuItems.add(new MenuItems(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        listMenuItems.add(new MenuItems(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        listMenuItems.add(new MenuItems(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

        navMenuIcons.recycle();
        Log.e("ListMenuItem "," "+listMenuItems.size());
        Log.e("ListMenuItem 1 "," "+listMenuItems.get(0).getTitle());

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        menuAdapter = new MenuAdapter(ExploreActivity.this,
                listMenuItems);
        mDrawerList.setAdapter(menuAdapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                null, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
           // displayView(0);
        }
        /*
        UserDAO userDAO = new UserDAO(ExploreActivity.this);
        userDAO.open();
        exploreTitle = (TextView) findViewById(R.id.)
        exploreTitle.setText(userDAO.getUserDetails().getFirstName()+" "+userDAO.getUserDetails().getLastName());

        new ExecuteThread().execute();

        ArrayList<Trip> tripStory = getListData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExploreActivity.this, Place.class);
                startActivity(intent);
            }
        });
        */
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ExploreFragment();
                break;
            case 1:
                fragment = new ExploreFragment();
                break;
            case 2:
                fragment = new ExploreFragment();
                break;
            case 3:
                fragment = new ExploreFragment();
                break;
            case 4:
                fragment = new ExploreFragment();
                break;
            case 5:
                fragment = new ExploreFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    public void initialization() {
        exploreTitle = (TextView) findViewById(R.id.explore_title);
        listView = (ListView) findViewById(R.id.listViewExploreTrip);
    }

    // Méthode qui met les données dans une arrayList
    private ArrayList<Trip> getListData() {
        //TODO récuperer les information via le webService
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

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ExploreActivity.this);
            pDialog.setMessage("Récupération des voyages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getExploreTrips();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {

                JSONArray tripList = json.getJSONArray("trip");
                ArrayList<Trip> tripArrayList = new ArrayList<>(10);
                if(json.getString("success").equals("1")) {
                        UserDAO userDAO = new UserDAO(ExploreActivity.this);

                        for(int i=0;i<6;i++) {
                            tripArrayList.add(new Trip(
                                    tripList.getJSONObject(i).getInt("trip_id"),
                                    tripList.getJSONObject(i).getString("trip_name"),
                                    tripList.getJSONObject(i).getString("trip_country"),
                                    tripList.getJSONObject(i).getString("trip_description"),
                                    tripList.getJSONObject(i).getString("trip_created_at"),
                                    tripList.getJSONObject(i).getString("user_last_name"),
                                    tripList.getJSONObject(i).getString("user_first_name"),
                                    tripList.getJSONObject(i).getInt("comment_count"),
                                    tripList.getJSONObject(i).getInt("photo_count")
                            ));
                        }
                        listView.setAdapter(new CustomListAdapter(ExploreActivity.this, tripArrayList));
                        Log.e("Message ",""+tripArrayList.get(0).getName()+""+tripArrayList.get(0).getName());
                    Log.e("Date ",""+tripList.getJSONObject(0).getString("trip_created_at")+" java : "+tripArrayList.get(0).getDateInsert());

                } else
                    Toast.makeText(ExploreActivity.this, "La récupération des voyages a échoué",
                            Toast.LENGTH_LONG).show();
                } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            // Storing  JSON item in a Variable
                // String msg = (String) c.getString(msg);
                //Set JSON Data in TextView

        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */



}
