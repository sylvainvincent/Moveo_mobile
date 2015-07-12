package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.CommentListAdapter;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 11/07/15.
 */
public class CommentActivity extends Activity {

    private ListView listView;
    private int id;
    private ArrayList<Comment> commentArrayList;
    private int commentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        id = getIntent().getIntExtra("tripId",0);
        setContentView(R.layout.my_comment_list);
        listView = (ListView) findViewById(R.id.my_comment_list);

        new ExecuteThread().execute();

    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
         private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CommentActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getComments(Integer.toString(id));
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setMessage("Connexion perdue");
                    builder.setPositiveButton("OK", null);
                    builder.show();

                }else if (json.getString("success").equals("1")) {
                    final JSONArray commentList = json.getJSONArray("comment");
                    commentArrayList = new ArrayList<>(commentList.length());
                    for (int i = 0; i < commentList.length(); i++) {
                        commentArrayList.add(new Comment(
                                commentList.getJSONObject(i).getInt("comment_id"),
                                commentList.getJSONObject(i).getString("comment_message"),
                                commentList.getJSONObject(i).getString("comment_added_datetime"),
                                commentList.getJSONObject(i).getInt("user_id"),
                                commentList.getJSONObject(i).getString("user_last_name"),
                                commentList.getJSONObject(i).getString("user_first_name"),
                                commentList.getJSONObject(i).getString("user_link_avatar")
                        ));
                        Log.e("comment", commentArrayList.get(i).toString());
                    }

                    listView.setAdapter(new CommentListAdapter(CommentActivity.this, commentArrayList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            commentId = commentArrayList.get(position).getId();
                            openContextMenu(view);
                        }
                    });
                    registerForContextMenu(listView);

                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setMessage("Récupération des commentaires échoué");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_comment,menu);
        menu.setHeaderTitle("Choisir une action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_delete_comment:
                final AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                builder.setMessage("Êtes vous sûr de supprimer ce commentaire ?");
                builder.setNegativeButton("non", null);
                builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ExecuteDeleteCommentThread().execute();
                    }
                });

                builder.show();
            break;
        }

        return super.onContextItemSelected(item);
    }

    private class ExecuteDeleteCommentThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CommentActivity.this);
            pDialog.setMessage("Suppression en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.deleteComment(Integer.toString(commentId));
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setMessage("Connexion perdue");
                    builder.setPositiveButton("OK", null);
                    builder.show();

                }else if (json.getString("success").equals("1")) {
                    startActivity(getIntent());
                    finish();

                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setMessage("Suppression du commentaire échoué");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
