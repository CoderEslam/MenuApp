package com.doubleclick.menu.Repository;

import static com.doubleclick.menu.Model.Constant.USER;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Interface.UserInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class UserRepository extends Repo {

    private UserInterface userInterface;
    private static final String TAG = "UserRepository";
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String uid;

    public UserRepository(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void User() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        uid = Objects.requireNonNull(firebaseUser).getUid();
        refe.keepSynced(true);
        refe.child(USER).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userInterface.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Users() {
        refe.keepSynced(true);
        refe.child(USER).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userInterface.UserAdd(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userInterface.UserUpdate(user);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
