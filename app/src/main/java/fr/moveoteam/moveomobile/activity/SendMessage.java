package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import fr.moveoteam.moveomobile.R;


public class SendMessage extends Activity {

    TextView receiveName;
    Button buttonSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        receiveName = (TextView) findViewById(R.id.receive_name);
        String name = receiveName.getText()+" <b><font color=#3cb2cc>"+getIntent().getExtras().getString("name")+"</font></b>";
        receiveName.setText(Html.fromHtml(name));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
