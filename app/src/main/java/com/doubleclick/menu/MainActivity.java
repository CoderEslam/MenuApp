package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.doubleclick.menu.Repository.Repo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        findViewById(R.id.sign_in).setOnClickListener(view -> {
            startActivity(new Intent(this, SignInActivity.class));
        });

        findViewById(R.id.sign_up).setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });


        authStateListener = firebaseAuth -> {
            if (user != null) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                finish();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }
}