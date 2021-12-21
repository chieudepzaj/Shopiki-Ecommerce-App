package com.example.shopiki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.StatusBarManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopiki.R;
import com.example.shopiki.models.NewProductsModel;
import com.example.shopiki.models.PopularProductsModel;
import com.example.shopiki.models.ShowAllModel;
import com.example.shopiki.models.SuggestProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price, quantity;
    Button addToCart, buyNow, chat;
    ImageView addItems, removeItems;
    float ratingba;
    private RatingBar ratingBar;
    private String admin = "admin1999@gmail.com";

    Toolbar toolbar;
    int totalQuantity = 1;
    int totalPrice = 0;
    //New Products
    NewProductsModel newProductsModel = null;
    //Popular Products
    PopularProductsModel popularProductsModel = null;
    //Trend Products
    SuggestProductsModel suggestProductsModel = null;
    //Show All
    ShowAllModel showAllModel = null;
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        //      getSupportActionBar().hide();

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel) {
            newProductsModel = (NewProductsModel) obj;
        } else if (obj instanceof PopularProductsModel) {
            popularProductsModel = (PopularProductsModel) obj;
        } else if (obj instanceof SuggestProductsModel) {
            suggestProductsModel = (SuggestProductsModel) obj;
        } else if (obj instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) obj;
        }

//        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_card);
        buyNow = findViewById(R.id.buy_now);
        chat = findViewById(R.id.chat);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);
        ratingBar = findViewById(R.id.my_rating);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffbe1a"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#ffbe1a"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#ffbe1a"), PorterDuff.Mode.SRC_ATOP);
        //image slider
        ImageSlider imageSlider = findViewById(R.id.detailed_img);
        List<SlideModel> slideModels = new ArrayList<>();

        //New Products
        if (newProductsModel != null) {
            slideModels.add(new SlideModel(newProductsModel.getImg_url(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(newProductsModel.getImg_url1(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(newProductsModel.getImg_url2(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(newProductsModel.getImg_url3(), ScaleTypes.CENTER_INSIDE));
            imageSlider.setImageList(slideModels);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);

            totalPrice = newProductsModel.getPrice() * totalQuantity;
        }
        //Popular Products
        if (popularProductsModel != null) {
            slideModels.add(new SlideModel(popularProductsModel.getImg_url(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(popularProductsModel.getImg_url1(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(popularProductsModel.getImg_url2(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(popularProductsModel.getImg_url3(), ScaleTypes.CENTER_INSIDE));
            imageSlider.setImageList(slideModels);
            name.setText(popularProductsModel.getName());
            rating.setText(popularProductsModel.getRating());
            description.setText(popularProductsModel.getDescription());
            price.setText(String.valueOf(popularProductsModel.getPrice()));
            name.setText(popularProductsModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);

            totalPrice = popularProductsModel.getPrice() * totalQuantity;
        }
        //Suggest Products
        if (suggestProductsModel != null) {
            slideModels.add(new SlideModel(suggestProductsModel.getImg_url(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(suggestProductsModel.getImg_url1(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(suggestProductsModel.getImg_url2(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(suggestProductsModel.getImg_url3(), ScaleTypes.CENTER_INSIDE));
            imageSlider.setImageList(slideModels);
            name.setText(suggestProductsModel.getName());
            rating.setText(suggestProductsModel.getRating());
            description.setText(suggestProductsModel.getDescription());
            price.setText(String.valueOf(suggestProductsModel.getPrice()));
            name.setText(suggestProductsModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);

            totalPrice = suggestProductsModel.getPrice() * totalQuantity;
        }
        //Show All product
        if (showAllModel != null) {
            slideModels.add(new SlideModel(showAllModel.getImg_url(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(showAllModel.getImg_url1(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(showAllModel.getImg_url2(), ScaleTypes.CENTER_INSIDE));
            slideModels.add(new SlideModel(showAllModel.getImg_url3(), ScaleTypes.CENTER_INSIDE));
            imageSlider.setImageList(slideModels);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());
            ratingba = Float.parseFloat(rating.getText().toString());
            ratingBar.setRating(ratingba);

            totalPrice = showAllModel.getPrice() * totalQuantity;
        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null) {
                    addToCart();
                } else {
                    Toast.makeText(DetailedActivity.this, "Vui lòng đăng nhập để thực hiện chức năng.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailedActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.facebook.com/dinhchieu.310599/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity < 10) {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null) {
                        totalPrice = newProductsModel.getPrice() * totalQuantity;
                    }
                    if (popularProductsModel != null) {
                        totalPrice = popularProductsModel.getPrice() * totalQuantity;
                    }
                    if (suggestProductsModel != null) {
                        totalPrice = suggestProductsModel.getPrice() * totalQuantity;
                    }
                    if (showAllModel != null) {
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }
            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
    }

    private void addToCart() {

        String saveCurrentTime, savecurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        savecurrentDate = currentDate.format(callForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", savecurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firebaseFirestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Đã thêm vào Giỏ Hàng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}