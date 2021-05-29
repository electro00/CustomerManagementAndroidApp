package com.example.customermanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ManagingCustomerAdapter extends RecyclerView.Adapter<ManagingCustomerAdapter.ViewHolder> implements Filterable {
    private ArrayList<CustomerItem> mCustomerItemData;
    private ArrayList<CustomerItem> mCustomerItemDataAll;
    private Context mContext;
    private int lastPosition = -1;

    ManagingCustomerAdapter(Context context, ArrayList<CustomerItem> itemData) {
        this.mCustomerItemData = itemData;
        this.mCustomerItemDataAll = itemData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_customers, parent, false));
    }

    @Override
    public void onBindViewHolder(ManagingCustomerAdapter.ViewHolder holder, int position) {
        CustomerItem currentItem = mCustomerItemData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mCustomerItemData.size();
    }

    @Override
    public Filter getFilter() {
        return managingFilter;
    }

    private Filter managingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CustomerItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mCustomerItemDataAll.size();
                results.values = mCustomerItemDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (CustomerItem item : mCustomerItemDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            mCustomerItemData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameText;
        private TextView mBoughtItemText;
        private TextView mPriceText;
        private ImageView mCustomerImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameText = itemView.findViewById(R.id.customerName);
            mBoughtItemText = itemView.findViewById(R.id.boughtItemName);
            mPriceText = itemView.findViewById(R.id.boughtItemPrice);
            mCustomerImage = itemView.findViewById(R.id.customerImage);
        }

        public void bindTo(CustomerItem currentItem) {
            mNameText.setText(currentItem.getName());
            mBoughtItemText.setText(currentItem.getItemName());
            mPriceText.setText(currentItem.getPrice());

            Glide.with(mContext).load(currentItem.getImageResource()).into(mCustomerImage);
        }
    }

}

