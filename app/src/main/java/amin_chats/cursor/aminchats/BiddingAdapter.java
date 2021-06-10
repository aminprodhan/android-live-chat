package amin_chats.cursor.aminchats;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BiddingAdapter  extends RecyclerView.Adapter<BiddingAdapter.MyViewHolder>{
    private List<BiddingMsg> MessageList;
    Context cx;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView user_rate,driver_rate;
        public MyViewHolder(View view)
        {
            super(view);
            user_rate = (TextView) view.findViewById(R.id.txt_bididng_rate);
            driver_rate = (TextView) view.findViewById(R.id.txt_bididng_rate);
        }
    }
    public BiddingAdapter(List<BiddingMsg> MessagesList, Context cx) {
        this.MessageList = MessagesList;
        this.cx=cx;
    }
    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public BiddingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bidding, parent, false);
        return new BiddingAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final BiddingAdapter.MyViewHolder holder, final int position)
    {
        final BiddingMsg m = MessageList.get(position);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(cx);
        final String user_id=sp.getString("user_id","0");
        String uid=m.getUid();
        String driver_id=m.getDriver_id();
        if(user_id.equalsIgnoreCase(uid))
        {
            holder.user_rate.setText(m.getUser_rate());
            holder.user_rate.setVisibility(View.VISIBLE);
            holder.driver_rate.setVisibility(View.GONE);
        }
        else{
            holder.driver_rate.setText(m.getDriver_rate());
            holder.user_rate.setVisibility(View.GONE);
            holder.driver_rate.setVisibility(View.VISIBLE);
        }


    }
}
