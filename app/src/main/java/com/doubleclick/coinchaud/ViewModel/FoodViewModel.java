package com.doubleclick.coinchaud.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.coinchaud.Interface.FoodInterface;
import com.doubleclick.coinchaud.Model.Food;
import com.doubleclick.coinchaud.Model.MenuFoods;
import com.doubleclick.coinchaud.Repository.FoodRepository;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class FoodViewModel extends ViewModel implements FoodInterface {

    private MutableLiveData<Food> foodItemAdd = new MutableLiveData<>();
    private MutableLiveData<Food> foodItemUpdate = new MutableLiveData<>();
    private MutableLiveData<Food> foodItemDelete = new MutableLiveData<>();
    private MutableLiveData<ArrayList<MenuFoods>> foodItemAll = new MutableLiveData<>();
    private MutableLiveData<ArrayList<MenuFoods>> foodItemAllVIP = new MutableLiveData<>();

    FoodRepository foodRepository = new FoodRepository(this);

    public FoodViewModel() {
        foodRepository.Menus();
    }

    public void MenuOperators() {
        foodRepository.FoodsOperator();
    }

    public LiveData<Food> FoodItemAdd() {
        return foodItemAdd;
    }

    public LiveData<ArrayList<MenuFoods>> FoodItemAll() {
        return foodItemAll;
    }

    public LiveData<ArrayList<MenuFoods>> FoodItemAllVIP() {
        return foodItemAllVIP;
    }

    public LiveData<Food> FoodItemUpdate() {
        return foodItemUpdate;
    }

    public LiveData<Food> FoodItemDelete() {
        return foodItemDelete;
    }


    @Override
    public void addFoodItem(Food food) {
        foodItemAdd.setValue(food);
    }

    @Override
    public void deleteFoodItem(Food food) {
        foodItemDelete.setValue(food);
    }

    @Override
    public void updateFoodItem(Food food) {
        foodItemUpdate.setValue(food);
    }

    @Override
    public void AllFoodsItem(ArrayList<MenuFoods> foods) {
        foodItemAll.setValue(foods);
    }

    @Override
    public void AllFoodsItemVIP(ArrayList<MenuFoods> foods) {
        foodItemAllVIP.setValue(foods);
    }
}
