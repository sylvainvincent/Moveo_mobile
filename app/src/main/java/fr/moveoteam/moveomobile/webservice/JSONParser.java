package fr.moveoteam.moveomobile.webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Sylvain on 04/04/15.
 */
public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructeur vide
    public JSONParser() {
    }

   /**
    * Fonction qui récupere les informations d'un formulaire,les traite puis les envoie à l'adresse indiqué en parametre(url).
    * Ensuite elle recupére et parse une réponse (un message d'erreur ou de succes par exemple)
    * Pour finir elle renvoie la reponse sous la forme d'un objet JSON
    *
    *
    *
    */
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> postParameters) {
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            // Pour créer une requête POST nous allons créer un objet HttpPost avec comme parametre l'url du web service
            HttpPost httpPost = new HttpPost(url);
            // on affecte le resultat du formulaire sur l'objet
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            //La requête est envoyée !!! on recupere la reponse
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try { // Nous lisons le resultat qui nous a été envoyé
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Erreur du Buffer", "Erreur dans la conversion du resultat " + e.toString());
        }
        // essayer de parser un string en un objet Json
        try {
            Log.e("Test",json);
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Erreur de parse " + e.toString());
        }
        // Retour un objet Json
        return jObj;
    }
}