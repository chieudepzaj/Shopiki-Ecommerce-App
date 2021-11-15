package com.example.shopiki.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopiki.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //image slider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1,"Siêu Sale Nghành Hàng Giảm Đến 40%", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner2,"Giảm đến 50% - 100% Chính Hãng", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner3,"Sale Hot Rầm Rộ Chỉ Từ 1K", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner4,"Hi-Tech Sale To Nhất Năm", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner5,"Ngày Hội Xế Yêu - Siêu Ưu Đãi", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner6,"Shopiki Ngon - Giá Hời Deal Ngon", ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(slideModels);
        return root;
    }
}