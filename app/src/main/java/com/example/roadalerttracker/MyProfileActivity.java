package com.example.roadalerttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileActivity extends AppCompatActivity {

    Toolbar profileToolbar;
    TextView tvUsername, tvEmail;
    ImageView imageView2;
    GoogleSignInClient mGoogleSignInClient;
    Button btnSignOut;

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

        imageView2 = findViewById(R.id.imageView2);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Email
            tvEmail.setText(user.getEmail());

            // Username
            if (user.getDisplayName() != null) {
                tvUsername.setText(user.getDisplayName());
            } else {
                tvUsername.setText("User");
            }

            // Profile Photo
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


        // Google Sign In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignOut = findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        // 1) Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // 2) Google sign out
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    Toast.makeText(MyProfileActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

                    // 3) Go back to sign-in screen
                    Intent intent = new Intent(MyProfileActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}

