package com.example.shopiki.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopiki.R;
import com.example.shopiki.activities.ShowAllActivity;
import com.example.shopiki.activities.ShowAllNewActivity;
import com.example.shopiki.activities.ShowAllPopularActivity;
import com.example.shopiki.activities.ShowAllSuggestActivity;
import com.example.shopiki.adapters.CategoryAdapter;
import com.example.shopiki.adapters.NewProductsAdapter;
import com.example.shopiki.adapters.PopularProductsAdapter;
import com.example.shopiki.adapters.ShowAllAdapter;
import com.example.shopiki.adapters.SuggestProductsAdapter;
import com.example.shopiki.models.CategoryModel;
import com.example.shopiki.models.NewProductsModel;
import com.example.shopiki.models.PopularProductsModel;
import com.example.shopiki.models.ShowAllModel;
import com.example.shopiki.models.SuggestProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView newproductShowAll, popularproductShowAll, suggestProductShowAll;

    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catrecyclerView, newProductRecyclerview, trendProductRecyclerview, suggestProductRecyclerview;
    // Category recyclerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New Product
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;
    //Popular product
    PopularProductsAdapter popularProductsAdapter;
    List<PopularProductsModel> popularProductsModelList;
    //Popular product
    SuggestProductsAdapter suggestProductsAdapter;
    List<SuggestProductsModel> suggestProductsModelList;
    //Search
    EditText search_box;
    private List<ShowAllModel> showAllModelList;
    private RecyclerView recyclerViewSearch;
    private ShowAllAdapter viewAllAdapter;
    //Firestore
    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        catrecyclerView = root.findViewById(R.id.rec_category);
        newProductRecyclerview = root.findViewById(R.id.new_product_rec);
        trendProductRecyclerview = root.findViewById(R.id.popular_rec);
        suggestProductRecyclerview = root.findViewById(R.id.suggest_product_rec);


        newproductShowAll = root.findViewById(R.id.newProducts_see_all);
        popularproductShowAll = root.findViewById(R.id.popular_see_all);
        suggestProductShowAll = root.findViewById(R.id.suggestProducts_see_all);


        newproductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllNewActivity.class);
                startActivity(intent);
            }
        });

        popularproductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllPopularActivity.class);
                startActivity(intent);
            }
        });

        suggestProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllSuggestActivity.class);
                startActivity(intent);
            }
        });

        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);
        //image slider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1, "Siêu Sale Nghành Hàng Giảm Đến 40%", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner2, "Giảm đến 50% - 100% Chính Hãng", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner3, "Sale Hot Rầm Rộ Chỉ Từ 1K", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner4, "Hi-Tech Sale To Nhất Năm", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner5, "Ngày Hội Xế Yêu - Siêu Ưu Đãi", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.banner6, "Shopiki Ngon - Giá Hời Deal Ngon", ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(slideModels);

        progressDialog.setTitle("Shopiki - Thương hiệu của người Việt \uD83C\uDDFB\uD83C\uDDF3");
        progressDialog.setMessage("Vui lòng chờ ....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Category
        catrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModelList);
        catrecyclerView.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //New Products
        newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(), newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Popular product
        trendProductRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.HORIZONTAL, false));
        popularProductsModelList = new ArrayList<>();
        popularProductsAdapter = new PopularProductsAdapter(getContext(), popularProductsModelList);
        trendProductRecyclerview.setAdapter(popularProductsAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularProductsModel popularProductsModel = document.toObject(PopularProductsModel.class);
                                popularProductsModelList.add(popularProductsModel);
                                popularProductsAdapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Suggest Products
        suggestProductRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        suggestProductsModelList = new ArrayList<>();
        suggestProductsAdapter = new SuggestProductsAdapter(getContext(), suggestProductsModelList);
        suggestProductRecyclerview.setAdapter(suggestProductsAdapter);

        db.collection("SuggestProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SuggestProductsModel suggestProductsModel = document.toObject(SuggestProductsModel.class);
                                suggestProductsModelList.add(suggestProductsModel);
                                suggestProductsAdapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Search View
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        search_box = root.findViewById(R.id.search_box);
        showAllModelList = new ArrayList<>();
        viewAllAdapter = new ShowAllAdapter(getContext(), showAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(viewAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty()) {
                    showAllModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                } else {

                    searchProducttype(editable.toString());
                }
            }
        });

        return root;
    }

    private void searchProducttype(String type) {

        if (!type.isEmpty()) {
            db.collection("ShowAll").whereArrayContains("keywords",type)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful() && task.getResult() != null) {
                                showAllModelList.clear();
                                viewAllAdapter.notifyDataSetChanged();
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    viewAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });
        }
    }
}