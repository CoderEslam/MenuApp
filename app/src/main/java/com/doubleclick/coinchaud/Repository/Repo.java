package com.doubleclick.coinchaud.Repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class Repo {
    public static final DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
}
