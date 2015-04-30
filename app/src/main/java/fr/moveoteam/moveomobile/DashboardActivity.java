package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 16/04/15.
 * Le Dashboard est la classe principal, elle va re-diriger l'utilisateur selon son statut de connection
 */
public class DashboardActivity extends Activity {

    private Menu m = null;
	UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!Function.beConnectedToTheInternet(DashboardActivity.this)) {
            Toast.makeText(DashboardActivity.this,"Vous n'êtes pas connecté sur internet",Toast.LENGTH_LONG).show();
        }

        UserDAO userDAO = new UserDAO(DashboardActivity.this);
        userDAO.open();
        // VERIFIER LE STATUT DU LOGIN DANS LA BASE DE DONNÉES
        if (userDAO.getRowCount()) {
            Intent explore = new Intent(getApplicationContext(), ExploreActivity.class);
            explore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(explore);
        } else {// Si l'utilisateur n'a pas de session d'ouverte il est renvoyé sur la page Login
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Fermer le dashboard
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.m = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch(item.getItemId())
        {
            case R.id.action_logout:
                Log.e("Test", "a");
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                // FERMER L'ÉCRAN DASHBOARD
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override // Fermer l'application lorsque l'on appuie sur le bouton "back"
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }
}
