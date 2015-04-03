package fr.moveoteam.moveomobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sylvain on 01/04/15.
 */import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends ActionBarActivity {

        Button buttonRegister;
        EditText editMail;
        EditText editPassword;
        EditText editName;
        EditText editFirstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editMail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editName = (EditText) findViewById(R.id.editName);
        editFirstName = (EditText) findViewById(R.id.editFirstName);



        buttonRegister.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        final String email = editMail.getText().toString();
                        final String password = editPassword.getText().toString();
                        final String name = editName.getText().toString();
                        final String firstName = editFirstName.getText().toString();
                        UserFunctions userFunction = new UserFunctions();
                        JSONObject json = userFunction.registerUser("email","password","name","firstName");


                        }
                }
        );
    }
}
