package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.OtherUserProfileActivity;
import fr.moveoteam.moveomobile.adapter.UsersAdapter;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.webservice.JSONSearch;

/**
 * Created by Sylvain on 07/07/15.
 */
public class SearchUserListFragment extends ListFragment {

        String query;
        String userId;
        Context context;

        ArrayList<Friend> userArrayList;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            query = getArguments().getString("query");
            Log.e("Test Search", query);
            UserDAO userDAO = new UserDAO(getActivity());
            userDAO.open();
            userId = Integer.toString(userDAO.getUserDetails().getId());
            userDAO.close();
            context = getActivity();
            new ExecuteThread().execute();
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            Friend friend = userArrayList.get(position);
            Log.e("Recuperation",friend.getFirstName());
            Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
            intent.putExtra("id",friend.getId());
            startActivity(intent);
        }

        private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
            private ProgressDialog pDialog;
/*
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Recherche en cours...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }*/

            @Override
            protected JSONObject doInBackground(String... args) {
                JSONSearch jsonSearch = new JSONSearch();
                return jsonSearch.searchUser(userId, query);
            }

            @Override
            protected void onPostExecute(JSONObject json) {
             //   pDialog.dismiss();
                try {
                    if (json == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Connexion perdu");
                        builder.setPositiveButton("ok", null);
                        builder.show();
                    } else if (json.getString("success").equals("1")) {
                        JSONArray userList = json.getJSONArray("user");
                        userArrayList = new ArrayList<>(userList.length());

                        for (int i = 0; i < userList.length(); i++) {
                            userArrayList.add(new Friend(
                                    userList.getJSONObject(i).getInt("user_id"),
                                    userList.getJSONObject(i).getString("user_last_name"),
                                    userList.getJSONObject(i).getString("user_first_name"),
                                    userList.getJSONObject(i).getString("user_avatar"),
                                    userList.getJSONObject(i).getInt("trip_count")
                            ));

                            setListAdapter(new UsersAdapter(getActivity(), userArrayList));
                        }
                    }else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Une erreur s'est produite lors de la recherche");
                        builder.setPositiveButton("ok", null);
                        builder.show();
                        setListAdapter(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


}
