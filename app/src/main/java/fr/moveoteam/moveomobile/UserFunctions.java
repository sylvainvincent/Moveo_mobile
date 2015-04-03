package fr.moveoteam.moveomobile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylvain on 01/04/15.
 */
public class UserFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // Il faut utiliser l'adresse http://10.0.2.2/ pour se connecter au localhost : http://localhost/
    private static String loginURL = "http://10.0.2.2/webService/";
    private static String registerURL = "http://10.0.2.2/webService/";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String modify_tag = "modify";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Fonction qui envoie sous forme de reponse JSON le formulaire d'inscription
     * @param email
     * @param password
     * @param name
     * @param firstName
     * */
    public JSONObject registerUser(String email, String password, String name, String firstName){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("firstName", firstName));
        // test
        System.out.println(params.get(0)+" "+params.get(1)+" "+params.get(2)+" "+params.get(3)+" "+params.get(4));
        // Obtenir un objet json avec l'adresse du webservice et le formulaire sous forme d'Arraylist
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // retourner l'objet json
        return json;
    }

}
