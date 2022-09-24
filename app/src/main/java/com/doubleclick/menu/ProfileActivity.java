package com.doubleclick.menu;

import static com.doubleclick.menu.Model.Constant.IMAGES;
import static com.doubleclick.menu.Model.Constant.USER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Fragment.EditFragment;
import com.doubleclick.menu.Model.User;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 100;
    private ImageView back, edit;
    private CircleImageView app_bar_image;
    private TextView username_tv;
    private TextView email_tv;
    private Uri uri;
    private UserViewModel userViewModel;
    private LinearLayout add_menu_item, add_new_dish, add_person, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back = findViewById(R.id.back);
        app_bar_image = findViewById(R.id.app_bar_image);
        username_tv = findViewById(R.id.username_tv);
        email_tv = findViewById(R.id.email_tv);
        edit = findViewById(R.id.edit);
        add_menu_item = findViewById(R.id.add_menu_item);
        add_new_dish = findViewById(R.id.add_new_dish);
        add_person = findViewById(R.id.add_person);
        logout = findViewById(R.id.logout);


        back.setOnClickListener(view -> {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        });

        logout.setOnClickListener(view -> {
            Repo.auth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });


        edit.setOnClickListener(view -> {
            EditFragment editFragment = new EditFragment();
            editFragment.show(getSupportFragmentManager(), "Edit");
        });
        add_person.setOnClickListener(view -> {
            startActivity(new Intent(this, AddPersonActivity.class));
        });

        add_new_dish.setOnClickListener(view -> {
            startActivity(new Intent(this, AddNewDishActivity.class));
        });

        add_menu_item.setOnClickListener(view -> {
            startActivity(new Intent(this, AddNewMenuItemActivity.class));
        });


        app_bar_image.setOnClickListener(view -> {
            openImage();
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                username_tv.setText(user.getName());
                email_tv.setText(user.getEmail());
                Glide.with(ProfileActivity.this).load(user.getImage()).into(app_bar_image);
            }
        });
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

    public void uploadImage() {
        if (uri != null) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.show();
            final StorageReference fileReference = FirebaseStorage.getInstance()
                    .getReference(IMAGES).child(Repo.uid + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("image", task.getResult().toString());
                    Repo.refe.child(USER).child(Repo.uid).updateChildren(map);
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
            app_bar_image.setImageURI(uri);
            uploadImage();
        }
    }
}