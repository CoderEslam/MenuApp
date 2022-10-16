package com.doubleclick.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText email, password;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button login;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(view -> {
            if (Check()) {
                LogIn(Objects.requireNonNull(email.getText()).toString().trim(), Objects.requireNonNull(password.getText()).toString().trim());
            }
        });

        authStateListener = firebaseAuth -> {
            if (user != null) {
                startActivity(new Intent(SignInActivity.this, MenuActivity.class));
                finish();
            }
        };


    }

    private void LogIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignInActivity.this, MenuActivity.class));
                }
            }
        });

    }

    private boolean Check() {
        if (email.getText().toString().equals("") && email.getText().toString().equals("")) {
            email.setError(getResources().getString(R.string.input_email));
            password.setError(getResources().getString(R.string.input_password));
            return false;
        } else {
            return true;
        }
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