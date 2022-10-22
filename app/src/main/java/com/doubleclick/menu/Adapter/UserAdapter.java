package com.doubleclick.menu.Adapter;

import static com.doubleclick.menu.Model.Constant.USER;
import static com.doubleclick.menu.Service.Network.isNetworkConnected;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.AddPersonActivity;
import com.doubleclick.menu.Interface.UserOptions;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.R;
import com.doubleclick.menu.Repository.Repo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<User> users = new ArrayList<>();
    private UserOptions options;
    private List<String> optionRole;
    private static final String TAG = "UserAdapter";


    public UserAdapter(ArrayList<User> users, UserOptions options, List<String> optionRole) {
        this.users = users;
        this.options = options;
        this.optionRole = optionRole;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_person_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        Glide.with(holder.itemView.getContext()).load(users.get(position).getImage()).placeholder(R.drawable.person).into(holder.image);
        holder.setRole(users.get(holder.getAbsoluteAdapterPosition()).getId());
        // on below line we are setting selection for our spinner to spinner position.
        holder.spinner.setSelection(optionRole.indexOf(users.get(holder.getAbsoluteAdapterPosition()).getRole()));
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                options.RoleUser(users.get(holder.getAbsoluteAdapterPosition()), optionRole.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> aa = new ArrayAdapter<String>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, optionRole);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(aa);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name;
        private Spinner spinner;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            spinner = itemView.findViewById(R.id.spinner);
        }

        private void setRole(String userKey) {
            Repo.refe.child(USER).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.hasChild(userKey)) {
                            if (dataSnapshot.child(userKey).hasChild("role")) {
                                Object role = Objects.requireNonNull(dataSnapshot.child(userKey).child("role").getValue()).toString();
                                if (role.toString().equals("Manger") || role.toString().equals("مدير") || role.toString().equals("Pesebre")) {
                                    spinner.setSelection(0);
                                } else if (role.toString().equals("Waiter") || role.toString().equals("نادل") || role.toString().equals("Mesero")) {
                                    spinner.setSelection(1);
                                } else if (role.toString().equals("Disable") || role.toString().equals("ايقاف") || role.toString().equals("Deshabilitar")) {
                                    spinner.setSelection(2);
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
