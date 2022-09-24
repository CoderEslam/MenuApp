package com.doubleclick.menu.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class Food implements Parcelable {
    private String name;
    private String image;
    private double price;
    private String id;
    private String idMenu;

    public Food() {
    }

    protected Food(Parcel in) {
        name = in.readString();
        image = in.readString();
        price = in.readDouble();
        id = in.readString();
        idMenu = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeDouble(price);
        parcel.writeString(id);
        parcel.writeString(idMenu);
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", id='" + id + '\'' +
                ", idMenu='" + idMenu + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return getId().equals(food.getId()) && idMenu.equals(food.idMenu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), idMenu);
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }
}
