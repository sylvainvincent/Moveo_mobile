package fr.moveoteam.moveomobile.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sylvain on 14/04/15.
 */
public class Function {

    /**
     * VERIFIER LA CONNECTION SUR INTERNET (VIA 3G,WIFI,...)
     */
    public static boolean beConnectedToInternet(Context c){

        boolean bool = false;
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) bool = true;

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) bool = true;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) bool = true;

        return bool;
    }


    /**
     * VERIFIER LA CONFORMITÉ DE L'EMAIL
     * @param email l'adresse mail à vérifier
     * @return vrai si l'email est conforme sinon faux
     */
    public static boolean isEmailAddress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();

    }

    /**
     * Verification d'un champ contenant seulement des lettres
     * @param text
     * @return
     */
    public static boolean isString(String text){
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = p.matcher(text);
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Verification de la date de naissance
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean testDate(int year, int month, int day){
        Boolean validation = false;

        GregorianCalendar calendrier = new GregorianCalendar();
        calendrier.set(year, month, day);
        int yearOk = calendrier.get(Calendar.YEAR);
        int monthOk = calendrier.get(Calendar.MONTH);
        int dayOk = calendrier.get(Calendar.DAY_OF_MONTH);
        System.out.println("MA DATE : "+ year +" "+ month +" "+ day );
        System.out.println("MA NOUVELLE DATE : "+yearOk+" "+ monthOk +" "+dayOk);

        if(yearOk == year & monthOk == month & dayOk == day){
            System.out.println("ok");
            validation = true;
        }

        return false;
    }

    public static Bitmap decodeBase64(String input)
    {
        Bitmap bitmap;

        if(input != null){
            Log.e("Image64", "taille : " + input.length());
            byte[] bytes = Base64.decode(input, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }else bitmap = null;
        return bitmap;
    }

    public static String displayCurrentDate() {
        String format = "dd/MM/yy H:mm:ss";

        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
        java.util.Date date = new java.util.Date();

        return formater.format(date);
    }

    public static String dateDifference (Date date){

        /** Date du jour */
        Date today = new Date( );

        // Calcul de différence
        long diff = today.getTime( ) - date.getTime( );

        return "Différence en nombre de jour entre "+date+ " et " + today +
                " nest " + (diff / (1000*60*60*24)) + " jours.";
    }

}
