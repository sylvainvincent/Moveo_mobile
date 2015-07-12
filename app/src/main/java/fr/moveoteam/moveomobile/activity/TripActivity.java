package fr.moveoteam.moveomobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.fragment.CommentCategoryFragment;
import fr.moveoteam.moveomobile.fragment.HomeCategoryFragment;
import fr.moveoteam.moveomobile.fragment.PhotoGalleryFragment;
import fr.moveoteam.moveomobile.fragment.PlaceListFragment;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;
import fr.moveoteam.moveomobile.model.Place;
import fr.moveoteam.moveomobile.model.Trip;
import fr.moveoteam.moveomobile.others.CustomScrollView;
import fr.moveoteam.moveomobile.webservice.JSONTrip;

/**
 * Created by Sylvain on 10/05/15.
 */
public class TripActivity extends Activity implements HomeCategoryFragment.OnInformationListener{

	private TextView tripName;
    private TextView tripCountry;
    private TextView tripAuthor;
    private TextView tripDate;
    private TextView tripDescription;
	
    private int id;

    RelativeLayout layout;

    private Trip trip;

    private ArrayList<Place> foodingArrayList;
    private ArrayList<Place> shoppingArrayList;
    private ArrayList<Place> leisureArrayList;

    ArrayList<Comment> commentArrayList;

    private AlertDialog.Builder alertDialog;
  

    private Bundle bundle;

    private PlaceListFragment gastronomyPlaceListFragment;
    private PlaceListFragment leisurePlaceListFragment;
    private PlaceListFragment shoppingPlaceListFragment;

    private Fragment fragment;

    private PhotoGalleryFragment photoGalleryFragment;
    private CommentCategoryFragment commentCategoryFragment;
    String gastronomyPlaceListFragmentTag;
    String leisurePlaceListFragmentTag;
    String shoppingPlaceListFragmentTag;
    private HomeCategoryFragment homeCategoryFragment;
    String  tripHomeFragmentTag;
    private ImageView homeCategory;
    private ImageView foodingCategory;
    private ImageView hobbiesCategory;
    private ImageView shoppingCategory;
    private ImageView picturesCategory;
    private ImageView commentsCategory;
    private ImageView imageCover;

    FragmentManager fragmentManager;
    private FragmentTransaction ft;

    private static final String HOME_FRAGMENT_TAG = "HOME";
    private LinearLayout tripcontent;

    private CustomScrollView scrollView;

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
        homeCategory = (ImageView) findViewById(R.id.home_category);
        foodingCategory = (ImageView) findViewById(R.id.fooding_category);
        hobbiesCategory = (ImageView) findViewById(R.id.hobbies_category);
        commentsCategory = (ImageView) findViewById(R.id.comments_category);
        picturesCategory = (ImageView) findViewById(R.id.pictures_category);
        shoppingCategory = (ImageView) findViewById(R.id.shopping_category);
        imageCover = (ImageView) findViewById(R.id.image_cover);
        tripcontent = (LinearLayout) findViewById(R.id.trip_content);
        scrollView = (CustomScrollView) findViewById(R.id.cover);
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
        JSONArray placeList;

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
        protected void onCancelled() {
            finish();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if(json == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TripActivity.this);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
                    builder.setMessage("Récupération du voyage échoué");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();

                }else if (json.getString("success").equals("1")) {

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

                    if(!json.getString("fooding").equals("0")){
                        JSONArray placeList = json.getJSONArray("fooding");
                        foodingArrayList = new ArrayList<>(placeList.length());
                        for (int i = 0; i < placeList.length(); i++) {
                            foodingArrayList.add(new Place(
                                    placeList.getJSONObject(i).getInt("place_id"),
                                    placeList.getJSONObject(i).getString("place_name"),
                                    placeList.getJSONObject(i).getString("place_address"),
                                    placeList.getJSONObject(i).getString("place_description"),
                                    placeList.getJSONObject(i).getInt("category_id")
                            ));
                            Log.e("fooding", foodingArrayList.get(i).toString());
                        }
                    }

                    if(!json.getString("shopping").equals("0")){
                        placeList = json.getJSONArray("shopping");
                        shoppingArrayList = new ArrayList<>(placeList.length());
                        for (int i = 0; i < placeList.length(); i++) {
                            shoppingArrayList.add(new Place(
                                    placeList.getJSONObject(i).getInt("place_id"),
                                    placeList.getJSONObject(i).getString("place_name"),
                                    placeList.getJSONObject(i).getString("place_address"),
                                    placeList.getJSONObject(i).getString("place_description"),
                                    placeList.getJSONObject(i).getInt("category_id")
                            ));
                            Log.e("shopping", shoppingArrayList.get(i).toString());
                        }
                    }

                    if(!json.getString("leisure").equals("0")){
                        JSONArray placeList = json.getJSONArray("leisure");
                        leisureArrayList = new ArrayList<>(placeList.length());
                        for (int i = 0; i < placeList.length(); i++) {
                            leisureArrayList.add(new Place(
                                    placeList.getJSONObject(i).getInt("place_id"),
                                    placeList.getJSONObject(i).getString("place_name"),
                                    placeList.getJSONObject(i).getString("place_address"),
                                    placeList.getJSONObject(i).getString("place_description"),
                                    placeList.getJSONObject(i).getInt("category_id")
                            ));
                            Log.e("leisure", leisureArrayList.get(i).toString());
                        }
                    }

                    /*if(json.getString("comment").equals("1")){
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

                        }



                    }*/

                    homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category_blue));
                    imageCover.setImageBitmap(Function.decodeBase64(trip.getCover()));
                    imageCover.setVisibility(View.VISIBLE);

                    fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    gastronomyPlaceListFragment = new PlaceListFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("placeList", foodingArrayList);
                    gastronomyPlaceListFragment.setArguments(bundle);
                    //ft.add(gastronomyPlaceListFragment, "gastronomy");


                    leisurePlaceListFragment = new PlaceListFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("placeList",leisureArrayList);
                    leisurePlaceListFragment.setArguments(bundle);
                    //leisurePlaceListFragmentTag = leisurePlaceListFragment.getTag();

                    shoppingPlaceListFragment = new PlaceListFragment();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("placeList",shoppingArrayList);
                    shoppingPlaceListFragment.setArguments(bundle);
                    //shoppingPlaceListFragmentTag = shoppingPlaceListFragment.getTag();

                    photoGalleryFragment = new PhotoGalleryFragment();
                    bundle = new Bundle();
                    bundle.putInt("tripId",id);
                    photoGalleryFragment.setArguments(bundle);

                    commentCategoryFragment = new CommentCategoryFragment();
                    bundle = new Bundle();
                    bundle.putInt("tripId",id);
                   // bundle.putParcelableArrayList("commentList",commentArrayList); // A enlever
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
                    fragment = homeCategoryFragment;
                    ft.replace(R.id.trip_content, homeCategoryFragment, HOME_FRAGMENT_TAG);
                    ft.commit();
                    tripcontent.setVisibility(View.VISIBLE);
                    scrollView.setEnableScrolling(true);


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
        shoppingCategory.setImageDrawable(getResources().getDrawable(R.drawable.shopping_category));

        ft = getFragmentManager().beginTransaction();
        fragment = homeCategoryFragment;
        // ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, fragment);
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
        shoppingCategory.setImageDrawable(getResources().getDrawable(R.drawable.shopping_category));

        ft = getFragmentManager().beginTransaction();
        fragment = photoGalleryFragment;
        //ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToGastronomyFragment(View view){

        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category_blue));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));
        shoppingCategory.setImageDrawable(getResources().getDrawable(R.drawable.shopping_category));


        ft = getFragmentManager().beginTransaction();

        fragment = gastronomyPlaceListFragment;
        //ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.trip_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToLeisureFragment(View view){

        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category_blue));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));
        shoppingCategory.setImageDrawable(getResources().getDrawable(R.drawable.shopping_category));

        ft = getFragmentManager().beginTransaction();
        //if(fragment != null)ft.remove(fragment);
        fragment = leisurePlaceListFragment;
        ft.replace(R.id.trip_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToShoppingFragment(View view) {

        shoppingCategory.setImageDrawable(getResources().getDrawable(R.drawable.shopping_category_blue));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));

        ft = getFragmentManager().beginTransaction();
        //if(fragment != null)ft.remove(fragment);
        fragment = shoppingPlaceListFragment;
        ft.replace(R.id.trip_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void linkToCommentsFragment(View view){

        scrollView.setEnableScrolling(false);
        commentsCategory.setImageDrawable(getResources().getDrawable(R.drawable.comments_category_blue));
        hobbiesCategory.setImageDrawable(getResources().getDrawable(R.drawable.hobbies_category));
        foodingCategory.setImageDrawable(getResources().getDrawable(R.drawable.fooding_category));
        homeCategory.setImageDrawable(getResources().getDrawable(R.drawable.home_category));
        picturesCategory.setImageDrawable(getResources().getDrawable(R.drawable.pictures_category));
        shoppingCategory.setImageDrawable(getResources().getDrawable(R.drawable.shopping_category));

        //ft = getFragmentManager().beginTransaction();
        ft = getFragmentManager().beginTransaction();
        fragment = commentCategoryFragment;

        ft.replace(R.id.trip_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override // Fermer l'activity lorsque l'on appuie sur le bouton "back"
    public void onBackPressed() {

        this.finish();
    }

    public int getId() {
        return id;
    }

    public void refreshFragment(){
        ft = getFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }
}
