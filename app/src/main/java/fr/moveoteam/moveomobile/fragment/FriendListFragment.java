package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.TripActivity;
import fr.moveoteam.moveomobile.UserProfile;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.adapter.FriendsListAdapter;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Amélie on 10/05/2015.
 */
public class FriendListFragment extends ListFragment {

    ArrayList<Friend> friendArrayList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FriendDAO friendDAO = new FriendDAO(getActivity());
        friendDAO.open();
        friendArrayList = friendDAO.getFriendList();
        friendDAO.close();
        if(friendArrayList != null)
            setListAdapter(new FriendsListAdapter(getActivity(), friendArrayList));
        else setListAdapter(null);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Friend friend = friendArrayList.get(position);
        Log.e("Recuperation", friend.getFirstName());
        Intent intent = new Intent(getActivity(), UserProfile.class);
        intent.putExtra("id",friend.getId());
        startActivity(intent);
    }

}
