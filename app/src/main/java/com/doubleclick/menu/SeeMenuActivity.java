package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class SeeMenuActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_menu);
        webView = findViewById(R.id.web);
        webView.loadUrl("https://menuapp-a9ad6.web.app/menu.html");
        webView.getSettings().setJavaScriptEnabled(true);

    }
}