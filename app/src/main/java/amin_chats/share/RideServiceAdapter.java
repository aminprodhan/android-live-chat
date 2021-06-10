package amin_chats.share;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import amin_chats.cursor.aminchats.MapsActivity;
import amin_chats.cursor.aminchats.R;

public class RideServiceAdapter extends RecyclerView.Adapter<RideServiceAdapter.MyViewHolder>{

    private Context context;
    private List<RideServiceList> contactList;

    public List<RideServiceList> getContactListRide() {
        return contactListRide;
    }

    public void setContactListRide(List<RideServiceList> contactListRide) {
        this.contactListRide = contactListRide;
    }

    public List<RideServiceList> contactListRide;
    private ContactsAdapterListenerRide listener;

    public int getRow_index() {
        return row_index;
    }

    public void setRow_index(int row_index) {
        this.row_index = row_index;
    }

    public  int row_index=-1;double distance=0;
    EditText txt_rate;
    MapsActivity mapsActivity;
    Button btnRideConfirm,count_ndriver;
    LinearLayout lay_ride_confirm;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cate_name, rent;
        public ImageView img_url;
        //public ImageView thumbnail;
        RelativeLayout ride_circle_active;
        public MyViewHolder(View view) {
            super(view);
            cate_name = (TextView)view.findViewById(R.id.cate_name);
            rent = (TextView)view.findViewById(R.id.rent);
            ride_circle_active = (RelativeLayout)view.findViewById(R.id.ride_circle_active);
        }
    }
    public RideServiceAdapter(Context context, List<RideServiceList> contactList,
                              ContactsAdapterListenerRide listener,double distance,LinearLayout lay_ride_confirm) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListRide = contactList;
        setContactListRide(contactList);
        this.distance=distance;
        this.lay_ride_confirm=lay_ride_confirm;
        //lay_ride_confirm=mapsActivity.lay_ride_confirm;
    }
    public RideServiceAdapter(Context context, List<RideServiceList> contactList,
                              ContactsAdapterListenerRide listener, double distance, LinearLayout lay_ride_confirm, EditText txt_rate) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListRide = contactList;
        setContactListRide(contactList);
        this.distance=distance;
        this.lay_ride_confirm=lay_ride_confirm;
        this.txt_rate=txt_rate;
        //lay_ride_confirm=mapsActivity.lay_ride_confirm;
    }
    public RideServiceAdapter(Context context) {
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final RideServiceList contact = contactListRide.get(position);
        holder.cate_name.setText(contact.getName());

        double base_rate=Double.parseDouble(contact.getBase_rate());

        final double total_rent=(distance * Float.parseFloat(contact.getRate_per_km())) + base_rate;
        holder.rent.setVisibility(View.VISIBLE);
        final DecimalFormat f = new DecimalFormat("##.00");
        holder.rent.setText(String.valueOf(f.format(total_rent)));

        // row_index=-1;
        //holder.phone.setText(contact.getPhone());
        holder.ride_circle_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                setRow_index(position);
                //Toast.makeText(context,"row="+row_index,Toast.LENGTH_LONG).show();
                lay_ride_confirm.setVisibility(View.VISIBLE);
                txt_rate.setText(String.valueOf(f.format(total_rent)));
                notifyDataSetChanged();
            }
        });
        if(row_index == position)
        {
            holder.ride_circle_active.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_ride_active));
        }
        else
        {
            holder.ride_circle_active.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circleshape));
        }
    }

    @Override
    public int getItemCount() {
        return contactListRide.size();
    }
    public interface ContactsAdapterListenerRide {
        void onContactSelectedRide(RideServiceList contact);
        void getJsonObject(String obj);
    }
}
