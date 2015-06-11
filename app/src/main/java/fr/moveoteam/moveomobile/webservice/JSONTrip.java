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
    private static String tripURL = "http://moveo.besaba.com/trip.php";

    // constructor
    public JSONTrip(){
        this.jsonParser = new JSONParser();
    }

    public JSONObject addTrip(String userId, String name, String country, String description, String photoBase64){

        List<NameValuePair> tripForm = new ArrayList<>();
        tripForm.add(new BasicNameValuePair("tag","addTrip"));
        tripForm.add(new BasicNameValuePair("user_id",userId));
        tripForm.add(new BasicNameValuePair("trip_name",name));
        tripForm.add(new BasicNameValuePair("trip_country",country));
        tripForm.add(new BasicNameValuePair("description",description));
        tripForm.add(new BasicNameValuePair("cover",photoBase64));

        return jsonParser.getJSONFromUrl(tripURL,tripForm);

    }


    public JSONObject getExploreTrips(String userId){

        List<NameValuePair> loginForm = new ArrayList<>();
        loginForm.add(new BasicNameValuePair("tag","getTenTrips"));
        loginForm.add(new BasicNameValuePair("user_id",userId));
        return jsonParser.getJSONFromUrl(tripURL,loginForm);

    }

    public JSONObject getTrip(String id){

        List<NameValuePair> tripRequest = new ArrayList<>();
        tripRequest.add(new BasicNameValuePair("tag","getTrip"));
        tripRequest.add(new BasicNameValuePair("trip_id",id));
        return jsonParser.getJSONFromUrl(tripURL,tripRequest);
    }

    public JSONObject getTripList(String otherUserId){

        List<NameValuePair> tripRequest = new ArrayList<>();
        tripRequest.add(new BasicNameValuePair("tag","getTripList"));
        tripRequest.add(new BasicNameValuePair("user_id",otherUserId));
        return jsonParser.getJSONFromUrl(tripURL,tripRequest);
    }

    public JSONObject getPhotoGallery(String tripId){

        List<NameValuePair> photoRequest = new ArrayList<>();
        photoRequest.add(new BasicNameValuePair("tag","getPhotoGallery"));
        photoRequest.add(new BasicNameValuePair("trip_id",tripId));
        return jsonParser.getJSONFromUrl(tripURL,photoRequest);
    }

}
