package com.doubleclick.coinchaud.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.coinchaud.Model.MenuFoods;
import com.doubleclick.coinchaud.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 10/18/2022
 */
public class LeftSideMenu extends RecyclerView.Adapter<LeftSideMenu.LeftSideViewHolder> {

    private ArrayList<MenuFoods> menuFoods;
    private LeftMenuInterface leftMenuInterface;
    private static final String TAG = "LeftSideMenu";

    public LeftSideMenu(ArrayList<MenuFoods> menuFoods, LeftMenuInterface leftMenuInterface) {
        this.menuFoods = menuFoods;
        this.leftMenuInterface = leftMenuInterface;
    }


    @NonNull
    @Override
    public LeftSideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeftSideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeftSideViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(menuFoods.get(holder.getAbsoluteAdapterPosition()).getMenuItem().getImage()).into(holder.image);
        holder.name.setText(menuFoods.get(holder.getAbsoluteAdapterPosition()).getMenuItem().getName());
        holder.name.setSelected(true);
        try {
            leftMenuInterface.MenuFoods(menuFoods.get(0));
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: " + e.getMessage());
        }
        holder.itemView.setOnClickListener(view -> {
            leftMenuInterface.MenuFoods(menuFoods.get(holder.getAbsoluteAdapterPosition()));
        });
    }


    @Override
    public int getItemCount() {
        return menuFoods.size();
    }

    public class LeftSideViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name;

        public LeftSideViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }


    public interface LeftMenuInterface {
        void MenuFoods(MenuFoods foods);
    }
}
