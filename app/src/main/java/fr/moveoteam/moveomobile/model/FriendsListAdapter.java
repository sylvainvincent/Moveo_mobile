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
 * Created by Amélie on 10/05/2015.
 */
public class FriendsListAdapter extends BaseAdapter {

    ArrayList<Friend> friendsList;
    LayoutInflater layoutInflater;
    ViewHolderFriend viewHolderFriend;

    public FriendsListAdapter(Context context, ArrayList<Friend> friendsList) {
        this.friendsList = friendsList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_list_friends, null);
            viewHolderFriend = new ViewHolderFriend();
            viewHolderFriend.friend_name = (TextView) convertView.findViewById(R.id.friend_name);
            viewHolderFriend.avatar = (ImageView) convertView.findViewById(R.id.friend_avatar);
            convertView.setTag(viewHolderFriend);
        } else {
            viewHolderFriend = (ViewHolderFriend) convertView.getTag();
        }
        viewHolderFriend.friend_name.setText(friendsList.get(position).getFirstName());
        return convertView;
    }

    static class ViewHolderFriend {
        TextView friend_name;
        ImageView avatar;
    }
}
