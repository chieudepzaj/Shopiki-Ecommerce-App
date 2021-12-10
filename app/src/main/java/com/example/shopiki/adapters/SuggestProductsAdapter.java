package com.example.shopiki.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopiki.R;
import com.example.shopiki.activities.DetailedActivity;
import com.example.shopiki.models.SuggestProductsModel;

import java.util.List;

public class SuggestProductsAdapter extends RecyclerView.Adapter<SuggestProductsAdapter.ViewHolder> {

    private Context context;
    private List<SuggestProductsModel> suggestProductsModelList;

    public SuggestProductsAdapter(Context context, List<SuggestProductsModel> suggestProductsModelList) {
        this.context = context;
        this.suggestProductsModelList = suggestProductsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(suggestProductsModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(suggestProductsModelList.get(position).getName());
        holder.price.setText(String.valueOf(suggestProductsModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed", suggestProductsModelList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return suggestProductsModelList.size() > 4 ? 4 : suggestProductsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.suggest_img);
            name = itemView.findViewById(R.id.suggest_product_name);
            price = itemView.findViewById(R.id.suggest_price);
        }
    }
}
