package amin.ride;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import amin.api.APIRideInfo;
import amin.api.ApiNodeJs;
import amin.background.Application;
import amin.bidding.RecycleBiddingAdapter;
import amin.bidding.RecycleBiddingMsg;
import amin.ride.share.RideInfo;
import amin_chats.cursor.aminchats.APIClient;
import amin_chats.cursor.aminchats.APIInterface;
import amin_chats.cursor.aminchats.BiddingMsg;
import amin_chats.cursor.aminchats.LoginActivity;
import amin_chats.cursor.aminchats.MapsActivity;
import amin_chats.cursor.aminchats.R;
import amin_chats.cursor.aminchats.User;
import amin_chats.share.LatLngInterpolator;
import amin_chats.share.LocationBoundedCheck;
import amin_chats.share.LocationSearch;
import amin_chats.share.LocationSearchAdapter;
import amin_chats.share.MarkerAnimation;
import amin_chats.share.MyDividerItemDecoration;
import amin_chats.share.RideServiceAdapter;
import amin_chats.share.RideServiceInfo;
import amin_chats.share.RideServiceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideParcel extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveCanceledListener,RideServiceAdapter.ContactsAdapterListenerRide,
    GoogleMap.OnCameraIdleListener,LocationSearchAdapter.ContactsAdapterListener {
    RideServiceInfo r_service_list;
    private GoogleApiClient mGoogleApiClient,mGoogleApiClient2;
    private List<RideServiceList> listRideServieList;
    private GoogleMap mMap;
    private final static int MY_PERMISSION_REQUEST_CODE=7192;
    private final static int PLAY_SERVICES_RESULATION_REQUEST=300193;
    private final static int UpdateInterval=5000;
    private final static int FastedInterval=3000;
    private final static int Displacement=10;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    //https://www.findlatitudeandlongitude.com/
    Marker mCurrent;
    APIRideInfo apiInterface,apiNodeJs;
    APIInterface apiInterfaceLocation;
    public Button btnBiddingReq,btnRideReq;
    public LinearLayout lay_ride_confirm;
    RelativeLayout bottom_sheet_location_confirm;
    private BottomSheetBehavior mBottomSheetBehaviorSearch,mBottomSheetBehaviorDriverProfile,mBottomSheetBidding;
    ProgressBar progressBar;
    int pickup_position=0,destination_position=0;
    View bottomSheetSearchView,bottomSheetDriverProfile,bottom_sheet_rent_showing,mBottomSheetBiddingView;
    LinearLayout layout_bottom_sheet_location_search;
    EditText auto_com_pickup_location,auto_com_destination_location;
    private List<amin_chats.share.Location> ara_location_list;
    private HashMap<String, Marker> mMarkersSelectTrack = new HashMap<>();
    private RideServiceAdapter mAdapterRide;
    RecycleBiddingAdapter mAdapterBidding;
    EditText txt_bdding_rate;
    private LocationSearchAdapter mAdapterLocation;
    RecyclerView recyclerViewLocation,recycleServiceList,recycleBidding;
    Button btn_location_confirm;
    LinearLayout lay_location_confirm;
    boolean isSelectPickup=true,isSelectDestination=false;
    LocationBoundedCheck isExist=new LocationBoundedCheck();
    private Application signalApplication;
    int driver_count_bidding=0;
    private LatLngBounds YOUR_CITY_OR_COUNTRY = new LatLngBounds(
            new LatLng(88.0844222351,20.670883287), new LatLng(92.6727209818, 26.4465255803));
    LinearLayout bottom_sheet_bidding_list_view;
    List<RecycleBiddingMsg> bidding_data;
    private ProgressDialog progressDialog;
    TextView txtConnectDriver,txtDriverFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_parcel);

        txt_bdding_rate=(EditText)findViewById(R.id.txt_bididng_rate);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        btn_location_confirm=(Button)findViewById(R.id.btn_location_confirm);
        btn_location_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCurrentMapLocation();
            }
        });
        btnBiddingReq=(Button)findViewById(R.id.btnRideConfirm);
        btnBiddingReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                rideBiddingReq();
            }
        });
        lay_location_confirm=(LinearLayout)findViewById(R.id.lay_location_confirm);
        apiInterface= APIClient.getClient().create(APIRideInfo.class);
        apiInterfaceLocation=APIClient.getClient().create(APIInterface.class);
        apiNodeJs=ApiNodeJs.getClient().create(APIRideInfo.class);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        bottomSheetSearchView = findViewById(R.id.bottom_sheet_location_search);
        bottom_sheet_rent_showing = findViewById(R.id.bottom_sheet_rent_showing);
        lay_ride_confirm=(LinearLayout)findViewById(R.id.lay_ride_confirm);
        recycleServiceList = (RecyclerView) findViewById(R.id.recycle_service_list);

        bottomSheetDriverProfile = findViewById(R.id.bottom_sheet_driver_profile);
        mBottomSheetBiddingView = findViewById(R.id.bottom_sheet_bidding_list_layout);
        txtConnectDriver=(TextView)findViewById(R.id.txtConnectDriver);
        txtDriverFound=(TextView)findViewById(R.id.txtDriverFound);

        recyclerViewLocation = (RecyclerView) findViewById(R.id.recycler_view_list);
        viewInitLocationSearch();
        uploadLocationList();
        isGpsOpen();
        isRideRunning();
        setSocketInfo();
    }
    private void showProgressDialog(String title){
        progressDialog = new ProgressDialog(RideParcel.this);
        progressDialog.setMessage(title);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void hideProgressDialog(){
        progressDialog.dismiss();
    }
    private  void setBiddingListLayout(){
        bidding_data=new ArrayList<>();
        bottom_sheet_bidding_list_view=(LinearLayout)findViewById(R.id.bottom_sheet_bidding_list_layout);
        bottom_sheet_bidding_list_view.setVisibility(View.VISIBLE);
        mBottomSheetBidding = BottomSheetBehavior.from(mBottomSheetBiddingView);
        mBottomSheetBidding.setPeekHeight(700);
        mBottomSheetBidding.setState(BottomSheetBehavior.STATE_EXPANDED);
        recycleBidding = (RecyclerView) findViewById(R.id.recycle_bidding_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleBidding.setLayoutManager(mLayoutManager);
        recycleBidding.setItemAnimator(new DefaultItemAnimator());
        recycleBidding.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.HORIZONTAL, 36));
        mAdapterBidding = new RecycleBiddingAdapter(this,bidding_data);
        recycleBidding.setAdapter(mAdapterBidding);
    }
    //bidding_write_received
    private void rideBiddingReq(){
        //   if(mLastLocation != null && mLastLocation != null)
        //    {

        signalApplication.getSocket().connect();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final String rider_id = sp.getString("user_id", "0");

        int ride_type=mAdapterRide.getRow_index();
        String ride_type_id=listRideServieList.get(ride_type).getId();
        String rate=txt_bdding_rate.getText().toString();

        System.out.println("ride type id = "+ride_type_id);

        if(rate.isEmpty())
        {
            txt_bdding_rate.setError("Rate is required");
        }
        else  if(rider_id.equalsIgnoreCase("0")){
            Toast.makeText(getApplicationContext(),"Please login again..",Toast.LENGTH_LONG).show();
        }
        else{
            showProgressDialog("Loading...");
            RideServiceInfo s=new RideServiceInfo();
            s.setLatitude(ara_location_list.get(pickup_position).getLatitude());
            s.setLongitude(ara_location_list.get(pickup_position).getLongnitute());
            s.setLatitude_destination(ara_location_list.get(destination_position).getLatitude());
            s.setLongitude_destination(ara_location_list.get(destination_position).getLongnitute());
            s.setSource_address_txt(ara_location_list.get(pickup_position).getAddress());
            s.setDestination_address_txt(ara_location_list.get(destination_position).getAddress());
            s.setRider_id(rider_id);
            s.setRate(rate);
            Call<RideServiceInfo> call1 = apiNodeJs.searchDriver(s);
            call1.enqueue(new Callback<RideServiceInfo>() {
                @Override
                public void onResponse(Call<RideServiceInfo> call, Response<RideServiceInfo> response)
                {
                   final RideServiceInfo info = response.body();
                    runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              String status=info.getStatus();
                              String msg=info.getMsg();
                              //txtConnectDriver.setText(msg);
                              driver_count_bidding=info.getList().size();
                              txtDriverFound.setText(driver_count_bidding+" driver found.");
                              if(status.equalsIgnoreCase("0"))
                              {
                                  //System.out.println("No Driver Found..");
                                  //txtConnectDriver.setText(msg);
                              }
                              else if(status.equalsIgnoreCase("2"))
                              {
                                  //internal server error
                                  //System.out.println("internal server error...");
                                  //txtConnectDriver.setText(msg);

                              }
                              else{
                                  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                  final String rider_id = sp.getString("user_id", "");
                                  signalApplication.getSocket().emit("joinDriver", rider_id, "client_info", new Ack() {
                                      @Override
                                      public void call(Object... args) {
                                          runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    viewVisibilityOff(4);
                                                    setBiddingListLayout();
                                                }
                                          });

                                      }
                                  });


                              }
                          }
                    });
                    hideProgressDialog();
                }
                @Override
                public void onFailure(Call<RideServiceInfo> call, Throwable t) {
                    //Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                    call.cancel();
                    rideBiddingReq();
                }
            });
        }
    }
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("connection error ...");
                    signalApplication.socketConnect();
                    setSocketInfo();
                }
            });
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("connection disconntectd ...");
                    signalApplication.socketConnect();
                    setSocketInfo();
                }
            });
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("connection success ...");
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        signalApplication.getSocket().off(Socket.EVENT_CONNECT, onConnect);
        signalApplication.getSocket().off(Socket.EVENT_DISCONNECT, onDisconnect);
        signalApplication.getSocket().off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        signalApplication.getSocket().off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        signalApplication.getSocket().off("bidding_rate_received", bidding_rate_received);
        signalApplication.getSocket().off("bidding_write_received", bidding_write_received);

        super.onDestroy();
    }

    private void setSocketInfo(){

        signalApplication = (Application)getApplication();
        signalApplication.getSocket().connect();
        if (signalApplication.getSocket() == null)
            signalApplication.CHAT_SOCKET = signalApplication.getSocket();

        signalApplication.getSocket().on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        signalApplication.getSocket().on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        signalApplication.getSocket().on(Socket.EVENT_CONNECT, onConnect);
        signalApplication.getSocket().on("bidding_rate_received", bidding_rate_received);
        signalApplication.getSocket().on("bidding_write_received", bidding_write_received);

    }
    private Emitter.Listener bidding_write_received = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final JSONObject data = (JSONObject) args[0];
            new Handler(getMainLooper())
                    .post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        String driver_id=data.getString("driver_id").toString();
                                        String status=data.getString("write_status").toString();
                                     //   txtConnectDriver.setText("Typeing....");

                                        if(status.equalsIgnoreCase("1"))
                                            txtConnectDriver.setText("Typeing....");
                                        else
                                            txtConnectDriver.setText("");
                                    }
                                    catch (JSONException e){

                                    }
                                }
                            }
                    );
        }
    };

    //driver_connection_status
    private Emitter.Listener bidding_rate_received = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
             final JSONObject data = (JSONObject) args[0];
            new Handler(getMainLooper())
                    .post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        String rate=data.getString("rate").toString();
                                        String ratting=data.getString("ratting").toString();
                                        String driver_name=data.getString("driver_name").toString();
                                        String img=data.getString("img").toString();
                                        String bill_id=data.getString("bill_id").toString();
                                        String driver_id=data.getString("driver_id").toString();
                                        //System.out.println("data event = "+data.getString("rate"));
                                        RecycleBiddingMsg m=new RecycleBiddingMsg();
                                        m.setRate(rate);
                                        m.setDriver_id(driver_id);
                                        m.setBill_id(bill_id);
                                        m.setDriver_name(driver_name);
                                        m.setImg_url(img);
                                        m.setRatting(ratting);
                                        bidding_data.add(m);
                                        mAdapterBidding.notifyDataSetChanged();
                                    }
                                    catch (JSONException e){

                                    }
                                }
                            }
                    );
        }
    };

    private void viewVisibilityOff(int status){
        if(status == 1)
        {
            bottomSheetSearchView.setVisibility(View.VISIBLE);
            lay_location_confirm.setVisibility(View.GONE);
           // bottom_sheet_bidding_list.setVisibility(View.GONE);

        }
        else if(status == 2)
        {
            //bottom_sheet_bidding_list.setVisibility(View.GONE);
            bottomSheetSearchView.setVisibility(View.GONE);
            lay_location_confirm.setVisibility(View.VISIBLE);
        }
        else if(status == 3)
        {
            //bottom_sheet_bidding_list.setVisibility(View.GONE);
            bottomSheetSearchView.setVisibility(View.GONE);
            lay_location_confirm.setVisibility(View.GONE);
            bottom_sheet_rent_showing.setVisibility(View.VISIBLE);
        }
        else if(status == 4)
        {
            bottomSheetSearchView.setVisibility(View.GONE);
            lay_location_confirm.setVisibility(View.GONE);
            bottom_sheet_rent_showing.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapLoaded() {



        /*try {
            Log.e("map", "Before init");
            //initilizeMap();
            LatLngBounds curScreen = mMap.getProjection()
                    .getVisibleRegion().latLngBounds;
            System.out.println(curScreen.toString());


            //top-left corner
            double topleftlatitude=curScreen.northeast.latitude;
            double topleftlongitude=curScreen.southwest.longitude;
            System.out.println("top left==>"+topleftlatitude+" " +topleftlongitude);

            //bottom-right corner
            double bottomrightlatitude=curScreen.southwest.latitude;
            double bottomrightlongitude=curScreen.northeast.longitude;
            System.out.println("bottom right==>"+bottomrightlatitude+" " +bottomrightlongitude);


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void getCurrentMapLocation(){

        LatLng center = mMap.getCameraPosition().target;
        Geocoder geocoder;
        List<Address> addresses;
        //geocoder = new Geocoder(this, Locale.getDefault());
         geocoder = new Geocoder(this, getResources().getConfiguration().locale);
       // List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),   location.getLongitude(), 5);

//        try {
//            addresses = geocoder.getFromLocation(center.latitude, center.longitude, 5);
            String knownName="";
            if(true){
         //   if(addresses != null && !addresses.isEmpty()) {
                /*String address = addresses.get(0).getAddressLine(0);
                knownName = addresses.get(0).getFeatureName();
                if (isSelectPickup) {
                    //bottom_sheet_location_confirm.setVisibility(View.GONE);
                    ara_location_list.get(pickup_position).setLatitude(String.valueOf(addresses.get(0).getLatitude()));
                    ara_location_list.get(pickup_position).setLongnitute(String.valueOf(addresses.get(0).getLongitude()));
                    ara_location_list.get(pickup_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    ara_location_list.get(pickup_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    auto_com_pickup_location.setText(knownName);
                } else if (isSelectDestination) {
                    ara_location_list.get(destination_position).setLatitude(String.valueOf(addresses.get(0).getLatitude()));
                    ara_location_list.get(destination_position).setLongnitute(String.valueOf(addresses.get(0).getLongitude()));
                    ara_location_list.get(destination_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    ara_location_list.get(destination_position).setAddress(String.valueOf(addresses.get(0).getAddressLine(0)));
                    bottomSheetSearchView.setVisibility(View.GONE);
                    auto_com_destination_location.setText(knownName);

                }*/
                if(isSelectPickup){
                    auto_com_pickup_location.setText("Chittaong");
                }
                else if(isSelectDestination){
                    bottomSheetSearchView.setVisibility(View.GONE);
                    auto_com_destination_location.setText("Dhaka");

                }
                String pickup=auto_com_pickup_location.getText().toString();
                String destination=auto_com_destination_location.getText().toString();
                if(!pickup.isEmpty() && !destination.isEmpty())
                {
                    //Toast.makeText(getApplicationContext(),"Search Driver",Toast.LENGTH_LONG).show();
                    getRideRent();
                }
                else{
                    mBottomSheetBehaviorSearch.setState(BottomSheetBehavior.STATE_EXPANDED);
                    viewVisibilityOff(1);
                }
            }
            else
                Toast.makeText(getApplicationContext(),"Location unknown...",Toast.LENGTH_LONG).show();
       /* } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }
    private void getRentInit(){
        isSelectDestination=false;
        isSelectPickup=false;
        viewVisibilityOff(3);
        showProgress();

    }
    private void updateRideServiceRecycleView(double distance){
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(RideParcel.this,
                LinearLayoutManager.HORIZONTAL, false);

        bottom_sheet_rent_showing.setVisibility(View.VISIBLE);
        //mBottomSheetConfirm.setPeekHeight(550);
        mAdapterRide = new RideServiceAdapter(this,listRideServieList,this,distance,lay_ride_confirm,txt_bdding_rate);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleServiceList.setLayoutManager(mLayoutManager);
        recycleServiceList.setItemAnimator(new DefaultItemAnimator());
        recycleServiceList.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.HORIZONTAL, 36));
        recycleServiceList.setLayoutManager(horizontalLayoutManagaer);
        recycleServiceList.setAdapter(mAdapterRide);
    }

    @Override
    public void onContactSelectedRide(RideServiceList contact) {

    }
    private void getRideRent()
    {
        getRentInit();
        RideServiceInfo s=new RideServiceInfo();
        s.setLatitude(ara_location_list.get(pickup_position).getLatitude());
        s.setLongitude(ara_location_list.get(pickup_position).getLongnitute());
        s.setLatitude_destination(ara_location_list.get(destination_position).getLatitude());
        s.setLongitude_destination(ara_location_list.get(destination_position).getLongnitute());

        Call<RideServiceInfo> call1 = apiInterface.getCalulatedRent(s);
        call1.enqueue(new Callback<RideServiceInfo>() {
            @Override
            public void onResponse(Call<RideServiceInfo> call, Response<RideServiceInfo> response)
            {
                r_service_list = response.body();
                listRideServieList=r_service_list.getList();
                if(r_service_list.getStatus().equalsIgnoreCase("1"))
                {
                    double distance=Double.parseDouble(r_service_list.getDistance());
                    updateRideServiceRecycleView(distance);
                }
                else
                {
                    Toast.makeText(RideParcel.this,r_service_list.getMsg(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RideServiceInfo> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                call.cancel();
                getRideRent();
            }
        });
    }

    private void removeProgress()
    {
        progressBar.setVisibility(View.GONE);
    }
    private void showProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void uploadLocationList()
    {
        showProgress();
        ara_location_list=new ArrayList<>();
        Call<LocationSearch> call = apiInterfaceLocation.doGetLocationList("jj");
        call.enqueue(new Callback<LocationSearch>() {
            @Override
            public void onResponse(Call<LocationSearch> call, Response<LocationSearch> response) {
                // Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    LocationSearch list=response.body();
                    ara_location_list=list.getList();
                    setLocationListForSearch();
                    removeProgress();
                }
            }

            @Override
            public void onFailure(Call<LocationSearch> call, Throwable t)
            {
                uploadLocationList();
            }
        });

    }

    @Override
    public void onContactSelected(amin_chats.share.Location c) {
        LatLng l=new LatLng(Double.parseDouble(c.getLatitude()),Double.parseDouble(c.getLongnitute()));
        hideKeyPad();
        collpaseSearchView();
        setMapPosition(l);

        if(isSelectPickup){
            //auto_com_pickup_location.setText(c.getAddress());
            pickup_position=Integer.parseInt(c.getId());
        }
        if(isSelectDestination){
            //auto_com_destination_location.setText(c.getAddress());
            //isConfirmForSearch();
            destination_position=Integer.parseInt(c.getId());
        }
        viewVisibilityOff(2);
    }
    private void isConfirmForSearch()
    {
        String pickup_location=auto_com_pickup_location.getText().toString();
        String destination_location=auto_com_destination_location.getText().toString();
    }
    @Override
    public void getJsonObject(String obj) {

    }

    private void setLocationListForSearch(){
        mAdapterLocation = new LocationSearchAdapter(getApplicationContext(),ara_location_list,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewLocation.setLayoutManager(mLayoutManager);
        recyclerViewLocation.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLocation.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerViewLocation.setAdapter(mAdapterLocation);
    }

    private void expandSearchView(){
        if(mBottomSheetBehaviorSearch.getState()==BottomSheetBehavior.STATE_COLLAPSED)
        {
            mBottomSheetBehaviorSearch.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
    private void collpaseSearchView(){
        mBottomSheetBehaviorSearch.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }
    public void onKeyLocationSearch(String s)
    {
        if (!s.toString().equals("")) {
            mAdapterLocation.getFilter().filter(s.toString());
        }else{
            //uploadLocationList();
        }
    }
    private void viewInitLocationSearch(){
        auto_com_pickup_location=(EditText)findViewById(R.id.auto_com_pickup_location);
        auto_com_destination_location=(EditText)findViewById(R.id.auto_com_destination_location);
        auto_com_destination_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandSearchView();
                isSelectDestination=true;
                isSelectPickup=false;
            }
        });
        auto_com_destination_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                onKeyLocationSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        auto_com_pickup_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                onKeyLocationSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        auto_com_pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandSearchView();
                isSelectDestination=false;
                isSelectPickup=true;
            }
        });
        auto_com_pickup_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                {
                    isSelectDestination=false;
                    isSelectPickup=true;
                    expandSearchView();
                }
            }
        });
        auto_com_destination_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                {
                    isSelectDestination=true;
                    isSelectPickup=false;
                    expandSearchView();
                }
            }
        });
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
    private void createExistRoute()
    {

    }
    private void createSearchView()
    {
        bottomSheetSearchView.setVisibility(View.VISIBLE);
        mBottomSheetBehaviorSearch = BottomSheetBehavior.from(bottomSheetSearchView);
        mBottomSheetBehaviorSearch.setPeekHeight(500);
        mBottomSheetBehaviorSearch.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehaviorSearch.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
    }
    private void createActivity()
    {
        int status=1;
        if(status == 1){
            createSearchView();
        }
    }
    private void isGpsOpen(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled")
                    .setCancelable(false)
                    .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            isGpsOpen();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    }
    private void locationUpdate(){
        displayLocation();
        startDisplayLocationUpdates();
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
    private void checkLocationPermission()
    {
        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        else{
            //System.out.println("location2 = "+mGoogleApiClient+","+mLocationRequest);
            if(mLocationRequest != null)
                displayLocation();
            //startDisplayLocationUpdates();
        }
    }
    @Override
    public void onStart()
    {
        super.onStart();
        if(this.mGoogleApiClient != null)
        {
            this.mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
    private synchronized  void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    public boolean checkPlayServices()
    {
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
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
    private void CreateLocationRequest()
    {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UpdateInterval);
        mLocationRequest.setFastestInterval(FastedInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Displacement);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlayServices()){
                        buildGoogleApiClient();
                        CreateLocationRequest();
                        checkLocationPermission();
                    }
                }
                break;
        }
    }
    private void isRideRunning()
    {
        RideInfo info=new RideInfo();
        info.setU_type("1");
        Call<RideInfo> call1 = apiInterface.getRideContinueInfo(info);
        call1.enqueue(new Callback<RideInfo>() {
            @Override
            public void onResponse(Call<RideInfo> call, Response<RideInfo> response) {
                RideInfo data = response.body();
                if(data.getStatus().equalsIgnoreCase("0"))
                {
                    createExistRoute();
                }
                else{
                    createActivity();
                }
                //Toast.makeText(RideParcel.this,user1.getStatus(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<RideInfo> call, Throwable t) {
                //Toast.makeText(RideParcel.this,t.getMessage(),Toast.LENGTH_LONG).show();
                //System.out.println("ride error = "+t.getMessage());
                call.cancel();
            }
        });

    }
    private void displayLocation(){

        System.out.println("try to connect location with map");

        try {
            mLastLocation= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){
                final double latitude=mLastLocation.getLatitude();
                final double lognitude=mLastLocation.getLongitude();
                if(mCurrent != null)
                    mCurrent.remove();
                mCurrent=mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude,lognitude))
                        .title("You"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,lognitude),12.0f));
                isRideRunning();
            }
            else{
                //System.out.println("Can't get your location ");
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Can't get your location")
                        .setCancelable(false)
                        .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                displayLocation();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        }
        catch (SecurityException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //System.out.println("is location change");
        mLastLocation=location;
        displayLocation();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //System.out.println("is connected");
        locationUpdate();
    }
    @Override
    public void onCameraMoveCanceled() {

    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }
    @Override
    public void onCameraMove() {

    }
    @Override
    public void onCameraIdle() {
        LatLng center = mMap.getCameraPosition().target;
        setMapPosition(center);
    }

    @Override
    public void onCameraMoveStarted(int i) {

    }
    private void setMapPosition(LatLng lat)
    {
        System.out.println("Searching...."+lat.latitude+",long="+lat.longitude);

        boolean b= isExist.isBounded(20.3756582, 26.6382534, 88.0075306, 92.6804979,lat.latitude,lat.longitude);
        System.out.println("is closed to area ="+b);

        /*if(YOUR_CITY_OR_COUNTRY.contains(lat)){
            System.out.println("is closed to area =");
        }*/

        String code="sm";
        String select_location="Select pickup location using map drag";
        if(isSelectDestination || isSelectPickup){
            if(isSelectDestination)
                select_location="Select destination using map drag";
            if (!mMarkersSelectTrack.containsKey(code)) { // is select marker
                mMarkersSelectTrack.put(code, mMap.addMarker(new MarkerOptions()
                        .position(lat)
                        .snippet(select_location)
                        .title(select_location)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                );

            }
            else {
                float bearing = (float) bearingBetweenLocations(mMarkersSelectTrack.get(code).getPosition(), lat);
                MarkerAnimation.rotateMarker(mMarkersSelectTrack.get(code), bearing, lat, new LatLngInterpolator.Spherical());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(lat).zoom(18).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.getUiSettings().setZoomControlsEnabled(true);
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
    public void setUpMap(){

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        //mMap.setMapType( MAP_TYPES[curMapTypeIndex] );
        //mMap.setTrafficEnabled( true );
        //mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point)
            {
                setMapPosition(point);
            }
        });
        try {
            mMap.setOnMapLoadedCallback(this);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //mMap.setLatLngBoundsForCameraTarget(YOUR_CITY_OR_COUNTRY);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
        //set this as map loaded callback


    }
        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();
        //LatLng sydney = new LatLng(-34, 151);
        /*mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
