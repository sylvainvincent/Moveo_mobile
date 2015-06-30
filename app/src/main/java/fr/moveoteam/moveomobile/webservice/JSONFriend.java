package fr.moveoteam.moveomobile.webservice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylvain on 18/06/15.
 */
public class JSONFriend {

    private JSONParser jsonParser;

    // Il faut utiliser l'adresse http://10.0.2.2/ pour se connecter au localhost : http://localhost/
    // 10.0.3.2 pour genymotion
    private static String friendURL = "http://moveo.besaba.com/friend.php";

    // constructeur
    public JSONFriend(){
        this.jsonParser = new JSONParser();
    }

    /**
     *
     * @return un objet <b>json</b> contenant une réponse indiquant si la suppression a réussi ou non
     */
    public JSONObject removeFriend(String userId, String friendId){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire d'inscription
        List<NameValuePair> registerForm = new ArrayList<>();
        registerForm.add(new BasicNameValuePair("tag", "removeFriend"));
        registerForm.add(new BasicNameValuePair("user_id", userId));
        registerForm.add(new BasicNameValuePair("friend_id", friendId));

        return jsonParser.getJSONFromUrl(friendURL, registerForm);
    }

    public JSONObject acceptFriend(String userId, String friendId){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire d'inscription
        List<NameValuePair> registerForm = new ArrayList<>();
        registerForm.add(new BasicNameValuePair("tag", "acceptFriend"));
        registerForm.add(new BasicNameValuePair("user_id", userId));
        registerForm.add(new BasicNameValuePair("friend_id", friendId));

        return jsonParser.getJSONFromUrl(friendURL, registerForm);
    }

    public JSONObject sendInvitation(String userId, String friendId){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire d'inscription
        List<NameValuePair> registerForm = new ArrayList<>();
        registerForm.add(new BasicNameValuePair("tag", "addFriend"));
        registerForm.add(new BasicNameValuePair("user_id", userId));
        registerForm.add(new BasicNameValuePair("friend_id", friendId));

        return jsonParser.getJSONFromUrl(friendURL, registerForm);
    }

}
