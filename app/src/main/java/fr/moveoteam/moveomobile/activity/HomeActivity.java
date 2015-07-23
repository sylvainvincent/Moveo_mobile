package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.DataBaseHandler;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.fragment.AboutFragment;
import fr.moveoteam.moveomobile.fragment.AccountSettingsFragment;
import fr.moveoteam.moveomobile.fragment.AddTripFragment;
import fr.moveoteam.moveomobile.fragment.ExploreFragment;
import fr.moveoteam.moveomobile.fragment.FriendCategoryFragment;
import fr.moveoteam.moveomobile.fragment.MessagingFragment;
import fr.moveoteam.moveomobile.fragment.MyTripListFragment;
import fr.moveoteam.moveomobile.fragment.SettingsFragment;
import fr.moveoteam.moveomobile.menu.MenuAdapter;
import fr.moveoteam.moveomobile.menu.MenuItems;
import fr.moveoteam.moveomobile.dao.UserDAO;


/**
 * Created by alexMac on 07/04/15.
 */
public class HomeActivity extends Activity {
	
	// ELEMENTS DE VUE
    private TextView exploreTitle;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ListView listSliderMenu;
    private ActionBarDrawerToggle mDrawerToggle;

	// FRAGMENTS
    private Fragment fragment = null;
    private Fragment fragment2 = null;
    private FriendCategoryFragment friendCategoryFragment;
    private MessagingFragment messagingFragment;
    private FragmentTransaction ft;

    // Titre dans l'action bar
    private CharSequence mDrawerTitle;

    // Utiliser pour stocker le titre actuel
    private CharSequence mTitle;

    // Les elements du menu
    private String[] listMenuTitles; // NOMS DES ELEMENTS
    private TypedArray listMenuIcons; // ICÔNES DES ELEMENTS

    private ArrayList<MenuItems> listMenuItems;
    private MenuAdapter menuAdapter;

    private UserDAO userDAO;

    private AlertDialog.Builder alertDialog;

	// LE COMPTEUR (Nombre de message non lue, demande d'amis)
    private String tripCounter = "0";
    private String friendRequestCounter = "0";
    private boolean friendRequestDisplay = false;
    private String inboxRequestCounter = "0";
    private boolean inboxRequestDisplay = false;
    private String friendCounter = "0";

    Bundle bundle;

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
        if(friendDAO.getFriendRequestList()!= null){
            friendRequestCounter = Integer.toString(friendDAO.getFriendRequestList().size());
            friendRequestDisplay = true;
        }
        if(friendDAO.getFriendList() != null){
            friendCounter = Integer.toString(friendDAO.getFriendList().size());
        }

        DialogDAO dialogDAO = new DialogDAO(HomeActivity.this);
        dialogDAO.open();
        if(dialogDAO.getInboxList()!= null){
            inboxRequestCounter = Integer.toString(dialogDAO.getInboxList().size());
            inboxRequestDisplay = true;
        }

        // EXPLORER
        listMenuItems.add(new MenuItems(listMenuTitles[0], listMenuIcons.getResourceId(0, -1)));
        // MES VOYAGES
        listMenuItems.add(new MenuItems(listMenuTitles[1], listMenuIcons.getResourceId(1, -1),true,tripCounter));
        // MON PROFIL
        listMenuItems.add(new MenuItems(listMenuTitles[2], listMenuIcons.getResourceId(2, -1)));
        // MES AMIS
        listMenuItems.add(new MenuItems(listMenuTitles[3], listMenuIcons.getResourceId(3, -1), friendRequestDisplay, friendRequestCounter));
        // MESSAGERIE
        listMenuItems.add(new MenuItems(listMenuTitles[4], listMenuIcons.getResourceId(4, -1), inboxRequestDisplay, inboxRequestCounter));
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

        getActionBar().setHomeButtonEnabled(true); // Activer l'icône d'ouverture du menu
        getActionBar().setIcon(R.drawable.ic_drawer);
        userDAO = new UserDAO(HomeActivity.this);
        userDAO.open();
        Toolbar t = new Toolbar(getApplication());

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                t, // ToolBar
                R.string.app_name, // Description à l'ouverture du DrawerToggle
                R.string.app_name // Description à la fermeture du DrawerToggle
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // Appel onPrepareOptionsMenu() pour afficher les icônes de l'action bar
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // Affiche le nom et prenom de l'utilisateur
                getActionBar().setTitle(Character.toUpperCase(userDAO.getUserDetails().getFirstName().charAt(0)) + userDAO.getUserDetails().getFirstName().substring(1, userDAO.getUserDetails().getFirstName().length()) + " " +
                                        Character.toUpperCase(userDAO.getUserDetails().getLastName().charAt(0)) + userDAO.getUserDetails().getLastName().substring(1, userDAO.getUserDetails().getLastName().length()));
                // Appel onPrepareOptionsMenu() pour caher les icônes de l'action bar
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);

        drawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            // La premiere page par défaut lors du lancement de l'application est "Explorer"
            if(fragment == null){
                displayView(0);
            }else{
                displayView(0);
            }
        }else{
            displayView(0);
        }

        friendCategoryFragment = new FriendCategoryFragment();
        messagingFragment = new MessagingFragment();
    }

    /**
     * Class appelé pour faire des actions selon l'item sélectionné
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Lors de la selection d'un element du menu
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_refresh:
                Intent intent2 = new Intent(this,DashboardActivity.class);
                finish();
                startActivity(intent2);
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    private void displayView(int position) {
        // Mettre à jour le contenu principal par un nouveau fragment
        FragmentManager fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        if(fragment2 != null)ft.remove(fragment2);
        if(fragment != null)ft.remove(fragment);
        switch (position) {
            case 0:
                fragment = new ExploreFragment();
                ft.add(R.id.frame_container, fragment);
                break;
            case 1:
                fragment = new MyTripListFragment();
                fragment2 = new AddTripFragment();
                ft.add(R.id.frame_container, fragment2);
                ft.add(R.id.frame_container, fragment);
                break;
            case 2:
                fragment = new AccountSettingsFragment();
                ft.add(R.id.frame_container, fragment);
                break;
            case 3:
                fragment = friendCategoryFragment;
                ft.add(R.id.frame_container, fragment);
                break;
            case 4:
                fragment = messagingFragment;
                ft.add(R.id.frame_container, fragment);
                break;
            case 5:
                fragment = new SettingsFragment();
                ft.add(R.id.frame_container, fragment);
                break;
            case 6:
                fragment = new AboutFragment();
                ft.add(R.id.frame_container, fragment);
                break;
            case 7:
                userDAO = new UserDAO(HomeActivity.this);
                userDAO.logoutUser(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                fragment = new ExploreFragment();
                ft.add(R.id.frame_container, fragment);
                break;
        }

        if (fragment != null) {
            ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            ft.addToBackStack(null);
            ft.commit();

            // Mettre à jour le titre et fermer le menu
            listSliderMenu.setItemChecked(position, true);
            listSliderMenu.setSelection(position);
            this.setTitle(listMenuTitles[position]);
            drawerLayout.closeDrawer(listSliderMenu);
        } else {
            // Erreur lors de la création du fragment
            Log.e("MainActivity", "Erreur lors de la création du fragment");
        }
    }

    /***
     * Appelé lorsque invalidateOptionsMenu() est déclenché
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override // Change le titre de l'actionBar
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * Pendant l'utilisation de l'ActionBarDrawerToggle, ce procedure est appellé lors du
     * onPostCreate() et du onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override // Appelé lorsque l'appareil change de configuration
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Passer une configuration change le drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override // Fermer l'application lorsque l'on appuie sur le bouton "back"
    public void onBackPressed() {
        alertDialog = new AlertDialog.Builder(
                HomeActivity.this);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Êtes vous sûr de vouloir quitter l'application ?");
        alertDialog.setPositiveButton("oui",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton("non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    // Procedure qui permet d'affecter les éléments de l'interface graphique aux objets de la classe
    void initialization() {
        exploreTitle = (TextView) findViewById(R.id.explore_title);
        listView = (ListView) findViewById(R.id.listViewExploreTrip);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

                this.refreshFragment();

        }

    }
	
	public void refreshFragment(){
        ft = getFragmentManager().beginTransaction();
        Log.e("HomeActivity",fragment.toString());
		ft.detach(fragment);
		ft.attach(fragment);
        ft.commit();
	}

    public void linkToInbox(View view) {
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        messagingFragment = ((MessagingFragment) fragment);
        messagingFragment.setRecieveIcon();
        ft.replace(R.id.messaging_content, messagingFragment.getInboxListFragment());
        ft.commit();
    }

    public void linkToSendbox(View view) {
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        messagingFragment = ((MessagingFragment) fragment);
        messagingFragment.setSendIcon();
        ft.replace(R.id.messaging_content,messagingFragment.getSendboxListFragment());
        ft.commit();
    }

}
