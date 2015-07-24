package fr.moveoteam.moveomobile.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.SendMessageActivity;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Dialog;
import fr.moveoteam.moveomobile.webservice.JSONUser;

/**
 * Created by Sylvain on 22/07/15.
 */
public class AboutFragment extends Fragment {

    Button send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);
        send = (Button) view.findViewById(R.id.send_message_contact);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SendMessageActivity.class);
                intent.putExtra("friendId",""+0);
                intent.putExtra("name","Moveo");
                startActivity(intent);
            }
        });
    }
}
