package fr.moveoteam.moveomobile.menu;

import android.app.Activity;
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
 * Created by Sylvain on 27/04/15.
 */
public class MenuAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MenuItems> menuItems;

    public MenuAdapter(Context context, ArrayList<MenuItems> menuItems){
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.element_menu, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(menuItems.get(position).getIcon());
        txtTitle.setText(menuItems.get(position).getTitle());

        // Afficher le compteur s'il est "visible"
        if(menuItems.get(position).getCounterVisibility()){
            txtCount.setText(menuItems.get(position).getCount());
        }else{
            // cacher le compteur
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }

}
