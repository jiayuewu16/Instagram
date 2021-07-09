package com.example.instagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.fragments.CreateFragment;
import com.example.instagram.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        Fragment homeFragment = HomeFragment.newInstance();
        final Fragment createFragment = new CreateFragment();
        //final Fragment profileFragment = new ProfileFragment();

        // handle navigation selection
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            default:
                            case R.id.action_home:
                                fragment = homeFragment;
                                break;
                            case R.id.action_create:
                                fragment = createFragment;
                                break;
                            /*case R.id.action_profile:
                                //fragment = profileFragment;
                                break;*/
                        }
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });

        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);

    }
}