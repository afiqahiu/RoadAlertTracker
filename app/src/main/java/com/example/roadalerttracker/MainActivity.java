package com.example.roadalerttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String name = prefs.getString("name", "User");

        TextView tv = findViewById(R.id.tvWelcome);
        tv.setText("Welcome, " + name);

        findViewById(R.id.btnMap)
                .setOnClickListener(v -> startActivity(
                        new Intent(this, MapActivity.class)));

        findViewById(R.id.btnReport)
                .setOnClickListener(v -> startActivity(
                        new Intent(this, ReportHazardActivity.class)));

        findViewById(R.id.btnFeed)
                .setOnClickListener(v -> startActivity(
                        new Intent(this, HazardFeedActivity.class)));
    }
}
