package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.zip.Inflater;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 23/05/15.
 */
public class AddCommentFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_comment,container,false);
        return view;
    }
}
