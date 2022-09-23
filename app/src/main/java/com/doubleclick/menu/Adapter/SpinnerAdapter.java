package com.doubleclick.menu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.doubleclick.menu.Model.MenuItem;
import com.doubleclick.menu.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Eslam Ghazy on 9/23/2022
 */
public class SpinnerAdapter extends ArrayAdapter<MenuItem> {

    public SpinnerAdapter(@NonNull Context context, ArrayList<MenuItem> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;
        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        MenuItem menuItem = getItem(position);

        CircleImageView image = currentItemView.findViewById(R.id.image);
        assert convertView != null;
        Glide.with(convertView.getContext()).load(menuItem.getImage()).into(image);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView name = currentItemView.findViewById(R.id.name);
        name.setText(menuItem.getName());

        return currentItemView;
    }
}
