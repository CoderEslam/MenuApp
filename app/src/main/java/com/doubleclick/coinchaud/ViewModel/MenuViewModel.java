package com.doubleclick.coinchaud.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.coinchaud.Interface.MenuInterface;
import com.doubleclick.coinchaud.Model.MenuItem;
import com.doubleclick.coinchaud.Repository.MenuRepository;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class MenuViewModel extends ViewModel implements MenuInterface {

    private MutableLiveData<MenuItem> menuItemAdd = new MutableLiveData<>();
    private MutableLiveData<MenuItem> menuItemUpdate = new MutableLiveData<>();
    private MutableLiveData<MenuItem> menuItemDelete = new MutableLiveData<>();
    private MutableLiveData<ArrayList<MenuItem>> menuItemAll = new MutableLiveData<>();


    MenuRepository menuRepository = new MenuRepository(this);

    public MenuViewModel() {
        menuRepository.Menu();
    }

    public void MenuOperators() {
        menuRepository.MenusOperator();
    }

    public LiveData<MenuItem> MenuItemAdd() {
        return menuItemAdd;
    }

    public LiveData<ArrayList<MenuItem>> MenuItemAll() {
        return menuItemAll;
    }

    public LiveData<MenuItem> MenuItemUpdate() {
        return menuItemUpdate;
    }

    public LiveData<MenuItem> MenuItemDelete() {
        return menuItemDelete;
    }


    @Override
    public void addMenuItem(MenuItem menuItem) {
        menuItemAdd.setValue(menuItem);
    }

    @Override
    public void deleteMenuItem(MenuItem menuItem) {
        menuItemDelete.setValue(menuItem);
    }

    @Override
    public void updateMenuItem(MenuItem menuItem) {
        menuItemUpdate.setValue(menuItem);
    }

    @Override
    public void AllMenuItem(ArrayList<MenuItem> menuItems) {
        menuItemAll.setValue(menuItems);
    }
}
