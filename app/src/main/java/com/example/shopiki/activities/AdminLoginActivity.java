package com.example.shopiki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopiki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    EditText email, password;
    String emailPattern = "[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth auth;
    String useradmin = "admin1999@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        //  getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

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
        if (!userEmail.equals(useradmin)) {
            Toast.makeText(this, "Bạn không phải là admin. Xin vui lòng đăng nhập bằng user!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(AdminLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminLoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminLoginActivity.this, AdminMenu.class));
                        } else {
                            Toast.makeText(AdminLoginActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signinuser(View view) {
        startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class));
    }
}