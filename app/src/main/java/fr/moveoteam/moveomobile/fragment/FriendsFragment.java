package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.adapter.FriendsListAdapter;

/**
 * Created by Amélie on 10/05/2015.
 */
public class FriendsFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FriendDAO friendDAO = new FriendDAO(getActivity());
        friendDAO.open();
        ArrayList<Friend> friendArrayList = friendDAO.getFriendList();
        if(friendArrayList != null)
            setListAdapter(new FriendsListAdapter(getActivity(), friendArrayList));
        else setListAdapter(null);
    }

}
