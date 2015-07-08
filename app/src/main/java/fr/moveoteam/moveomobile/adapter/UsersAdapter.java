package fr.moveoteam.moveomobile.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Function;

/**
 * Created by Sylvain on 07/07/15.
 */
public class UsersAdapter extends BaseAdapter {
    ArrayList<Friend> friendsList;
    LayoutInflater layoutInflater;
    ViewHolderFriend viewHolderFriend;
    Context context;
    String friendId;
    boolean accepted;

    public UsersAdapter(Context context, ArrayList<Friend> friendsList) {
        this.friendsList = friendsList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void updateResult(ArrayList<Friend> friendsList){
        this.friendsList = friendsList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friendsList != null?friendsList.size():0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(friendsList != null) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_users, null);
                viewHolderFriend = new ViewHolderFriend();
                viewHolderFriend.otherUserName = (TextView) convertView.findViewById(R.id.other_user_name);
                viewHolderFriend.tripCount = (TextView) convertView.findViewById(R.id.user_trip_count);
                viewHolderFriend.avatar = (ImageView) convertView.findViewById(R.id.other_user_avatar);
                convertView.setTag(viewHolderFriend);
            } else {
                viewHolderFriend = (ViewHolderFriend) convertView.getTag();
            }
            viewHolderFriend.otherUserName.setText(friendsList.get(position).getFirstName() + " " + friendsList.get(position).getLastName());
            viewHolderFriend.tripCount.setText(friendsList.get(position).getTripCount());
            Log.i("test friend", friendsList.get(position).toString());

            if (!friendsList.get(position).getAvatarBase64().equals("") || !friendsList.get(position).getAvatarBase64().isEmpty())
                viewHolderFriend.avatar.setImageBitmap(Function.decodeBase64(friendsList.get(position).getAvatarBase64()));
            else {
                viewHolderFriend.avatar.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.default_avatar));
                Log.i("FriendListAdapter", "passage image ok");
            }
            Log.i("friendListAdapter", "photo : " + friendsList.get(position).getAvatarBase64());

       }
        return convertView;
    }

    static class ViewHolderFriend {
        TextView otherUserName, tripCount;
        ImageView avatar;
    }

}
