package com.example.madfour;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        username = getIntent().getStringExtra("username");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(username)).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_home) {
                        selectedFragment = HomeFragment.newInstance(username);
                    } else if (item.getItemId() == R.id.nav_orders) {
                        selectedFragment = OrdersFragment.newInstance(username);
                    } else if (item.getItemId() == R.id.nav_disease_prediction) {
                        selectedFragment = new DiseasePredictionFragment();
                    } else if (item.getItemId() == R.id.nav_profile) {
                        selectedFragment = ProfileFragment.newInstance(username);
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };
}



