package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.HomeActivity;
import fr.moveoteam.moveomobile.adapter.MessageListAdapter;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Dialog;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 31/05/15.
 */
public class InboxListFragment extends ListFragment {

    private ArrayList<Dialog> dialogArrayList;
	private int position;
	private int id;
	private int recipientId;
	private String date;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        id = userDAO.getUserDetails().getId();
        userDAO.close();

        DialogDAO dialogDAO = new DialogDAO(getActivity());
        dialogDAO.open();
        dialogArrayList = dialogDAO.getInboxList();
		dialogDAO.close();

        Log.e("Inbox"," ok ");

        if(dialogArrayList != null) {
            setListAdapter(new MessageListAdapter(getActivity(), dialogArrayList));
            Log.e("Inbox"," ok2 ");
        } else setListAdapter(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e("Inbox","Passage 1");
        if(dialogArrayList.get(position).isRead()){
            Log.e("Inbox","Passage 2");
            this.position = position;
            recipientId = dialogArrayList.get(position).getRecipientId();
            date = dialogArrayList.get(position).getDate();
            new ExecuteThread().execute();
        }

    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {


            JSONUser jsonUser = new JSONUser();

            return jsonUser.readMessage(Integer.toString(id), Integer.toString(recipientId), date);
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                if (json == null) {

                } else if (json.getString("success").equals("1")) {
                    Log.e("Inbox","Passage 3");
                    ((HomeActivity) getActivity()).refreshFragment();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
