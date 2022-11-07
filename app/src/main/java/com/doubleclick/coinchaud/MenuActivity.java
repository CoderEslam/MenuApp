package com.doubleclick.coinchaud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doubleclick.coinchaud.Fragment.NormalMenuFragment;
import com.doubleclick.coinchaud.Fragment.VIPMenuFragment;
import com.doubleclick.coinchaud.Interface.onClickMenu;
import com.doubleclick.coinchaud.ViewModel.UserViewModel;
import com.doubleclick.coinchaud.Views.bubblenavigation.BubbleNavigationConstraintView;
import com.doubleclick.coinchaud.Views.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private TextView name;
    //    private ViewPager viewPager;
    //    private SmartTabLayout viewPagerTab;
    private UserViewModel userViewModel;
    private static final String TAG = "MenuActivity";
    private FragmentContainerView container_menu;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private BubbleNavigationConstraintView floating_top_bar_navigation;
    private onClickMenu onClick;
    private ImageView menu;

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        image_profile = findViewById(R.id.image_profile);
        name = findViewById(R.id.name);
        container_menu = findViewById(R.id.container_menu);
        menu = findViewById(R.id.menu);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        Log.e(TAG, "onCreate: " + firebaseUser.getUid());
        floating_top_bar_navigation = findViewById(R.id.floating_top_bar_navigation);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, user -> {
            if (user != null && firebaseUser != null) {
                if (user.getRole().equals("Disable") || user.getRole().equals("ايقاف") || user.getRole().equals("Deshabilitar")) {
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.disable), Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent(MenuActivity.this, MainActivity.class));
                }
                name.setText(user.getName());
                Glide.with(MenuActivity.this).load(user.getImage()).placeholder(R.drawable.person).into(image_profile);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(container_menu.getId(), new NormalMenuFragment()).commit();
        floating_top_bar_navigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
//                viewPager.setCurrentItem(position, true);
                if (position == 0) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(container_menu.getId(), new NormalMenuFragment())
                            .commit();
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(container_menu.getId(), new VIPMenuFragment())
                            .commit();
                }
            }
        });

        image_profile.setOnClickListener(view -> {
            if (firebaseUser != null) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                Toast.makeText(MenuActivity.this, getResources().getString(R.string.sign_in_frist), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
        menu.setOnClickListener(view -> {
            onClick.onClick(view);
        });
    }


    public void setOnClick(onClickMenu onClick) {
        this.onClick = onClick;
    }
}