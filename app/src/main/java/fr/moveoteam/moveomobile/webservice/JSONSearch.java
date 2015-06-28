package fr.moveoteam.moveomobile.webservice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylvain on 28/06/15.
 */
public class JSONSearch {

    private JSONParser jsonParser;

    // Il faut utiliser l'adresse http://10.0.2.2/ pour se connecter au localhost : http://localhost/
    // 10.0.3.2 pour genymotion
    private static String searchURL = "http://moveo.besaba.com/search.php";

    // constructeur
    public JSONSearch(){
        this.jsonParser = new JSONParser();
    }

    /**
     *
     * @return un objet <b>json</b> contenant une réponse indiquant si la recherche retourne une liste
     */
    public JSONObject searchTrip(String userId, String query){

        List<NameValuePair> searchForm = new ArrayList<>();
        searchForm.add(new BasicNameValuePair("tag", "searchTrip"));
        searchForm.add(new BasicNameValuePair("userId", userId));
        searchForm.add(new BasicNameValuePair("query", query));

        return jsonParser.getJSONFromUrl(searchURL, searchForm);
    }

}
