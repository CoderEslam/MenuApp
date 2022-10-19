package com.doubleclick.menu.Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/24/2022
 */
public class MenuFoods {

    private MenuItem menuItem;
    private ArrayList<Food> food;
    private int id;


    public MenuFoods(ArrayList<Food> food, MenuItem menuItem) {
        this.menuItem = menuItem;
        this.food = food;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public ArrayList<Food> getFood() {
        return food;
    }

    public void setFood(ArrayList<Food> food) {
        this.food = food;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuFoods)) return false;
        MenuFoods menuFoods = (MenuFoods) o;
        return getMenuItem().equals(menuFoods.getMenuItem()) && getFood().equals(menuFoods.getFood());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuItem(), getFood());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
