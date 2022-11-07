package com.doubleclick.coinchaud.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class Food implements Parcelable {
    private String name;
    private String image;
    private double priceSmall;
    private double priceMedium;
    private double priceLarge;
    private String id;
    private String idMenu;
    private String details;
    private String classification;

    public Food() {
        details = "";
        image = "";
        priceSmall = 0;
        priceMedium = 0;
        priceLarge = 0;
        idMenu = "";
        id = "";
        name = "";
        classification = "";
    }

    protected Food(Parcel in) {
        name = in.readString();
        image = in.readString();
        priceSmall = in.readDouble();
        priceMedium = in.readDouble();
        priceLarge = in.readDouble();
        id = in.readString();
        idMenu = in.readString();
        details = in.readString();
        classification = in.readString();

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + priceSmall +
                ", id='" + id + '\'' +
                ", idMenu='" + idMenu + '\'' +
                '}';
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeDouble(priceSmall);
        parcel.writeDouble(priceMedium);
        parcel.writeDouble(priceLarge);
        parcel.writeString(id);
        parcel.writeString(idMenu);
        parcel.writeString(details);
        parcel.writeString(classification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return getId().equals(food.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public double getPriceSmall() {
        return priceSmall;
    }

    public double getPriceMedium() {
        return priceMedium;
    }

    public double getPriceLarge() {
        return priceLarge;
    }
}
