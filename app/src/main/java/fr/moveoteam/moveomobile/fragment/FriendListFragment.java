package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.FriendProfileActivity;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.adapter.FriendsListAdapter;

/**
 * Created by Amélie on 10/05/2015.
 */
public class FriendListFragment extends ListFragment {

    private ArrayList<Friend> friendArrayList;
    private FriendsListAdapter listAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FriendDAO friendDAO = new FriendDAO(getActivity());
        friendDAO.open();
        friendArrayList = friendDAO.getFriendList();
        friendDAO.close();


        if(friendArrayList != null) {
            if(listAdapter != null) listAdapter.updateResult(friendArrayList);
            else {
                listAdapter = new FriendsListAdapter(getActivity(), friendArrayList);
            }

            setListAdapter(listAdapter);

        } else {
            if (listAdapter != null) listAdapter.updateResult(null);
            else listAdapter = new FriendsListAdapter(getActivity(), null);

            setListAdapter(listAdapter);

        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Friend friend = friendArrayList.get(position);
        Log.e("Recuperation", friend.getFirstName());
        Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
        intent.putExtra("id",friend.getId());
        intent.putExtra("friend",1);
        startActivity(intent);
    }

    @Override
      public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e("FriendListFragment","attach");

    }



    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("FriendListFragment","detach");
    }
}
