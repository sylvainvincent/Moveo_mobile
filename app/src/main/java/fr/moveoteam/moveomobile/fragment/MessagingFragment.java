package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 25/06/15.
 */
public class MessagingFragment extends Fragment {

    InboxListFragment inboxListFragment;
    SendboxListFragment sendboxListFragment;
    ImageView sendIcon;
    ImageView receiveIcon;
    TextView messageCount;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_informations,container,false);
        messageCount = (TextView) view.findViewById(R.id.message_count);
        receiveIcon = (ImageView) view.findViewById(R.id.receive_messages_icon);
        sendIcon = (ImageView) view.findViewById(R.id.send_messages_icon);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inboxListFragment = new InboxListFragment();
        sendboxListFragment = new SendboxListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.messaging_content, inboxListFragment);
        ft.commit();
    }


    public InboxListFragment getInboxListFragment() {
        return inboxListFragment;
    }

    public SendboxListFragment getSendboxListFragment() {
        return sendboxListFragment;
    }

    public void setRecieveIcon(){
        sendIcon.setImageDrawable(getResources().getDrawable(R.drawable.send_messages));
    }

    public void setSendIcon(){
        sendIcon.setImageDrawable(getResources().getDrawable(R.drawable.send_messages));
    }

}
