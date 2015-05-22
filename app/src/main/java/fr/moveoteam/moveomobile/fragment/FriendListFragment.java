package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 11/05/15.
 */
public class FriendListFragment extends Fragment {


    private TextView friendcounter;
    private TextView friendsrequesttitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        if(!getArguments().getString("requestCounter").equals("0"))
            friendsrequesttitle.setText(getArguments().getString("requestCounter")+" "+friendsrequesttitle.getText());
        else friendsrequesttitle.setText(null);

        if(getArguments().getString("friendCounter").equals("0") || getArguments().getString("friendCounter").equals("1"))
            friendcounter.setText(getArguments().getString("friendCounter")+" contact");
        else friendcounter.setText(getArguments().getString("friendCounter")+" "+friendcounter.getText());
    }

    private void initialize() {

        friendcounter = (TextView) getActivity().findViewById(R.id.friend_counter);
        friendsrequesttitle = (TextView) getActivity().findViewById(R.id.friends_request_title);
    }
}
