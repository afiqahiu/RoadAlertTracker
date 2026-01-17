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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar myToolbar;
    Button btnMap, btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Road Alert Tracker");

        // Buttons
        btnMap = findViewById(R.id.btnMap);
        btnReport = findViewById(R.id.btnReport);

        btnMap.setOnClickListener(this);
        btnReport.setOnClickListener(this);
    }

    /**
     * 🔐 Enforce Google Sign-In
     * User cannot access MainActivity without login
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }


    /**
     * Toolbar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Toolbar item clicks
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (item.getItemId() == R.id.item_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out this cool app! https://github.com/afiqahiu/RoadAlertTracker.git"
            );
            startActivity(Intent.createChooser(shareIntent, "Share via"));

            return true;

        } else if (item.getItemId() == R.id.item_about) {

            startActivity(new Intent(this, AboutUsActivity.class));
            return true;

        } else if (item.getItemId() == R.id.item_profile) {

        Intent profileIntent = new Intent(this, MyProfileActivity.class);

        // Get current user data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            profileIntent.putExtra("name", user.getDisplayName());
            profileIntent.putExtra("email", user.getEmail());
            profileIntent.putExtra("personId", user.getUid());

            if (user.getPhotoUrl() != null) {
                profileIntent.putExtra("personPhoto", user.getPhotoUrl().toString());
            }
        }

        startActivity(profileIntent);
    }

            return true;
        }

    /**
     * Button clicks
     */
    @Override
    public void onClick(View v) {
        if (v == btnMap) {
            startActivity(new Intent(this, MapsActivity.class));
        } else if (v == btnReport) {
            startActivity(new Intent(this, ReportActivity.class));
        }
    }
}
