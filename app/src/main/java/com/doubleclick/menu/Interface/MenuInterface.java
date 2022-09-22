package com.doubleclick.menu.Interface;

import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Model.User;

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
