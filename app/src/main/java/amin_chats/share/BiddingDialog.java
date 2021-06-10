package amin_chats.share;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amin_chats.cursor.aminchats.BiddingActivity;
import amin_chats.cursor.aminchats.BiddingAdapter;
import amin_chats.cursor.aminchats.BiddingMsg;
import amin_chats.cursor.aminchats.MapsActivity;
import amin_chats.cursor.aminchats.R;

/**
 * Created by cursor on 3/10/2020.
 */

public class BiddingDialog extends DialogFragment {

    EditText editText;
    RelativeLayout addBtn;
    String key="-1";
    RelativeLayout posting_lay_disabled,posting_lay_id;
    Button btnReqCancel,btnRideConfirm,btnReqCancelImmidiate;
    HashMap<String,HashMap<String,String>> avail_driver_list;
    DatabaseReference ref,ref_exist;
    GeoFire geoFire;String isConnected="0";
    View rootView;
    TextView count_ndriver,appro_rate;
    int count=0;
    String c_lati,c_logni;
    public BiddingAdapter biddingAdapter;
    public List<BiddingMsg> MessageList ;
    public RecyclerView myRecylerView ;
    int numberOfCompeteSearch=0;
    RideServiceAdapter rideServiceAdapter;
    HashMap<String,String> cancelList;
    double route_distance=0.0;
    List<RideServiceList> rideServiceLists;
    List<Location> locationList;
    int s_postion=-1,p_position=-1,d_position=-1;
    MapsActivity m=new MapsActivity();

    SharedPreferences sp_exist;
    SharedPreferences.Editor edit_exist;
    public static String TAG = "FullScreenDialog";

    public static BiddingDialog newInstance() {
        BiddingDialog f = new BiddingDialog();
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {
                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    //This is the filter
                    if (event.getAction()!= KeyEvent.ACTION_DOWN)
                    {
                        Toast.makeText(getActivity(),"Back not working for bidding purpose...you can cancle it",Toast.LENGTH_LONG).show();
                        /*getDialog().dismiss();
                        ((MapsActivity)getActivity()).showBoomSheetInit();
                        ((MapsActivity)getActivity()).mAutoCompleteDestination.setText("");
                        ((MapsActivity)getActivity()).mAutocompleteTextView.setText("");
                        ((MapsActivity)getActivity()).mAutocompleteTextView.requestFocus();
                        */
                        return false;
                       // return true;
                    }
                    else
                    {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                        //return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }
    public void cancelUpload(){
        getDialog().dismiss();
        ((MapsActivity)getActivity()).showBoomSheetInit();
        ((MapsActivity)getActivity()).mAutoCompleteDestination.setText("");
        ((MapsActivity)getActivity()).mAutocompleteTextView.setText("");
        ((MapsActivity)getActivity()).mAutocompleteTextView.requestFocus();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.bidding_chat, container, false);
        //rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_access_time);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelUpload();
            }
        });
        toolbar.setTitle("My Dialog");

        ref= FirebaseDatabase.getInstance().getReference("driver_avail");
        rideServiceAdapter=new RideServiceAdapter(getActivity());
        geoFire=new GeoFire(ref);
        count=0;
        avail_driver_list=new HashMap<>();
        posting_lay_disabled=(RelativeLayout)rootView.findViewById(R.id.posting_lay_disabled);
        posting_lay_id=(RelativeLayout)rootView.findViewById(R.id.posting_lay_id);
        btnReqCancel=(Button)rootView.findViewById(R.id.btnReqCancel);
        btnRideConfirm=(Button)rootView.findViewById(R.id.btnRideConfirm);
        btnRideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideConfirm();
            }
        });
        btnReqCancelImmidiate=(Button)rootView.findViewById(R.id.btnReqCancelImmidiate);

        cancelList=new HashMap<>();
        //key=getIntent().getStringExtra("key");
        editText=(EditText)rootView.findViewById(R.id.editText);
        count_ndriver=(TextView)rootView.findViewById(R.id.count_ndriver);
        appro_rate=(TextView)rootView.findViewById(R.id.appro_rate);
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        addBtn=(RelativeLayout)rootView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSendBiddingRate();
            }
        });
        btnReqCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelReq();
            }
        });
        route_distance=Double.parseDouble(getArguments().getString("route_distance"));
        p_position=Integer.parseInt(getArguments().getString("p_position"));
        d_position=Integer.parseInt(getArguments().getString("d_position"));
        isConnected=getArguments().getString("isConnected");
        c_lati="22.3775442";
        c_logni="91.8168635";
        //c_lati = getArguments().getString("c_loti");
        s_postion=Integer.parseInt(getArguments().getString("position"));
        rideServiceLists=(ArrayList<RideServiceList>)getArguments().getSerializable("list");
        locationList=(ArrayList<Location>)getArguments().getSerializable("l_list");

        sp_exist = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        edit_exist = sp_exist.edit();


     //   c_lati = locationList.get(p_position).getLatitude();
      //  c_logni = locationList.get(p_position).getLongnitute();

        //System.out.println("ride list "+rideServiceLists.get(0).getRate_per_km());
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initializeUI(rootView);
        if(isConnected.equalsIgnoreCase("1"))
        {
            key=isConnected;
            count_ndriver.setText("Conntected");
            getBidingTrans();
        }
        else{
            findDriver();
        }

        return rootView;
    }
    private void getDriverCurrentLocation(){

        ref_exist= FirebaseDatabase.getInstance().getReference("driver_avail").child(key).child("l");
        ref_exist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Object value = dataSnapshot.getValue();
                    if(value instanceof List) {
                        List<Object> values = (List<Object>) value;
                        Double lat = Double.parseDouble(values.get(0).toString());
                        Double lng = Double.parseDouble(values.get(1).toString());

                        updateDriverLocation(lat,lng);

                    }
                    else {
                        // handle other possible types
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            };
        });
    }
    private void updateDriverLocation(double lat,double lng)
    {

    }
    private void rideConfirm()
    {
        DatabaseReference refs = FirebaseDatabase.getInstance().getReference("driver_avail").child(key);
        refs.removeValue();

        ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req").child("status");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()) {
                   ref.setValue("2");

                   ((MapsActivity)getActivity()).confirmRideStartAndUpdate(key);

                    /*getDialog().cancel();
                    getDialog().dismiss();
                   ((MapsActivity)getActivity()).mAutoCompleteDestination.setText("");
                   ((MapsActivity)getActivity()).mAutocompleteTextView.setText("");
*/
               }
           }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void cancelReq()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key);
        ref.removeValue();
        cancelList.put(key,key);
        findDriver();
    }
    private void btnSendBiddingRate()
    {

        final String rate=editText.getText().toString();
        if(!rate.equalsIgnoreCase("0") && !rate.equalsIgnoreCase(""))
        {
            editText.setText("");
            addBtn.setEnabled(false);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String sender_id=sp.getString("user_id","0");
            ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    addBtn.setEnabled(true);
                    if (dataSnapshot.exists())
                    {
                        Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                       // String u_logni=td.get("u_logni").toString();
                        String uid=td.get("uid").toString();
                        String driver_id=td.get("driver_id").toString();
                       /* String p_lati=td.get("p_lati").toString();
                        String p_logni=td.get("p_logni").toString();
                        String d_lati=td.get("d_lati").toString();
                        String d_logni=td.get("d_logni").toString();
                        String distance=td.get("distance").toString();*/
                        String c_rate=td.get("c_rate").toString();
                       // String d_rate=td.get("d_rate").toString();
                       // String sender=td.get("sender").toString();
                        ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key);
                        HashMap<String, Object> result = new HashMap<>();
                        //result.put("u_logni", u_logni);
                        result.put("uid", uid);
                        result.put("driver_id", driver_id);
                        result.put("pickup_id", p_position);
                        result.put("destination_id", d_position);
                        result.put("c_rate", c_rate);
                        result.put("d_rate", rate);
                        result.put("status", 0);
                        result.put("sender", sender_id);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/customer_req", result);
                        ref.updateChildren(childUpdates);


                    }
                    else{
                        final HashMap<String, String> dInfo = avail_driver_list.get(String.valueOf("1"));
                        key = dInfo.get("driver_id");
                        ref_exist= FirebaseDatabase.getInstance().getReference("driver_avail").child(key).child("l");
                        ref_exist.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    Object value = dataSnapshot.getValue();
                                    if(value instanceof List) {
                                        List<Object> values = (List<Object>) value;
                                        String uid = dInfo.get("uid");
                                        Double lat = Double.parseDouble(values.get(0).toString());
                                        Double lng = Double.parseDouble(values.get(1).toString());
                                         uid = dInfo.get("uid");
                                        String driver_id = dInfo.get("driver_id");
                                        String sender = dInfo.get("sender");
                                         driver_id = dInfo.get("driver_id");
                                        ref = FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key);
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("driver_id", driver_id);
                                        result.put("sender", sender);
                                        result.put("pickup_id", p_position);
                                        result.put("destination_id", d_position);
                                        result.put("sender", sender);
                                        result.put("uid", uid);
                                        result.put("status", 0);
                                        int sPosition=rideServiceAdapter.getRow_index();
                                        System.out.println("row index = "+s_postion);
                                        // List<RideServiceList> rideInfo=rideServiceLists.getContactListRide();
                                        double whats_km=route_distance / 1000;
                                        if(whats_km <=0)
                                            whats_km=1;
                                        //double total_rent=whats_km * Float.parseFloat(rideServiceLists.get(s_postion).getRate_per_km());
                                        DecimalFormat f = new DecimalFormat("##.00");
                                        result.put("c_rate",rate);
                                        result.put("d_rate", rate);
                                        appro_rate.setText("Approximate Rate : "+rate);
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("/customer_req", result);
                                        ref.updateChildren(childUpdates);

                                        sp_exist = PreferenceManager
                                                .getDefaultSharedPreferences(getActivity());
                                        edit_exist = sp_exist.edit();
                                        edit_exist.putString("c_ride_id", driver_id);
                                        edit_exist.commit();

                                        //    numberOfCompeteSearch--;
                                        getBidingTrans();
                                        //System.out.println("map value = "+values.get(0));
                                    }
                                    else {
                                        // handle other possible types
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            };
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        //        Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    private void sendReqToDriver(){

        double whats_km=route_distance / 1000;
        double total_rent=whats_km * Float.parseFloat(rideServiceLists.get(s_postion).getRate_per_km());
        DecimalFormat f = new DecimalFormat("##.00");
        editText.setText(""+f.format(total_rent));


    }
    private void findDriver(){

        count=0;
        count_ndriver.setText("Connecting.....");
        System.out.println("lati = "+c_lati);
        System.out.println("logni = "+c_logni);
        // searchText.setVisibility(View.VISIBLE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String user_id=sp.getString("user_id","0");
           // String source_lati = locationList.get(pickup_position).getLatitude();
            //String source_logni = locationList.get(pickup_position).getLongnitute();
                GeoQuery geoQuery = geoFire.queryAtLocation
                        (new GeoLocation(Double.parseDouble(c_lati), Double.parseDouble(c_logni)), 5.0f);
                geoQuery.removeAllListeners();
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        //showAvailDriverList(location);
                        //System.out.println("key="+key+",isExist="+cancelList.get(key));
                        if(cancelList.get(key) == null) {
                            ///if(cancelList.get())
                            count++;
                            double x1 = Double.parseDouble(c_lati);
                            double y1 = Double.parseDouble(c_logni);

                            double x2 = location.latitude;
                            double y2 = location.latitude;

                            android.location.Location locationA = new android.location.Location("point A");
                            locationA.setLatitude(Double.parseDouble(c_lati));
                            locationA.setLongitude(Double.parseDouble(c_logni));
                            android.location.Location locationB = new android.location.Location("point B");
                            locationB.setLatitude(Double.parseDouble(locationList.get(d_position).getLatitude()));
                            locationB.setLongitude(Double.parseDouble(locationList.get(d_position).getLongnitute()));
                            double distance = SphericalUtil.computeDistanceBetween(new LatLng(x1, y1), new LatLng(x2, y2));
                            HashMap<String, String> list = new HashMap<String, String>();
                            list.put("uid", user_id);
                            list.put("driver_id", key);
                            list.put("sender", String.valueOf(user_id));
                            avail_driver_list.put(String.valueOf(count), list);
                        }
                    }
                    @Override
                    public void onKeyExited(String key) {
                       // System.out.println("send notification...out of danger");
                    }
                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        //System.out.println("send notification...move within the danger area");
                    }
                    @Override
                    public void onGeoQueryReady() {

                        //System.out.println("routing counting...."+count);
                        if (count > 0) {
                            addBtn.setEnabled(true);
                            numberOfCompeteSearch=count;
                            sendReqToDriver();
                            count_ndriver.setText(""+count+" Drivers connected with you...");
                        }
                        else
                        {
                            count_ndriver.setText("Connectting....");


                            //addBtn.setEnabled(false);

                            //btnReqCancel.setText("Research");

                        }

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
    }
    private void getBidingTrans(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String user_id=sp.getString("user_id","0");
        //System.out.println("key id ="+key);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child("drivers").
                child(key).child("customer_req");
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

                    if(sender.equalsIgnoreCase(user_id)){

                        posting_lay_id.setVisibility(View.GONE);
                        posting_lay_disabled.setVisibility(View.VISIBLE);
                        btnRideConfirm.setVisibility(View.GONE);
                        btnReqCancelImmidiate.setVisibility(View.GONE);


                    }
                    else{
                        posting_lay_id.setVisibility(View.VISIBLE);
                        posting_lay_disabled.setVisibility(View.GONE);
                        btnRideConfirm.setVisibility(View.VISIBLE);
                        btnReqCancelImmidiate.setVisibility(View.VISIBLE);
                        editText.setText("");
                        editText.setFocusable(true);
                    }

                    BiddingMsg m=new BiddingMsg(c_rate,d_rate,sender,driver_id);
                    MessageList.add(m);
                    biddingAdapter = new BiddingAdapter(MessageList,getActivity());
                    biddingAdapter.notifyDataSetChanged();
                    myRecylerView.setAdapter(biddingAdapter);

                }
                else{
                    //Intent i = new Intent(BiddingActivity.this, MapsActivity.class);
                    ///i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
//                    Intent i = new Intent(BiddingActivity.this, MapsActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
            }
        });
    }

    private void initializeUI(View rootView) {
        //getChildFragmentManager().beginTransaction().replace(R.id.fv_container,FragmentVisitHistory.getInstance(), AppConstant.FRAGMENT_VISIT_HISTORY).commit();
    }

}