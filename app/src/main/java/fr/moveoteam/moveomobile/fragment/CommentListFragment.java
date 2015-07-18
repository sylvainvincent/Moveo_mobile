package fr.moveoteam.moveomobile.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.OtherUserProfileActivity;
import fr.moveoteam.moveomobile.activity.TripActivity;
import fr.moveoteam.moveomobile.adapter.CommentListAdapter;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 23/05/15.
 */
public class CommentListFragment extends ListFragment {

    //Element de vue
    private EditText editComment;

    private ArrayList<Comment> commentArrayList;
    private int tripId;
    private int userId;
    private int commentId;
    private String commentMessage;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        userId = userDAO.getUserDetails().getId();
        userDAO.close();
        tripId = ((TripActivity) getActivity()).getId();
        // tripId = getArguments().getInt("tripId");
        ExecuteThread executeThread = new ExecuteThread();
        executeThread.execute();

    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {

        super.onListItemClick(l, v, position, id);
        final Comment comment = commentArrayList.get(position);
        if(comment.getIdUser() != userId) {
            Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
            intent.putExtra("id", comment.getIdUser());
            startActivity(intent);
        }else{
            LayoutInflater commentView = LayoutInflater.from(getActivity());
            final View alertDialogView = commentView.inflate(R.layout.my_comment, null);

            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

            adb.setView(alertDialogView);

            Button modifyButton = (Button)alertDialogView.findViewById(R.id.modify_comment);
            Button deleteButton = (Button)alertDialogView.findViewById(R.id.delete_comment);
            editComment = (EditText)alertDialogView.findViewById(R.id.edit_my_comment);
            editComment.setText(comment.getMessage());
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!editComment.getText().toString().equals(comment.getMessage()) && !editComment.getText().toString().equals("")){
                        commentId = commentArrayList.get(position).getId();
                        commentMessage =  editComment.getText().toString();
                        Log.e("comment", "id : "+commentId+" message : "+commentMessage);
                        new ExecuteModifyCommentThread().execute();
                    }
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Êtes vous sûr de supprimer le commentaire ?");
                    builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentId = commentArrayList.get(position).getId();
                            new ExecuteDeleteCommentThread().execute();
                        }
                    });
                    builder.setNegativeButton("non", null);
                    builder.show();
                }
            });

            AlertDialog alertDialog = adb.create();
            alertDialog.show();
        }


    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {

        @Override //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getComments(Integer.toString(tripId));
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {

            try {
                if(json == null){
                    setListAdapter(null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    if(commentArrayList != null)
                        setListAdapter(new CommentListAdapter(getActivity(), commentArrayList, userId));

                }else{
                    setListAdapter(null);
                    Toast.makeText(getActivity(),"Il n'y a pas de commentaires",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private class ExecuteModifyCommentThread extends AsyncTask<String, String, JSONObject> {
         private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mise à jour ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {

            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.modifyComment(Integer.toString(commentId), commentMessage);
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                if(json == null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Connexion perdue");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }else if (json.getString("success").equals("1")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Votre commentaire a été modifié");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*startActivity(getActivity().getIntent());
                            getActivity().finish();*/
                            ((TripActivity)getActivity()).refreshFragment();
                        }
                    });
                    builder.show();

                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Modification du commentaire échoué");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private class ExecuteDeleteCommentThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Suppression en cours ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Connexion perdue");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }else if (json.getString("success").equals("1")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Votre commentaire a été supprimé");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(getActivity().getIntent());
                            getActivity().finish();
                            //((TripActivity)getActivity()).refreshFragment();
                        }
                    });
                    builder.show();

                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
