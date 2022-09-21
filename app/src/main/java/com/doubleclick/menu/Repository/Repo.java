package com.doubleclick.menu.Repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class Repo {

    public static final DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
    public static final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().toString();


}
