package com.example.xuanhung.demotrieuchung;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MaiNam on 4/15/2016.
 */
public class ListTrieuChungAdapter extends
        RecyclerView.Adapter<ListTrieuChungAdapter.ViewHolder>   {

    private List<Contact> mContacts;

    // Pass in the contact array into the constructor
    public ListTrieuChungAdapter(List<Contact> contacts) {
        mContacts = contacts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTenBenh;

        public ViewHolder(View itemView) {

            super(itemView);

            tvTenBenh = (TextView) itemView.findViewById(R.id.tvTenBenh);
        }
    }


    @Override
    public ListTrieuChungAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_recylcelview_benh, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ListTrieuChungAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Contact contact = mContacts.get(position);

        // Set item views based on the data model
        TextView tvTenBenh = viewHolder.tvTenBenh;
        //tvTenBenh.setText(contact.getName());
        tvTenBenh.setText("Dau Chan");

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mContacts.size();
    }


}
