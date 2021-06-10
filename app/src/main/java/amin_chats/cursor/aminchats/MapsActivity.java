package amin_chats.cursor.aminchats;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.nearby.messages.Distance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.JsonArray;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amin.bidding.BiddingFragment;
import amin_chats.share.BiddingDialog;
import amin_chats.share.ErrorModalShow;
import amin_chats.share.LatLngInterpolator;
import amin_chats.share.LocationSearch;
import amin_chats.share.LocationSearchAdapter;
import amin_chats.share.MarkerAnimation;
import amin_chats.share.MyDividerItemDecoration;
import amin_chats.share.PlaceArrayAdapter;
import amin_chats.share.RideServiceAdapter;
import amin_chats.share.RideServiceInfo;
import amin_chats.share.RideServiceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,LocationSearchAdapter.ContactsAdapterListener,
        RideServiceAdapter.ContactsAdapterListenerRide,GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {
    PolylineOptions popt;
    private GoogleMap mMap,rPickupMap;
    private final static int MY_PERMISSION_REQUEST_CODE=7192;
    private final static int PLAY_SERVICES_RESULATION_REQUEST=300193;
    private final static int UpdateInterval=5000;
    private final static int FastedInterval=3000;
    private final static int Displacement=10;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    Button btnFindDriver,btnSendBiddingRate,btnBiddingNow,btnReqCancel,btnLocationConfirm;
    EditText et_send_bidding_rate;
    DatabaseReference ref;
    GeoFire geoFire;
    Marker mCurrent;
    double latitude=22.3353526; double lognitude=91.81275979999998;
    HashMap<String,HashMap<String,String>> avail_driver_list;
    boolean isDriverFound=false;
    long count = 0;
    TextView txtRate,countNearestDriver,searchText;
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    public RecyclerView myRecylerView ;
    public List<BiddingMsg> MessageList ;
    public BiddingAdapter biddingAdapter;

    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    public AutoCompleteTextView mAutocompleteTextView,mAutoCompleteDestination;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;
    private GoogleApiClient mGoogleApiClient,mGoogleApiClient2;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(88.08, 20.67), new LatLng(92.67, 26.45));

    String isDriverConnected="0";
    List<PolylineOptions> lines = new ArrayList<PolylineOptions>();
    private BottomSheetBehavior sheetBehavior;
    private Button btn_bottom_sheet;
    public LinearLayout bottom_sheet,lay_ride_confirm;
    NestedScrollView nestedScrollView;
    private FrameLayout fragment_container;
    AppBarLayout app_lay;
    RelativeLayout relCollapseLayout,rPckupLayout,bottom_sheet_location_confirm;
    boolean isVisible=true;
    Thread thread;int init=0;
    private List<amin_chats.share.Location> dataArrayList;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    double route_distance=0.0;
    private List<amin_chats.share.Location> locationList;
    private List<RideServiceList> listRideServieList;
    private LocationSearchAdapter mAdapter;
    private RideServiceAdapter mAdapterRide;
    RecyclerView recyclerView,recycleServiceList;
    private HashMap<String, Marker> mMarkersSelectTrack = new HashMap<>();
    private HashMap<String, Marker> mMarkerSourceDestination = new HashMap<>();
    String code = "01";
    private BottomSheetBehavior mBottomSheetBehavior1,mBottomSheetConfirm;
    LinearLayout tapactionlayout;
    View white_forground_view;
    View bottomSheet,bottomSheetConfirm;
    boolean isPickupSearchBoolean=true,isDestinationSearchBoolean=false;
    ProgressBar progressBar;
    int pickup_position=-1,destination_position=-1,select_flag=0;
    RideServiceInfo r_service_list;
    int isLocationConfirm=0;
    public Button btnRideConfirm;
    BiddingDialog newFragment;
    BiddingFragment fragBidding;
    SharedPreferences sp_exist;
    SharedPreferences.Editor edit_exist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_map_layout);
        hideKeyPad();
      //  rPckupLayout=(RelativeLayout)findViewById(R.id.posting_lay_id);

        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .auto_com_source);
        btnRideConfirm=(Button)findViewById(R.id.btnRideConfirm);
        lay_ride_confirm=(LinearLayout)findViewById(R.id.lay_ride_confirm);
        btnRideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBiddingDialog("0");
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        route_distance=0.0;
        btnLocationConfirm=(Button)findViewById(R.id.btnLocationConfirm);
        btnLocationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationSearchBar(1);

            }
        });
        showProgress();
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle("Search here");

        avail_driver_list=new HashMap<>();
        locationList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);
        recycleServiceList = (RecyclerView) findViewById(R.id.recycle_service_list);


        bottom_sheet_location_confirm=(RelativeLayout)findViewById(R.id.bottom_sheet_location_confirm);
        //tapactionlayout = (LinearLayout) findViewById(R.id.tap_action_layout);
        mAutoCompleteDestination=(AutoCompleteTextView)findViewById(R.id.auto_com_destination);
        bottomSheet = findViewById(R.id.bottom_sheet_location_search);
        bottomSheetConfirm = findViewById(R.id.bottom_sheet_confirm);

        //mBottomSheetConfirm = BottomSheetBehavior.from(bottomSheetConfirm);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        //bottomSheetConfirm = BottomSheetBehavior.from(bottomSheetConfirm);
        mBottomSheetBehavior1.setPeekHeight(700);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    //tapactionlayout.setVisibility(View.VISIBLE);
                }

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //tapactionlayout.setVisibility(View.GONE);
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    // tapactionlayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mAutoCompleteDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDestinationSearchBoolean=true;
                isPickupSearchBoolean=false;
                onClickAutoComplete();
            }
        });
        mAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDestinationSearchBoolean=false;
                isPickupSearchBoolean=true;
                onClickAutoComplete();
            }
        });

        mAutocompleteTextView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                isDestinationSearchBoolean=false;
                isPickupSearchBoolean=true;
                onKeyLocationSearch(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {

            }

            public void afterTextChanged(Editable s)
            {
                isDestinationSearchBoolean=false;
                isPickupSearchBoolean=true;
            }
        });
        mAutoCompleteDestination.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                isDestinationSearchBoolean=true;
                isPickupSearchBoolean=false;
                onKeyLocationSearch(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {

            }

            public void afterTextChanged(Editable s)
            {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("driver_avail");
        geoFire=new GeoFire(ref);
        setUpLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        uploadLocationList();
    }
    public void confirmRideStartAndUpdate(String rider_id)
    {
        //displayLocation();
         init=0;
        String c_ride_id="-1";
         sp_exist = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
         edit_exist = sp_exist.edit();
        isDriverConnected="0";
        if(rider_id.equalsIgnoreCase(""))
        {
              //getOurServiceListInit(1);
            c_ride_id=sp_exist.getString("c_ride_id","0");
            System.out.println("ride id="+c_ride_id);
            isDriverConnected=c_ride_id;
        }
        else{
            init=1;
            c_ride_id=rider_id;
            edit_exist.putString("c_ride_id", rider_id);
            edit_exist.commit();
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child("drivers").
                child(c_ride_id).child("customer_req");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    openBottomSheetForRent();
                    Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                    String c_rate=td.get("c_rate").toString();
                    String d_rate=td.get("d_rate").toString();
                    String sender=td.get("sender").toString();
                    String driver_id=td.get("driver_id").toString();
                    String status=td.get("status").toString();
                    String pickup_id=td.get("pickup_id").toString();
                    String destination_id=td.get("destination_id").toString();
                    pickup_position=Integer.valueOf(pickup_id);
                    destination_position=Integer.valueOf(destination_id);

                    isDriverConnected=driver_id;
                     if(status.equalsIgnoreCase("1"))
                    {
                        showBiddingDialog(isDriverConnected);
//                        if(init == 0)
//                        {
//                            showBiddingDialog(isDriverConnected);
//                        }
                    }
                    else if(status.equalsIgnoreCase("2"))
                    {
                        if(td.get("location") != null) {
                            if(init != 0)
                            {
                                newFragment.dismiss();
                                showBoomSheetInit();
                                mAutoCompleteDestination.setText("");
                                mAutocompleteTextView.setText("");
                            }
                            bottomSheetConfirm.setVisibility(View.GONE);

                            Map<String, Object> tdd = (HashMap<String, Object>) td.get("location");
                            if(tdd.get("driver_latitude") != null && tdd.get("driver_lognitude") != null) {
                                String lati_des = tdd.get("driver_latitude").toString();
                                String logni_des = tdd.get("driver_lognitude").toString();
                                LatLng lat = new LatLng(Double.parseDouble(lati_des), Double.parseDouble(logni_des));

                                if (mMarkersSelectTrack.get(code) != null) {
                                    float bearing = (float) bearingBetweenLocations(mMarkersSelectTrack.get(code).getPosition(), lat);
                                    MarkerAnimation.rotateMarker(mMarkersSelectTrack.get(code), bearing, lat, new LatLngInterpolator.Spherical());
                                } else {
                                    createRoute();
                                    mMarkersSelectTrack.put(code, mMap.addMarker(new MarkerOptions()
                                            .position(lat)
                                            .snippet("On the way")
                                            .title("On the way")
                                            .draggable(true)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                                    );

                                }
                            }
                        }
                    }
                    else{
                         edit_exist.putString("c_ride_id", "0");
                         edit_exist.commit();
                     }

                    /*Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                    String lati_des=td.get("driver_latitude").toString();
                    String logni_des=td.get("driver_lognitude").toString();
                    LatLng lat=new LatLng(Double.parseDouble(lati_des),Double.parseDouble(logni_des));
                    if(mMarkersSelectTrack.get(code) != null) {
                        float bearing = (float) bearingBetweenLocations(mMarkersSelectTrack.get(code).getPosition(), lat);
                        MarkerAnimation.rotateMarker(mMarkersSelectTrack.get(code), bearing, lat, new LatLngInterpolator.Spherical());
                    }*/
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            };
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(newFragment != null){
           // newFragment.dismiss();
        }
    }

    private void showLocationSearchBar(int status)
    {


        LatLng center = mMap.getCameraPosition().target;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(center.latitude, center.longitude, 1);
            String knownName="";
            if(addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                knownName = addresses.get(0).getFeatureName();



                if (select_flag == 1) {
                    //bottom_sheet_location_confirm.setVisibility(View.GONE);
                    locationList.get(pickup_position).setLatitude(String.valueOf(addresses.get(0).getLatitude()));
                    locationList.get(pickup_position).setLongnitute(String.valueOf(addresses.get(0).getLongitude()));
                    locationList.get(pickup_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    locationList.get(pickup_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));

                    mAutocompleteTextView.setText(knownName);

                    showPickupGoogleMap();
                    isDestinationSearchBoolean = true;
                } else if (select_flag == 2) {
                    locationList.get(destination_position).setLatitude(String.valueOf(addresses.get(0).getLatitude()));
                    locationList.get(destination_position).setLongnitute(String.valueOf(addresses.get(0).getLongitude()));
                    locationList.get(destination_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    locationList.get(destination_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    bottom_sheet_location_confirm.setVisibility(View.GONE);
                    mAutoCompleteDestination.setText(knownName);
                    openBottomSheetForRent();
                    searchDriverForRoute();
                }
            }
            else
                Toast.makeText(getApplicationContext(),"Location unknown...",Toast.LENGTH_LONG).show();
           // System.out.println("select id "+select_flag);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void showBiddingDialog(String isConnected) {
        System.out.println("bidding id = "+isConnected);
        if(mLastLocation != null && mLastLocation != null)
        {
            if(listRideServieList == null){
                //System.out.println("bidding id 2= "+isConnected);
                getOurServiceListInit(Integer.valueOf(isConnected));
            }
            else {
                //System.out.println("bidding id 3 = "+isConnected);

                //bottomSheetConfirm.setVisibility(View.GONE);
                ArrayList<RideServiceList> al_HOSPITAL = new ArrayList<>(listRideServieList.size());
                al_HOSPITAL.addAll(listRideServieList);
                ArrayList<amin_chats.share.Location> send_location_list = new ArrayList<>(locationList.size());
                send_location_list.addAll(locationList);
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("c_loti", String.valueOf(mLastLocation.getLatitude()));
                bundle.putString("c_logni", String.valueOf(mLastLocation.getLongitude()));
                bundle.putString("route_distance", String.valueOf(route_distance));
                bundle.putString("p_position", String.valueOf(pickup_position));
                bundle.putString("d_position", String.valueOf(destination_position));
                bundle.putString("isConnected", String.valueOf(isConnected));
                // bundle.putString("per_km",String.valueOf(listRideServieList.get()));
                //bundle.putString("position",String.valueOf(mAdapterRide.getRow_index()));
                bundle.putString("position", "0");
                bundle.putSerializable("list", al_HOSPITAL);
                bundle.putSerializable("l_list", send_location_list);

                fragBidding=new BiddingFragment();
                android.app.FragmentTransaction fft = getFragmentManager().beginTransaction();
                fragBidding.setArguments(bundle);
                fragBidding.show(fft, "FullScreenDialog");

                /*newFragment=new BiddingDialog();
                android.app.FragmentTransaction fft = getFragmentManager().beginTransaction();
                newFragment.setArguments(bundle);
                newFragment.show(fft, "FullScreenDialog");*/
                //newFragment = BiddingDialog.newInstance();
                //newFragment.show(ft, "FullScreenDialog");
            }
        }
        else{
            Toast.makeText(this,"Maybe current location has been removed,please try again...",Toast.LENGTH_LONG).show();
        }

    }
    private void removeProgress()
    {
        progressBar.setVisibility(View.GONE);
    }
    private void showProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideKeyPad()
    {
        try {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        catch (Exception e)
            {
               // TODO: handle exception
            }
    }

    @Override
    public void onBackPressed() {
        if(mBottomSheetBehavior1.getState()==BottomSheetBehavior.STATE_COLLAPSED) {
            super.onBackPressed();
        }
        else{
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void openBottomSheetForRent(){

        bottomSheet.setVisibility(View.GONE);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    public void showBoomSheetInit(){
       // mMap.clear();

        mMarkersSelectTrack.get("s").remove();
        mMarkersSelectTrack.get("d").remove();
        mMarkersSelectTrack.get(code).remove();
        lines.get(0).visible(false);

        bottomSheet.setVisibility(View.VISIBLE);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    public void showPickupGoogleMap()
    {

        bottomSheet.setVisibility(View.VISIBLE);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

    }
    public void onKeyLocationSearch(String s)
    {
        if (!s.toString().equals("")) {
            mAdapter.getFilter().filter(s.toString());
        }else{
            //uploadLocationList();
        }
    }
    public void onClickAutoComplete(){
        if(mBottomSheetBehavior1.getState()==BottomSheetBehavior.STATE_COLLAPSED)
        {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

        }

    }

    @Override
    public void onContactSelectedRide(RideServiceList contact) {

    }
    private void searchDriverForRoute(){
        hideKeyPad();
        getOurServiceList();
        findDriver();
    }
    @Override
    public void onContactSelected(amin_chats.share.Location c) {
        //System.out.println("list = "+c.getAddress());
        openBottomSheetForRent();
        bottom_sheet_location_confirm.setVisibility(View.VISIBLE);
        LatLng l=new LatLng(Double.parseDouble(c.getLatitude()),Double.parseDouble(c.getLongnitute()));
        if(isDestinationSearchBoolean)
        {
            destination_position=Integer.parseInt(c.getId());
            mAutoCompleteDestination.setText(c.getAddress());
            select_flag=2;
           // searchDriverForRoute();
        }
        else if(isPickupSearchBoolean){
            select_flag=1;
            pickup_position=Integer.parseInt(c.getId());
            mAutocompleteTextView.setText(c.getAddress());
        }

        //System.out.println("asss");
        hideKeyPad();
        //LatLng ll=new LatLng(Double.parseDouble(c.getLatitude()),Double.parseDouble(c.getLongnitute()));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(l).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        setLocationOnMap(l);
    }

    private void setLocationOnMap(LatLng l){
       /* if(pickup_position > 0 && destination_position > 0)
        {
            isLocationConfirm=1;

        }
        else*/
        if(isDestinationSearchBoolean)
        {
            // showPickupGoogleMap();
            btnLocationConfirm.setText("Confirm Destination");
            isPickupSearchBoolean=true;
            isDestinationSearchBoolean=false;
            setMapPosition("Drag the map to change your destination position",l);

        }
        else{
            isPickupSearchBoolean=false;
            isDestinationSearchBoolean=true;
            btnLocationConfirm.setText("Confirm Pickup");
            //isDestinationSearchBoolean=false;
            //showPickupGoogleMap();
            setMapPosition("Drag the map to change your pickup position",l);
        }
    }
    private void setMapPosition(String title,LatLng lat)
    {
       // System.out.println("add new marker...."+isDestinationSearchBoolean+","+isPickupSearchBoolean+","+isLocationConfirm);
        if((isDestinationSearchBoolean || isPickupSearchBoolean)) {


            if (!mMarkersSelectTrack.containsKey(code)) {
               // System.out.println("add new marker....");
                mMarkersSelectTrack.put(code, mMap.addMarker(new MarkerOptions()
                        .position(lat)
                        .snippet(title)
                        .title(title)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                );
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(lat).zoom(18).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                // setLocationAddress(center.latitude,center.longitude,etPickup);
            } else if(mMarkersSelectTrack.get(code) != null){
                //LatLng center = mMap.getCameraPosition().target;

                float bearing = (float) bearingBetweenLocations(mMarkersSelectTrack.get(code).getPosition(), lat);
                MarkerAnimation.rotateMarker(mMarkersSelectTrack.get(code), bearing, lat, new LatLngInterpolator.Spherical());
                //setLocationAddress(center.latitude,center.longitude,etPickup);
            }
        }
    }
    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }


    private void setMarkerToCenter()
    {
        LatLng center = mMap.getCameraPosition().target;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(center.latitude,center.longitude))
                .title("Pickup Point")
                .snippet("Drag the map to change you Pickup Position")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap = null;
    }

    private void createRoute(){

        String lati= locationList.get(pickup_position).getLatitude();
        String logni= locationList.get(pickup_position).getLongnitute();

        String lati_des= locationList.get(destination_position).getLatitude();
        String logni_des= locationList.get(destination_position).getLongnitute();

        LatLng latLng1 = new LatLng(Double.parseDouble(lati), Double.parseDouble(logni)); // New York
        LatLng latLng2 = new LatLng(Double.parseDouble(lati_des), Double.parseDouble(logni_des)); // London

        mMarkersSelectTrack.put("s", mMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .snippet("Pickup")
                .title("Pickup")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        );
        mMarkersSelectTrack.put("d", mMap.addMarker(new MarkerOptions()
                .position(latLng2)
                .snippet("Destination")
                .title("Destination")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        );

    //    Marker marker1 = mMap.addMarker(new MarkerOptions().position(latLng1).title("Pickup"));
     //   Marker marker2 = mMap.addMarker(new MarkerOptions().position(latLng2).title("Destination"));

        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30),new Dash(40));
        /* popt = new PolylineOptions().add(latLng1).add(latLng2)
                .width(10).color(Color.MAGENTA).pattern(pattern)
                .geodesic(true);
        mMap.addPolyline(popt);*/

        lines.add(new PolylineOptions().add(latLng1).add(latLng2)
                .width(10).color(Color.MAGENTA).pattern(pattern)
                .geodesic(true));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mMarkersSelectTrack.get("s").getPosition());
        builder.include(mMarkersSelectTrack.get("d").getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        mMap.animateCamera(cu);

        Location locationA = new Location("point A");
        locationA.setLatitude(Double.parseDouble(lati));
        locationA.setLongitude(Double.parseDouble(lati));
        Location locationB = new Location("point B");
        locationB.setLatitude(Double.parseDouble(lati));
        locationB.setLongitude(Double.parseDouble(logni_des));
        //float distance = locationA.distanceTo(locationB);
         route_distance = SphericalUtil.computeDistanceBetween(new LatLng(Double.parseDouble(lati),Double.parseDouble(logni)),
                new LatLng(Double.parseDouble(lati_des),Double.parseDouble(logni_des)));

        updateRideServiceRecycleView(route_distance);
        removeProgress();
    }
    private void getOurServiceList(){

        openBottomSheetForRent();
        showProgress();
        getOurServiceListInit(0);
    }
    private void getOurServiceListInit(final int status){
        Call<RideServiceInfo> call1 = apiInterface.getRideServiceList();
        call1.enqueue(new Callback<RideServiceInfo>() {
            @Override
            public void onResponse(Call<RideServiceInfo> call, Response<RideServiceInfo> response)
            {
                r_service_list = response.body();
                listRideServieList=r_service_list.getList();
                if(r_service_list.getStatus().equalsIgnoreCase("1"))
                {
                    if(status == 0)
                        createRoute();
                    else if (status == 1){
                        showBiddingDialog(String.valueOf(status));
                    }
                }
                else
                {
                    Toast.makeText(MapsActivity.this,r_service_list.getMsg(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<RideServiceInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
    private void updateRideServiceRecycleView(double distance){


        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MapsActivity.this,
                LinearLayoutManager.HORIZONTAL, false);

        bottomSheetConfirm.setVisibility(View.VISIBLE);
        //mBottomSheetConfirm.setPeekHeight(550);
        mAdapterRide = new RideServiceAdapter(this,listRideServieList,this,distance,lay_ride_confirm);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleServiceList.setLayoutManager(mLayoutManager);
        recycleServiceList.setItemAnimator(new DefaultItemAnimator());
        recycleServiceList.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.HORIZONTAL, 36));
        recycleServiceList.setLayoutManager(horizontalLayoutManagaer);
        recycleServiceList.setAdapter(mAdapterRide);
    }
    public void getJsonObject(String obj){
      /*  try
        {
           *//* // categoryLists=new ArrayList<>();
            prepareListData(new JSONObject(obj));
            updateRecycleView();
//            this.mAdapter.notifyDataSetChanged();
            String txt=searchView.getQuery().toString();
            System.out.println("data search = "+txt);
            Search.this.mAdapter.getFilter().filter(txt);
            //System.out.println("data set = "+new JSONObject(obj));*//*
        }
        catch (JSONException e){
            e.printStackTrace();
        }*/
    }
    private void uploadLocationList(){


        //  System.out.println("show list");
/*        dataArrayList = new ArrayList<>();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.supporta2z.com/").
                addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface requestInterface=retrofit.create(APIInterface.class);
        Call<List<LocationSearch>> call= requestInterface.doGetLocationList("ssd");
        call.enqueue(new Callback<List<LocationSearch>>() {
            @Override
            public void onResponse(Call<List<LocationSearch>> call, Response<List<LocationSearch>> response) {
                dataArrayList = response.body();
                Log.e("hello","ass");
                //dataAdapter=new DataAdapter(getApplicationContext(),dataArrayList);
                //recyclerView.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<List<LocationSearch>> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });*/
        locationList=new ArrayList<>();
        LocationSearch user = new LocationSearch("hello");
        Call<LocationSearch> call = apiInterface.doGetLocationList("jj");
        call.enqueue(new Callback<LocationSearch>() {
            @Override
            public void onResponse(Call<LocationSearch> call, Response<LocationSearch> response) {
                // Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    LocationSearch list=response.body();
                    locationList=list.getList();
                        /*LocationSearch list=response.body();
                        List<amin_chats.share.Location> listss=list.getList();
                        for (amin_chats.share.Location c : listss) {
                            locationList.add(new amin_chats.share.Location(c.getId(),
                                    c.getAddress()));
                        }*/
                    updateRecycleView();
                    removeProgress();
                    // JSONObject jj=new JSONObject(""+listss);

                       /* JSONArray nextCategoryJson = response.body().getJSONArray("list");
                        for (int i = 0; i < nextCategoryJson.length(); i++) {
                            JSONObject c = nextCategoryJson.getJSONObject(i);
                            locationList.add(new LocationSearch(c.getString("id"),
                                    c.getString("address")));
                        }
                        //JsonArray user_array= response.
                        updateRecycleView();*/

                }
            }

            @Override
            public void onFailure(Call<LocationSearch> call, Throwable t) {
                //ErrorModalShow cdd=new ErrorModalShow(getApplicationContext());
                //cdd.show();
                //Log.i("onEmptyResponse", ""+t.getMessage());
                Toast.makeText(getApplicationContext(),"please try again....",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void updateRecycleView(){
        mAdapter = new LocationSearchAdapter(getApplicationContext(),locationList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //*final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);

            /*final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient2, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);*/
        }
    };

    @Override
    public void onStart()
    {
        super.onStart();
        if(this.mGoogleApiClient != null)
        {
            this.mGoogleApiClient.connect();
        }
        if(this.mGoogleApiClient2 != null)
        {
            //this.mGoogleApiClient2.connect();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if (mGoogleApiClient2 != null) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient2, this);
        }
    }
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
//                Log.e(LOG_TAG, "Place query did not complete. Error: " +
//                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

    private void cancelReq(){

    }
    private void updateDriverLocation(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driver_avail");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String key = dataSnapshot.getKey();
                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                    //System.out.println("map value = "+dataSnapshot.get);
                    //Double lat = Double.parseDouble(value.get("latitude").toString());
                    //Double lng = Double.parseDouble(value.get("longitude").toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void openChatBox(){

        HashMap<String,String> dInfo=avail_driver_list.get("1");
        if(dInfo != null) {
            String key = dInfo.get("driver_id");
            Intent i = new Intent(MapsActivity.this, BiddingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("key",key);
            startActivity(i);
        }
        else
            Toast.makeText(this,"Please cancel this req...",Toast.LENGTH_LONG).show();
    }
    private void btnSendBiddingRate()
    {
        HashMap<String,String> dInfo=avail_driver_list.get("1");
        String rate=et_send_bidding_rate.getText().toString();
        if(!rate.equalsIgnoreCase("0") && !rate.equalsIgnoreCase("") && dInfo != null)
        {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            final String sender_id=sp.getString("user_id","0");
            String key = dInfo.get("driver_id");
            ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req").child("d_rate");
            ref.setValue(rate);
            ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req").child("sender");
            ref.setValue(sender_id);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlayServices()){
                        buildGoogleApiClient();
                        CreateLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private  void setUpLocation(){
        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED)
        {

            if(checkPlayServices())
            {
                buildGoogleApiClient();
                CreateLocationRequest();
                getBidingTrans();
                displayLocation();
            }

            //startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_REQUEST_CODE);
        }
    }
    private void showAvailDriverList(GeoLocation location,String key){

        LatLng latLng = new LatLng(location.latitude, location.longitude);
        if (!mMarkers.containsKey(key))
        {
            mMarkers.put(key, mMap.addMarker(
                    new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng)
            ));
        }
        else{
            MarkerAnimation.animateMarkerToGB(mMarkers.get(key), latLng, new LatLngInterpolator.Spherical());
        }


    }
    private void removeMarkerFromMap(String key)
    {
        System.out.println("key="+key);
        Marker marker = mMarkers.get(key);
        marker.remove();
        mMarkers.get(key).remove();
    }
    private void addMapCircle(){

        final double latitude = mLastLocation.getLatitude();
        final double lognitude = mLastLocation.getLongitude();
        LatLng dangerous=new LatLng(latitude,lognitude);
        mMap.addCircle(new CircleOptions() //500 means metter
                .center(dangerous).radius(5000).strokeColor(Color.BLUE)
                .fillColor(0x220000FF).strokeWidth(0.5f)
        );
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(dangerous.latitude,dangerous.longitude),5.0f);; //5km
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                // showAvailDriverList(location,key);
                //System.out.println("send notification1...entire danger"+key+"-"+location.latitude);
            }
            @Override
            public void onKeyExited(String key) {
                //System.out.println("send notification...out of danger");
                // removeMarkerFromMap(key);
            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                //System.out.println("send notification...move within the danger area");
                //showAvailDriverList(location,key);
            }

            @Override
            public void onGeoQueryReady() {
                //updateDriverLocation();
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private void displayLocation(){
        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // System.out.println("hellow");
        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //uploadDataSet();
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //System.out.println("isUpdate 23"+mLastLocation);
        if(mLastLocation != null){
            //findDriver();
            final double latitude=mLastLocation.getLatitude();
            final double lognitude=mLastLocation.getLongitude();
            if(mCurrent != null)
                mCurrent.remove();
            mCurrent=mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude,lognitude))
                    .title("You"));


            //  addMapCircle();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,lognitude),12.0f));
            confirmRideStartAndUpdate("");

            System.out.println("Your location was changed : "+latitude+","+lognitude);
        }
        else{
            System.out.println("Can't get your location ");
        }
    }
    private void CreateLocationRequest(){
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UpdateInterval);
        mLocationRequest.setFastestInterval(FastedInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Displacement);
    }
    private synchronized  void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

       /* mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient2.connect();*/
    }
    public boolean checkPlayServices(){

        //System.out.println("isUpdate 1");

        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            //System.out.println("isUpdate 2");
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESULATION_REQUEST).show();
            }
            else{
                Toast.makeText(this,"this device is not supported",Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    public void onConnected(Bundle bundle) {
        //mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient2);
        displayLocation();
        startDisplayLocationUpdates();
        //checkPermissions();
    }
    private void startDisplayLocationUpdates(){
        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED)
            return;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient2,mLocationRequest,this);
    }
    @Override
    public void onConnectionSuspended(int i) {
        //mPlaceArrayAdapter.setGoogleApiClient(null);
        mGoogleApiClient.connect();
        // mGoogleApiClient2.connect();
        //Do whatever you need
        //You can display a message here
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        displayLocation();
    }

    private void getBidingTrans(){
        HashMap<String,String> dInfo=avail_driver_list.get("1");
        if(dInfo != null) {
            String key = dInfo.get("driver_id");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                        String c_rate=td.get("d_rate").toString();
                        String d_rate=td.get("d_rate").toString();
                        String sender=td.get("sender").toString();
                        String driver_id=td.get("driver_id").toString();

                        // btnBiddingNow.setVisibility(View.VISIBLE);
                        //  btnFindDriver.setVisibility(View.GONE);


                        /*BiddingMsg m=new BiddingMsg(c_rate,d_rate,sender,driver_id);
                        MessageList.add(m);
                        biddingAdapter = new BiddingAdapter(MessageList,getApplicationContext());
                        biddingAdapter.notifyDataSetChanged();
                        myRecylerView.setAdapter(biddingAdapter);*/

                        //txtRate.setText("Bidding Rate : "+td.get("d_rate").toString());
                        //List<Object> values = td.values();
                        //txtRate.setText("Bidding Rate : "+dataSnapshot.);
                        //BiddingMsg m=new BiddingMsg();


                    } else
                    {
                        //txtRate.setText("Bidding Rate : 0");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendReqToDriver(){

        //System.out.println("obj1 = "+avail_driver_list);

        for (Object objectName : avail_driver_list.keySet())
        {
            //System.out.println("obj = "+objectName);
            //System.out.println("obj details = "+avail_driver_list.get(objectName).get("u_lati"));
        }

        HashMap<String,String> dInfo=avail_driver_list.get("1");
        String key=dInfo.get("driver_id");
        String uid=dInfo.get("uid");
        String driver_id=dInfo.get("driver_id");
        String u_lati=dInfo.get("u_lati");
        String u_logni=dInfo.get("u_logni");
        String d_lati=dInfo.get("d_lati");
        String d_logni=dInfo.get("d_logni");
        String distance=dInfo.get("distance");
        String sender=dInfo.get("sender");


        ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key);
        HashMap<String, Object> result = new HashMap<>();
        result.put("u_logni", u_logni);
        result.put("uid", uid);
        result.put("driver_id", driver_id);
        result.put("u_lati", u_lati);
        result.put("d_lati", d_lati);
        result.put("d_logni", d_logni);
        result.put("distance", distance);
        result.put("c_rate", "100");
        result.put("d_rate", "100");
        result.put("sender", sender);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/customer_req", result);
        ref.updateChildren(childUpdates);

    }
    private void findDriver(){


        // searchText.setVisibility(View.VISIBLE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        avail_driver_list=new HashMap<>();
        final String user_id=sp.getString("user_id","0");

        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        count = 0;
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //System.out.println("location searching...");

        if(mLastLocation != null) {
            //     System.out.println("location found...");
           /* String source_lati = locationList.get(pickup_position).getLatitude();
            String source_logni = locationList.get(pickup_position).getLongnitute();
            if (!source_lati.isEmpty() && source_lati != null && !source_logni.isEmpty() && source_logni != null) {
                ///System.out.println("check routing....");
                // hideKeyPad();

                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 5.0f);
                geoQuery.removeAllListeners();
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        //showAvailDriverList(location);
                        count++;
                        isDriverFound = true;
                        Location locationA = new Location("point A");
                        locationA.setLatitude(mLastLocation.getLatitude());
                        locationA.setLongitude(mLastLocation.getLongitude());
                        Location locationB = new Location("point B");
                        locationB.setLatitude(location.latitude);
                        locationB.setLongitude(location.longitude);
                        float distance = locationA.distanceTo(locationB);

                        HashMap<String, String> list = new HashMap<String, String>();
                        list.put("uid", user_id);
                        list.put("driver_id", key);
                        list.put("u_lati", String.valueOf(mLastLocation.getLatitude()));
                        list.put("u_logni", String.valueOf(mLastLocation.getLongitude()));
                        list.put("d_lati", String.valueOf(location.latitude));
                        list.put("d_logni", String.valueOf(location.longitude));
                        list.put("distance", String.valueOf(distance));
                        list.put("sender", String.valueOf(user_id));
                        avail_driver_list.put(String.valueOf(count), list);
                    }

                    @Override
                    public void onKeyExited(String key) {
                        System.out.println("send notification...out of danger");
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        System.out.println("send notification...move within the danger area");
                    }

                    @Override
                    public void onGeoQueryReady() {

                        System.out.println("routing counting....");
                        if (count > 0) {
                            //searchText.setVisibility(View.GONE);
                            // countNearestDriver.setText("Find "+count+" nearest driver");

                            sendReqToDriver();
                            getBidingTrans();
                        }

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
            }*/
        }
    }

    @Override
    public void onCameraIdle() {
        LatLng center = mMap.getCameraPosition().target;
        setMapPosition("",center);
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            // btnSearchPic.setEnabled(false);
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION)
        {
            //btnSearchPic.setEnabled(false);
        }
        else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION)
        {
            //Toast.makeText(this, "The app moved the camera.", Toast.LENGTH_SHORT).show();
            // btnSearchPic.setEnabled(false);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //rPickupMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        //mMap.setMapType( MAP_TYPES[curMapTypeIndex] );
        mMap.setTrafficEnabled( true );
        mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point)
            {
                 setMapPosition("",point);
                //map.clear();
                //map.addMarker(new MarkerOptions().position(point));
            }
        });

      //  rPickupMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        /*LatLng dangerous=new LatLng(22.3110316,91.80072989999996);
        mMap.addCircle(new CircleOptions() //500 means metter
        .center(dangerous).radius(5000).strokeColor(Color.BLUE)
                .fillColor(0x220000FF).strokeWidth(5.0f)
        );*/

    }
}
