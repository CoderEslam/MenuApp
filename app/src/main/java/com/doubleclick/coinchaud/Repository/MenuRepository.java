package com.doubleclick.coinchaud.Repository;

import static com.doubleclick.coinchaud.Model.Constant.INDEX;
import static com.doubleclick.coinchaud.Model.Constant.MENU;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doubleclick.coinchaud.Interface.MenuInterface;
import com.doubleclick.coinchaud.Model.MenuItem;
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
        refe.child(MENU).orderByChild(INDEX).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuItem menuItem = snapshot.getValue(MenuItem.class);
                menuInterface.addMenuItem(menuItem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuItem menuItem = snapshot.getValue(MenuItem.class);
                Log.e(TAG, "onChildChanged: " + menuItem.getName());
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
        refe.child(MENU).orderByChild(INDEX).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = dataSnapshot.getValue(MenuItem.class);
                    menuItems.add(menuItem);
                }
                menuInterface.AllMenuItem(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
