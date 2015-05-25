package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.TripActivity;
import fr.moveoteam.moveomobile.dao.FriendDAO;
import fr.moveoteam.moveomobile.model.Friend;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 11/05/15.
 */
public class FriendCategoryFragment extends Fragment {


    private TextView friendcounter;
    private TextView friendsrequesttitle;
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
            view = inflater.inflate(R.layout.fragment_friend_list, container, false);
            friendcounter = (TextView) view.findViewById(R.id.friend_counter);
            friendsrequesttitle = (TextView) view.findViewById(R.id.friends_request_title);
        } catch (InflateException ignored) {

        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FriendDAO friendDAO = new FriendDAO(getActivity());
        friendDAO.open();

        if(friendDAO.getFriendRequestList().size() != 0)
            friendsrequesttitle.setText(friendDAO.getFriendRequestList().size()+" "+friendsrequesttitle.getText());
        else friendsrequesttitle.setText(null);

        if(friendDAO.getFriendRequestList().size() == 0 || friendDAO.getFriendRequestList().size() == 1)
            friendcounter.setText(friendDAO.getFriendList().size()+" contact");
        else friendcounter.setText(friendDAO.getFriendList().size()+" "+friendcounter.getText());
    }

    @Override // onDestroy est appelé lorsque l'activity est stoper (onStop)
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
