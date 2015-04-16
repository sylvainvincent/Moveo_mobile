package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.moveoteam.moveomobile.LoginActivity;
import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.webservice.UserFunctions;

/**
 * Created by Sylvain on 16/04/15.
 */
public class DashboardActivity extends Activity {

    private Menu m = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // VERIFIER LE STATUT DU LOGIN DANS LA BASE DE DONNÉES
        if(UserFunctions.isUserLoggedIn(getApplicationContext())) {
            Intent explore = new Intent(getApplicationContext(), ExploreActivity.class);
            explore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(explore);
        }else {// L'utilisateur n'est pas connecter
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // FERMER L'ECRAN DASHBOARD
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        m = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_logout:
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                // FERMER L'ECRAN DASHBOARD
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
