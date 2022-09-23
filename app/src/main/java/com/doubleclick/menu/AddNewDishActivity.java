package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.doubleclick.menu.Adapter.SpinnerAdapter;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.ViewModel.MenuViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddNewDishActivity extends AppCompatActivity {

    private MenuViewModel menuViewModel;
    private ImageView image;
    private TextInputEditText name, price;
    private SmartMaterialSpinner spinner;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_dish);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        price = findViewById(R.id.price_food);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.MenuItemAll().observe(this, menuItems -> {
            SpinnerAdapter aa = new SpinnerAdapter(AddNewDishActivity.this, menuItems);
            aa.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(aa);
        });
    }
}