package com.doubleclick.coinchaud.Interface;

import com.doubleclick.coinchaud.Model.Food;
import com.doubleclick.coinchaud.Model.MenuFoods;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public interface FoodInterface {

    void addFoodItem(Food food);

    void deleteFoodItem(Food food);

    void updateFoodItem(Food food);

    void AllFoodsItem(ArrayList<MenuFoods> foods);

    void AllFoodsItemVIP(ArrayList<MenuFoods> foods);

}
