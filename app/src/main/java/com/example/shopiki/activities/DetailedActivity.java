package com.example.shopiki.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.StatusBarManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopiki.R;
import com.example.shopiki.models.NewProductsModel;
import com.example.shopiki.models.PopularProductsModel;
import com.example.shopiki.models.ShowAllModel;
import com.example.shopiki.models.SuggestProductsModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price;
    Button addToCart,buyNow;
    ImageView addItems,removeItems ;
    float ratingba;
    private RatingBar ratingBar;

    //New Products
    NewProductsModel newProductsModel = null;
    //Popular Products
    PopularProductsModel popularProductsModel = null;
    //Trend Products
    SuggestProductsModel suggestProductsModel = null;
    //Show All
    ShowAllModel showAllModel = null;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getSupportActionBar().hide();

        firebaseFirestore = FirebaseFirestore.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof NewProductsModel){
            newProductsModel = (NewProductsModel) obj;
        } else if(obj instanceof PopularProductsModel){
            popularProductsModel = (PopularProductsModel) obj;
        } else if (obj instanceof SuggestProductsModel){
            suggestProductsModel = (SuggestProductsModel) obj;
        } else if (obj instanceof ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_card);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);
        ratingBar = findViewById(R.id.my_rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffbe1a"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#ffbe1a"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#ffbe1a"), PorterDuff.Mode.SRC_ATOP);
        //New Products
        if(newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);
        }
        //Popular Products
        if(popularProductsModel != null){
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            rating.setText(popularProductsModel.getRating());
            description.setText(popularProductsModel.getDescription());
            price.setText(String.valueOf(popularProductsModel.getPrice()));
            name.setText(popularProductsModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);
        }
        //Suggest Products
        if(suggestProductsModel != null){
            Glide.with(getApplicationContext()).load(suggestProductsModel.getImg_url()).into(detailedImg);
            name.setText(suggestProductsModel.getName());
            rating.setText(suggestProductsModel.getRating());
            description.setText(suggestProductsModel.getDescription());
            price.setText(String.valueOf(suggestProductsModel.getPrice()));
            name.setText(suggestProductsModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);
        }
        //Show All product
        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);
        }
    }
}