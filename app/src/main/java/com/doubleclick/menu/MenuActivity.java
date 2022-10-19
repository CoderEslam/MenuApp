package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Adapter.ViewPagerAdapter;
import com.doubleclick.menu.Fragment.NormalMenuFragment;
import com.doubleclick.menu.Fragment.VIPMenuFragment;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.UserViewModel;
import com.doubleclick.menu.Views.bubblenavigation.BubbleNavigationConstraintView;
import com.doubleclick.menu.Views.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView name;
    private ViewPager viewPager;
    //    private SmartTabLayout viewPagerTab;
    private UserViewModel userViewModel;
    private static final String TAG = "MenuActivity";
    private BubbleNavigationConstraintView floating_top_bar_navigation;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        image_profile = findViewById(R.id.image_profile);
        name = findViewById(R.id.name);
        viewPager = findViewById(R.id.viewpager);
//        viewPagerTab = findViewById(R.id.viewpagertab);
        floating_top_bar_navigation = findViewById(R.id.floating_top_bar_navigation);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
//        creator.add("Normal", NormalMenuFragment.class);
//        creator.add("VIP", VIPMenuFragment.class);
//        viewPager.setAdapter(adapter);
//        viewPagerTab.setViewPager(viewPager);
//        adapter.notifyDataSetChanged();
        userViewModel.getUser().observe(this, user -> {
            if (user != null && Repo.user != null) {
                if (user.getRole().equals("Disable") || user.getRole().equals("ايقاف") || user.getRole().equals("Deshabilitar")) {
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.disable), Toast.LENGTH_SHORT).show();
                    Repo.auth.signOut();
                    startActivity(new Intent(MenuActivity.this, MainActivity.class));
                }
                name.setText(user.getName());
                Glide.with(MenuActivity.this).load(user.getImage()).into(image_profile);
            }
        });
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), 2));
        viewPager.setOnTouchListener((view, motionEvent) -> false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                floating_top_bar_navigation.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        floating_top_bar_navigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position, true);
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

    }
}