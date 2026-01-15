package com.example.roadalerttracker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ReportActivity extends AppCompatActivity {

    Toolbar reportToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        reportToolbar = findViewById(R.id.report_toolbar);
        setSupportActionBar(reportToolbar);
        getSupportActionBar().setTitle("Report Hazard");

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