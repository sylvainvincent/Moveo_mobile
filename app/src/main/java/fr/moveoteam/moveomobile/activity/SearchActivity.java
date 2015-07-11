package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.fragment.MyTripListFragment;
import fr.moveoteam.moveomobile.fragment.SearchTripListFragments;
import fr.moveoteam.moveomobile.fragment.SearchUserListFragment;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 27/06/15.
 */
public class SearchActivity extends Activity {

    EditText searchBar;
    SearchTripListFragments searchTripListFragments;
    SearchUserListFragment searchUserListFragment;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        String[] list = new String[]{"Amis","Voyage"};

        final Spinner spin = (Spinner) findViewById(R.id.spinner);
        searchBar = (EditText) findViewById(R.id.search_edittext);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(adapter);

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Si l'utilisateur appui sur entrer la recherche est lancé
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(spin.getSelectedItem().toString().equals("Voyage")) {
                        query = searchBar.getText().toString();
                   /* Toast.makeText(SearchActivity.this,spin.getSelectedItem().toString()+
                            " YOU CLICKED ENTER KEY " + searchBar.getText().toString(),
                            Toast.LENGTH_LONG).show();*/
                        // permet de cacher le clavier
                        InputMethodManager imm = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        searchTripListFragments = new SearchTripListFragments();
                        Bundle bundle = new Bundle();
                        bundle.putString("query", query);
                        searchTripListFragments.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.search_content, searchTripListFragments, "search");
                        ft.commit();
                    }else{
                        query = searchBar.getText().toString();
                   /* Toast.makeText(SearchActivity.this,spin.getSelectedItem().toString()+
                            " YOU CLICKED ENTER KEY " + searchBar.getText().toString(),
                            Toast.LENGTH_LONG).show();*/
                        // permet de cacher le clavier
                        InputMethodManager imm = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        searchUserListFragment = new SearchUserListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("query", query);
                        searchUserListFragment.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.search_content, searchUserListFragment, "search");
                        ft.commit();
                    }
                }
                return true;
            }
        });
    }

    /*private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Récupération des voyages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTripList(Integer.toString(1));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
                }else if(json.getString("success").equals("1")) {
                    // Si la récupération des voyages a été un succès on affecte les voyages dans un ArrayList

                    // Recuperation des voyages sous la forme d'un JSONArray
                    JSONArray tripList = json.getJSONArray("trip");
                    MyTripListFragment m = new MyTripListFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.search_content,m);
                    ft.commit();

                }

            } catch (JSONException e1) {
                e1.printStackTrace();

            }

        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
