package com.doubleclick.menu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Interface.UserOptions;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<User> users = new ArrayList<>();
    private UserOptions options;


    public UserAdapter(ArrayList<User> users, UserOptions options) {
        this.users = users;
        this.options = options;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_person_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        Glide.with(holder.itemView.getContext()).load(users.get(position).getImage()).into(holder.image);
        holder.delete.setOnClickListener(view -> {
            options.deleteUser(users.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
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
