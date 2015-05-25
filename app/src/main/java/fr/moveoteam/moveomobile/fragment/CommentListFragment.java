package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.adapter.CommentListAdapter;
import fr.moveoteam.moveomobile.model.Comment;

/**
 * Created by Sylvain on 23/05/15.
 */
public class CommentListFragment extends ListFragment {

    ArrayList<Comment> commentArrayList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments().getParcelableArrayList("commentList") != null) {
            commentArrayList = getArguments().getParcelableArrayList("commentList");
            Log.e("array", commentArrayList.toString());
        }
        else commentArrayList = null;


        if(commentArrayList != null) {
            setListAdapter(new CommentListAdapter(getActivity(), commentArrayList));
        }else {
            setListAdapter(null);
        }

    }



}
