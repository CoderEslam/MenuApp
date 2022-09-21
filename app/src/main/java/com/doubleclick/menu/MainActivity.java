package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in).setOnClickListener(view -> {
            startActivity(new Intent(this, SignInActivity.class));
        });

        findViewById(R.id.sign_up).setOnClickListener(view -> {
            startActivity(new Intent(this, SignInActivity.class));
        });
    }
}