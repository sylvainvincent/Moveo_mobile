package fr.moveoteam.moveomobile.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.HomeActivity;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.webservice.JSONFriend;

/**
 * Created by Amélie on 10/05/2015.
 */
public class FriendsListAdapter extends BaseAdapter {

    ArrayList<Friend> friendsList;
    LayoutInflater layoutInflater;
    ViewHolderFriend viewHolderFriend;
    Context context;
    String friendId;
    boolean accepted;

    public FriendsListAdapter(Context context, ArrayList<Friend> friendsList) {
        this.friendsList = friendsList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
                convertView = layoutInflater.inflate(R.layout.row_list_friends, null);
                viewHolderFriend = new ViewHolderFriend();
                viewHolderFriend.friend_name = (TextView) convertView.findViewById(R.id.friend_name);
                viewHolderFriend.accept_friend = (ImageView) convertView.findViewById(R.id.accept_friend);
                viewHolderFriend.refuse_friend = (ImageView) convertView.findViewById(R.id.refuse_friend);
                viewHolderFriend.delete_friend = (ImageView) convertView.findViewById(R.id.delete_friend);
                viewHolderFriend.avatar = (ImageView) convertView.findViewById(R.id.friend_avatar);
                convertView.setTag(viewHolderFriend);
            } else {
                viewHolderFriend = (ViewHolderFriend) convertView.getTag();
            }
            viewHolderFriend.friend_name.setText(friendsList.get(position).getFirstName() + " " + friendsList.get(position).getLastName());

            Log.i("test friend", friendsList.get(position).toString());

            if (!friendsList.get(position).getAvatarBase64().equals("") || !friendsList.get(position).getAvatarBase64().isEmpty())
                viewHolderFriend.avatar.setImageBitmap(Function.decodeBase64(friendsList.get(position).getAvatarBase64()));
            else {
                viewHolderFriend.avatar.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.drawable.default_avatar));
                Log.i("FriendListAdapter", "passage image ok");
            }
            Log.i("friendListAdapter", "photo : " + friendsList.get(position).getAvatarBase64());

            if (friendsList.get(position).isFriend()) {
                viewHolderFriend.refuse_friend.setVisibility(View.GONE);
                viewHolderFriend.accept_friend.setVisibility(View.GONE);
                viewHolderFriend.delete_friend.setVisibility(View.VISIBLE);
                viewHolderFriend.delete_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Êtes vous sûr de retirer cette personne de vos amis ?");
                        alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendId = Integer.toString(friendsList.get(position).getId());
                                accepted = false;
                                new ExecuteThread().execute();
                            }
                        });
                        alert.setNegativeButton("non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    }
                });
            } else {
                viewHolderFriend.refuse_friend.setVisibility(View.VISIBLE);
                viewHolderFriend.accept_friend.setVisibility(View.VISIBLE);
                viewHolderFriend.accept_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Êtes vous sûr d'accepter l'invitation ?");
                        alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendId = Integer.toString(friendsList.get(position).getId());
                                accepted = true;
                                new ExecuteThread().execute();
                            }
                        });
                        alert.setNegativeButton("non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                        alert.show();
                    }
                });
                viewHolderFriend.refuse_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Êtes vous sûr de refuser l'invitation ?");
                        alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendId = Integer.toString(friendsList.get(position).getId());
                                accepted = false;
                                new ExecuteThread().execute();
                            }
                        });
                        alert.setNegativeButton("non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    }
                });
                viewHolderFriend.delete_friend.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolderFriend {
        TextView friend_name;
        ImageView avatar,accept_friend,refuse_friend, delete_friend;
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            UserDAO userDAO = new UserDAO(context);
            userDAO.open();
			Log.e("FriendsListAdapter","user "+userDAO.getUserDetails().getId()+" friend "+friendId);
            JSONFriend jsonFriend = new JSONFriend();
            return accepted?jsonFriend.acceptFriend(Integer.toString(userDAO.getUserDetails().getId()),friendId):jsonFriend.removeFriend(Integer.toString(userDAO.getUserDetails().getId()),friendId);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json == null) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Connexion perdu");
                alert.setPositiveButton("OK",null);
                alert.show();
            } else try {
                if (json.getString("success").equals("1")) {
                    FriendDAO friendDAO = new FriendDAO(context);
                    friendDAO.open();
                    if (accepted)
                        friendDAO.acceptFriend(Integer.parseInt(friendId));

                    if (!accepted)
                        friendDAO.removeFriend(Integer.parseInt(friendId));

                    pDialog.dismiss();

                    ((HomeActivity) context).refreshFragment();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Erreur lors de la suppression de l'amis");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
