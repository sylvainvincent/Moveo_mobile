package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by alexMac on 08/04/15.
 */
public class CustomListAdapter extends BaseAdapter {

    ArrayList<Trip> tripList;
    LayoutInflater layoutInflater;
    ViewHolderTrip viewHolderTrip;
    boolean otherUser;
    String avatar;
    StringBuilder st;

    public CustomListAdapter (Context context, ArrayList<Trip> tripList, boolean otherUser, String avatar){
        this.tripList = tripList;
        layoutInflater = LayoutInflater.from(context);
        this.otherUser = otherUser;
        this.avatar = avatar;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(tripList != null) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_list_trip, null);
                viewHolderTrip = new ViewHolderTrip();
                viewHolderTrip.explore_trip_name = (TextView) convertView.findViewById(R.id.explore_trip_name);
                viewHolderTrip.explore_country = (TextView) convertView.findViewById(R.id.explore_country);
                viewHolderTrip.explore_username = (TextView) convertView.findViewById(R.id.explore_username);
                viewHolderTrip.imageViewMainPictureTrip = (ImageView) convertView.findViewById(R.id.imageViewMainPictureTrip);
                viewHolderTrip.imageButtonComments = (ImageView) convertView.findViewById(R.id.image_button_comments);
                viewHolderTrip.imageButtonPictures = (ImageView) convertView.findViewById(R.id.image_button_pictures);
                viewHolderTrip.number_of_comments = (TextView) convertView.findViewById(R.id.number_of_comments);
                viewHolderTrip.number_of_pictures = (TextView) convertView.findViewById(R.id.number_of_picture);
                convertView.setTag(viewHolderTrip);
            } else {
                viewHolderTrip = (ViewHolderTrip) convertView.getTag();
            }
            viewHolderTrip.explore_trip_name.setText(tripList.get(position).getName());
            viewHolderTrip.explore_country.setText(tripList.get(position).getCountry());
            if (this.otherUser) {
                //String authorHTML = "<font color=#000>par</font> <i>" + tripList.get(position).getAuthor_first_name() + " " + tripList.get(position).getAuthor_last_name() + "</i>";
                //viewHolderTrip.explore_username.setText(Html.fromHtml(authorHTML));

                String authorHTML = "<img src='http://xamarin.com/resources/design/home/devices.png' width=100 height=100/>";
                Log.e("Affiche", avatar);
                viewHolderTrip.imageViewMainPictureTrip.setImageBitmap(decodeBase64(avatar));

            } else {
                viewHolderTrip.explore_username.setText(Html.fromHtml("<font color=#000>par</font> <i>moi</i>"));
            }
            viewHolderTrip.number_of_comments.setText((Integer.toString(tripList.get(position).getCommentCount())));
            viewHolderTrip.number_of_pictures.setText(Integer.toString(tripList.get(position).getPhotoCount()));
        }/*else{
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.fragment_button_add_trip, null);
                viewHolderTrip = new ViewHolderTrip();
                viewHolderTrip.explore_trip_name = (TextView) convertView.findViewById(R.id.explore_trip_name);
                viewHolderTrip.explore_country = (TextView) convertView.findViewById(R.id.explore_country);
                viewHolderTrip.explore_username = (TextView) convertView.findViewById(R.id.explore_username);
                viewHolderTrip.imageViewMainPictureTrip = (ImageView) convertView.findViewById(R.id.imageViewMainPictureTrip);
                viewHolderTrip.imageButtonComments = (ImageView) convertView.findViewById(R.id.image_button_comments);
                viewHolderTrip.imageButtonPictures = (ImageView) convertView.findViewById(R.id.image_button_pictures);
                viewHolderTrip.number_of_comments = (TextView) convertView.findViewById(R.id.number_of_comments);
                viewHolderTrip.number_of_pictures = (TextView) convertView.findViewById(R.id.number_of_picture);
                convertView.setTag(viewHolderTrip);
            } else {
                viewHolderTrip = (ViewHolderTrip) convertView.getTag();
            }
        } */
        return convertView;
    }

    static class ViewHolderTrip {
        TextView explore_trip_name, explore_country, explore_username,number_of_comments,number_of_pictures;
        ImageView imageViewMainPictureTrip;
         ImageView imageButtonComments, imageButtonPictures;
    }

    public Bitmap decodeBase64(String input)
    {
        Log.e("Image64","taille : "+input.length());
        byte[] bytes = Base64.decode(input.getBytes(),Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
