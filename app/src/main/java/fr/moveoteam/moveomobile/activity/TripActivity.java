package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.fragment.CommentCategoryFragment;
import fr.moveoteam.moveomobile.fragment.CommentListFragment;
import fr.moveoteam.moveomobile.fragment.HomeCategoryFragment;
import fr.moveoteam.moveomobile.fragment.PhotoGalleryFragment;
import fr.moveoteam.moveomobile.fragment.PlaceListFragment;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 10/05/15.
 */
public class TripActivity extends Activity implements HomeCategoryFragment.OnInformationListener{

    int id;

    RelativeLayout layout;

    Trip trip;
    ArrayList<Place> placeArrayList;
    ArrayList<Comment> commentArrayList;

    AlertDialog.Builder alertDialog;
    private TextView tripName;
    private TextView tripCountry;
    private TextView tripAuthor;
    private TextView tripDate;
    private TextView tripDescription;
    private  LinearLayout tripHome;

    Bundle bundle;

    PlaceListFragment gastronomyPlaceListFragment;
    PlaceListFragment leisurePlaceListFragment;
    PlaceListFragment shoppingPlaceListFragment;
    PhotoGalleryFragment photoGalleryFragment;
    CommentCategoryFragment commentCategoryFragment;
    String gastronomyPlaceListFragmentTag;
    String leisurePlaceListFragmentTag;
    String shoppingPlaceListFragmentTag;
    HomeCategoryFragment homeCategoryFragment;
    String  tripHomeFragmentTag;
    private ImageView homeCategory;
    private ImageView foodingCategory;
    private ImageView hobbiesCategory;
    private ImageView shoppingCategory;
    private ImageView picturesCategory;
    private ImageView commentsCategory;
    private ImageView imageCover;

    FragmentManager fragmentManager;
    FragmentTransaction ft;

    private static final String HOME_FRAGMENT_TAG = "HOME";
    private FrameLayout tripcontent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);
        initialize();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        id = getIntent().getExtras().getInt("id",0);

        new ExecuteThread().execute();

    }

    private void initialize() {

        tripName = (TextView) findViewById(R.id.trip_name);
        tripCountry = (TextView) findViewById(R.id.trip_country);
        tripAuthor = (TextView) findViewById(R.id.trip_author);
        tripDate = (TextView) findViewById(R.id.trip_date);
        tripDescription = (TextView) findViewById(R.id.trip_description);
        tripHome = (LinearLayout) findViewById(R.id.trip_home);
        homeCategory = (ImageView) findViewById(R.id.home_category);
        foodingCategory = (ImageView) findViewById(R.id.fooding_category);
        hobbiesCategory = (ImageView) findViewById(R.id.hobbies_category);
        commentsCategory = (ImageView) findViewById(R.id.comments_category);
        picturesCategory = (ImageView) findViewById(R.id.pictures_category);
        imageCover = (ImageView) findViewById(R.id.image_cover);
        tripcontent = (FrameLayout) findViewById(R.id.trip_content);

    }

    @Override
    public void getInformation(Trip trip) {
        //if(trip != null) {
            tripName.setText("Bonjour");
          /*  tripCountry.setText(trip.getCountry());
            tripDescription.setText(trip.getDescription());
            tripAuthor.setText(Html.fromHtml("<font color=#000>par</font> <b>" + trip.getAuthor_last_name() + " " + trip.getAuthor_first_name() + " </b>"));
            tripDate.setText(tripDate.getText() + " " + trip.getDate());
            tripHome.setVisibility(View.VISIBLE);
        /*}else{
           Log.e("Trip","est a nul");
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private class ExecuteThread extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        private FragmentManager fragmentManager;
        private FragmentTransaction ft;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TripActivity.this);
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONTrip jsonTrip = new JSONTrip();
            return jsonTrip.getTrip(Integer.toString(id));
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){
                    finish();
                }else if (json.getString("error").equals("0")) {

                    trip = new Trip();
                    trip.setId(Integer.parseInt(json.getJSONObject("trip").getString("trip_id")));
                    trip.setName(json.getJSONObject("trip").getString("trip_name"));
                    trip.setCountry(json.getJSONObject("trip").getString("trip_country"));
                    trip.setDescription(json.getJSONObject("trip").getString("trip_description"));
                    trip.setCover(json.getJSONObject("trip").getString("trip_cover"));
                    trip.setDate(json.getJSONObject("trip").getString("trip_created_at"));
                    trip.setAuthor_last_name(json.getJSONObject("trip").getString("user_last_name"));
                    trip.setAuthor_first_name(json.getJSONObject("trip").getString("user_first_name"));
                    trip.setUserId(Integer.parseInt(json.getJSONObject("trip").getString("user_id")));
                    Log.e("Trip", trip.toString());
                    if((json.getString("success").equals("1")) || (json.getString("success").equals("2"))){
                        JSONArray placeList = json.getJSONArray("place");
                        placeArrayList = new ArrayList<>(placeList.length());
                        for (int i = 0; i < placeList.length(); i++) {
                            placeArrayList.add(new Place(
                                    placeList.getJSONObject(i).getInt("place_id"),
                                    placeList.getJSONObject(i).getString("place_name"),
                                    placeList.getJSONObject(i).getString("place_address"),
                                    placeList.getJSONObject(i).getString("place_description"),
                                    placeList.getJSONObject(i).getInt("category_id")
                            ));
                            Log.e("Place", placeArrayList.get(i).toString());
                        }
                    }

                    if((json.getString("success").equals("1")) || (json.getString("success").equals("3"))){
                        JSONArray commentList = json.getJSONArray("comment");
                        commentArrayList = new ArrayList<>(commentList.length());
                        for (int i = 0; i < commentList.length(); i++) {
                            commentArrayList.add(new Comment(
                                    commentList.getJSONObject(i).getInt("comment_id"),
                                    commentList.getJSONObject(i).getString("comment_message"),
                                    commentList.getJSONObject(i).getString("comment_added_datetime"),
                                    commentList.getJSONObject(i).getInt("user_id"),
                                    commentList.getJSONObject(i).getString("user_last_name"),
                                    commentList.getJSONObject(i).getString("user_first_name"),
                                    commentList.getJSONObject(i).getString("user_link_avatar")
                            ));
                            Log.e("comment", commentArrayList.get(i).toString());
                            /*
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {

                                Date date = dateFormat.parse(commentList.getJSONObject(i).getString("trip_created_at"));
                                Log.e("Test de date",""+Function.dateDifference(date));
                                Log.e("Test de date 2", dateFormat.format(date));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            */
                        }



                    }

                    homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category_blue));
                    imageCover.setImageBitmap(Function.decodeBase64(trip.getCover()));
                    imageCover.setVisibility(View.VISIBLE);

                    fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    gastronomyPlaceListFragment = new PlaceListFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("placeList", placeArrayList);
                    gastronomyPlaceListFragment.setArguments(bundle);
                    //ft.add(gastronomyPlaceListFragment, "gastronomy");


                    leisurePlaceListFragment = new PlaceListFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("placeList",placeArrayList);
                    leisurePlaceListFragment.setArguments(bundle);
                    leisurePlaceListFragmentTag = leisurePlaceListFragment.getTag();

                    shoppingPlaceListFragment = new PlaceListFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("placeList",placeArrayList);
                    shoppingPlaceListFragment.setArguments(bundle);
                    shoppingPlaceListFragmentTag = shoppingPlaceListFragment.getTag();

                    photoGalleryFragment = new PhotoGalleryFragment();
                    bundle.putInt("tripId",id);
                    photoGalleryFragment.setArguments(bundle);

                    commentCategoryFragment = new CommentCategoryFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("commentList",commentArrayList);
                    commentCategoryFragment.setArguments(bundle);

                    homeCategoryFragment = new HomeCategoryFragment();
                    bundle = new Bundle();
                    bundle.putInt("userId", trip.getUserId());
                    bundle.putString("name", trip.getName());
                    bundle.putString("description",trip.getDescription());
                    bundle.putString("date",trip.getDate());
                    bundle.putString("country",trip.getCountry());
                    bundle.putString("author", trip.getAuthor_first_name() + " " + trip.getAuthor_last_name());
                    homeCategoryFragment.setArguments(bundle);
                    ft.replace(R.id.trip_content, homeCategoryFragment, HOME_FRAGMENT_TAG);
                    ft.commit();
                    tripcontent.setVisibility(View.VISIBLE);


                    /*tripName.setText(trip.getName());
                    tripCountry.setText(trip.getCountry());
                    tripDescription.setText(trip.getDescription());
                    tripAuthor.setText(Html.fromHtml("<font color=#000>par</font> <b>"+trip.getAuthor_last_name()+" "+trip.getAuthor_first_name()+" </b>"));
                    tripDate.setText(tripDate.getText()+" "+trip.getDate());
                    tripHome.setVisibility(View.VISIBLE);*/

                } else {

                    alertDialog = new AlertDialog.Builder(
                            TripActivity.this);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Une erreur s'est produite lors de la récupération du voyage");
                    alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void linkToHomeFragment(View view){
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category_blue));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        ft = getFragmentManager().beginTransaction();
        //ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, homeCategoryFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToAlbumFragment(View view){
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category_blue));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        ft = getFragmentManager().beginTransaction();
        //ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, photoGalleryFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToGastronomyFragment(View view){
        Log.e("id de la vue",""+view.getId());
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category_blue));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));

        ft = getFragmentManager().beginTransaction();
        //ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, gastronomyPlaceListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToLeisureFragment(View view){
        Log.e("id de la vue",""+view.getId());
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category_blue));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));

        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, gastronomyPlaceListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToCommentsFragment(View view){
        Log.e("id de la vue",""+view.getId());
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category_blue));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));

        //ft = getFragmentManager().beginTransaction();
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, commentCategoryFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void ChangeIcon(View view){

    }
    @Override // Fermer l'activity lorsque l'on appuie sur le bouton "back"
    public void onBackPressed() {
        this.finish();
    }



}
