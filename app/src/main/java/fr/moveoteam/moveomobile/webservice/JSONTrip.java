package fr.moveoteam.moveomobile.webservice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylvain on 18/04/15.
 */
public class JSONTrip {

    private JSONParser jsonParser;

    // Il faut utiliser l'adresse http://10.0.2.2/ pour se connecter au localhost : http://localhost/
    // 10.0.3.2 pour genymotion
    private static String tripURL = "http://192.168.1.33/Moveo_webservice/trip.php";

    // constructor
    public JSONTrip(){
        this.jsonParser = new JSONParser();
    }

    public JSONObject getExploreTrips(){

        List<NameValuePair> loginForm = new ArrayList<>();
        loginForm.add(new BasicNameValuePair("tag","getTenTrips"));
        return jsonParser.getJSONFromUrl(tripURL,loginForm);

    }
}
