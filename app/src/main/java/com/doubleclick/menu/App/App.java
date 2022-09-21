package com.doubleclick.menu.App;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
