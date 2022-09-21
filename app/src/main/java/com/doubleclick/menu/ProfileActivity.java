package com.doubleclick.menu;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private ImageView back, done;
    private CircleImageView app_bar_image;
    private EditText username_et;
    private TextView email_txt;
    private Uri uri;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back = findViewById(R.id.back);
        done = findViewById(R.id.done);
        app_bar_image = findViewById(R.id.app_bar_image);
        username_et = findViewById(R.id.username_et);
        email_txt = findViewById(R.id.email_et);


        back.setOnClickListener(view -> {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        });

        done.setOnClickListener(view -> {
            update();
        });

        app_bar_image.setOnClickListener(view -> {
            openImage();
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                username_et.setText(user.getName());
                email_txt.setText(user.getEmail());
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


    private void update() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", Repo.uid);
        map.put("name", username_et.getText().toString());
        Repo.refe.child("User").child(Repo.uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    uploadImage();
                    startActivity(new Intent(ProfileActivity.this, MenuActivity.class));
                    finish();
                }
            }
        });
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance()
                    .getReference("Images").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                    url.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("image", task.getResult().toString());
                            Repo.refe.child("User").child(Repo.uid).updateChildren(map);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        uri = data.getData();
        app_bar_image.setImageURI(uri);
    }
}