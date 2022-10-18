package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Fragment.PageFragment;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.MenuFoods;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.FoodViewModel;
import com.doubleclick.menu.ViewModel.MenuViewModel;
import com.doubleclick.menu.ViewModel.UserViewModel;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView name;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private UserViewModel userViewModel;
    private MenuViewModel menuViewModel;
    private FoodViewModel foodViewModel;
    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        image_profile = findViewById(R.id.image_profile);
        name = findViewById(R.id.name);
        viewPager = findViewById(R.id.viewpager);
        viewPagerTab = findViewById(R.id.viewpagertab);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getRole().equals("Disable") || user.getRole().equals("ايقاف") || user.getRole().equals("Deshabilitar")) {
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.disable), Toast.LENGTH_SHORT).show();
                    Repo.auth.signOut();
                    startActivity(new Intent(MenuActivity.this, MainActivity.class));
                }
                name.setText(user.getName());
                Glide.with(MenuActivity.this).load(user.getImage()).into(image_profile);
            }
        });

        menuViewModel.MenuItemAll().observe(this, new Observer<ArrayList<MenuItem>>() {
            @Override
            public void onChanged(ArrayList<MenuItem> menuItems) {

            }
        });


        image_profile.setOnClickListener(view -> {
            if (Repo.user != null) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                Toast.makeText(MenuActivity.this, getResources().getString(R.string.sign_in_frist), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
                finish();
            }
        });

        foodViewModel.FoodItemAll().observe(this, new Observer<ArrayList<MenuFoods>>() {
            @Override
            public void onChanged(ArrayList<MenuFoods> menuFoods) {
                for (MenuFoods item : menuFoods) {
                    creator.add(item.getMenuItem().getName(), PageFragment.class, new Bundler().putParcelableArrayList("array", item.getFood()).putString("image", item.getMenuItem().getImage()).get());
                }
                viewPager.setAdapter(adapter);
                viewPagerTab.setViewPager(viewPager);
                adapter.notifyDataSetChanged();
            }
        });
    }
}