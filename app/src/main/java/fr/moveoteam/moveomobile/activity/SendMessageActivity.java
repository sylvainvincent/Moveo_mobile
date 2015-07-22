package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Dialog;
import fr.moveoteam.moveomobile.webservice.JSONUser;


public class SendMessageActivity extends Activity {

    // ELEMENTS DE VUE
    private TextView receiveName;
    private EditText messageContent;
    private Button buttonSendMessage;

    // BUNDLE
    private String userId;
    private String recipientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        receiveName = (TextView) findViewById(R.id.receive_name);
        buttonSendMessage = (Button) findViewById(R.id.button_send_message);
        messageContent = (EditText) findViewById(R.id.send_message_content);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageContent.getText().length()>0){
                    new ExecuteThread().execute();
                }else{
                    messageContent.setError("Veuillez saisir un message");
                }
            }
        });
        String name = receiveName.getText()+" <b><font color=#3cb2cc>"+getIntent().getExtras().getString("name")+"</font></b>";

        receiveName.setText(Html.fromHtml(name));

        recipientId = getIntent().getExtras().getString("friendId");
        UserDAO userDAO = new UserDAO(SendMessageActivity.this);
        userDAO.open();
        userId = Integer.toString(userDAO.getUserDetails().getId());

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject>{

        ProgressDialog pDialog;
        JSONUser jsonUser;

        String recipientFirstName,
               recipientLastName,
               message,
               date;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendMessageActivity.this);
            pDialog.setMessage("Envoie du message");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            message = messageContent.getText().toString();
            jsonUser = new JSONUser();

            Log.e("id user",""+userId);
            Log.e("id friend",""+recipientId);
            return jsonUser.sendMessage(message,""+userId,recipientId);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            if(json == null){
                Log.e("test json", "null");
                AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this);
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", null);
                builder.show();
            }else {
                try {
                    if(json.getString("success").equals("1")) {
                       recipientLastName = json.getString("lastname");
                       recipientFirstName = json.getString("firstname");
                       date = json.getString("date");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this);

                        builder.setMessage("Votre message a bien été envoyé");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog newDialog = new Dialog(Integer.parseInt(recipientId), recipientLastName, recipientFirstName, message, date, false);
                                DialogDAO dialogDAO = new DialogDAO(SendMessageActivity.this);
                                dialogDAO.open();
                                dialogDAO.addSendDialog(newDialog);
                                dialogDAO.close();
                                finish();
                            }
                        });
                        builder.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this);
                        builder.setMessage("Une erreur s'est produite lors l'envoi du message");
                        builder.setPositiveButton("ok", null);
                        builder.show();
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            }

        }
    }
}
