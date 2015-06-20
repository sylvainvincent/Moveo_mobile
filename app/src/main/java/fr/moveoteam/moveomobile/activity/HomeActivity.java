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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.dao.TripDAO;
import fr.moveoteam.moveomobile.fragment.AddTripFragment;
import fr.moveoteam.moveomobile.fragment.ExploreFragment;
import fr.moveoteam.moveomobile.fragment.FriendCategoryFragment;
import fr.moveoteam.moveomobile.fragment.MyTripListFragment;
import fr.moveoteam.moveomobile.menu.MenuAdapter;
import fr.moveoteam.moveomobile.menu.MenuItems;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;


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
    Fragment fragment = null;
    Fragment fragment2 = null;
    FriendCategoryFragment friendCategoryFragment;
    FragmentTransaction ft;

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

    AlertDialog.Builder alertDialog;

	// LE COMPTEUR (Nombre de message non lue, demande d'amis)
    String tripCounter = "0";
    String friendRequestCounter = "0";
    boolean friendRequestDisplay = false;
    String inboxRequestCounter = "0";
    boolean inboxRequestDisplay = false;
    String friendCounter = "0";

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

       // drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enabling action bar app icon and behaving it as toggle button

        /*try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){

        }*/
        //getActionBar().setIcon(R.drawable.ic_drawer);
        getActionBar().setHomeButtonEnabled(true); // Activer l'icône d'ouverture du menu
        getActionBar().setIcon(R.drawable.ic_drawer);
        userDAO = new UserDAO(HomeActivity.this);
        userDAO.open();
        Toolbar t = new Toolbar(getApplication());
        //t.setNavigationIcon(R.drawable.ic_drawer);


        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                t, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(userDAO.getUserDetails().getFirstName().toUpperCase()+" "+userDAO.getUserDetails().getLastName().toUpperCase());
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);

        drawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            // La premiere page par défaut lors du lancement de l'application est "Explorer"
            displayView(0);
        }

        friendCategoryFragment = new FriendCategoryFragment();

    }

    /**
     * Class appelé pour faire des actions selon l'item sélectionné
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
                //fragment = new ExploreFragment();
                Intent intent = new Intent(this,AccountSettingsActivity.class);
                startActivity(intent);
                break;
            case 3:
                fragment = friendCategoryFragment;
                ft.add(R.id.frame_container, fragment);
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
                intent = new Intent(HomeActivity.this, LoginActivity.class);
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
            Log.e("MainActivity", "Erreur lors de la création du fragment");
        }
    }

    /***
     * Appelé lorsque invalidateOptionsMenu() est déclenché
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Si le menu est ouvert, cacher certains items de la barre
        // boolean drawerOpen = drawerLayout.isDrawerOpen(listSliderMenu);
        // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
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
        // Sync the toggle state after onRestoreInstanceState has occurred.
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
        alertDialog.setMessage("Êtes vous sûr de vouloir l'application ?");
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
    public void initialization() {
        exploreTitle = (TextView) findViewById(R.id.explore_title);
        listView = (ListView) findViewById(R.id.listViewExploreTrip);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Récupération des informations d'une photo sélectionné dans l'album
            if (requestCode == 1) {

                // RECUPERATION DE L'ADRESSE DE LA PHOTO
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                // FIN DE LA RECUPERATION
                c.close();

                Bitmap thumbnail2 = (BitmapFactory.decodeFile(picturePath));
                Log.w("path de l'image", picturePath + "");
                // Remplir le champ en dessous de la photo avec le chemin de la nouvelle
                linkPhoto.setText(picturePath);

                // Stoker la photo en base64 dans une variable
                photoBase64 = Function.encodeBase64(thumbnail2);

                // Changer la photo actuel avec la nouvelle
                thumbnail.setImageBitmap(thumbnail2);
            }
        }
    }*/
	
	public void refreshFragment(){
        ft = getFragmentManager().beginTransaction();
        Log.e("HomeActivity",fragment.toString());
		ft.detach(fragment);
		ft.attach(fragment);
        ft.commit();
	}

}
