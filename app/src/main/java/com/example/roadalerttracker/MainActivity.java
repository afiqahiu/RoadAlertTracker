package com.example.roadalerttracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar myToolbar;
    Button btnMap, btnReport, btnSignOut;
    ImageView imageView2;
    TextView tvUsername, tvEmail;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Road Alert Tracker");

        // Views
        imageView2 = findViewById(R.id.imageView2);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);

        btnMap = findViewById(R.id.btnMap);
        btnReport = findViewById(R.id.btnReport);
        btnSignOut = findViewById(R.id.btnSignOut);

        btnMap.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        // Google Sign In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Load user data
        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());

            if (user.getDisplayName() != null) {
                tvUsername.setText(user.getDisplayName());
            } else {
                tvUsername.setText("User");
            }

            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.default_avatar)
                        .into(imageView2);
            } else {
                imageView2.setImageResource(R.drawable.default_avatar);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
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
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out this cool app! https://github.com/afiqahiu/RoadAlertTracker.git");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;

        } else if (item.getItemId() == R.id.item_about) {
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                });
    }

    @Override
    public void onClick(View v) {
        if (v == btnMap) {
            startActivity(new Intent(this, MapsActivity.class));

        } else if (v == btnReport) {
            String url = "http://10.0.2.2/serverside/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);

        } else if (v == btnSignOut) {
            signOut();
        }
    }
}
