package amin_chats.share;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amin_chats.cursor.aminchats.R;

public class LocationSearchAdapter extends RecyclerView.Adapter<LocationSearchAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Location> contactList;
    private List<Location> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cate_name, phone;
        //public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            cate_name = (TextView)view.findViewById(R.id.cate_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public LocationSearchAdapter(Context context, List<Location> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Location contact = contactListFiltered.get(position);
        holder.cate_name.setText(contact.getAddress());
        //holder.phone.setText(contact.getPhone());

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty() || contactList.size() <= 0) {
                    contactListFiltered = contactList;
                } else {
                    List<Location> filteredList = new ArrayList<>();
                    for (Location row : contactList) {
                       if (row.address.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
                /*if(contactListFiltered.size() <= 5)
                {
                    List<Location> filteredList = new ArrayList<>();
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;
                    return filterResults;
                }
                else{
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                }
*/

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Location>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Location contact);
        void getJsonObject(String obj);
    }
}


