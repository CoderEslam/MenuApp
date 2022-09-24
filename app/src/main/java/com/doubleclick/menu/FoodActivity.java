package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.User;

public class FoodActivity extends AppCompatActivity {

    private TextView name_food, price_food, details, number, total_price;
    private ImageView minimize, add, image_food;
    private RecyclerView recyclerView_ingrendiets;
    private int quntity = 0;
    private Food food;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        name_food = findViewById(R.id.name_food);
        price_food = findViewById(R.id.price_food);
        details = findViewById(R.id.details);
        number = findViewById(R.id.number);
        minimize = findViewById(R.id.minimize);
        add = findViewById(R.id.add);
        total_price = findViewById(R.id.total_price);
        image_food = findViewById(R.id.image_food);
        food = (Food) getIntent().getParcelableExtra("food");
        recyclerView_ingrendiets = findViewById(R.id.recyclerView_ingrendiets);
        Glide.with(this).load(food.getImage()).into(image_food);
        name_food.setText(food.getName());
        price_food.setText(String.valueOf(food.getPrice()));
        details.setText(food.getDetails());
        add.setOnClickListener(view -> {
            quntity++;
            number.setText(String.valueOf(quntity));
            total_price.setText(String.valueOf(quntity * food.getPrice()));
        });


        minimize.setOnClickListener(view -> {
            if (quntity > 0) {
                quntity--;
                number.setText(String.valueOf(quntity));
                total_price.setText(String.valueOf(quntity * food.getPrice()));
            }

        });


    }
}