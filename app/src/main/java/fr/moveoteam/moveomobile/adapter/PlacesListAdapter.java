package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Place;

/**
 * Created by Amélie on 09/05/2015.s
 */
public class PlacesListAdapter extends BaseAdapter {

    ArrayList<Place> placesList;
    LayoutInflater layoutInflater;
    ViewHolderPlace viewHolderPlace;

    public PlacesListAdapter(Context context, ArrayList<Place> placesList) {
        this.placesList = placesList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return placesList.size();
    }

    @Override
    public Object getItem(int position) {
        return placesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_list_places, null);
            viewHolderPlace = new ViewHolderPlace();
            viewHolderPlace.place_name = (TextView) convertView.findViewById(R.id.place_list_name);
            viewHolderPlace.place_address = (TextView) convertView.findViewById(R.id.place_list_adress);
            convertView.setTag(viewHolderPlace);
        } else {
            viewHolderPlace = (ViewHolderPlace) convertView.getTag();
        }
        viewHolderPlace.place_name.setText(placesList.get(position).getName());
        viewHolderPlace.place_address.setText(placesList.get(position).getAddress());
        return convertView;
    }

    static class ViewHolderPlace {
        TextView place_name, place_address;
    }
}
