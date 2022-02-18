package com.appblend.handfree.yaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.tcl.sevend.yaw.YawChair;

import java.util.List;

public class YawChairListAdapter extends RecyclerView.Adapter<YawChairListAdapter.YawChairListViewHolder> {


    private List<YawChair> mDataset;
    private YawChairListCallBack mYawChairListCallBack;

    public void setDataset(List<YawChair> mDataset) {
        this.mDataset = mDataset;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class YawChairListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View container;
        private TextView title;
        public YawChairListViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.ll_container);
            title = v.findViewById(R.id.tv_ip_address);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public YawChairListAdapter(List<YawChair> myDataset, YawChairListCallBack deviceListCallBack) {
        mDataset = myDataset;
        mYawChairListCallBack = deviceListCallBack;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public YawChairListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        // create a new view
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_found_yaw_chair, parent, false);

        YawChairListViewHolder vh = new YawChairListViewHolder(root);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final YawChairListViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String name = mDataset.get(position).getName();
        holder.title.setText(name);



        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mYawChairListCallBack.onClick(name);
                mYawChairListCallBack.onClick(holder.getAdapterPosition());
            }
        });

        holder.container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                holder.container.setSelected(b);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}