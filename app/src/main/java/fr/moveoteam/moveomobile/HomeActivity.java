package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
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

import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.fragment.AddButtonTripFragment;
import fr.moveoteam.moveomobile.fragment.ExploreFragment;
import fr.moveoteam.moveomobile.fragment.TripFragment;
import fr.moveoteam.moveomobile.menu.MenuAdapter;
import fr.moveoteam.moveomobile.menu.MenuItems;
import fr.moveoteam.moveomobile.model.CustomListAdapter;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.webservice.JSONTrip;


/**
 * Created by alexMac on 07/04/15.
 */
public class HomeActivity extends Activity {

    private TextView exploreTitle;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ListView listSliderMenu;
    private ActionBarDrawerToggle mDrawerToggle;

    Fragment fragment = null;
    Fragment fragment2 = null;

    // Titre dans l'action bar
    private CharSequence mDrawerTitle;

    // Utiliser pour stocker le titre actuel
    private CharSequence mTitle;

    // Les elements du menu
    private String[] listMenuTitles; // NOMS DES ELEMENTS
    private TypedArray listMenuIcons; // ICÔNES DES ELEMENTS

    private ArrayList<MenuItems> listMenuItems;
    private MenuAdapter menuAdapter;

    UserDAO userDAO;

    Toast toast;

    String tripCounter = "0";
    String friendCounter = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // La vue par défaut contient un menu caché sur la gauche ainsi qu'un FrameLayout qui affiche les fragments
        setContentView(R.layout.menu);
        this.initialization();

        mTitle = mDrawerTitle = getTitle();

        // Récupérer le nom des éléments de la liste
        listMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // Récupérer les icônes du menu
       listMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliderMenu = (ListView) findViewById(R.id.list_slidermenu);

        listMenuItems = new ArrayList<>();

        TripDAO tripDAO = new TripDAO(HomeActivity.this);
        tripDAO.open();
        if(tripDAO.getTripList()!= null){
            tripCounter = Integer.toString(tripDAO.getTripList().size());
        }

        FriendDAO friendDAO = new FriendDAO(HomeActivity.this);
        friendDAO.open();
        if(friendDAO.getFriendList()!= null){
            friendCounter = Integer.toString(friendDAO.getFriendList().size());
        }

        // EXPLORER
        listMenuItems.add(new MenuItems(listMenuTitles[0], listMenuIcons.getResourceId(0, -1)));
        // MES VOYAGES
        listMenuItems.add(new MenuItems(listMenuTitles[1], listMenuIcons.getResourceId(1, -1),true,tripCounter));
        // MON PROFIL
        listMenuItems.add(new MenuItems(listMenuTitles[2], listMenuIcons.getResourceId(2, -1)));
        // MES AMIS
        listMenuItems.add(new MenuItems(listMenuTitles[3], listMenuIcons.getResourceId(3, -1), true, friendCounter));
        // MESSAGERIE
        listMenuItems.add(new MenuItems(listMenuTitles[4], listMenuIcons.getResourceId(4, -1), true, "3"));
        // PARAMÈTRE
        listMenuItems.add(new MenuItems(listMenuTitles[5], listMenuIcons.getResourceId(5, -1)));
        // A PROPOS
        listMenuItems.add(new MenuItems(listMenuTitles[6], listMenuIcons.getResourceId(6, -1)));
        // DÉCONNEXION
        listMenuItems.add(new MenuItems(listMenuTitles[7], listMenuIcons.getResourceId(7, -1)));

        listMenuIcons.recycle();
        Log.e("ListMenuItem "," "+listMenuItems.size());
        Log.e("ListMenuItem 1 ", " " + listMenuItems.get(0).getTitle());

        listSliderMenu.setOnItemClickListener(new SlideMenuClickListener());

        menuAdapter = new MenuAdapter(HomeActivity.this,
                listMenuItems);
        listSliderMenu.setAdapter(menuAdapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
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
        drawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            // displayView(0);
        }

        // La page par defaut est "Explorer"
        displayView(0);

    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // Afficher la vue correspondant à la position
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override // Gestion des évènements associés au menu
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
        // Mettre à jour le contenu principal par un nouveau fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(fragment2 != null)ft.remove(fragment2);
        if(fragment != null)ft.remove(fragment);
        switch (position) {
            case 0:
                fragment = new ExploreFragment();
                ft.add(R.id.frame_container, fragment);
                break;
            case 1:
                fragment = new TripFragment();
                fragment2 = new AddButtonTripFragment();
                ft.add(R.id.frame_container, fragment2);
                ft.add(R.id.frame_container, fragment);
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
            case 6:
                fragment = new ExploreFragment();
                break;
            case 7:
                userDAO = new UserDAO(HomeActivity.this);
                userDAO.logoutUser(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                fragment = new ExploreFragment();
                break;
        }

        if (fragment != null) {
         //   ft.remove(fragment);
         //   ft.add(R.id.frame_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            ft.addToBackStack(null);
            ft.commit();

            // update selected item and title, then close the drawer
            listSliderMenu.setItemChecked(position, true);
            listSliderMenu.setSelection(position);
            this.setTitle(listMenuTitles[position]);
            drawerLayout.closeDrawer(listSliderMenu);
        } else {
            // Erreur lors de la création du fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = drawerLayout.isDrawerOpen(listSliderMenu);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // Change le titre de l'actionBar
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

    @Override // Appelé lorsque l'appareil change de configuration
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Passer une configuration change le drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    public void initialization() {
        exploreTitle = (TextView) findViewById(R.id.explore_title);
        listView = (ListView) findViewById(R.id.listViewExploreTrip);
    }

}
