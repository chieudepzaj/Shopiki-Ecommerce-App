package com.example.shopiki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    String emailPattern = "[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth auth;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        imageView = findViewById(R.id.imageView_main);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }

    public void signin(View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Nhập địa chỉ email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!userEmail.trim().matches(emailPattern)) {
            Toast.makeText(this, "Địa chỉ email không đúng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 8) {
            Toast.makeText(this, "Mật khẩu có ít nhất 8 kí tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void signinadmin(View view) {
        startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
    }
}