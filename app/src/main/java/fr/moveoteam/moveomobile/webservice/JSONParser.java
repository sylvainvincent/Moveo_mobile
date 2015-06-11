package fr.moveoteam.moveomobile.webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by Sylvain on 04/04/15.
 */
public class JSONParser {



    // constructeur vide
    public JSONParser() {
    }

   /**
    * Fonction qui récupère les informations d'un tableau associatif,les traite puis les envoie à l'adresse indiqué en paramètre(url).
    * Ensuite elle récupère et parse une réponse (un message d'erreur ou de succès par exemple)
    * Pour finir elle renvoie la réponse sous la forme d'un objet JSON
	* @param  url l'adresse du fichier du Webservice à utilisé
	* @param postParameters le tableau associatif avec un tag
    */
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> postParameters) {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        int timeout;

        try {
            timeout = 20000; // 20 secondes
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            // Pour créer une requête POST nous allons créer un objet HttpPost avec comme paramètre l'URL du web service
            HttpPost httpPost = new HttpPost(url);
            // on affecte le résultat du formulaire sur l'objet
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,timeout);
            HttpConnectionParams.setSoTimeout(httpParams, timeout);
            httpPost.setParams(httpParams);
            // La requête est envoyée !!! on récupère la réponse
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException | SocketTimeoutException | ConnectTimeoutException e) {
            Log.e("Time out", "timeout");
            e.printStackTrace();
            return jObj;
        } catch (IOException e) {
            e.printStackTrace();
            return jObj;
        }
        try { // Nous lisons le résultat qui nous a été envoyé
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
            Log.e("Erreur du Buffer", "Erreur dans la conversion du résultat " + e.toString());
        }
        // essayer de parser un string en un objet Json
        try {
            Log.e("Réponse du JSON : ",json);
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Erreur de parse " + e.toString());
        }

        // Retourne un objet Json
        return jObj;
    }
}