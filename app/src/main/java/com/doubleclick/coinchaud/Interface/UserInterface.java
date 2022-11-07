package com.doubleclick.coinchaud.Interface;

import com.doubleclick.coinchaud.Model.User;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public interface UserInterface {
    void setUser(User user);

    void UserAdd(User user);

    void UserUpdate(User user);

    void setUsers(ArrayList<User> users);

}
