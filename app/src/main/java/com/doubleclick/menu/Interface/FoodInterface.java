package com.doubleclick.menu.Interface;

import android.view.MenuInflater;

import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.MenuItem;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public interface FoodInterface {

    void addFoodItem(Food food);

    void deleteFoodItem(Food food);

    void updateFoodItem(Food food);

    void AllFoodsItem(ArrayList<ArrayList<Food>> foods);

}
