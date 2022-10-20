package com.doubleclick.menu;

import static com.doubleclick.menu.App.App.deleteCache;
import static com.doubleclick.menu.Model.Constant.USER;
import static com.doubleclick.menu.Service.Network.isNetworkConnected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.doubleclick.menu.Repository.Repo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText TextInputEditTextName, TextInputEditTextEmail, TextInputEditTextPassword;
    private Button sign_up;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextInputEditTextName = findViewById(R.id.name);
        TextInputEditTextEmail = findViewById(R.id.email);
        TextInputEditTextPassword = findViewById(R.id.password);
        sign_up = findViewById(R.id.sign_up);
        progressBar = findViewById(R.id.progressBar);

        sign_up.setOnClickListener(view -> {
            if (Check()) {
                if (isNetworkConnected(SignUpActivity.this)) {
                    SignUp(Objects.requireNonNull(TextInputEditTextEmail.getText()).toString().trim(), Objects.requireNonNull(TextInputEditTextPassword.getText()).toString().trim(), Objects.requireNonNull(TextInputEditTextName.getText()).toString().trim());
                    progressBar.setVisibility(View.VISIBLE);
                    sign_up.setEnabled(false);
                    sign_up.setTextColor(getResources().getColor(R.color.gray));
                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SignUp(String email, String password, String name) {
        Repo.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && isNetworkConnected(SignUpActivity.this)) {
                HashMap<String, Object> map = new HashMap<>();
                String id = Objects.requireNonNull(Repo.auth.getCurrentUser()).getUid().toString();
                map.put("name", name);
                map.put("email", email);
                map.put("role", "Disable");
                map.put("id", id);
                map.put("image", "");
                Repo.refe.child(USER).child(id).updateChildren(map).addOnCompleteListener(task1 -> {
                    TextInputEditTextName.setText("");
                    TextInputEditTextEmail.setText("");
                    TextInputEditTextPassword.setText("");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                    Repo.auth.signOut();
                    deleteCache(SignUpActivity.this);
                });
            }
        });
    }


    private boolean Check() {
        if (Objects.requireNonNull(TextInputEditTextEmail.getText()).toString().equals("") && Objects.requireNonNull(TextInputEditTextPassword.getText()).toString().equals("") && Objects.requireNonNull(TextInputEditTextName.getText()).toString().equals("")) {
            TextInputEditTextEmail.setError(getResources().getString(R.string.input_email));
            TextInputEditTextPassword.setError(getResources().getString(R.string.input_password));
            TextInputEditTextName.setError(getResources().getString(R.string.input_name));
            return false;
        } else {
            return true;
        }
    }

}