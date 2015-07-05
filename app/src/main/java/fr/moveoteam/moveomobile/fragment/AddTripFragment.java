package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.AddTripActivity;
import fr.moveoteam.moveomobile.activity.HomeActivity;

/**
 * Created by Sylvain on 07/05/15.
 */
public class AddTripFragment extends Fragment{


    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_add_trip,container,false);
        textView = (TextView) view.findViewById(R.id.add_trip_text);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTripActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("MyTripListFragment", "onActivityResult OK");
        ((HomeActivity) getActivity()).refreshFragment();
    }



}
