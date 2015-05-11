package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.FriendsListAdapter;

/**
 * Created by Sylvain on 11/05/15.
 */
public class FriendRequestFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FriendDAO friendDAO = new FriendDAO(getActivity());
        friendDAO.open();
        ArrayList<Friend> friendArrayList = friendDAO.getFriendRequestList();
        if(friendArrayList != null)
            setListAdapter(new FriendsListAdapter(getActivity(), friendArrayList));
        else setListAdapter(null);
    }

}