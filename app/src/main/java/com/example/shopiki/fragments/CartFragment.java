package com.example.shopiki.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopiki.R;
import com.example.shopiki.adapters.MyCartAdapter;
import com.example.shopiki.models.MyCartModel;
import com.example.shopiki.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    int overAllTotalAmount;
    TextView overAllAmount;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_cart, container, false);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        overAllAmount = root.findViewById(R.id.textview3);
        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        firebaseFirestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                        cartModelList.add(myCartModel);
                        cartAdapter.notifyDataSetChanged();
                    }
                    calculateTotalAmount(cartModelList);
                }
            }
        });
        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount = 0.0;
        for (MyCartModel myCartModel : cartModelList) {

            totalAmount += myCartModel.getTotalPrice();

        }
        long number = Double.valueOf(totalAmount).longValue();
        overAllAmount.setText("Tổng tiền hàng :" + number + " ₫");
    }


}