package com.doubleclick.menu.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Repository.UserRepository;
import com.doubleclick.menu.Interface.UserInterface;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class UserViewModel extends ViewModel implements UserInterface {


    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<User>> usersMutableLiveData = new MutableLiveData<>();
    private UserRepository userRepository = new UserRepository(this);

    public UserViewModel() {
        userRepository.User();
    }

    public LiveData<ArrayList<User>> getUsers() {
        userRepository.Users();
        return usersMutableLiveData;
    }

    public LiveData<User> getUser() {
        return userMutableLiveData;
    }


    @Override
    public void setUser(@NonNull User user) {
        userMutableLiveData.setValue(user);
    }

    @Override
    public void setUsers(ArrayList<User> users) {
        usersMutableLiveData.setValue(users);
    }
}
