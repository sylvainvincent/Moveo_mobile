package fr.moveoteam.moveomobile.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;

/**
 * Created by alexMac on 08/04/15.
 */
public class CustomListAdapter extends BaseAdapter {

    ArrayList<Trip> tripList;
    LayoutInflater layoutInflater;

    public CustomListAdapter (Context context, ArrayList<Trip> tripList){
        this.tripList = tripList;
        layoutInflater = LayoutInflater.from(context);
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
        ViewHolderTrip viewHolderTrip;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_list_trip, null);
            viewHolderTrip = new ViewHolderTrip();
            viewHolderTrip.explore_trip_name = (TextView) convertView.findViewById(R.id.explore_trip_name);
            viewHolderTrip.explore_country = (TextView) convertView.findViewById(R.id.explore_country);
            viewHolderTrip.explore_username = (TextView) convertView.findViewById(R.id.explore_username);
            viewHolderTrip.imageViewMainPictureTrip = (ImageView) convertView.findViewById(R.id.imageViewMainPictureTrip);
            viewHolderTrip.imageButtonComments = (ImageView) convertView.findViewById(R.id.imageButtonComments);
            viewHolderTrip.imageButtonPictures = (ImageView) convertView.findViewById(R.id.imageButtonPictures);
            convertView.setTag(viewHolderTrip);
        }else {
            viewHolderTrip = (ViewHolderTrip) convertView.getTag();
        }
        viewHolderTrip.explore_trip_name.setText(tripList.get(position).getName());
        viewHolderTrip.explore_country.setText(tripList.get(position).getCountry());
        viewHolderTrip.explore_username.setText(tripList.get(position).getUser().firstname);
        return convertView;
    }

    static class ViewHolderTrip {
        TextView explore_trip_name, explore_country, explore_username;
        ImageView imageViewMainPictureTrip, imageButtonComments, imageButtonPictures;
    }
}
