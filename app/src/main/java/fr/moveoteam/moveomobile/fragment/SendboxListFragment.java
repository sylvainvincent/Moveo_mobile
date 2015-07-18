package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.HomeActivity;
import fr.moveoteam.moveomobile.adapter.MessageListAdapter;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Dialog;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 31/05/15.
 */
public class SendboxListFragment extends ListFragment {

    private ArrayList<Dialog> dialogArrayList;
    int position;
    Dialog message;
    private int id;
    private int recipientId;
    private String date;
    AlertDialog alertDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserDAO userDAO = new UserDAO(getActivity());
        userDAO.open();
        id = userDAO.getUserDetails().getId();
        userDAO.close();

        DialogDAO dialogDAO = new DialogDAO(getActivity());
        dialogDAO.open();
        dialogArrayList = dialogDAO.getSendboxList();
		dialogDAO.close();

        Log.e("Sendbox"," ok ");

        if(dialogArrayList != null)
		  setListAdapter(new MessageListAdapter(getActivity(), dialogArrayList));
        else setListAdapter(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        this.position = position;
        message = dialogArrayList.get(position);
        recipientId = dialogArrayList.get(position).getRecipientId();
        date = dialogArrayList.get(position).getDate();

        LayoutInflater commentView = LayoutInflater.from(getActivity());
        final View alertDialogView = commentView.inflate(R.layout.message_content, null);

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setCancelable(true);

        adb.setView(alertDialogView);

        TextView messageauthor = (TextView)alertDialogView.findViewById(R.id.message_author);
        TextView messageContent = (TextView)alertDialogView.findViewById(R.id.message);
        TextView messagedate = (TextView)alertDialogView.findViewById(R.id.date_message);
        ImageView delete = (ImageView)alertDialogView.findViewById(R.id.delete_message);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Etes vous sûr de vouloir supprimer ce message ?");
                builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ExecuteDeleteThread().execute();
                        alertDialog.dismiss();
                    }
                });
                builder.setNegativeButton("non",null);
                builder.show();
            }
        });

        messageauthor.setText(message.getRecipientFirstName()+" "+message.getRecipientLastName());
        messageContent.setText(message.getMessage());
        messagedate.setText(Function.dateSqlToFullDateJava(message.getDate()));
        alertDialog = adb.create();
        alertDialog.show();

    }

    private class ExecuteDeleteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override //Procedure appelée avant le traitement (optionnelle)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Suppression en cours...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        //Méthode appelée pendant le traitement (obligatoire)
        protected JSONObject doInBackground(String... args) {
            JSONUser jsonUser = new JSONUser();
            Log.e("test sendbox",""+id+" "+recipientId+ " "+date);
            return jsonUser.deleteMessageSendbox(Integer.toString(id), Integer.toString(recipientId), date);
        }

        @Override
        //Procedure appelée après le traitement (optionnelle)
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                if (json == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Connexion perdue");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else if (json.getString("success").equals("1")) {
                    DialogDAO dialogDAO = new DialogDAO(getActivity());
                    dialogDAO.open();
                    dialogDAO.deleteMessage(message.getId());
                    dialogDAO.close();

                    ((HomeActivity) getActivity()).refreshFragment();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Erreur");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
