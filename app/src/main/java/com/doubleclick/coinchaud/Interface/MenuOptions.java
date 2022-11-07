package com.doubleclick.coinchaud.Interface;

import com.doubleclick.coinchaud.Model.MenuItem;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public interface MenuOptions {
    void deleteMenu(MenuItem menuItem);

    void UpdateMenu(MenuItem menuItem);

    void onRowMoved(int fromPosition, int toPosition);

}
