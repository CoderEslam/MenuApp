package com.doubleclick.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Fragment.PageFragment;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.UserViewModel;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView name;
    private UserViewModel userViewModel;
    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        image_profile = findViewById(R.id.image_profile);
        name = findViewById(R.id.name);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                name.setText(user.getName());
                Glide.with(MenuActivity.this).load(user.getImage()).into(image_profile);
            }
        });
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.sign_in, PageFragment.class)
                .add(R.string.sign_up, PageFragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        image_profile.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        Log.e(TAG, "onCreate: " + Repo.uid);
    }
}