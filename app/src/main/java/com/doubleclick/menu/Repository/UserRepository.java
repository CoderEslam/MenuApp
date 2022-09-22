package com.doubleclick.menu.Repository;

import static com.doubleclick.menu.Model.Constant.USER;

import android.util.Log;

import androidx.annotation.NonNull;

import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Interface.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class UserRepository extends Repo {

    private UserInterface userInterface;
    private ArrayList<User> users = new ArrayList<>();
    private static final String TAG = "UserRepository";

    public UserRepository(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void User() {
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
        refe.child(USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.e(TAG, "onDataChange: " + user.toString());
                    assert user != null;
                    users.add(user);
                    userInterface.setUsers(users);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
