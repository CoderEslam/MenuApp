package com.doubleclick.menu.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.FoodActivity;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<Food> foods = new ArrayList<>();

    public FoodAdapter(ArrayList<Food> foods) {
        this.foods = foods;
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(foods.get(holder.getAdapterPosition()).getImage()).into(holder.img_food);
        holder.name_food.setText(foods.get(holder.getAdapterPosition()).getName());
        holder.price_food.setText(String.valueOf(foods.get(holder.getAdapterPosition()).getPrice()));
        holder.itemView.setOnClickListener(view -> {
            holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), FoodActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_food;
        private TextView name_food, price_food;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            img_food = itemView.findViewById(R.id.img_food);
            name_food = itemView.findViewById(R.id.name_food);
            price_food = itemView.findViewById(R.id.price_food);

        }
    }
}
