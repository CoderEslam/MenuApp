package com.doubleclick.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.doubleclick.menu.Adapter.UserAdapter;
import com.doubleclick.menu.Interface.UserOptions;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.ViewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AddPersonActivity extends AppCompatActivity implements UserOptions {

    private UserViewModel userViewModel;
    private RecyclerView allPerson;
    private static final String TAG = "AddPersonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        allPerson = findViewById(R.id.allPerson);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                allPerson.setAdapter(new UserAdapter(users, AddPersonActivity.this));
            }
        });

    }

    @Override
    public void deleteUser(User user) {
        Toast.makeText(this, "" + user.getName(), Toast.LENGTH_SHORT).show();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User account deleted.");
                }
            }
        });
    }
}