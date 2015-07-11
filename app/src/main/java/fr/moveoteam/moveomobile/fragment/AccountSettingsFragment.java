package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
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
public class AccountSettingsFragment extends Fragment {

	// ELEMENTS DE VUES
    Button buttonModifyAccount;

    EditText modifyLastName;
    EditText modifyFirstName;
    EditText modifyEmail;
    EditText modifyCity;
    EditText modifyCountry;
    EditText linkPhoto;
	EditText dateEdit;
	
	TextView modifythumbnail;
	TextView cancel;
	
	ImageView thumbnail;

	// Classe metier
	ImageButton birthdayButton;	
	User user;


    String photoBase64 = null;
    String oldLastName, oldFirstName, oldBirthday, oldCountry, oldCity;

    String userId = null;

    // Date de naissance par défaut
    int year = 2014;
    int month = 0; // janvier
    int day = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account,container,false);
        buttonModifyAccount = (Button) view.findViewById(R.id.button_account_settings);
        modifyLastName = (EditText) view.findViewById(R.id.edit_last_name);
        modifyFirstName = (EditText) view.findViewById(R.id.edit_first_name);
        modifyEmail = (EditText) view.findViewById(R.id.edit_email);
        modifyCity = (EditText) view.findViewById(R.id.edit_city);
        modifyCountry = (EditText) view.findViewById(R.id.edit_country);
        dateEdit = (EditText) view.findViewById(R.id.edit_birthday);
        birthdayButton = (ImageButton) view.findViewById(R.id.birthday_button);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        modifythumbnail = (TextView) view.findViewById(R.id.modify_thumbnail);
        cancel = (TextView) view.findViewById(R.id.cancel_my_account);
        linkPhoto = (EditText) view.findViewById(R.id.link_photo);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.eventButton();

        UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        user = userDAO.getUserDetails();
        userDAO.close();
        userId = Integer.toString(user.getId());
        photoBase64 = user.getAvatar();
        oldLastName = user.getLastName();
        oldFirstName = user.getFirstName();

        // Pour les 3 lignes suivantes on affecter une chaîne vide au variable contenant les informations de l'utilisateur
        // Si la valeur récupérer grâce aux getter est null alors on affecte une chaîne vide
        oldBirthday = user.getBirthday().equals("null") ? "" : Function.dateSqlToDateJava(user.getBirthday());
        oldCountry = user.getCountry().equals("null") ? "" : user.getCountry();
        oldCity = user.getCity().equals("null") ? "" : user.getCity();

        modifyLastName.setText(oldLastName);
        modifyFirstName.setText(oldFirstName);
        modifyEmail.setText(user.getEmail());
        dateEdit.setText(oldBirthday);
        modifyCity.setText(oldCity);
        modifyCountry.setText(oldCountry);

        thumbnail.setImageBitmap(Function.decodeBase64(photoBase64));

        // Afficher la photo de profil lors de la selection
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder print= new AlertDialog.Builder(getActivity());
                LayoutInflater factory = LayoutInflater.from(getActivity());
                View photoView = factory.inflate(R.layout.photo, null);
                ImageView image = (ImageView) photoView.findViewById(R.id.photo);
                TextView photoDate = (TextView) photoView.findViewById(R.id.photo_publication_date);

                // Recuperation du Bitmap de la photo de profil pour le mettre sur le la nouvelle vue
                image.setImageBitmap(((BitmapDrawable)thumbnail.getDrawable()).getBitmap());
                photoDate.setVisibility(View.GONE);
                print.setView(photoView);
                //AlertDialog d = print.create();
                print.show();
            }
        });


        // Bouton permettant de sélectionner la date de naissance
        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CREATION DE LA BOITE DE DIALOGUE qui étant DatePickerDialog
                // INITIALISER LA DATE AVEC CELUI DU EDITTEXT DE LA DATE DE NAISSANCE
                // SI L'EDITTEXT EST VIDE ALORS INITIALISATION A 01/01/2014
                final BirthdayFragment birthdayFragment = new BirthdayFragment(getActivity(),1,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }

                },year,month,day);

                // configuration du bouton "valider" qui permet de changer le contenu de l'edittext de la date de naissance avec la date sélectionner
                birthdayFragment.setButton(DatePickerDialog.BUTTON_POSITIVE,"VALIDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        day = birthdayFragment.getDatePicker().getDayOfMonth();
                        month = birthdayFragment.getDatePicker().getMonth();
                        year = birthdayFragment.getDatePicker().getYear();
                        dateEdit.setText(day+"/"+(month+ 1)+"/"+year);


                    }
                });

                // configuration du bouton "cancel" qui fermer la boite de dialog
                birthdayFragment.setButton(DatePickerDialog.BUTTON_NEUTRAL,"CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        birthdayFragment.dismiss();

                    }
                });

                // configuration du bouton "effacer" qui permet d'effacer le contenu de l'edittext de la date de naissance
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

    // Procédure qui permet déclencher un évènement lorsque l'on clique sur un bouton
    public void eventButton() {
        buttonModifyAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (modifyLastName.getText().toString().equals("")){
                            // Nom manquant
                            modifyLastName.setError("Champ obligatoire");
                        }
                        else if (modifyFirstName.getText().toString().equals("")) {
                            // Prénom manquant
                            modifyFirstName.setError("Champ obligatoire");
                        }
                        else if (!Function.isString(modifyLastName.getText().toString())){
                            // Nom avec des chiffres
                            modifyLastName.setError("Champ invalide");
                        }
                        else if (!Function.isString(modifyFirstName.getText().toString())){
                            // Prénom avec des chiffres
                            modifyFirstName.setError("Champ invalide");
                        }
                        else if (!Function.isString(modifyCity.getText().toString())
                                    && !modifyCity.getText().toString().equals("")){
                            // Ville avec des chiffres
                            modifyCity.setError("Champ invalide");
                        }
                        else if (!Function.isString(modifyCountry.getText().toString())
                                   && !modifyCountry.getText().toString().equals("")){
                            // Pays avec des chiffres
                            modifyCountry.setError("Champ invalide");
                        }
                        else {
                            // VERIFICATIONS DES CHAMPS MODIFIÉ
                            if(!modifyLastName.getText().toString().equals(oldLastName)
                            || !modifyFirstName.getText().toString().equals(oldFirstName)
                            || !dateEdit.getText().toString().equals(oldBirthday)
                            || !modifyCountry.getText().toString().equals(oldCountry)
                            || !modifyCity.getText().toString().equals(oldCity)
                            || !linkPhoto.getText().toString().equals("") ){
                                new ExecuteThread().execute();
                            }else{
                                Log.i("AccountSettingsActivity"," Pas de modif");
                            }

                        }
                    }
                }
        );

        modifythumbnail.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }

        });

    }


    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            user = new User();

                user.setLastName(modifyLastName.getText().toString());
                user.setFirstName(modifyFirstName.getText().toString());
                user.setBirthday(Function.dateJavaToDateSql(dateEdit.getText().toString())) ;
                user.setCountry(modifyCountry.getText().toString());
                user.setCity(modifyCity.getText().toString());
                user.setAvatar(photoBase64);

            JSONUser jsonUser = new JSONUser();
            return jsonUser.modifyUser(userId, user.getLastName(), user.getFirstName(), photoBase64, user.getBirthday(), user.getCity(), user.getCountry());
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            pDialog.dismiss();
            if(json == null){
                Log.e("test json","null");
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }else try {
                if (json.getString("success").equals("1")) {
                    Log.i("AccountSettingsActivity","update OK");
                    UserDAO userDAO = new UserDAO(getActivity());
                    userDAO.open();
                    userDAO.modifyUser(user);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setMessage("La modification a été réalisé avec succès");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setMessage("Une erreur est survenue lors de l'enregistrement de la photo");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public EditText getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(EditText linkPhoto) {
        this.linkPhoto = linkPhoto;
    }

    public ImageView getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Account","test");
        if (resultCode == Activity.RESULT_OK) {

            // Récupération des informations d'une photo sélectionné dans l'album
            if (requestCode == 1) {

                // RECUPERATION DE L'ADRESSE DE LA PHOTO
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);

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
    }
}
