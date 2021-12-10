package com.example.shopiki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.shopiki.R;
import com.example.shopiki.fragments.CartFragment;
import com.example.shopiki.fragments.ContractFrangment;
import com.example.shopiki.fragments.HomeFragment;
import com.example.shopiki.fragments.PolicyFrangment;
import com.example.shopiki.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment homeFragment, contractFragment, cartFragment;
    Toolbar toolbar;
    FirebaseAuth auth;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        if (auth.getCurrentUser() == null) {
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_cart).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_wishlist).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
        } else {
            menu.findItem(R.id.nav_login).setVisible(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView headername = headerView.findViewById(R.id.header_name);
        TextView headeremail = headerView.findViewById(R.id.header_email);
        CircleImageView headerimg = headerView.findViewById(R.id.header_img);

        //load data user from firebase to header
        if (auth.getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
            reference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String emailstr = "" + snapshot.child("email").getValue();
                    String namestr = "" + snapshot.child("name").getValue();
                    String imagestr = "" + snapshot.child("img_url").getValue();

                    headername.setText(namestr);
                    headeremail.setText(emailstr);

                    try {
                        Glide.with(MainActivity.this).load(imagestr).placeholder(R.drawable.profileimg).into(headerimg);
                    } catch (Exception e) {
                        headerimg.setImageResource(R.drawable.profileimg);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        contractFragment = new ContractFrangment();
        cartFragment = new CartFragment();
    }


    private void loadFragment(Fragment homeFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            loadFragment(homeFragment);
        } else if (id == R.id.nav_contract) {
            loadFragment(contractFragment);
            navigationView.getMenu().findItem(R.id.nav_contract).setChecked(true);
        } else if (id == R.id.nav_cart) {
            loadFragment(cartFragment);
        } else if (id == R.id.nav_logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_refesh) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.nav_wishlist) {
            Toast.makeText(this, "Chức năng chưa được phát triển.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
        } else if (id == R.id.nav_share) {
            loadFragment(new PolicyFrangment());
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}