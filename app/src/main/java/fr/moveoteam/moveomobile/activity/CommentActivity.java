package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    ListView listView;
    int id;
    ArrayList<Comment> commentArrayList;

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
                    JSONArray commentList = json.getJSONArray("comment");
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
}
