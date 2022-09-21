package com.doubleclick.menu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doubleclick.menu.R;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        public FoodViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }
}
