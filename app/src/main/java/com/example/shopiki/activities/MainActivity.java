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
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.shopiki.R;
import com.example.shopiki.fragments.CartFragment;
import com.example.shopiki.fragments.ContractFrangment;
import com.example.shopiki.fragments.HomeFragment;
import com.example.shopiki.fragments.PolicyFrangment;
import com.example.shopiki.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment homeFragment,contractFragment,cartFragment;
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
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        contractFragment = new ContractFrangment();
        cartFragment = new CartFragment();
    }


    private void loadFragment(Fragment homeFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            loadFragment(homeFragment);
        }
        else if (id == R.id.nav_contract){
            loadFragment(contractFragment);
            navigationView.getMenu().findItem(R.id.nav_contract).setChecked(true);
        }else if(id == R.id.nav_cart){
//            startActivity(new Intent(MainActivity.this,CartActivity.class));
            loadFragment(cartFragment);
        } else if(id == R.id.nav_logout){
            auth.signOut();
            startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            finish();
        } else if(id == R.id.nav_refesh){
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.nav_wishlist){
            Toast.makeText(this, "Chức năng chưa được phát triển.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile){
            loadFragment(new ProfileFragment());
        } else if(id == R.id.nav_share){
            loadFragment(new PolicyFrangment());
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}