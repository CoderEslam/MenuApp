package com.doubleclick.coinchaud.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.coinchaud.Model.User;
import com.doubleclick.coinchaud.Repository.UserRepository;
import com.doubleclick.coinchaud.Interface.UserInterface;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class UserViewModel extends ViewModel implements UserInterface {


    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userAdd = new MutableLiveData<>();
    private MutableLiveData<User> userUpdate = new MutableLiveData<>();

    private MutableLiveData<ArrayList<User>> usersMutableLiveData = new MutableLiveData<>();
    private UserRepository userRepository = new UserRepository(this);

    public UserViewModel() {
        userRepository.User();
    }

    public LiveData<ArrayList<User>> getUsers() {
        return usersMutableLiveData;
    }

    public void getUserOperation() {
        userRepository.Users();
    }

    public LiveData<User> userAdd() {
        return userAdd;
    }

    public LiveData<User> userUpdate() {
        return userUpdate;
    }

    public LiveData<User> getUser() {
        return userMutableLiveData;
    }


    @Override
    public void setUser(@NonNull User user) {
        userMutableLiveData.setValue(user);
    }

    @Override
    public void UserAdd(User user) {
        userAdd.setValue(user);
    }

    @Override
    public void UserUpdate(User user) {
        userUpdate.setValue(user);
    }

    @Override
    public void setUsers(ArrayList<User> users) {
        usersMutableLiveData.setValue(users);
    }
}
