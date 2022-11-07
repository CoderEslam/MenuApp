package com.doubleclick.coinchaud.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doubleclick.coinchaud.Model.MenuItem;
import com.doubleclick.coinchaud.R;

import java.util.ArrayList;

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

//        CircleImageView image = currentItemView.findViewById(R.id.image);
//        assert convertView != null;
//        Glide.with(convertView.getContext()).load(menuItem.getImage()).into(image);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView name = currentItemView.findViewById(R.id.name);
        name.setText(menuItem.getName());

        return currentItemView;
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
    }
}
