package amin.bidding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.github.nkzawa.socketio.client.Ack;

import java.util.List;

import amin.api.APIRideInfo;
import amin.api.ApiNodeJs;
import amin.ride.RideParcel;
import amin_chats.cursor.aminchats.MapsActivity;
import amin_chats.cursor.aminchats.R;
import amin_chats.share.RideServiceAdapter;
import amin_chats.share.RideServiceInfo;
import amin_chats.share.RideServiceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cursor on 4/16/2020.
 */

public class RecycleBiddingAdapter extends RecyclerView.Adapter<RecycleBiddingAdapter.MyViewHolder> {
    List<RecycleBiddingMsg> dataList;
    Context context;
    APIRideInfo apiInterface,apiNodeJs;
    private ProgressDialog progressDialog;

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView driver_name, rate,ratting;
        public SimpleDraweeView img_url;
        Button btnRateAccpet;
        RelativeLayout ride_circle_active;
        public MyViewHolder(View view) {
            super(view);
            img_url = (SimpleDraweeView) view.findViewById(R.id.img_driver_profile);
            driver_name = (TextView)view.findViewById(R.id.txt_driver_name);
            btnRateAccpet = (Button)view.findViewById(R.id.btnRateAccpet);
            rate = (TextView)view.findViewById(R.id.txt_driver_rate);
            ratting = (TextView)view.findViewById(R.id.txt_driver_ratting);
            ride_circle_active = (RelativeLayout)view.findViewById(R.id.ride_circle_active);
            apiNodeJs= ApiNodeJs.getClient().create(APIRideInfo.class);

        }
    }
    public RecycleBiddingAdapter(Context context, List<RecycleBiddingMsg> dataList){
        this.dataList=dataList;
        this.context=context;
    }

    private void rideAccept(final String bill_id, final String driver_id)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RecycleBiddingMsg m=new RecycleBiddingMsg();
        m.setBill_id(bill_id);
        m.setDriver_id(driver_id);
        Call<RecycleBiddingMsg> call1 = apiNodeJs.ride_accept(m);
        call1.enqueue(new Callback<RecycleBiddingMsg>() {
            @Override
            public void onResponse(Call<RecycleBiddingMsg> call, Response<RecycleBiddingMsg> response)
            {
                progressDialog.dismiss();
                final RecycleBiddingMsg info = response.body();
                if(info.getStatus().equalsIgnoreCase("0"))
                {
                    Toast.makeText(context,info.getMsg(),Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i=new Intent(context, MapsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<RecycleBiddingMsg> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                call.cancel();
                rideAccept(bill_id,driver_id);
            }
        });
    }

    @Override
    public void onBindViewHolder(RecycleBiddingAdapter.MyViewHolder holder, int position) {
        final RecycleBiddingMsg contact = dataList.get(position);

        holder.btnRateAccpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rideAccept(contact.getBill_id(),contact.getDriver_id());
            }
        });
        holder.rate.setText("Rate : "+contact.getRate());
        holder.img_url.setController(
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(ImageRequest.fromUri(contact.getImg_url()))
                        .setAutoPlayAnimations(true)
                        .build());
        holder.driver_name.setText("D.Name: "+contact.getDriver_name());
        holder.ratting.setText("Ratting: "+contact.getRatting());
    }
    @Override
    public RecycleBiddingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bidding, parent, false);
        return new RecycleBiddingAdapter.MyViewHolder(itemView);
    }
}
