package com.doubleclick.menu.Repository;

import androidx.annotation.NonNull;

import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Interface.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class UserRepository extends Repo {

    private UserInterface userInterface;

    public UserRepository(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void User() {
        refe.keepSynced(true);
        refe.child("User").child(uid).addValueEventListener(new ValueEventListener() {
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

}
