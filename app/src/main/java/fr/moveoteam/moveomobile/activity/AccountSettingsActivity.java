package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.fragment.BirthdayFragment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Amélie on 12/05/2015.
 */
public class AccountSettingsActivity extends Activity {

    Button buttonModifyAccount;
    EditText modifyName;
    EditText modifyFirstname;
    EditText modifyEmail;
    EditText modifyBirthDate;
    EditText modifyCity;
    EditText modifyBirthPlace;
    TextView dateEdit;
    ImageButton birthdayButton;
    Toast toast;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        this.initialization();
        this.eventButton();

        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final BirthdayFragment birthdayFragment = new BirthdayFragment(AccountSettingsActivity.this,1,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }

                },2014,3,1);

                birthdayFragment.setButton(DatePickerDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateEdit.setText(birthdayFragment.getDatePicker().getDayOfMonth()+"/"
                                +birthdayFragment.getDatePicker().getMonth()+"/"+
                                +birthdayFragment.getDatePicker().getYear());
                    }
                });

                birthdayFragment.setButton(DatePickerDialog.BUTTON_NEGATIVE,"EFFACER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateEdit.setText("");
                    }
                });
                birthdayFragment.show();
            }
        });

    }

    // Procédure qui permet d'affecter les elements de l'interface graphique aux objets de la classe
    public void initialization() {
        buttonModifyAccount = (Button) findViewById(R.id.button_account_settings);
        modifyName = (EditText) findViewById(R.id.edit_name);
        modifyFirstname = (EditText) findViewById(R.id.edit_firstname);
        modifyEmail = (EditText) findViewById(R.id.edit_email);
        modifyCity = (EditText) findViewById(R.id.edit_city);
        modifyBirthPlace = (EditText) findViewById(R.id.edit_birthplace);
        dateEdit = (TextView) findViewById(R.id.edit_birthday);
        birthdayButton = (ImageButton) findViewById(R.id.birthday_button);
    }

    // Procédure qui permet déclencher un évènement lorsque l'on clique sur un bouton
    public void eventButton() {
        buttonModifyAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(modifyName.getText().toString().equals("") ||
                           modifyFirstname.getText().toString().equals("") ||
                           modifyEmail.getText().toString().equals("") ||
                           modifyBirthDate.getText().toString().equals("") ||
                           modifyCity.getText().toString().equals("") ||
                           modifyBirthPlace.getText().toString().equals("")) {
                                toast = Toast.makeText(AccountSettingsActivity.this, "Tous les champs sont obligatoires.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM,0,15);
                                toast.show();
                        } else if(!Function.isEmailAddress(modifyEmail.getText().toString())) {
                                toast = Toast.makeText(AccountSettingsActivity.this, "L'adresse email n'est pas valide.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM,0,15);
                                toast.show();
                        } else if (!(Function.isString(modifyName.getText().toString())
                                    && Function.isString(modifyFirstname.getText().toString())
                                    && Function.isString(modifyCity.getText().toString())
                                    && Function.isString(modifyBirthPlace.getText().toString()))) {
                                toast = Toast.makeText(AccountSettingsActivity.this, "Certains champs ne doivent contenir que des lettres.",
                                    Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM,0,15);
                                toast.show();
                        } else {
                            new ExecuteThread().execute();
                        }
                    }
                }
        );
    }



    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String name = modifyName.getText().toString();
            String firstname = modifyFirstname.getText().toString();
            String email = modifyEmail.getText().toString();
            String date = dateEdit.toString();
            Log.e("dateEdit", date);
            String city = modifyCity.getText().toString();
            String birthplace = modifyBirthPlace.getText().toString();

            JSONUser jsonUser = new JSONUser();
            return jsonUser.modifyUser(name, firstname, email, date, city, birthplace);
        }
    }
}
