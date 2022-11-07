package com.doubleclick.coinchaud;

import static com.doubleclick.coinchaud.Service.Network.isNetworkConnected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText email, password;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button login;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        login.setOnClickListener(view -> {
            if (Check()) {
                if (isNetworkConnected(SignInActivity.this)) {
                    LogIn(Objects.requireNonNull(email.getText()).toString().trim(), Objects.requireNonNull(password.getText()).toString().trim());
                    login.setEnabled(false);
                    login.setTextColor(getResources().getColor(R.color.gray));
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        authStateListener = firebaseAuth -> {
            if (user != null) {
                startActivity(new Intent(SignInActivity.this, MenuActivity.class));
                finish();
                progressBar.setVisibility(View.GONE);
            }
        };


    }

    private void LogIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignInActivity.this, MenuActivity.class));
                    finish();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean Check() {
        if (Objects.requireNonNull(email.getText()).toString().equals("") && Objects.requireNonNull(password.getText()).toString().equals("")) {
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