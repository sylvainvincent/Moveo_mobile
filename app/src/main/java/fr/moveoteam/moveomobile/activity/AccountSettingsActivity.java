package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.fragment.BirthdayFragment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.User;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Amélie on 12/05/2015.
 */
public class AccountSettingsActivity extends Activity {

    Button buttonModifyAccount;
    EditText modifyLastName;
    EditText modifyFirstName;
    EditText modifyEmail;
    EditText modifyBirthDate;
    EditText modifyCity;
    EditText modifyCountry;
    TextView dateEdit;
    TextView cancel;
    ImageButton birthdayButton;
    Toast toast;
    User user;
    private ImageView thumbnail;
    private TextView modifythumbnail;
    ExecuteThread executeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        this.initialization();
        this.eventButton();

        UserDAO userDAO = new UserDAO(AccountSettingsActivity.this);
        userDAO.open();
        user = userDAO.getUserDetails();
        userDAO.close();

        modifyLastName.setText(user.getLastName());
        modifyFirstName.setText(user.getFirstName());
        modifyEmail.setText(user.getEmail());
        if(user.getCity() != null) modifyCity.setText(user.getCity());
        if(user.getCountry() != null) modifyCountry.setText(user.getCountry());
        thumbnail.setImageBitmap(Function.decodeBase64(user.getAvatar()));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final BirthdayFragment birthdayFragment = new BirthdayFragment(AccountSettingsActivity.this,1,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }

                },2014,1,1);

                birthdayFragment.setButton(DatePickerDialog.BUTTON_POSITIVE,"VALIDER", new DialogInterface.OnClickListener() {
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
        modifyLastName = (EditText) findViewById(R.id.edit_last_name);
        modifyFirstName = (EditText) findViewById(R.id.edit_first_name);
        modifyEmail = (EditText) findViewById(R.id.edit_email);
        modifyCity = (EditText) findViewById(R.id.edit_city);
        modifyCountry = (EditText) findViewById(R.id.edit_country);
        dateEdit = (TextView) findViewById(R.id.edit_birthday);
        birthdayButton = (ImageButton) findViewById(R.id.birthday_button);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        modifythumbnail = (TextView) findViewById(R.id.modify_thumbnail);
        cancel = (TextView) findViewById(R.id.cancel_my_account);
    }

    // Procédure qui permet déclencher un évènement lorsque l'on clique sur un bouton
    public void eventButton() {
        buttonModifyAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (modifyLastName.getText().toString().equals("")){
                            modifyLastName.setError("Champ obligatoire");
                        } else if (modifyFirstName.getText().toString().equals("")) {
                            modifyFirstName.setError("Champ obligatoire");
                        } else if (!Function.isString(modifyLastName.getText().toString())){
                            modifyLastName.setError("Champ invalide");
                        } else if (!Function.isString(modifyFirstName.getText().toString())){
                            modifyFirstName.setError("Champ invalide");
                        } else if (!Function.isString(modifyCity.getText().toString())
                                    && !modifyCity.getText().toString().equals("")){
                            modifyCity.setError("Champ invalide");
                        } else if (!Function.isString(modifyCountry.getText().toString())
                                   && !modifyCity.getText().toString().equals("")){
                            modifyCountry.setError("Champ invalide");
                        } else {
                            executeThread = new ExecuteThread();
                            executeThread.execute();
                        }
                    }
                }
        );
    }

    public void onBackPressed() {
        this.executeThread.cancel(true);
    }



    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog pDialog = new ProgressDialog(AccountSettingsActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String lastName = modifyFirstName.getText().toString();
            String firstName = modifyFirstName.getText().toString();
            String avatar = "test";
            String birthday = dateEdit.toString();
            Log.e("dateEdit", birthday);
            String city = modifyCity.getText().toString();
            String country = modifyCountry.getText().toString();

            JSONUser jsonUser = new JSONUser();
            return jsonUser.modifyUser(Integer.toString(user.getId()), lastName, firstName, avatar, birthday, city, country);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
