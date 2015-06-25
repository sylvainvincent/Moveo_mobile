package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 25/06/15.
 */
public class MessagingFragment extends Fragment {

    InboxListFragment inboxListFragment;
    SendboxListFragment sendboxListFragment;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_informations,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inboxListFragment = new InboxListFragment();
        sendboxListFragment = new SendboxListFragment();
        fragment = inboxListFragment;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.messaging_content, inboxListFragment);
        ft.commit();
    }

    public void linkToInbox(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.messaging_content, inboxListFragment);
        ft.commit();
    }

    public void linkToSendbox(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.messaging_content,sendboxListFragment);
        ft.commit();
    }

    public InboxListFragment getInboxListFragment() {
        return inboxListFragment;
    }

    public SendboxListFragment getSendboxListFragment() {
        return sendboxListFragment;
    }
}
