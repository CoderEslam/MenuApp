package com.doubleclick.menu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Interface.MenuOptions;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.UserViewHolder> {
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private MenuOptions menuOptions;

    public MenuAdapter(ArrayList<MenuItem> menuItems, MenuOptions menuOptions) {
        this.menuItems = menuItems;
        this.menuOptions = menuOptions;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(menuItems.get(position).getName());
        Glide.with(holder.itemView.getContext()).load(menuItems.get(position).getImage()).into(holder.image);
        holder.delete.setOnClickListener(view -> {
            menuOptions.deleteMenu(menuItems.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name;
        private ImageView delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
