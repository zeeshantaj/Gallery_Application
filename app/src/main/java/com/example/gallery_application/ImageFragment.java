package com.example.gallery_application;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.commit451.elasticdragdismisslayout.ElasticDragDismissFrameLayout;
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageFragment extends Fragment {

    private String imagePath;

    // Factory method to create a new instance of ImageFragment with an image path
    public static ImageFragment newInstance(String imagePath) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("imagePath", imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imagePath = getArguments().getString("imagePath");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        PhotoView imageView = rootView.findViewById(R.id.imageView);

        Glide.with(this)
                .load(imagePath)
                .into(imageView);
       imageView.setMaximumScale(5); // Adjust the maximum zoom level as needed


        return rootView;
    }


    public class MyGestureListener  extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getY() - e1.getY() > 200 && Math.abs(velocityY ) > 100){
                if (getActivity()!=null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
            return false;
        }
    }
}
