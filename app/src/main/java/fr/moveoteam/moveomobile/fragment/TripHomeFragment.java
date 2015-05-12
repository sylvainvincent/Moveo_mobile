package fr.moveoteam.moveomobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.model.Trip;

/**
 * Created by Sylvain on 10/05/15.
 */
public class TripHomeFragment extends Fragment {

    private TextView tripName;
    private TextView tripCountry;
    private TextView tripAuthor;
    private TextView tripDate;
    private TextView tripDescription;
    private  LinearLayout tripHome;
    OnInformationListener information;
    Trip trip;
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_description, container,false);
        tripName = (TextView) view.findViewById(R.id.trip_name);
        tripCountry = (TextView) view.findViewById(R.id.trip_country);
        tripAuthor = (TextView) view.findViewById(R.id.trip_author);
        tripDate = (TextView) view.findViewById(R.id.trip_date);
        tripDescription = (TextView) view.findViewById(R.id.trip_description);
        tripHome = (LinearLayout) view.findViewById(R.id.trip_home);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            information = (OnInformationListener) activity;
        }catch (Exception e){

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public interface OnInformationListener{

        public void getInformation(Trip trip);

    }

    public void setList(Trip trip){
        tripCountry.setText(trip.getCountry());
        tripDescription.setText(trip.getDescription());
        tripAuthor.setText(Html.fromHtml("<font color=#000>par</font> <b>" + trip.getAuthor_last_name() + " " + trip.getAuthor_first_name() + " </b>"));
        tripDate.setText(tripDate.getText() + " " + trip.getDate());
        tripHome.setVisibility(View.VISIBLE);
    }

}
