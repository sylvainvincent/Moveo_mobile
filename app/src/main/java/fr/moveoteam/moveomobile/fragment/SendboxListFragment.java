package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.adapter.MessageListAdapter;
import fr.moveoteam.moveomobile.dao.DialogDAO;
import fr.moveoteam.moveomobile.model.Dialog;

/**
 * Created by Sylvain on 31/05/15.
 */
public class SendboxListFragment extends ListFragment {

    ArrayList<Dialog> dialogArrayList;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DialogDAO dialogDAO = new DialogDAO(getActivity());
        dialogDAO.open();
        dialogArrayList = dialogDAO.getSendboxList();
		dialogDAO.close();

        Log.e("Sendbox"," ok ");

        if(dialogArrayList != null)
		  setListAdapter(new MessageListAdapter(getActivity(), dialogArrayList));
        else setListAdapter(null);
    }
}
