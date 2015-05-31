package fr.moveoteam.moveomobile.adapter;

import android.app.Activity;
import android.content.Context;
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

    public final String LOG = "PlacesListAdapter";

    private Context context;

    ArrayList<Place> placesList;
    LayoutInflater layoutInflater;
    ViewHolderPlace viewHolderPlace;

    public PlacesListAdapter(Context context, ArrayList<Place> placeList) {
        this.context = context;
        this.placesList = placeList;

        if(placeList != null) {
            Log.e(LOG, placeList.get(0).getName());
            Log.e(LOG, placeList.size() + "");
        }

    }

    @Override // Si la liste de lieu est vide on renvoi une ligne
    public int getCount() {
        return placesList != null?placesList.size():1;
    }

    @Override
    public Object getItem(int position) {
        return placesList != null?placesList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.row_list_places, null);
            viewHolderPlace = new ViewHolderPlace();
            viewHolderPlace.place_name = (TextView) convertView.findViewById(R.id.place_list_name);
            viewHolderPlace.place_address = (TextView) convertView.findViewById(R.id.place_list_adress);
            viewHolderPlace.placeIcon = (ImageView) convertView.findViewById(R.id.view_place_icon);
            convertView.setTag(viewHolderPlace);
        } else {
            viewHolderPlace = (ViewHolderPlace) convertView.getTag();
        }

        // Si la liste des lieux est vide on affiche alors un message indiquant que la catégorie est vide
        if(placesList == null){
            viewHolderPlace.place_name.setText("Catégorie vide");
            viewHolderPlace.placeIcon.setVisibility(View.GONE);
            viewHolderPlace.place_address.setVisibility(View.GONE);
        }else {
            viewHolderPlace.place_name.setText(placesList.get(position).getName());
            Log.e(LOG, " position : " + position);
            Log.e(LOG, "nom : "+placesList.get(position).getName());
            viewHolderPlace.place_address.setText(placesList.get(position).getAddress());
        }
        return convertView;
    }

    static class ViewHolderPlace {
        TextView place_name, place_address;
        ImageView placeIcon;
    }
}
