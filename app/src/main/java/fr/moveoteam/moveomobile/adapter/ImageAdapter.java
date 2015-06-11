package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Photo;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 31/05/15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Photo> tripArrayList;

    public ImageAdapter(Context c, ArrayList<Photo> tripArrayList) {
        mContext = c;
        this.tripArrayList = tripArrayList;
    }

    public int getCount() {
        return tripArrayList.size();
    }

    public Object getItem(int position) {
        return tripArrayList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(Function.decodeBase64(tripArrayList.get(position).getPhotoBase64()));
        return imageView;
    }


}