package com.doubleclick.menu.Repository;

import static com.doubleclick.menu.Model.Constant.FOOD;
import static com.doubleclick.menu.Model.Constant.MENU;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doubleclick.menu.Interface.FoodInterface;
import com.doubleclick.menu.Interface.MenuInterface;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.MenuFoods;
import com.doubleclick.menu.Model.MenuItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class FoodRepository extends Repo {

    private static final String TAG = "FoodRepository";
    private FoodInterface foodInterface;

    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    public FoodRepository(FoodInterface foodInterface) {
        this.foodInterface = foodInterface;
    }


    public void FoodsOperator() {
        refe.keepSynced(true);
        refe.child(FOOD).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                Log.e(TAG, "onChildAdded: " + food.toString());
                foodInterface.addFoodItem(food);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                foodInterface.updateFoodItem(food);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                foodInterface.deleteFoodItem(food);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Menus() {
        refe.keepSynced(true);
        refe.child(MENU).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = dataSnapshot.getValue(MenuItem.class);
                    assert menuItem != null;
                    menuItems.add(menuItem);
                }
                Foods();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Foods() {
        refe.child(FOOD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Food foodObject = dataSnapshot1.getValue(Food.class);
                    foods.add(foodObject);
                }
                Rearengment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void Rearengment() {
        ArrayList<MenuFoods> menuFoods = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            ArrayList<Food> fods = new ArrayList<>();
            for (Food food : foods) {
                if (menuItem.getId().equals(food.getIdMenu())) {
                    fods.add(food);
                }
            }
            menuFoods.add(new MenuFoods(fods, menuItem));
        }
        foodInterface.AllFoodsItem(menuFoods);
    }


}
