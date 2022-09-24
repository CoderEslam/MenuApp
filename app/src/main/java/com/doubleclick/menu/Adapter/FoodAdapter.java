package com.doubleclick.menu.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
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
            Intent intent = new Intent(holder.itemView.getContext(), FoodActivity.class);
            intent.putExtra("food", foods.get(holder.getAdapterPosition()));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(foods.get(holder.getAdapterPosition()).getName());
                builder.setMessage(holder.itemView.getContext().getResources().getString(R.string.details));
                TextView text = new TextView(holder.itemView.getContext());
                text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                text.setTextSize(32f);
                text.setGravity(Gravity.CENTER);
                text.setPadding(30, 5, 30, 5);
                text.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
                text.setText(foods.get(holder.getAdapterPosition()).getDetails());
                builder.setView(text);
                builder.setCancelable(true);
                builder.setPositiveButton(holder.itemView.getContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            }
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
