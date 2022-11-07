package com.doubleclick.coinchaud.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.doubleclick.coinchaud.Interface.FoodOptions;
import com.doubleclick.coinchaud.Model.Food;
import com.doubleclick.coinchaud.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/23/2022
 */
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private ArrayList<Food> foods = new ArrayList<>();
    private FoodOptions foodOptions;

    public DishAdapter(ArrayList<Food> foods, FoodOptions foodOptions) {
        this.foods = foods;
        this.foodOptions = foodOptions;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(foods.get(holder.getAdapterPosition()).getImage()).into(holder.image);
        holder.name.setText(foods.get(holder.getAdapterPosition()).getName());
        holder.delete.setOnClickListener(view -> {
            foodOptions.deleteFood(foods.get(holder.getAdapterPosition()));
        });
        if (foods.get(holder.getBindingAdapterPosition()).getClassification().equals("VIP")) {
            holder.vip.setVisibility(View.VISIBLE);
        } else {
            holder.vip.setVisibility(View.GONE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                foodOptions.UpdateFood(foods.get(holder.getAdapterPosition()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name;
        private ImageView delete;
        private LottieAnimationView vip;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
            vip = itemView.findViewById(R.id.vip);

        }
    }
}
