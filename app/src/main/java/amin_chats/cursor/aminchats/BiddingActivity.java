package amin_chats.cursor.aminchats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiddingActivity extends AppCompatActivity {

    public RecyclerView myRecylerView ;
    public List<BiddingMsg> MessageList ;
    public BiddingAdapter biddingAdapter;
    EditText editText;
    DatabaseReference ref;
    GeoFire geoFire;
    RelativeLayout addBtn;
    String key="-1";
    RelativeLayout posting_lay_disabled,posting_lay_id;
    Button btnReqCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bidding_chat);

        posting_lay_disabled=(RelativeLayout)findViewById(R.id.posting_lay_disabled);
        posting_lay_id=(RelativeLayout)findViewById(R.id.posting_lay_id);
        btnReqCancel=(Button)findViewById(R.id.btnReqCancel);
        key=getIntent().getStringExtra("key");
        editText=(EditText)findViewById(R.id.editText);
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        addBtn=(RelativeLayout)findViewById(R.id.addBtn);
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
        getBidingTrans();

    }
    private void cancelReq(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key);
        ref.removeValue();

    }
    private void getBidingTrans(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id=sp.getString("user_id","0");

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

                        if(sender.equalsIgnoreCase(user_id)){

                            posting_lay_id.setVisibility(View.GONE);
                            posting_lay_disabled.setVisibility(View.VISIBLE);

                        }
                        else{
                            posting_lay_id.setVisibility(View.VISIBLE);
                            posting_lay_disabled.setVisibility(View.GONE);
                        }

                           BiddingMsg m=new BiddingMsg(c_rate,d_rate,sender,driver_id);
                           MessageList.add(m);
                           biddingAdapter = new BiddingAdapter(MessageList,getApplicationContext());
                           biddingAdapter.notifyDataSetChanged();
                           myRecylerView.setAdapter(biddingAdapter);

                    }
                    else{
                        Intent i = new Intent(BiddingActivity.this, MapsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
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
    private void btnSendBiddingRate()
    {
        final String rate=editText.getText().toString();
        if(!rate.equalsIgnoreCase("0") && !rate.equalsIgnoreCase(""))
        {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            final String sender_id=sp.getString("user_id","0");
            ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                        String u_logni=td.get("u_logni").toString();
                        String uid=td.get("uid").toString();
                        String driver_id=td.get("driver_id").toString();
                        String u_lati=td.get("u_lati").toString();
                        String d_lati=td.get("d_lati").toString();
                        String d_logni=td.get("d_logni").toString();
                        String distance=td.get("distance").toString();
                        String c_rate=td.get("c_rate").toString();
                        String d_rate=td.get("d_rate").toString();
                        String sender=td.get("sender").toString();

                        ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key);
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("u_logni", u_logni);
                        result.put("uid", uid);
                        result.put("driver_id", driver_id);
                        result.put("u_lati", u_lati);
                        result.put("d_lati", d_lati);
                        result.put("d_logni", d_logni);
                        result.put("distance", distance);
                        result.put("c_rate", c_rate);
                        result.put("d_rate", rate);
                        result.put("sender", sender_id);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/customer_req", result);
                        ref.updateChildren(childUpdates);


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /*ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req").child("d_rate");
            ref.setValue(rate);
            ref= FirebaseDatabase.getInstance().getReference("users").child("drivers").child(key).child("customer_req").child("sender");
            ref.setValue(sender_id);*/
        }
    }
}
