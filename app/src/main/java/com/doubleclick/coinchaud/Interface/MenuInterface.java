package com.doubleclick.coinchaud.Interface;

import com.doubleclick.coinchaud.Model.MenuItem;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public interface MenuInterface {

    void addMenuItem(MenuItem menuItem);

    void deleteMenuItem(MenuItem menuItem);

    void updateMenuItem(MenuItem menuItem);

    void AllMenuItem(ArrayList<MenuItem> menuItems);

}
