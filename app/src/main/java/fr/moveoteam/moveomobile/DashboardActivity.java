package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import fr.moveoteam.moveomobile.dao.DataBaseHandler;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;

/**
 * Created by Sylvain on 16/04/15.
 * Le Dashboard est la classe principal, elle va re-diriger l'utilisateur selon son statut de connection
 */
public class DashboardActivity extends Activity {

    private Menu m = null;
	UserDAO userDAO;
    Boolean internet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Création de la classe UserDAO pour manipuler la table user de ma BDD
        userDAO = new UserDAO(DashboardActivity.this);
        //On ouvre la base de données pour écrire dedans
        userDAO.open();

        // VERIFIER LE STATUT DU LOGIN DANS LA BASE DE DONNÉES
        if (userDAO.getRowCount()) {
            /** ---------------- TEST
             TripDAO tripDAO = new TripDAO(DashboardActivity.this);
             tripDAO.open();
             Log.e("Test du trip 1:",tripDAO.getTripList().get(0).getName());
             Log.e("Test du trip 1:",tripDAO.getTripList().get(0).getCountry());
             Log.e("Test du trip 2:",tripDAO.getTripList().get(1).getName());
             Log.e("Test du trip 2:",tripDAO.getTripList().get(1).getCountry());
             */
            // VERIFICATION DE LA CONNEXION INTERNET
            if(!Function.beConnectedToInternet(DashboardActivity.this))
                buildDialog(DashboardActivity.this).show();
            else {
                Intent explore = new Intent(getApplicationContext(), HomeActivity.class);
                explore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(explore);
                finish();
            }

            userDAO.close();

        } else {// Si l'utilisateur n'a pas de session d'ouverte il est renvoyé sur la page Login
            DataBaseHandler db = new DataBaseHandler(DashboardActivity.this);
            db.resetTables();
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Fermer le dashboard
            finish();
        }

    }

    // Boite de dialogue d'alerte activé si internet n'est pas activé
    public AlertDialog.Builder buildDialog(Context context) {
        internet = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                System.exit(0);
            }
        });
        builder.setMessage("un accès Internet est requis, Vérifier votre connexion Internet puis réessayez");
        builder.setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Function.beConnectedToInternet(DashboardActivity.this)) {
                    dialog.dismiss();
                    buildDialog(DashboardActivity.this).show();
                }else{
                    Intent explore = new Intent(getApplicationContext(), HomeActivity.class);
                    explore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(explore);
                    finish();
                }
            }
        });

        return builder;
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
        return super.onOptionsItemSelected(item);
    }

}
