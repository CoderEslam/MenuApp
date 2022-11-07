package com.doubleclick.coinchaud;

import static com.doubleclick.coinchaud.Model.Constant.IMAGES;
import static com.doubleclick.coinchaud.Model.Constant.USER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doubleclick.coinchaud.Fragment.EditFragment;
import com.doubleclick.coinchaud.Model.User;
import com.doubleclick.coinchaud.Repository.Repo;
import com.doubleclick.coinchaud.ViewModel.UserViewModel;
import com.doubleclick.coinchaud.Views.cropper.CropImage;
import com.doubleclick.coinchaud.Views.cropper.CropImageView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 100;
    private ImageView back, edit;
    private CircleImageView app_bar_image;
    private TextView username_tv;
    private TextView email_tv;
    private Uri uri;
    private UserViewModel userViewModel;
    private LinearLayout add_menu_item, add_new_dish, add_person, see_menu, logout;
    private CardView adding;
    private View divider1;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String uid;

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
        see_menu = findViewById(R.id.see_menu);
        logout = findViewById(R.id.logout);
        adding = findViewById(R.id.adding);
        divider1 = findViewById(R.id.divider1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.profile_details));
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        uid = Objects.requireNonNull(firebaseUser).getUid();
        back.setOnClickListener(view -> {
            finish();
        });
        see_menu.setOnClickListener(view -> {
            startActivity(new Intent(this, SeeMenuActivity.class));
        });

        logout.setOnClickListener(view -> {
            if (firebaseUser != null) {
                auth.signOut();
                userViewModel.getUser().removeObservers(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });


        edit.setOnClickListener(view -> {
            EditFragment editFragment = new EditFragment();
            editFragment.show(getSupportFragmentManager(), "Edit");
        });
        add_person.setOnClickListener(view -> {
            if (firebaseUser != null) {
                startActivity(new Intent(this, AddPersonActivity.class));
            }
        });

        add_new_dish.setOnClickListener(view -> {
            if (firebaseUser != null) {
                startActivity(new Intent(this, AddNewDishActivity.class));
            }
        });

        add_menu_item.setOnClickListener(view -> {
            if (firebaseUser != null) {
                startActivity(new Intent(this, AddNewMenuItemActivity.class));
            }
        });


        app_bar_image.setOnClickListener(view -> {
            openImage();
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (firebaseUser != null) {
                    if (user.getRole().equals("Manger") || user.getRole().equals("مدير") || user.getRole().equals("Deshabilitar")) {
                        adding.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);
                    } else {
                        adding.setVisibility(View.GONE);
                        divider1.setVisibility(View.GONE);
                    }
                    username_tv.setText(user.getName());
                    email_tv.setText(user.getEmail());
                    Glide.with(ProfileActivity.this).load(user.getImage()).placeholder(R.drawable.person).into(app_bar_image);
                }
            }
        });
    }

    public void openImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST);
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
                    .getReference(IMAGES).child(uid + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("image", task.getResult().toString());
                    Repo.refe.child(USER).child(uid).updateChildren(map);
                    pd.dismiss();
                });
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                String filePath = SiliCompressor.with(ProfileActivity.this).compress(
                        result.getUri().toString(),
                        new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        .toString() + "/Menu/Images/"
                        )
                );
                uri = Uri.parse(filePath);
                app_bar_image.setImageURI(uri);
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}