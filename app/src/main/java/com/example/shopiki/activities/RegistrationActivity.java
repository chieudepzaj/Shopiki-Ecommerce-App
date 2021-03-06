package com.example.shopiki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shopiki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password, cfpassword;
    String emailPattern = "[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth auth;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
          getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        }
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cfpassword = findViewById(R.id.confirm_password);
        imageView = findViewById(R.id.imageView_register);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

    }

    public void signup(View view) {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String cfpass = cfpassword.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Nh???p t??n ng?????i d??ng!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Nh???p ?????a ch??? email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!userEmail.trim().matches(emailPattern)) {
            Toast.makeText(this, "?????a ch??? email kh??ng ????ng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Nh???p m???t kh???u!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 8) {
            Toast.makeText(this, "M???t kh???u c?? ??t nh???t 8 k?? t???!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cfpass)) {
            Toast.makeText(this, "Nh???p m???t kh???u x??c nh???n !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cfpass.equals(userPassword)) {
            Toast.makeText(this, "M???t kh???u kh??ng kh???p", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "????ng k?? t??i kho???n th??nh c??ng", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegistrationActivity.this, "T??i kho???n ???? t???n t???i!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signin(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

    }
}

