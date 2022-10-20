package com.doubleclick.menu.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Adapter.LeftSideMenu;
import com.doubleclick.menu.MenuActivity;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.MenuFoods;
import com.doubleclick.menu.R;
import com.doubleclick.menu.ViewModel.FoodViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NormalMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalMenuFragment extends Fragment implements LeftSideMenu.LeftMenuInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private FoodViewModel foodViewModel;
    private RecyclerView recyclerLeftSide;
    private FragmentContainerView container_normal;
    private ImageView image_bg;

    public NormalMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NormalMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NormalMenuFragment newInstance(String param1, String param2) {
        NormalMenuFragment fragment = new NormalMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        recyclerLeftSide = view.findViewById(R.id.recyclerLeftSide);
        container_normal = view.findViewById(R.id.container_normal);
        image_bg = view.findViewById(R.id.image_bg);
        foodViewModel.FoodItemAll().observe(getViewLifecycleOwner(), menuFoods -> {
            recyclerLeftSide.setAdapter(new LeftSideMenu(menuFoods, this));
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void MenuFoods(MenuFoods foods) {
        PageFragment pageFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("array", foods.getFood());
        bundle.putString("image", foods.getMenuItem().getImage());
        pageFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(container_normal.getId(), pageFragment).commit();
        Glide.with(requireActivity()).load(foods.getMenuItem().getImage()).placeholder(getResources().getDrawable(R.drawable.bg_white)).into(image_bg);
    }
}