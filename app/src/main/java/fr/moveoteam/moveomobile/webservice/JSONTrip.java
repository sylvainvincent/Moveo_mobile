package fr.moveoteam.moveomobile.webservice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui envoie des informations(Tag,id,nom,...) sous forme list <Clé,valeur> vers la classe JsonParser
 * qui va traiter les informations et les envoyer vers le fichier "trip" du webService 
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
    
    // VOYAGE --------------------------
    
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
    
    public JSONObject deleteTrip(String tripId, String userId) {
        List<NameValuePair> tripForm = new ArrayList<>();
        tripForm.add(new BasicNameValuePair("tag","deleteTrip"));
        tripForm.add(new BasicNameValuePair("trip_id",tripId));
        tripForm.add(new BasicNameValuePair("user_id", userId));
        return jsonParser.getJSONFromUrl(tripURL,tripForm);
    }

    public JSONObject getExploreTrips(String userId){
        List<NameValuePair> tripRequest = new ArrayList<>();
        tripRequest.add(new BasicNameValuePair("tag","getTenTrips"));
        tripRequest.add(new BasicNameValuePair("user_id",userId));
        return jsonParser.getJSONFromUrl(tripURL,tripRequest);
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
    
    public JSONObject updateDescription(String tripId, String description) {
        List<NameValuePair> descriptionForm = new ArrayList<>();
        descriptionForm.add(new BasicNameValuePair("tag", "modifyDescription"));
        descriptionForm.add(new BasicNameValuePair("trip_id", tripId));
        descriptionForm.add(new BasicNameValuePair("description", description));
        return jsonParser.getJSONFromUrl(tripURL, descriptionForm);
    }
    
    public JSONObject updateCoverImage(String tripId, String cover) {
        List<NameValuePair> coverRequest = new ArrayList<>();
        coverRequest.add(new BasicNameValuePair("tag", "modifyCover"));
        coverRequest.add(new BasicNameValuePair("trip_id", tripId));
        coverRequest.add(new BasicNameValuePair("cover", cover));
        return jsonParser.getJSONFromUrl(tripURL, coverRequest);
    }
    
    // PHOTOS --------------------------

    public JSONObject addPhoto(String tripId, String photo){
        List<NameValuePair> commentForm = new ArrayList<>();
        commentForm.add(new BasicNameValuePair("tag","addPhoto"));
        commentForm.add(new BasicNameValuePair("trip_id",tripId));
        commentForm.add(new BasicNameValuePair("photo",photo));
        return jsonParser.getJSONFromUrl(tripURL,commentForm);
    }

    public JSONObject getPhotoGallery(String tripId){
        List<NameValuePair> photoRequest = new ArrayList<>();
        photoRequest.add(new BasicNameValuePair("tag","getPhotoGallery"));
        photoRequest.add(new BasicNameValuePair("trip_id",tripId));
        return jsonParser.getJSONFromUrl(tripURL,photoRequest);
    }

    public JSONObject deletePhoto(String photoId) {
        List<NameValuePair> photoRequest = new ArrayList<>();
        photoRequest.add(new BasicNameValuePair("tag","deletePhoto"));
        photoRequest.add(new BasicNameValuePair("photo_id",photoId));
        return jsonParser.getJSONFromUrl(tripURL,photoRequest);
    }

    public JSONObject reportPhoto(String photoId, String userId) {
        List<NameValuePair> photoRequest = new ArrayList<>();
        photoRequest.add(new BasicNameValuePair("tag","reportPhoto"));
        photoRequest.add(new BasicNameValuePair("photoId",photoId));
        photoRequest.add(new BasicNameValuePair("userId",userId));
        return jsonParser.getJSONFromUrl(tripURL,photoRequest);
    }

    // COMMENTAIRE --------------------------
	
	public JSONObject addComment(String userId, String tripId, String message){
        List<NameValuePair> commentForm = new ArrayList<>();
        commentForm.add(new BasicNameValuePair("tag","addComment"));
        commentForm.add(new BasicNameValuePair("user_id",userId));
        commentForm.add(new BasicNameValuePair("trip_id",tripId));
        commentForm.add(new BasicNameValuePair("comment_message",message));
        return jsonParser.getJSONFromUrl(tripURL,commentForm);
    }
    
    public JSONObject getComments(String tripId){
        List<NameValuePair> commentRequest = new ArrayList<>();
        commentRequest.add(new BasicNameValuePair("tag","getCommentList"));
        commentRequest.add(new BasicNameValuePair("trip_id",tripId));
        return jsonParser.getJSONFromUrl(tripURL,commentRequest);
    }

    public JSONObject deleteComment(String commentId) {
        List<NameValuePair> commentRequest = new ArrayList<>();
        commentRequest.add(new BasicNameValuePair("tag","deleteComment"));
        commentRequest.add(new BasicNameValuePair("comment_id",commentId));
        return jsonParser.getJSONFromUrl(tripURL,commentRequest);
    }

    public JSONObject modifyComment(String commentId, String message){
        List<NameValuePair> commentRequest = new ArrayList<>();
        commentRequest.add(new BasicNameValuePair("tag","modifyComment"));
        commentRequest.add(new BasicNameValuePair("comment_id",commentId));
        commentRequest.add(new BasicNameValuePair("message",message));
        return jsonParser.getJSONFromUrl(tripURL,commentRequest);
    }

    // LIEUX -------------------------------
    
    public JSONObject addPlace(String placeName, String placeAddress, String placeDescription, String category, String tripId){
    
        List<NameValuePair> placeRequest = new ArrayList<>();
        placeRequest.add(new BasicNameValuePair("tag", "addPlace"));
        placeRequest.add(new BasicNameValuePair("place_name", placeName));
        placeRequest.add(new BasicNameValuePair("place_address", placeAddress));
        placeRequest.add(new BasicNameValuePair("place_description", placeDescription));
        placeRequest.add(new BasicNameValuePair("category_id", category));
        placeRequest.add(new BasicNameValuePair("trip_id", tripId));
        return jsonParser.getJSONFromUrl(tripURL, placeRequest);
    
    }
    
    public JSONObject updatePlace(String placeId, String placeName, String placeAddress, String placeDescription){
    
        List<NameValuePair> placeRequest = new ArrayList<>();
        placeRequest.add(new BasicNameValuePair("tag", "modifyPlace"));
        placeRequest.add(new BasicNameValuePair("place_id", placeId));
        placeRequest.add(new BasicNameValuePair("place_name", placeName));
        placeRequest.add(new BasicNameValuePair("place_address", placeAddress));
        placeRequest.add(new BasicNameValuePair("place_description", placeDescription));

        return jsonParser.getJSONFromUrl(tripURL, placeRequest);
    
    }
    
    public JSONObject deletePlace(String placeId){

        List<NameValuePair> placeRequest = new ArrayList<>();
        placeRequest.add(new BasicNameValuePair("tag","deletePlace"));
        placeRequest.add(new BasicNameValuePair("place_id",placeId));
        return jsonParser.getJSONFromUrl(tripURL,placeRequest);
    }

}
