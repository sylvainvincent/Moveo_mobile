package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Friend;

/**
 * Created by Sylvain on 07/07/15.
 */
public class UsersAdapter extends BaseAdapter {
    private ArrayList<Friend> friendsList;
    private LayoutInflater layoutInflater;
    private ViewHolderUser viewHolderUser;
    private Context context;

    public UsersAdapter(Context context, ArrayList<Friend> friendsList) {
        this.friendsList = friendsList;
        this.context = context;
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
                this.layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.row_users, null);
                viewHolderUser = new ViewHolderUser();
                viewHolderUser.otherUserName = (TextView) convertView.findViewById(R.id.other_user_name);
                viewHolderUser.tripCount = (TextView) convertView.findViewById(R.id.user_trip_count);
                viewHolderUser.avatar = (ImageView) convertView.findViewById(R.id.other_user_avatar);
                convertView.setTag(viewHolderUser);
            } else {
                viewHolderUser = (ViewHolderUser) convertView.getTag();
            }
            viewHolderUser.otherUserName.setText(friendsList.get(position).getFirstName() + " " + friendsList.get(position).getLastName());
            viewHolderUser.tripCount.setText(""+friendsList.get(position).getTripCount());
            Log.i("test friend", friendsList.get(position).toString());

            if (!friendsList.get(position).getAvatarBase64().equals("") || !friendsList.get(position).getAvatarBase64().isEmpty()) {
                viewHolderUser.imageUrl = friendsList.get(position).getAvatarBase64();
                new DownloadImage().execute(viewHolderUser);
                //viewHolderFriend.avatar.setImageBitmap(Function.decodeBase64(friendsList.get(position).getAvatarBase64()));
            } else {
                viewHolderUser.avatar.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.default_avatar));
                Log.i("FriendListAdapter", "passage image ok");
            }
            Log.i("friendListAdapter", "photo : " + friendsList.get(position).getAvatarBase64());

       }
        return convertView;
    }

    static class ViewHolderUser {
        TextView otherUserName, tripCount;
        ImageView avatar;
        Bitmap bitmap;
        String imageUrl;
    }

    private class DownloadImage extends AsyncTask<ViewHolderUser, Void, ViewHolderUser> {

        String url;
        URL urlImage;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        @Override
        protected ViewHolderUser doInBackground(ViewHolderUser... args) {
            ViewHolderUser viewHolderTripImage = args[0];
            try {
                urlImage = new URL("http://moveo.besaba.com/"+viewHolderTripImage.imageUrl);
                connection = (HttpURLConnection) urlImage.openConnection();
                if (connection != null) {
                    inputStream = connection.getInputStream();
                    viewHolderTripImage.bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return viewHolderTripImage;
        }

        @Override
        protected void onPostExecute(ViewHolderUser result) {
            if (result.bitmap == null) {
                result.avatar.setImageResource(R.drawable.default_avatar);
            } else {
                result.avatar.setImageBitmap(result.bitmap);
            }
        }
    }


}
