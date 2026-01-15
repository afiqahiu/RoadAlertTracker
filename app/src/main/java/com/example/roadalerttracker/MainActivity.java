package com.example.roadalerttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar myToolbar;
    Button btnMap, btnFeed, btnReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Road Alert Tracker");

        btnMap = findViewById(R.id.btnMap);
        btnFeed = findViewById(R.id.btnFeed);
        btnReport = findViewById(R.id.btnReport);

        btnMap.setOnClickListener(this);
        btnFeed.setOnClickListener(this);
        btnReport.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app! https://github.com/afiqahiu/RoadAlertTracker.git");
            startActivity(Intent.createChooser(shareIntent, null));

            return true;

        } else if (item.getItemId() == R.id.item_about) {
            Intent aboutIntent = new Intent(this, AboutUsActivity.class);
            startActivity(aboutIntent);

        } else if (item.getItemId() == R.id.item_profile) {
            Intent profileIntent = new Intent(this, MyProfileActivity.class);
            startActivity(profileIntent);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == btnMap) {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            startActivity(mapIntent);
        } else if (v == btnFeed) {
            Intent feedIntent = new Intent(this, FeedActivity.class);
            startActivity(feedIntent);
        } else if (v == btnReport) {
            Intent reportIntent = new Intent(this, ReportActivity.class);
            startActivity(reportIntent);
        }
    }
}