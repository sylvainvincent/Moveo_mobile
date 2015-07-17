package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.fragment.SearchTripListFragments;
import fr.moveoteam.moveomobile.fragment.SearchUserListFragment;

/**
 * Created by Sylvain on 27/06/15.
 */
public class SearchActivity extends Activity {

    private EditText searchBar;
    private SearchTripListFragments searchTripListFragments;
    private SearchUserListFragment searchUserListFragment;
    private String query;

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
                }else if(keyCode == KeyEvent.KEYCODE_BACK){
                    finish();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
