package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by alexMac on 09/04/15.
 */
public class AddPlaceActivity extends Activity{

    private TextView exploretitle;
    private TextView category;
    private TextView place;
    private EditText editPlace;
    private TextView adress;
    private EditText editAdress;
    private TextView descriptionPlace;
    private EditText editDescriptionPlace;
    private Button buttonAddPlace;
    private TextView cancelAddPlace;
    private RelativeLayout addplace;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        this.initialization();
    }

    // Procedure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    private void initialization() {

        exploretitle = (TextView) findViewById(R.id.explore_title);
        category = (TextView) findViewById(R.id.add_place_category);
        place = (TextView) findViewById(R.id.place);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
        editPlace = (EditText) findViewById(R.id.editPlace);
        adress = (TextView) findViewById(R.id.adress);
        editAdress = (EditText) findViewById(R.id.edit_address);
        descriptionPlace = (TextView) findViewById(R.id.descriptionPlace);
        editDescriptionPlace = (EditText) findViewById(R.id.editDescriptionPlace);
        buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        cancelAddPlace = (TextView) findViewById(R.id.cancelAddPlace);
        addplace = (RelativeLayout) findViewById(R.id.add_place);
    }
}
