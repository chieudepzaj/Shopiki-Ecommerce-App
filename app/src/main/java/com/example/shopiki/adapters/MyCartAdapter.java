package com.example.shopiki.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopiki.R;
import com.example.shopiki.activities.DetailedActivity;
import com.example.shopiki.models.MyCartModel;
import com.example.shopiki.models.NewProductsModel;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;
    int totalAmount = 0;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
        holder.name.setText(list.get(position).getProductName());
//        holder.time.setText(list.get(position).getCurrentTime());
        holder.price.setText(list.get(position).getProductPrice() + " ₫");
//        holder.date.setText(list.get(position).getCurrentDate());
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()) + " ₫");
        holder.totalQuantity.setText(list.get(position).getTotalQuantity());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, date, time, totalQuantity, totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            //      date = itemView.findViewById(R.id.current_date);
            //     time = itemView.findViewById(R.id.current_time);
            totalQuantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
        }
    }
}
