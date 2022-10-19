package com.doubleclick.menu;

import static com.doubleclick.menu.Model.Constant.FOOD;
import static com.doubleclick.menu.Model.Constant.IMAGES;
import static com.doubleclick.menu.Service.Network.isNetworkConnected;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import com.doubleclick.menu.Adapter.DishAdapter;
import com.doubleclick.menu.Interface.FoodOptions;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.FoodViewModel;
import com.doubleclick.menu.ViewModel.MenuViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddNewDishActivity extends AppCompatActivity implements FoodOptions {

    private ImageView image;
    private TextInputEditText name, price, details;
    private SmartMaterialSpinner<MenuItem> spinnerMenu;
    private SmartMaterialSpinner<Food> spinnerClassification;
    private final int IMAGE_REQUEST = 100;
    private Uri uri = null;
    private MenuItem menuItemSelected = null;
    private String menuOptionItemSelected = "";
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private List<String> menuOption;
    private DishAdapter dishAdapter;
    private static final String TAG = "AddNewDishActivity";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_dish);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        spinnerMenu = findViewById(R.id.spinnerMenu);
        spinnerClassification = findViewById(R.id.spinnerClassification);
        price = findViewById(R.id.price_food);
        Button upload = findViewById(R.id.upload);
        RecyclerView dishs = findViewById(R.id.dishs);
        details = findViewById(R.id.details);
        dishAdapter = new DishAdapter(foods, this);
        dishs.setAdapter(dishAdapter);
        FoodViewModel foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.MenuOperators();
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuOption = Arrays.asList(getResources().getStringArray(R.array.menu_option));
        menuViewModel.MenuItemAll().observe(this, menuItems -> {
            this.menuItems = menuItems;
            ArrayAdapter<MenuItem> adapter = new ArrayAdapter<>(AddNewDishActivity.this, android.R.layout.simple_spinner_item, menuItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMenu.setAdapter(adapter);
            spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menuItemSelected = menuItems.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewDishActivity.this, android.R.layout.simple_spinner_item, menuOption);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(adapter);
        spinnerClassification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menuOptionItemSelected = menuOption.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        foodViewModel.FoodItemAdd().observe(this, food -> {
            foods.add(food);
            dishAdapter.notifyItemInserted(foods.size() - 1);
            dishAdapter.notifyDataSetChanged();
        });

        foodViewModel.FoodItemUpdate().observe(this, food -> {
            int pos = foods.indexOf(food);
            foods.set(pos, food);
            dishAdapter.notifyItemChanged(pos);
            dishAdapter.notifyDataSetChanged();

        });

        foodViewModel.FoodItemDelete().observe(this, food -> {
            foods.remove(food);
            dishAdapter.notifyItemRemoved(foods.indexOf(food));
            dishAdapter.notifyDataSetChanged();

        });

        image.setOnClickListener(view -> openImage());

        upload.setOnClickListener(view -> {
            if (uri != null && !Objects.requireNonNull(name.getText()).toString().equals("") && !Objects.requireNonNull(price.getText()).toString().equals("") && !Objects.requireNonNull(details.getText()).toString().equals("") && menuItemSelected != null) {
                uploadImage(uri, name.getText().toString().trim(), price.getText().toString().trim(), details.getText().toString().trim(), menuItemSelected);
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void uploadImage(Uri uri, String n, String p, String d, MenuItem menuItem) {
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
                map.put("name", n);
                map.put("details", d);
                map.put("price", Double.valueOf(p));
                map.put("id", id);
                map.put("classification", menuOptionItemSelected);
                map.put("idMenu", menuItem.getId());
                Repo.refe.child(FOOD).child(id).updateChildren(map).addOnCompleteListener(task1 -> {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
                    name.setText("");
                    price.setText("");
                    details.setText("");
                    pd.dismiss();
                });

            });
        });

    }

    public void editDish(Uri uri, String name, String price, String details, MenuItem menuItem, String id, String menuOptionItemSelected) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance()
                    .getReference(IMAGES).child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                url.addOnCompleteListener(task -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("image", task.getResult().toString());
                    map.put("name", name);
                    map.put("details", details);
                    map.put("price", Double.valueOf(price));
                    map.put("id", id);
                    map.put("classification", menuOptionItemSelected);
                    map.put("idMenu", menuItem.getId());
                    Repo.refe.child(FOOD).child(id).updateChildren(map).addOnCompleteListener(task1 -> {
                        pd.dismiss();
                        Toast.makeText(AddNewDishActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                    });

                });
            });
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("details", details);
            map.put("price", Double.valueOf(price));
            map.put("id", id);
            map.put("classification", menuOptionItemSelected);
            map.put("idMenu", menuItem.getId());
            Repo.refe.child(FOOD).child(id).updateChildren(map).addOnCompleteListener(task -> {
                pd.dismiss();
                Toast.makeText(AddNewDishActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            String filePath = SiliCompressor.with(AddNewDishActivity.this).compress(
                    data.getData().toString(),
                    new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .toString() + "/Menu/Images/"
                    )
            );
            uri = Uri.parse(filePath);
            image.setImageURI(uri);
        }
    }

    @Override
    public void deleteFood(Food food) {
        Repo.refe.child(FOOD).child(food.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful() && isNetworkConnected(AddNewDishActivity.this)) {
                Toast.makeText(AddNewDishActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void UpdateFood(Food food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewDishActivity.this);
        builder.setTitle(food.getName());
        View v = LayoutInflater.from(AddNewDishActivity.this).inflate(R.layout.edit_dish_item, null, false);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        v.setPadding(30, 5, 30, 5);
        image = v.findViewById(R.id.image);
        Glide.with(AddNewDishActivity.this).load(food.getImage()).into(image);
        image.setOnClickListener(view -> openImage());
        TextInputEditText name = v.findViewById(R.id.name);
        TextInputEditText details = v.findViewById(R.id.details);
        TextInputEditText price_food = v.findViewById(R.id.price_food);
        SmartMaterialSpinner<MenuItem> spinnerMenu = v.findViewById(R.id.spinnerMenu);
        SmartMaterialSpinner<String> spinnerClassification = v.findViewById(R.id.spinnerClassification);
        name.setText(food.getName());
        details.setText(food.getDetails());
        price_food.setText(String.valueOf(food.getPrice()));
        spinnerMenu.setSelection(menuItems.indexOf(new MenuItem(food.getIdMenu())));
        spinnerClassification.setSelection(menuOption.indexOf(food.getClassification()));
        Log.e(TAG, "UpdateFood: " + menuItems.get(menuItems.indexOf(new MenuItem(food.getIdMenu()))).Menu());
        ArrayAdapter<MenuItem> adapter = new ArrayAdapter<>(AddNewDishActivity.this, android.R.layout.simple_spinner_item, menuItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter);
        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menuItemSelected = menuItems.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                menuItemSelected = menuItems.get(menuItems.indexOf(new MenuItem(food.getIdMenu())));
            }
        });
        ArrayAdapter<String> adapterClassification = new ArrayAdapter<>(AddNewDishActivity.this, android.R.layout.simple_spinner_item, menuOption);
        adapterClassification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(adapterClassification);
        spinnerClassification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menuOptionItemSelected = menuOption.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerClassification.setSelection(menuOption.indexOf(food.getClassification()));
            }
        });
        Button edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(view -> {
            if (isNetworkConnected(AddNewDishActivity.this)) {
                if (!Objects.requireNonNull(name.getText()).toString().equals("") && !Objects.requireNonNull(price_food.getText()).toString().equals("") && menuItemSelected != null) {
                    editDish(uri, name.getText().toString().trim(), price_food.getText().toString().trim(), Objects.requireNonNull(details.getText()).toString().trim(), menuItemSelected, food.getId(), menuOptionItemSelected);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.choose_menu), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}