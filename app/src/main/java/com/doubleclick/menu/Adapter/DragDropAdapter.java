package com.doubleclick.menu.Adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Interface.MenuOptions;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.R;
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 10/2/2022
 */
public class DragDropAdapter extends DragDropSwipeAdapter<MenuItem, DragDropAdapter.DragDropViewHolder> {

    private MenuOptions menuOptions;
    private static final String TAG = "DragDropAdapter";

    public DragDropAdapter(@NonNull List<? extends MenuItem> dataSet, MenuOptions menuOptions) {
        super(dataSet);
        this.menuOptions = menuOptions;
    }

    @NonNull
    @Override
    protected DragDropViewHolder getViewHolder(@NonNull View view) {
        return new DragDropViewHolder(view);
    }

    @Nullable
    @Override
    protected View getViewToTouchToStartDraggingItem(MenuItem menuItem, @NonNull DragDropViewHolder viewHolder, int i) {
        return viewHolder.icon;
    }

    @Override
    protected void onBindViewHolder(MenuItem menuItem, @NonNull DragDropViewHolder viewHolder, int i) {
        viewHolder.name.setText(menuItem.getName());
        Log.e(TAG, "onBindViewHolder: " + menuItem.Menu());
        Glide.with(viewHolder.view.getContext()).load(menuItem.getImage()).into(viewHolder.image);
        viewHolder.delete.setOnClickListener(view -> {
            menuOptions.deleteMenu(menuItem);
        });
        viewHolder.view.setOnLongClickListener(view -> {
            menuOptions.UpdateMenu(menuItem);
            return true;
        });
    }

    public class DragDropViewHolder extends ViewHolder {
        private CircleImageView image;
        private TextView name;
        private ImageView icon, delete;
        private View view;

        public DragDropViewHolder(@NonNull View layout) {
            super(layout);
            image = layout.findViewById(R.id.image);
            icon = layout.findViewById(R.id.icon);
            name = layout.findViewById(R.id.name);
            delete = layout.findViewById(R.id.delete);
            view = layout;
        }
    }
}
