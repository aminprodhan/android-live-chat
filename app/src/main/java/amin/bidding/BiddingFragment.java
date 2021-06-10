package amin.bidding;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amin.background.Application;
import amin.background.MessageEvent;
import amin_chats.cursor.aminchats.BiddingActivity;
import amin_chats.cursor.aminchats.BiddingAdapter;
import amin_chats.cursor.aminchats.BiddingMsg;
import amin_chats.cursor.aminchats.MapsActivity;
import amin_chats.cursor.aminchats.R;
import amin_chats.share.*;


public class BiddingFragment extends DialogFragment {
    EditText editText;
    RelativeLayout addBtn;
    String key = "-1";
    RelativeLayout posting_lay_disabled, posting_lay_id;
    Button btnReqCancel, btnRideConfirm, btnReqCancelImmidiate;
    HashMap<String, HashMap<String, String>> avail_driver_list;
    DatabaseReference ref, ref_exist;
    GeoFire geoFire;
    String isConnected = "0";
    View rootView;
    TextView count_ndriver, appro_rate;
    int count = 0;
    String c_lati, c_logni;
    public BiddingAdapter biddingAdapter;
    public List<BiddingMsg> MessageList;
    public RecyclerView myRecylerView;
    int numberOfCompeteSearch = 0;
    RideServiceAdapter rideServiceAdapter;
    HashMap<String, String> cancelList;
    double route_distance = 0.0;
    List<RideServiceList> rideServiceLists;
    List<amin_chats.share.Location> locationList;
    int s_postion = -1, p_position = -1, d_position = -1;
    MapsActivity m = new MapsActivity();
    private Application signalApplication;

    SharedPreferences sp_exist;
    SharedPreferences.Editor edit_exist;
    public static String TAG = "FullScreenDialog";

    public static BiddingFragment newInstance() {
        BiddingFragment f = new BiddingFragment();
        return f;
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        Toast.makeText(getActivity(), "Back not working for bidding purpose...you can cancle it", Toast.LENGTH_LONG).show();
                        return false;
                        // return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                        //return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    @Override
    public void onPause() {
        getDialog().dismiss();
        super.onPause();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.bidding_chat, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        initializeUI(rootView);
        return rootView;

    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent event){

        String msg=event.getMessage().toString();
        BiddingMsg m=new BiddingMsg(msg,msg,msg,msg);
        MessageList.add(m);
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                biddingAdapter = new BiddingAdapter(MessageList,getActivity());
                biddingAdapter.notifyDataSetChanged();
                myRecylerView.setAdapter(biddingAdapter);

                // Stuff that updates the UI

            }
        });

        ///System.out.println("data event client = "+event.getMessage().toString());
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void sendBidRate(){
        final String rate=editText.getText().toString();
        if(!rate.equalsIgnoreCase("0") && !rate.equalsIgnoreCase("")) {
            editText.setText("");
            //addBtn.setEnabled(false);
            //socket.emit("messagedetection",Nickname,messagetxt.getText().toString());
            signalApplication.getSocket().emit("messagedetection", "amin",rate);

        }
    }
    public void onStart() {
        super.onStart();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_access_time);
        addBtn=(RelativeLayout)rootView.findViewById(R.id.addBtn);
        editText=(EditText)rootView.findViewById(R.id.editText);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBidRate();
            }
        });
        count_ndriver=(TextView)rootView.findViewById(R.id.count_ndriver);
        MessageList=new ArrayList<>();
        myRecylerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        addBtn.setEnabled(true);
        numberOfCompeteSearch=3;
        posting_lay_disabled=(RelativeLayout)rootView.findViewById(R.id.posting_lay_disabled);
        posting_lay_id=(RelativeLayout)rootView.findViewById(R.id.posting_lay_id);
        btnReqCancel=(Button)rootView.findViewById(R.id.btnReqCancel);
        btnRideConfirm=(Button)rootView.findViewById(R.id.btnRideConfirm);
        btnReqCancelImmidiate=(Button)rootView.findViewById(R.id.btnReqCancelImmidiate);
        count_ndriver.setText(""+count+" Drivers connected with you...");

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            setSocketInfo();
        }
    }
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("connection error ...");
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
                    signalApplication.getSocket().emit("join", "amin");
                }
            });
        }
    };
    private void setSocketInfo(){
        signalApplication = (Application) getActivity().getApplication();
        signalApplication.getSocket().connect();

        if (signalApplication.getSocket() == null)
            signalApplication.CHAT_SOCKET = signalApplication.getSocket();
        signalApplication.getSocket().on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        signalApplication.getSocket().on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        signalApplication.getSocket().on(Socket.EVENT_CONNECT, onConnect);

    }
    private void initializeUI(View rootView) {
    }

}