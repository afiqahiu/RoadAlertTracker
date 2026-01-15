package com.example.roadalerttracker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyProfileActivity extends AppCompatActivity {

    Toolbar profileToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        profileToolbar = findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setTitle("My Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return true;
    }
}