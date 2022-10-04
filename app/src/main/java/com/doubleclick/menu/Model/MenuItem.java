package com.doubleclick.menu.Model;

import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class MenuItem {
    private String name;
    private String image;
    private String id;
    private int index;

    public MenuItem(String id) {
        this.id = id;
        name = "";
        image = "";
        index = 0;
    }

    public MenuItem(Object name) {
        this.id = "";
        this.name = name.toString();
        image = "";
        index = 0;
    }

    public MenuItem() {
        name = "";
        id = "";
        image = "";
        index = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem menuItem = (MenuItem) o;
        return (getId().equals(menuItem.getId()) || getName().equals(menuItem.getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return name;
    }

    public String Menu() {
        return name + "\n" + id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
