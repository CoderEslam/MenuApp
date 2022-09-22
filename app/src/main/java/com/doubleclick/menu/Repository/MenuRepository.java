package com.doubleclick.menu.Repository;

import static com.doubleclick.menu.Model.Constant.MENU;
import static com.doubleclick.menu.Model.Constant.USER;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doubleclick.menu.Interface.MenuInterface;
import com.doubleclick.menu.Interface.UserInterface;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class MenuRepository extends Repo {

    private static final String TAG = "MenuRepository";
    private MenuInterface menuInterface;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    public MenuRepository(MenuInterface menuInterface) {
        this.menuInterface = menuInterface;
    }

    public void MenusOperator() {
        refe.keepSynced(true);
        refe.child(MENU).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuItem menuItem = snapshot.getValue(MenuItem.class);
                menuInterface.addMenuItem(menuItem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuItem menuItem = snapshot.getValue(MenuItem.class);
                menuInterface.updateMenuItem(menuItem);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                MenuItem menuItem = snapshot.getValue(MenuItem.class);
                menuInterface.deleteMenuItem(menuItem);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Menu() {
        refe.keepSynced(true);
        refe.child(MENU).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = dataSnapshot.getValue(MenuItem.class);
                    menuItems.add(menuItem);
                    Log.e(TAG, "onDataChange: " + menuItem.toString());
                }
                menuInterface.AllMenuItem(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
