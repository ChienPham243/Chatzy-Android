package com.example.chatzy.activities;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Toast;

import com.example.chatzy.R;

import com.example.chatzy.databinding.ActivityMainBinding;

import com.example.chatzy.fragments.friendsfrag.FriendsFragment;
import com.example.chatzy.fragments.mainfrag.MainFragment;
import com.example.chatzy.utilities.Constants;
import com.example.chatzy.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatDelegate;



import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class MainActivity extends BaseActivity  {

    private ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;
    MainFragment mainFragment = new MainFragment();
    private FirebaseFirestore database;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
            getFriendRequests();
            return true;
        });
        bottomNavigationView.setVisibility(View.GONE);
        getFriendRequests();
        setListener();
    }

    private void setListener() {
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),UsersActivity.class)));
    }

    private void getFriendRequests() {
        database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID)).collection(Constants.KEY_COLLECTION_REQUEST_FRIENDS).get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                int countRequests = 0;
                for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {
                    countRequests++;
                }
                if (countRequests != 0) {
                    bottomNavigationView.getOrCreateBadge(R.id.action_friends).setNumber(countRequests);
                }
            } else {
                bottomNavigationView.removeBadge(R.id.action_friends);
            }
        });
    }
}