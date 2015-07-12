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
import fr.moveoteam.moveomobile.dao.DialogDAO;

/**
 * Created by Sylvain on 25/06/15.
 */
public class MessagingFragment extends Fragment {

    private InboxListFragment inboxListFragment;
    private SendboxListFragment sendboxListFragment;
    private ImageView sendIcon;
    private ImageView receiveIcon;
    private TextView messageCount;
    Fragment fragment;

    private int inboxCount;
    private int sendboxCount;

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

        DialogDAO dialogDAO= new DialogDAO(getActivity());
        dialogDAO.open();

        if(dialogDAO.getInboxList() != null) inboxCount = dialogDAO.getInboxList().size();
        else inboxCount = 0;
        if(dialogDAO.getSendboxList() != null) sendboxCount = dialogDAO.getSendboxList().size();
        else sendboxCount = 0;
        dialogDAO.close();

        messageCount.setText(inboxCount+ " messages");

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
        receiveIcon.setImageDrawable(getResources().getDrawable(R.drawable.receive_messages_hover));
        sendIcon.setImageDrawable(getResources().getDrawable(R.drawable.send_messages));
        messageCount.setText(inboxCount+ " messages");
    }

    public void setSendIcon(){
        sendIcon.setImageDrawable(getResources().getDrawable(R.drawable.send_messages_hover));
        receiveIcon.setImageDrawable(getResources().getDrawable(R.drawable.receive_messages));
        messageCount.setText(sendboxCount+" messages");

    }

}
