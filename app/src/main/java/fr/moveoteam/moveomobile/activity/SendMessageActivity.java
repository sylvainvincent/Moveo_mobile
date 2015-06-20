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
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.webservice.JSONUser;


public class SendMessageActivity extends Activity {

    // ELEMENTS DE VUE
    TextView receiveName;
    EditText messageContent;
    Button buttonSendMessage;

    // BUNDLE
    String userId;
    String recipientId;

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
            String message = messageContent.getText().toString();
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
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        System.exit(0);
                    }
                });
                builder.setMessage("Connexion perdu");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.show();
            }
            // Si la récupération des voyages a été un succès on affecte les voyages dans un ArrayList
            else {
                try {
                    if(json.getString("success").equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this);

                        builder.setMessage("Votre message a bien été envoyé");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        builder.show();
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            }

        }
    }
}
