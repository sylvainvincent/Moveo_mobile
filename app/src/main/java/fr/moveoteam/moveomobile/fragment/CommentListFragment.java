package fr.moveoteam.moveomobile.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.activity.MyTripActivity;
import fr.moveoteam.moveomobile.adapter.CommentListAdapter;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 23/05/15.
 */
public class CommentListFragment extends ListFragment {

    ArrayList<Comment> commentArrayList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if(getArguments().getParcelableArrayList("commentList") != null) {

            commentArrayList = getArguments().getParcelableArrayList("commentList");
            Log.e("array", commentArrayList.toString());
        }
        else */ commentArrayList = null;

        setListAdapter(new CommentListAdapter(getActivity(), commentArrayList));


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        /*
        super.onListItemClick(l, v, position, id);
        Comment comment = commentArrayList.get(position);
        Log.e("Recuperation", ""+comment.getId());
        Intent intent = new Intent(getActivity(), MyTripActivity.class);
        intent.putExtra("id",comment.getIdUser());
        startActivity(intent);
        */
    }



}
