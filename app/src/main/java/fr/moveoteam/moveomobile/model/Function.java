package fr.moveoteam.moveomobile.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;
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
    public static boolean etreConnecterAInternet(Context c){

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
     */
    public static boolean isEmailAdress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();

    }

    public static boolean testDate(int annee,int mois,int jour){
        GregorianCalendar calendrier = new GregorianCalendar();
        calendrier.set(annee,mois,jour);
        int anneeOk = calendrier.get(Calendar.YEAR);
        int moisOk = calendrier.get(Calendar.MONTH);
        int jourOk = calendrier.get(Calendar.DAY_OF_MONTH);
        System.out.println("MA DATE : "+annee+" "+mois+" "+jour);
        System.out.println("MA DATE FORMATER : "+anneeOk+" "+moisOk+" "+jourOk);

        if(anneeOk==annee & moisOk==mois & jourOk==jour){
            System.out.println("ok");
            return true;
        }else return false;

    }
}
