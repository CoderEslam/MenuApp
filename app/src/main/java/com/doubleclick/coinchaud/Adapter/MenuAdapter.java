package com.doubleclick.coinchaud.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.coinchaud.Interface.MenuOptions;
import com.doubleclick.coinchaud.Model.MenuItem;
import com.doubleclick.coinchaud.R;
import com.doubleclick.coinchaud.Service.ItemMoveCallback;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.UserViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
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
        holder.option.setOnClickListener(view -> {
            PopupMenu menu = new PopupMenu(holder.itemView.getContext(), view);
            menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
            menu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.edit) {
                    menuOptions.UpdateMenu(menuItems.get(holder.getAdapterPosition()));
                    return true;
                } else if (menuItem.getItemId() == R.id.delete) {
                    menuOptions.deleteMenu(menuItems.get(holder.getAdapterPosition()));
                    return true;
                } else {
                    return false;
                }
            });
            menu.show();
        });


    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(menuItems, i, i + 1);
                menuOptions.onRowMoved(fromPosition, toPosition);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(menuItems, i, i - 1);
                menuOptions.onRowMoved(fromPosition, toPosition);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(UserViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(UserViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name;
        private ImageView option;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            option = itemView.findViewById(R.id.option);
        }
    }
}
