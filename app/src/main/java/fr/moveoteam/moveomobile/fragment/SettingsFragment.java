package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.HomeActivity;
import fr.moveoteam.moveomobile.activity.LoginActivity;
import fr.moveoteam.moveomobile.dao.DataBaseHandler;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 31/05/15.
 */
public class SettingsFragment extends Fragment {

    private EditText password;
    private EditText newPassword;
    private EditText checkNewPassword;
    private TextView deleteAccount;
    private Button saveButton;
    private Switch informationVisibility;
    private Switch indicator;
    private String passwordSave;
    private int accessSave;
    private String id;

    private int access;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings,container,false);
        password = (EditText) view.findViewById(R.id.settings_password);
        newPassword = (EditText) view.findViewById(R.id.settings_new_password);
        checkNewPassword = (EditText) view.findViewById(R.id.settings_check_new_password);
        saveButton = (Button) view.findViewById(R.id.save_settings);
        informationVisibility = (Switch) view.findViewById(R.id.set_informations_visible);
        deleteAccount = (TextView) view.findViewById(R.id.delete_account_button);
        indicator = (Switch) view.findViewById(R.id.activate_indicator);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        passwordSave = userDAO.getUserDetails().getPassword();
        accessSave = userDAO.getUserDetails().getAccess();
        id = Integer.toString(userDAO.getUserDetails().getId());
        userDAO.close();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.defaultindicator);
        String value = sharedPref.getString(getString(R.string.indicator), defaultValue);

        if(value.equals("indicator"))indicator.setChecked(true);
        else indicator.setChecked(false);

        indicator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.indicator), "indicator");
                    editor.commit();
                    ((HomeActivity) getActivity()).changeIndicator(true);
                }else{
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.indicator), "nonindicator");
                    editor.commit();
                    ((HomeActivity) getActivity()).changeIndicator(false);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDAO.open();
                int count = 0;

                if(!password.getText().toString().isEmpty()){
                    if(Function.encrypt(password.getText().toString()).equals(passwordSave)){
                        count ++;
                    }else{
                        password.setError("Mot de passe incorrect");
                    }
                }else {
                    password.setError("Ce champ est vide");
                }

                if(!newPassword.getText().toString().isEmpty()){
                    if(newPassword.getText().length() <= 7){
                        newPassword.setError("Votre mot de passe doit contenir 8 caractères ou plus.");
                    }else{
                        count++;
                    }
                }else{
                    newPassword.setError("Ce champ est vide");
                }

                if(!checkNewPassword.getText().toString().isEmpty()){
                    count++;
                }else {
                    checkNewPassword.setError("Ce champ est vide");
                }

                if(count == 3){
                    if(newPassword.getText().toString().equals(checkNewPassword.getText().toString())){
                        new ExecuteThread().execute();
                    }else{
                        Toast.makeText(getActivity(),"Les mots de passes ne sont pas identiques",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        if(accessSave == 2)informationVisibility.setChecked(false);
        else informationVisibility.setChecked(true);

        informationVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) access = 3;
                else  access = 2;
                new ExecuteInformationVisibilityThread().execute();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().isEmpty()){
                    if(Function.encrypt(password.getText().toString()).equals(passwordSave)){

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Êtes vous sûr de vouloir supprimer votre compte ?");
                        builder.setNegativeButton("non", null);
                        builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ExecuteDeleteAccountThread().execute();
                            }
                        });
                        builder.show();
                    }else{
                        password.setError("Mot de passe incorrect");
                    }
                }else {
                    password.setError("Remplir ce champ afin de valider la suppression");
                }
            }
        });
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Modification du mot de passe en cours");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONUser jsonUser = new JSONUser();
            Log.e("pass",password.getText().toString()+" "+newPassword.getText().toString()+" id : "+id);
            return jsonUser.changePassword(id, password.getText().toString(), newPassword.getText().toString());
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if(json == null){
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
                if(json.getString("success").equals("1")){
                    UserDAO userDAO = new UserDAO(getActivity());
                    userDAO.open();
                    userDAO.modifyPassword(newPassword.getText().toString());
                    userDAO.close();

                    password.setText("");
                    newPassword.setText("");
                    checkNewPassword.setText("");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Votre mot de passe a été modifié");
                    builder.setPositiveButton("ok", null);
                    builder.show();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Une erreur est survenue lors de l'enregistrement du mot de passe");
                    builder.setPositiveButton("ok", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteInformationVisibilityThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONUser jsonUser = new JSONUser();
            Log.e("access",""+access);
            return jsonUser.changeAccess(id, Integer.toString(access), passwordSave);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if(json == null){
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((HomeActivity) getActivity()).refreshFragment();
                    }
                });
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((HomeActivity) getActivity()).refreshFragment();
                    }
                });
                builder.show();
            }else try {
                if(json.getString("success").equals("1")){
                    UserDAO userDAO = new UserDAO(getActivity());
                    userDAO.open();
                    userDAO.modifyAccess(access);
                    userDAO.close();
                    ((HomeActivity) getActivity()).refreshFragment();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Une erreur est survenue lors du changement de visibilité des informations");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((HomeActivity) getActivity()).refreshFragment();
                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteDeleteAccountThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Suppression en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONUser jsonUser = new JSONUser();
            Log.e("access",""+access);
            return jsonUser.deleteAccount(id, password.getText().toString());
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if(json == null){
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", null);
                builder.show();
            }else try {
                if(json.getString("success").equals("1")){
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(getActivity());
                    dataBaseHandler.resetTables();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("deleteAccount",true);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Une erreur est survenue lors du changement de visibilité des informations");
                    builder.setPositiveButton("ok", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
