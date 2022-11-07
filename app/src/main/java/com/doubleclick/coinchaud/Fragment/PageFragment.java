package com.doubleclick.coinchaud.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.doubleclick.coinchaud.Adapter.FoodAdapter;
import com.doubleclick.coinchaud.Model.Food;
import com.doubleclick.coinchaud.R;
import com.doubleclick.coinchaud.Views.carousellayoutmanager.CarouselLayoutManager;
import com.doubleclick.coinchaud.Views.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.doubleclick.coinchaud.Views.carousellayoutmanager.CenterScrollListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMAGE = "image";
    private static final String ARG_ARRAY = "array";
    private static final String TAG = "PageFragment";
    // TODO: Rename and change types of parameters
    private String mImage;
    private ArrayList<Food> marray;
    private TextView text;
    private LottieAnimationView animationView;
    private RecyclerView foods;
    private ImageView bg_image;

    public PageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageFragment newInstance(String param1, String param2) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, param1);
        args.putString(ARG_ARRAY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImage = getArguments().getString(ARG_IMAGE);
            marray = getArguments().getParcelableArrayList(ARG_ARRAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        foods = view.findViewById(R.id.foods);
        bg_image = view.findViewById(R.id.bg_image);
        text = view.findViewById(R.id.text);
        animationView = view.findViewById(R.id.animationView);
        if (marray.isEmpty()) {
            text.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.GONE);
            animationView.setVisibility(View.GONE);
        }
        Glide.with(view).asBitmap().load(mImage).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                bg_image.setImageBitmap(blurRenderScript(requireActivity(), resource, 25/*radius must be 0 < r <= 25*/));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        //second parametre is radius
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        foods.setLayoutManager(layoutManager);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        foods.setHasFixedSize(true);
        foods.addOnScrollListener(new CenterScrollListener());
        foods.setAdapter(new FoodAdapter(marray));
    }


    @SuppressLint("NewApi")
    public Bitmap blurRenderScript(Context context, Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(requireContext());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

}