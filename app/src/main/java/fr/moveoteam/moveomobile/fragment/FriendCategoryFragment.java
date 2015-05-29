package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.adapter.FriendsListAdapter;
import fr.moveoteam.moveomobile.dao.FriendDAO;

/**
 * Created by Sylvain on 11/05/15.
 */
public class FriendCategoryFragment extends Fragment {


    private TextView friendcounter;
    private TextView friendsrequesttitle;
    FriendRequestFragment friendRequestFragment;
    FriendListFragment friendListFragment;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.friend, container, false);
            friendRequestFragment = (FriendRequestFragment) getFragmentManager().findFragmentById(R.id.fragment_friend_list_request);
            friendListFragment = (FriendListFragment) getFragmentManager().findFragmentById(R.id.fragment_friend_list);

            friendcounter = (TextView) view.findViewById(R.id.friend_counter);
            friendsrequesttitle = (TextView) view.findViewById(R.id.friends_request_title);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("FriendCategory","Entrer");
        FriendDAO friendDAO = new FriendDAO(getActivity());
        friendDAO.open();

        int friendRequestSize = friendDAO.getFriendRequestList() != null ? friendDAO.getFriendRequestList().size():0;
        int friendSize = friendDAO.getFriendList() != null ? friendDAO.getFriendList().size():0;
        friendDAO.close();

        // Affichage du nombre de demande d'amis
        if(friendRequestSize == 0)
            friendsrequesttitle.setText(friendRequestSize+" "+"nouvelles demandes d'amis");
        else if(friendRequestSize == 1)
            friendsrequesttitle.setText(friendRequestSize+" "+"nouvelle demande d'ami");
        else
            friendsrequesttitle.setText(friendRequestSize+" "+"nouvelles demandes d'amis");

        // Affichage du nombre d'amis
        if(friendSize == 0 || friendSize == 1)
            friendcounter.setText(friendSize+" contact");
        else friendcounter.setText(friendSize+" "+" contacts");
    }

    @Override // onDestroy est appelé lorsque l'activity est stoppé (onStop)
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = (Fragment) getFragmentManager()
                .findFragmentById(R.id.fragment_friend_list);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i("FriendCat","onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.i("FriendCat","onDetach");
        super.onDetach();
    }
}
