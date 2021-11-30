package com.example.shopiki.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.shopiki.R;
import com.example.shopiki.models.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    //ui views
    private Button addproduct;
    private ImageView imageView;
    private TextView category,event;
    private EditText name,price,rating,description;
    // permission image
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constant
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    //permission array
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private Uri image_uri;
    private ProgressDialog progressDialog;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private UploadTask uploadTask;

    int inputprice,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        //ui add product
        addproduct = findViewById(R.id.add_product);
        category = findViewById(R.id.add_category_product);
        event = findViewById(R.id.add_type_product);
        name = findViewById(R.id.add_name_product);
        description = findViewById(R.id.add_desc_product);
        price = findViewById(R.id.add_price_product);
        rating = findViewById(R.id.add_rating_product);
        imageView = findViewById(R.id.img_icon);
        // toolbar
        toolbar = findViewById(R.id.add_product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm Sản Phẩm");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //init permission array
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDialog();
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputData();
            }
        });

    }

    private String productname,productrating,productDescription,productprice,productcategory,eventtype;
    private String event1 = "Hôm Nay Có Gì Hot";
    private String event2 = "Xu Hướng Mua Sắm";
    private String event3 = "Gợi Ý Hôm Nay";
    private void inputData() {
        productname = name.getText().toString().trim();
        productDescription = description.getText().toString().trim();
        productprice = price.getText().toString().trim();
        productcategory = category.getText().toString().trim();
        productrating = rating.getText().toString().trim();
        inputprice = Integer.parseInt(price.getText().toString().trim());
        double ratingstar = Double.parseDouble(productrating);
        eventtype = event.getText().toString().trim();
        //check input
        if (TextUtils.isEmpty(productname)) {
            Toast.makeText(this, "Nhập tên sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productDescription)) {
            Toast.makeText(this, "Nhập miêu tả chi tiết sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productprice)) {
            Toast.makeText(this, "Nhập giá sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productrating)) {
            Toast.makeText(this, "Nhập đánh giá sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productcategory)) {
            Toast.makeText(this, "Hãy chọn danh mục của sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ratingstar < 0 || ratingstar > 5){
            Toast.makeText(this, "Nhập đánh giá sản phẩm từ 0-5!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (eventtype.equalsIgnoreCase(event1)){
            type = 1;
        }
        if (eventtype.equalsIgnoreCase(event2)){
            type = 2;
        }
        if (eventtype.equalsIgnoreCase(event3)){
            type = 3;
        }
        addProduct();

    }

    private void addProduct() {
        progressDialog.setMessage("Vui lòng chờ ....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

       String timestamp = "" + System.currentTimeMillis();
        if(image_uri == null){
            Toast.makeText(this, "Bạn chưa upload ảnh sản phẩm!", Toast.LENGTH_SHORT).show();
        }else{
            String filePathAndName = "products/"+"" + timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            uploadTask = storageReference.putFile(image_uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(taskSnapshot.getMetadata() != null){
                                if (taskSnapshot.getMetadata().getReference() != null){
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            //createNewPost(imageUrl);
                                            final HashMap<String,Object> cartMap = new HashMap<>();
                                            cartMap.put("name",name.getText().toString());
                                            cartMap.put("price",inputprice);
                                            cartMap.put("description",description.getText().toString());
                                            cartMap.put("rating",rating.getText().toString());
                                            cartMap.put("img_url",imageUrl);
                                            cartMap.put("type",category.getText().toString());

                                            db.collection("ShowAll").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                }
                                            });
                                            if (type == 1){
                                                db.collection("NewProducts").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddProductActivity.this, "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
                                                        clearData();
                                                    }
                                                });
                                            }
                                            if (type == 2){
                                                db.collection("PopularProducts").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddProductActivity.this, "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
                                                        clearData();
                                                    }
                                                });
                                            }
                                            if (type == 3){
                                                db.collection("SuggestProducts").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddProductActivity.this, "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
                                                        clearData();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }





                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                            progressDialog.setMessage("Đang tải " +(int)progress+" %");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        }
    }

    private void clearData() {
        name.setText("");
        category.setText("");
        description.setText("");
        price.setText("");
        rating.setText("");
        imageView.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
    }

    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danh mục sản phẩm")
                .setItems(Constant.option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String categorytv = Constant.option[i];
                        category.setText(categorytv);

                    }
                })
                .show();
    }

    private void eventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danh mục sự kiện")
                .setItems(Constant.event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String categorytv = Constant.event[i];
                        event.setText(categorytv);

                    }
                })
                .show();
    }

    private void showImagePickDialog() {
        String[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            if(checkCameraPermission()){
                                pickFromCamera();
                            }
                            else {
                                requestCameraPermission();
                            }
                        }
                        else {
                            if (checkStoragePermission()){
                                pickFromGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private  boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return  result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private  boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    } else{
                        Toast.makeText(this, "Quyền truy cập camera và bộ nhớ là bắt buộc !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        pickFromGallery();
                    } else{
                        Toast.makeText(this, "Quyền truy cập bộ nhớ là bắt buộc !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                imageView.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                imageView.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}