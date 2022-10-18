package com.doubleclick.menu;

import static com.doubleclick.menu.Model.Constant.IMAGES;
import static com.doubleclick.menu.Model.Constant.USER;
import static com.doubleclick.menu.Service.Network.isNetworkConnected;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.doubleclick.menu.Adapter.UserAdapter;
import com.doubleclick.menu.Interface.UserOptions;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPersonActivity extends AppCompatActivity implements UserOptions {

    private UserViewModel userViewModel;
    private RecyclerView allPerson;
    private UserAdapter userAdapter;
    private CircleImageView image;
    private TextInputEditText name, email, password;
    private SmartMaterialSpinner<String> spinner;
    private Button register;
    private ArrayList<User> users = new ArrayList<>();
    private static final String TAG = "AddPersonActivity";
    private static final int IMAGE_REQUEST = 100;
    private Uri uri;
    private List<String> optionRole;
    private String roleSelected = "";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        allPerson = findViewById(R.id.allPerson);
        spinner = findViewById(R.id.spinner);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        image = findViewById(R.id.image);
        optionRole = Arrays.asList(getResources().getStringArray(R.array.user_option));
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserOperation();
        userAdapter = new UserAdapter(users, AddPersonActivity.this, optionRole);
        allPerson.setAdapter(userAdapter);


        userViewModel.userAdd().observe(this, user -> {
            if (!users.contains(user)) {
                users.add(user);
                userAdapter.notifyItemInserted(users.size() - 1);
                userAdapter.notifyDataSetChanged();
            }
        });

        userViewModel.userUpdate().observe(this, user -> {
            int pos = users.indexOf(user);
            users.set(pos, user);
            userAdapter.notifyItemChanged(pos);
            userAdapter.notifyDataSetChanged();

        });

        register.setOnClickListener(view -> {
            SignUp(Objects.requireNonNull(name.getText()).toString().trim(), Objects.requireNonNull(email.getText()).toString().trim(), Objects.requireNonNull(password.getText()).toString().trim());
        });

        image.setOnClickListener(view -> {
            openImage();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roleSelected = optionRole.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setSelection(1);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPersonActivity.this, android.R.layout.simple_spinner_item, optionRole);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void SignUp(String name, String email, String password) {
        if (!name.equals("") && !email.equals("") && !password.equals("") && !uri.toString().equals("") && !roleSelected.equals("")) {
            Repo.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && isNetworkConnected(AddPersonActivity.this)) {
                        uploadImage(name, email);
                    }
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.required), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void RoleUser(User user, String role) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("role", role);
        Repo.refe.child(USER).child(user.getId()).updateChildren(map);
    }


    public void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(String n, String e) {
        if (uri != null) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.show();
            String id = Objects.requireNonNull(Repo.auth.getCurrentUser()).getUid().toString();
            final StorageReference fileReference = FirebaseStorage.getInstance()
                    .getReference(IMAGES).child(id + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", n);
                    map.put("email", e);
                    map.put("role", roleSelected);
                    map.put("id", id);
                    map.put("image", task.getResult().toString());
                    Repo.refe.child(USER).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            image.setImageURI(null);
                            name.setText("");
                            email.setText("");
                            password.setText("");
                            roleSelected = "";
                            Toast.makeText(AddPersonActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                            Repo.auth.signOut();
                            startActivity(new Intent(AddPersonActivity.this, MainActivity.class));
                            Repo.auth = null;
                            Repo.user = null;
                            finish();
                        }
                    });
                    pd.dismiss();
                });
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            image.setImageURI(uri);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}