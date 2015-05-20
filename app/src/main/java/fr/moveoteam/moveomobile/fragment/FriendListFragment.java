package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 11/05/15.
 */
public class FriendListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container,false);
    }
}
