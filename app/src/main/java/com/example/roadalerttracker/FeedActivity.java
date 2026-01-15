package com.example.roadalerttracker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FeedActivity extends AppCompatActivity {

    Toolbar feedToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        feedToolbar = findViewById(R.id.feed_toolbar);
        setSupportActionBar(feedToolbar);
        getSupportActionBar().setTitle("Hazard Information Feed");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return true;}
}