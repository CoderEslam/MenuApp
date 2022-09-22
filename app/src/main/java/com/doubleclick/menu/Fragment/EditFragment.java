package com.doubleclick.menu.Fragment;

import static com.doubleclick.menu.Model.Constant.USER;
import static com.doubleclick.menu.Service.Network.isNetworkConnected;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doubleclick.menu.MenuActivity;
import com.doubleclick.menu.ProfileActivity;
import com.doubleclick.menu.R;
import com.doubleclick.menu.Repository.Repo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class EditFragment extends BottomSheetDialogFragment {


    private TextInputEditText name;
    private Button edit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.edit_fragmeny, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name);
        edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            update();
        });
    }

    private void update() {
        HashMap<String, Object> map = new HashMap<>();
        if (!Objects.requireNonNull(name.getText()).toString().equals("")) {
            map.put("name", name.getText().toString());
        }
        Repo.refe.child(USER).child(Repo.uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && isNetworkConnected(requireContext())) {
                    Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.update), Toast.LENGTH_SHORT).show();
                    name.setText("");
                    dismiss();
                }
            }
        });
    }
}
