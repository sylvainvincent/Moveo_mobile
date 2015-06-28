package fr.moveoteam.moveomobile.webservice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui envoie des informations(Tag,id,nom,...) sous forme list <Clé,valeur> vers la classe JsonParser
 * qui va traiter les informations et les envoyer vers le fichier "User" du webService 
 * Created by Sylvain on 01/04/15.
 */
public class JSONUser {

    private JSONParser jsonParser;

    // Il faut utiliser l'adresse http://10.0.2.2/ pour se connecter au localhost : http://localhost/
    // 10.0.3.2 pour genymotion
    private static String userURL = "http://moveo.besaba.com/user.php";

    // constructeur
    public JSONUser(){
        this.jsonParser = new JSONParser();
    }

    /**
     * Envoie les informations saisies du formulaire d'inscription sous la forme d'un objet json vers le webservice
     * @param email une adresse mail saisie par l'utilisateur dans le formulaire d'inscription
     * @param password un mot de passe saisi par l'utilisateur dans le formulaire d'inscription
     * @param name un nom saisi par l'utilisateur
     * @param firstName un prénom saisi par l'utilisateur
     * @return un objet <b>json</b> contenant une réponse indiquant si l'ajout a réussi ou non
     */
    public JSONObject addUser(String email, String password, String name, String firstName){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire d'inscription
        List<NameValuePair> registerForm = new ArrayList<>();
        registerForm.add(new BasicNameValuePair("tag", "register"));
        registerForm.add(new BasicNameValuePair("email", email));
        registerForm.add(new BasicNameValuePair("password", password));
        registerForm.add(new BasicNameValuePair("name", name));
        registerForm.add(new BasicNameValuePair("firstName", firstName));

        return jsonParser.getJSONFromUrl(userURL, registerForm);
    }

   /**
    * Envoie les informations saisies du formulaire de connexion sous la forme d'un objet json vers le webservice
    * @param email une adresse mail saisie par l'utilisateur dans le formulaire de connexion
    * @param password un mot de passe saisi par l'utilisateur dans le formulaire de connexion
    * @see JSONParser
    * @return un objet json contenant soit un message d'erreur ou soit tous les informations de l'utilisateur(amis, messages, ...)
    */
    public JSONObject loginUser(String email, String password){

        //ArrayList sous la forme <clé,valeur> contenant les informations du formulaire de connexion
        List<NameValuePair> loginForm = new ArrayList<>();
        loginForm.add(new BasicNameValuePair("tag","login"));
        loginForm.add(new BasicNameValuePair("email",email));
        loginForm.add(new BasicNameValuePair("password",password));

        return jsonParser.getJSONFromUrl(userURL, loginForm);
    }

    /**
     * Méthode qui envoie l'adresse mail saisie dans la popup Lost_password sous la forme d'un objet json vers le Webservice
     * @param email l'adresse mail saisie par l'utilisateur en question
     * @return un objet json contenant l'adresse mail de l'utilisateur
     */
    public JSONObject lostPassword (String email){
        List<NameValuePair> lostPasswordForm = new ArrayList<>();
        lostPasswordForm.add(new BasicNameValuePair("tag", "forgetPassword"));
        lostPasswordForm.add(new BasicNameValuePair("user_email", email));

        return jsonParser.getJSONFromUrl(userURL, lostPasswordForm);
    }

    public JSONObject modifyUser(String id, String lastName, String firstName, String avatar, String birthday, String city, String country) {
        List<NameValuePair> modifyUserForm = new ArrayList<>();
        modifyUserForm.add(new BasicNameValuePair("tag", "updateProfil"));
        modifyUserForm.add(new BasicNameValuePair("userId", id));
        modifyUserForm.add(new BasicNameValuePair("lastName", lastName));
        modifyUserForm.add(new BasicNameValuePair("firstName", firstName));
        modifyUserForm.add(new BasicNameValuePair("avatar", avatar));
        modifyUserForm.add(new BasicNameValuePair("birthday", birthday));
        modifyUserForm.add(new BasicNameValuePair("city", city));
        modifyUserForm.add(new BasicNameValuePair("country", country));

        return jsonParser.getJSONFromUrl(userURL, modifyUserForm);
    }

    public JSONObject getOtherUser(String id){
        List<NameValuePair> otherUserForm = new ArrayList<>();
        otherUserForm.add(new BasicNameValuePair("tag", "getOtherUser"));
        otherUserForm.add(new BasicNameValuePair("idOtherUser", id));

        return jsonParser.getJSONFromUrl(userURL, otherUserForm);
    }

    public JSONObject sendMessage(String message, String userId, String recipientId){
        List<NameValuePair> messageForm = new ArrayList<>();
        messageForm.add(new BasicNameValuePair("tag", "addDialog"));
        messageForm.add(new BasicNameValuePair("message", message));
        messageForm.add(new BasicNameValuePair("userId", userId));
        messageForm.add(new BasicNameValuePair("recipientId", recipientId));

        return jsonParser.getJSONFromUrl(userURL,messageForm);
    }
	
	public JSONObject readMessage (String userId, String recipientId, String date){
		List<NameValuePair> isRead = new ArrayList<>();
        isRead.add(new BasicNameValuePair("tag", "readDialog"));
        isRead.add(new BasicNameValuePair("userId", userId));
		isRead.add(new BasicNameValuePair("recipientId", recipientId));
        isRead.add(new BasicNameValuePair("date", date));
        
		return jsonParser.getJSONFromUrl(userURL,isRead);
	}

    public JSONObject changePassword (String userId, String password, String newPassword){
        List<NameValuePair> passwordForm = new ArrayList<>();
        passwordForm.add(new BasicNameValuePair("tag", "changePassword"));
        passwordForm.add(new BasicNameValuePair("userId", userId));
        passwordForm.add(new BasicNameValuePair("password", password));
        passwordForm.add(new BasicNameValuePair("newPassword", newPassword));

        return jsonParser.getJSONFromUrl(userURL,passwordForm);
    }

    public JSONObject changeAccess (String userId, String access, String password){
        List<NameValuePair> passwordForm = new ArrayList<>();
        passwordForm.add(new BasicNameValuePair("tag", "changeAccess"));
        passwordForm.add(new BasicNameValuePair("userId", userId));
        passwordForm.add(new BasicNameValuePair("access", access));
        passwordForm.add(new BasicNameValuePair("password", password));

        return jsonParser.getJSONFromUrl(userURL,passwordForm);
    }
}
