package com.doubleclick.menu;

import static com.doubleclick.menu.Model.Constant.FOOD;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.doubleclick.menu.Adapter.SpinnerAdapter;
import com.doubleclick.menu.Interface.FoodOptions;
import com.doubleclick.menu.Model.Food;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.Repository.Repo;
import com.doubleclick.menu.ViewModel.FoodViewModel;
import com.doubleclick.menu.ViewModel.MenuViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddNewDishActivity extends AppCompatActivity implements FoodOptions {

    private MenuViewModel menuViewModel;
    private ImageView image;
    private TextInputEditText name, price, details;
    private SmartMaterialSpinner spinner;
    private RecyclerView dishs;
    private Button upload;
    private SpinnerAdapter spinnerAdapter;
    private final int IMAGE_REQUEST = 100;
    private Uri uri = null;
    private MenuItem menuItemSelected = null;
    private FoodViewModel foodViewModel;
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    private DishAdapter dishAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_dish);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        price = findViewById(R.id.price_food);
        upload = findViewById(R.id.upload);
        dishs = findViewById(R.id.dishs);
        details = findViewById(R.id.details);
        dishAdapter = new DishAdapter(foods, this);
        dishs.setAdapter(dishAdapter);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.MenuOperators();
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.MenuItemAll().observe(this, menuItems -> {
            this.menuItems = menuItems;
            ArrayAdapter<MenuItem> adapter = new ArrayAdapter<MenuItem>(AddNewDishActivity.this, android.R.layout.simple_spinner_item, menuItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menuItemSelected = menuItems.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /*spinnerAdapter = new SpinnerAdapter(AddNewDishActivity.this, menuItems);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);*/

        });

        foodViewModel.FoodItemAdd().observe(this, new Observer<Food>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Food food) {
                foods.add(food);
                dishAdapter.notifyItemInserted(foods.size() - 1);
                dishAdapter.notifyDataSetChanged();
            }
        });

        foodViewModel.FoodItemUpdate().observe(this, new Observer<Food>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Food food) {
                int pos = foods.indexOf(food);
                foods.set(pos, food);
                dishAdapter.notifyItemChanged(pos);
                dishAdapter.notifyDataSetChanged();

            }
        });

        foodViewModel.FoodItemDelete().observe(this, new Observer<Food>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Food food) {
                foods.remove(food);
                dishAdapter.notifyItemRemoved(foods.indexOf(food));
                dishAdapter.notifyDataSetChanged();

            }
        });

        image.setOnClickListener(view -> {
            openImage();
        });

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
                map.put("idMenu", menuItem.getId());
                Repo.refe.child(FOOD).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        image.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
                        name.setText("");
                        price.setText("");
                        details.setText("");
                        pd.dismiss();
                    }
                });

            });
        });

    }

    public void editDish(Uri uri, String name, String price, String details, MenuItem menuItem, String id) {
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
                    map.put("idMenu", menuItem.getId());
                    Repo.refe.child(FOOD).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                        }
                    });

                });
            });
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("details", details);
            map.put("price", Double.valueOf(price));
            map.put("id", id);
            map.put("idMenu", menuItem.getId());
            Repo.refe.child(FOOD).child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pd.dismiss();
                }
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
    public void deleteFood(Food food) {
        Repo.refe.child(FOOD).child(food.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && isNetworkConnected(AddNewDishActivity.this)) {
                    Toast.makeText(AddNewDishActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
                }
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
        image.setOnClickListener(view -> {
            openImage();
        });
        TextInputEditText name = v.findViewById(R.id.name);
        TextInputEditText details = v.findViewById(R.id.details);
        TextInputEditText price_food = v.findViewById(R.id.price_food);
        SmartMaterialSpinner<MenuItem> spinner = v.findViewById(R.id.spinner);
        name.setText(food.getName());
        details.setText(food.getDetails());
        price_food.setText(String.valueOf(food.getPrice()));
        ArrayAdapter<MenuItem> adapter = new ArrayAdapter<MenuItem>(AddNewDishActivity.this, android.R.layout.simple_spinner_item, menuItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menuItemSelected = menuItems.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(view -> {
            if (!Objects.requireNonNull(name.getText()).toString().equals("") && !Objects.requireNonNull(price_food.getText()).toString().equals("") && menuItemSelected != null) {
                editDish(uri, name.getText().toString().trim(), price_food.getText().toString().trim(), details.getText().toString().trim(), menuItemSelected, food.getId());
            } else {
                Toast.makeText(this, getResources().getString(R.string.choose_menu), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(v);
        builder.setCancelable(true);
        builder.show();
    }
}