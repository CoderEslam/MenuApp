package com.doubleclick.menu;

import static com.doubleclick.menu.Model.Constant.IMAGES;
import static com.doubleclick.menu.Model.Constant.MENU;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.doubleclick.menu.Adapter.MenuAdapter;
import com.doubleclick.menu.Interface.MenuOptions;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.MenuViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddNewMenuItemActivity extends AppCompatActivity implements MenuOptions {

    private ImageView image;
    private TextInputEditText name;
    private Button upload;
    private RecyclerView menu_items;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private MenuViewModel menuViewModel;
    private MenuAdapter menuAdapter;
    private Uri uri;
    private static final int IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_menu_item);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        upload = findViewById(R.id.upload);
        menu_items = findViewById(R.id.menu_items);
        menuAdapter = new MenuAdapter(menuItems, this);
        menu_items.setAdapter(menuAdapter);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.MenuOperators();
        menuViewModel.MenuItemAdd().observe(this, new Observer<MenuItem>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(MenuItem menuItem) {
                menuItems.add(menuItem);
                menuAdapter.notifyItemInserted(menuItems.size() - 1);
                menuAdapter.notifyDataSetChanged();
            }
        });

        menuViewModel.MenuItemDelete().observe(this, new Observer<MenuItem>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(MenuItem menuItem) {
                menuItems.remove(menuItem);
                menuAdapter.notifyItemRemoved(menuItems.indexOf(menuItem));
                menuAdapter.notifyDataSetChanged();
            }
        });

        menuViewModel.MenuItemUpdate().observe(this, new Observer<MenuItem>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(MenuItem menuItem) {
                menuItems.set(menuItems.indexOf(menuItem), menuItem);
                menuAdapter.notifyItemChanged(menuItems.indexOf(menuItem));
                menuAdapter.notifyDataSetChanged();
            }
        });

        image.setOnClickListener(view -> {
            openImage();
        });

        upload.setOnClickListener(view -> {
            uploadImage();
        });

    }

    public void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            image.setImageURI(uri);
        }
    }

    public void uploadImage() {
        if (uri != null && !Objects.requireNonNull(name.getText()).toString().equals("")) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.show();
            final StorageReference fileReference = FirebaseStorage.getInstance()
                    .getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    String id = Repo.refe.push().getKey() + System.currentTimeMillis();
                    map.put("image", task.getResult().toString());
                    map.put("name", name.getText().toString().trim());
                    map.put("id", id);
                    Repo.refe.child(MENU).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            name.setText("");
                            image.setImageURI(null);
                        }
                    });
                    pd.dismiss();
                });
            });
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void deleteMenu(MenuItem menuItem) {
        Repo.refe.child(MENU).child(menuItem.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (isNetworkConnected(AddNewMenuItemActivity.this)) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddNewMenuItemActivity.this, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}