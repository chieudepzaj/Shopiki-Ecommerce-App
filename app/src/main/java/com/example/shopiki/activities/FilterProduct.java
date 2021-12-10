package com.example.shopiki.activities;

import android.widget.Filter;

import com.example.shopiki.adapters.ShowAllAdapter;
import com.example.shopiki.models.ShowAllModel;

import java.util.ArrayList;
import java.util.Locale;

public class FilterProduct extends Filter {

    private ShowAllAdapter adapter;
    private ArrayList<ShowAllModel> filterList;

    public FilterProduct(ShowAllAdapter adapter, ArrayList<ShowAllModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if (charSequence != null && charSequence.length() > 0) {


            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ShowAllModel> filteredModel = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getName().toUpperCase().contains(charSequence) || filterList.get(i).getType().toUpperCase().contains(charSequence)) {
                    filteredModel.add(filterList.get(i));
                }
            }
            results.count = filteredModel.size();
            results.values = filteredModel;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return null;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.list = (ArrayList<ShowAllModel>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}
