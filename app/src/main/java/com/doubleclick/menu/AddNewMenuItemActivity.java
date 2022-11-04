package com.doubleclick.menu;

import static com.doubleclick.menu.Model.Constant.FOOD;
import static com.doubleclick.menu.Model.Constant.IMAGES;
import static com.doubleclick.menu.Model.Constant.MENU;
import static com.doubleclick.menu.Service.Network.isNetworkConnected;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.doubleclick.menu.Adapter.DragDropAdapter;
import com.doubleclick.menu.Adapter.MenuAdapter;
import com.doubleclick.menu.Interface.MenuOptions;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.Service.ItemMoveCallback;
import com.doubleclick.menu.ViewModel.MenuViewModel;
import com.doubleclick.menu.Views.cropper.CropImage;
import com.doubleclick.menu.Views.cropper.CropImageView;
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView;
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener;
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddNewMenuItemActivity extends AppCompatActivity implements MenuOptions {

    private ImageView image, image_bg;
    private TextInputEditText name;
    private Button upload;
    private RecyclerView menu_items;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private MenuViewModel menuViewModel;
    private MenuAdapter menuAdapter;
    private Uri uri = null;
    private Uri uri_bg = null;
    private static final int IMAGE_REQUEST = 100;
    private static final String TAG = "AddNewMenuItemActivity";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_menu_item);
        image = findViewById(R.id.image);
        image_bg = findViewById(R.id.image_bg);
        name = findViewById(R.id.name);
        upload = findViewById(R.id.upload);
        menu_items = findViewById(R.id.menu_items);
        menuAdapter = new MenuAdapter(menuItems, AddNewMenuItemActivity.this);
        menu_items.setAdapter(menuAdapter);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(menuAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(menu_items);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.MenuOperators();
        menuViewModel.MenuItemAdd().observe(this, menuItem -> {
            menuItems.add(menuItem);
            menuAdapter.notifyItemInserted(menuItems.size() - 1);
            menuAdapter.notifyDataSetChanged();
            /*menu_items.setDragListener(new OnItemDragListener<Object>() {
                @Override
                public void onItemDropped(int previousPosition, int newPosition, Object item) {
                    try {
                        MenuItem m = menuItems.set(newPosition, menuItems.get(menuItems.indexOf(new MenuItem(item))));
                        Log.e(TAG, "onItemDropped: " + previousPosition + " => " + newPosition + " => " + m.Menu());
                        updateIndex(newPosition);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, "Error : " + e);
                    }

                }

                @Override
                public void onItemDragged(int initialPosition, int finalPosition, Object item) {
//                        Log.e(TAG, "onItemDropped: " + initialPosition + " => " + finalPosition);
                }
            });*/
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
                int pos = menuItems.indexOf(menuItem);
                menuItems.set(pos, menuItem);
                menuAdapter.notifyItemChanged(pos);
                menuAdapter.notifyDataSetChanged();
            }
        });

        image.setOnClickListener(view -> {
            openImage();
        });

        upload.setOnClickListener(view -> {
            uploadImage();
        });

        image_bg.setOnClickListener(view -> {
            galleryForBackground();
        });

    }


    public void openImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST);
    }

    public void galleryForBackground() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                String filePath = SiliCompressor.with(AddNewMenuItemActivity.this).compress(
                        result.getUri().toString(),
                        new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        .toString() + "/Menu/Images/"
                        )
                );
                uri = Uri.parse(filePath);
                image.setImageURI(uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
            }
        }
        if (requestCode == IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String filePath = SiliCompressor.with(AddNewMenuItemActivity.this).compress(
                        data.getData().toString(),
                        new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        .toString() + "/Menu/Images/"
                        )
                );
                uri_bg = Uri.parse(filePath);
                image_bg.setImageURI(uri_bg);
            }
        }
    }

    public void uploadImage() {
        if (uri != null && !Objects.requireNonNull(name.getText()).toString().equals("")) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.show();
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    final StorageReference fileReference_bg = FirebaseStorage.getInstance().getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri_bg));
                    fileReference_bg.putFile(uri_bg).addOnSuccessListener(taskSnapshot1 -> {
                        Task<Uri> url_bg = taskSnapshot1.getStorage().getDownloadUrl();
                        url_bg.addOnCompleteListener(task_bg -> {
                            HashMap<String, Object> map = new HashMap<>();
                            String id = Repo.refe.push().getKey() + System.currentTimeMillis();
                            map.put("image", task.getResult().toString());
                            map.put("image_bg", task_bg.getResult().toString());
                            map.put("name", name.getText().toString().trim());
                            map.put("id", id);
                            map.put("index", 0);
                            Repo.refe.child(MENU).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("UseCompatLoadingForDrawables")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    name.setText("");
                                    image.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
                                    image_bg.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
                                }
                            });
                            pd.dismiss();
                        });
                    });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewMenuItemActivity.this);
        builder.setTitle(getResources().getString(R.string.deleted));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void UpdateMenu(MenuItem menuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewMenuItemActivity.this);
        builder.setTitle(menuItem.getName());
        View v = LayoutInflater.from(AddNewMenuItemActivity.this).inflate(R.layout.edit_menu_item, null, false);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        v.setPadding(30, 5, 30, 5);
        image = v.findViewById(R.id.image);
        image_bg = v.findViewById(R.id.image_bg);
        Glide.with(AddNewMenuItemActivity.this).load(menuItem.getImage()).into(image);
        Glide.with(AddNewMenuItemActivity.this).load(menuItem.getImage_bg()).into(image_bg);
        image.setOnClickListener(view -> {
            openImage();
        });
        image_bg.setOnClickListener(view -> {
            galleryForBackground();
        });
        TextInputEditText name = v.findViewById(R.id.name);
        name.setText(menuItem.getName());
        Button edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(view -> {
            if (isNetworkConnected(AddNewMenuItemActivity.this)) {
                if (!Objects.requireNonNull(name.getText()).toString().equals("")) {
                    editDish(uri, uri_bg, name.getText().toString().trim(), menuItem.getId(), builder);
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(v);
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        try {
            Log.e(TAG, "onRowMoved: " + fromPosition + " => " + toPosition);
            HashMap<String, Object> maptoPosition = new HashMap<>();
            maptoPosition.put("index", toPosition);
            HashMap<String, Object> mapfromPosition = new HashMap<>();
            mapfromPosition.put("index", fromPosition);
            Repo.refe.child(MENU).child(menuItems.get(fromPosition).getId()).updateChildren(mapfromPosition);
            Repo.refe.child(MENU).child(menuItems.get(toPosition).getId()).updateChildren(maptoPosition);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "ArrayIndexOutOfBoundsException: " + e.getMessage());
        }


    }

    public void editDish(Uri uri, Uri uri_bg, String name, String id, AlertDialog.Builder builder) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null && uri_bg == null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("image", task.getResult().toString());
                    map.put("name", name);
                    map.put("id", id);
                    Repo.refe.child(MENU).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            Toast.makeText(AddNewMenuItemActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                        }
                    });

                });
            });
        } else if (uri_bg != null && uri == null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri_bg));
            fileReference.putFile(uri_bg).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("image_bg", task.getResult().toString());
                    map.put("name", name);
                    map.put("id", id);
                    Repo.refe.child(MENU).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            Toast.makeText(AddNewMenuItemActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                        }
                    });

                });
            });
        } else if (uri_bg != null && uri != null) {
            final StorageReference fileReference_bg = FirebaseStorage.getInstance().getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri_bg));
            fileReference_bg.putFile(uri_bg).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url_bg = taskSnapshot.getStorage().getDownloadUrl();
                url_bg.addOnCompleteListener(task_bg -> {
                    final StorageReference fileReference = FirebaseStorage.getInstance().getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri));
                    fileReference.putFile(uri).addOnSuccessListener(taskSnapshot1 -> {
                        Task<Uri> url = taskSnapshot1.getStorage().getDownloadUrl();
                        url.addOnCompleteListener(task1 -> {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("image_bg", task_bg.getResult().toString());
                            map.put("image", task1.getResult().toString());
                            map.put("name", name);
                            map.put("id", id);
                            Repo.refe.child(MENU).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    Toast.makeText(AddNewMenuItemActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    });
                });
            });
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("id", id);
            Repo.refe.child(MENU).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pd.dismiss();
                    Toast.makeText(AddNewMenuItemActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}