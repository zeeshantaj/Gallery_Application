package com.example.gallery_application.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery_application.ImageShowActivity;
import com.example.gallery_application.Model.ImagesData;
import com.example.gallery_application.R;
import com.example.gallery_application.ViewPagerActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

//    private List<ImagesData> imagesDataList;
//    private Context context;
//
//    public ImageAdapter(List<ImagesData> imagesDataList, Context context) {
//        this.imagesDataList = imagesDataList;
//        this.context = context;
//    }
//    public void addImages(List<ImagesData> newImagesData) {
//        imagesDataList.addAll(newImagesData);
//        notifyDataSetChanged(); // Notify the adapter that the data set has changed
//    }
//
//    @NonNull
//    @Override
//    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_layout,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
//        ImagesData imagesData = imagesDataList.get(position);
//        Glide.with(context)
//                .load(imagesData.getImagePath())
//                .into(holder.imageView);
//        int pos = position;
//
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                List<String> imagePaths = new ArrayList<>();
//                for (ImagesData imageData : imagesDataList) {
//                    imagePaths.add(imageData.getImagePath()); // Assuming getImagePath() returns the image path as a string
//                }
//
//                Intent intent = new Intent(context.getApplicationContext(), ViewPagerActivity.class);
//                //intent.putExtra("imagePath",imagesData.getImagePath());
//                intent.putStringArrayListExtra("imagePath",new ArrayList<>(imagePaths));
//                intent.putExtra("position",pos);
//                context.startActivity(intent);
//
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return imagesDataList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView imageView;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.itemImage);
//        }
//    }

    private List<ImagesData> imagesData;
    private Context context;
    private int fixedImageWidth; // Set your desired fixed width
    private int fixedImageHeight; // Set your desired fixed height
    private int spanCount;

    // Constructor to initialize data and context
    public ImageAdapter(List<ImagesData> imagesData, Context context, int spanCount) {
        this.imagesData = imagesData;
        this.context = context;
        this.spanCount = spanCount;

        this.fixedImageWidth = 200;
        this.fixedImageHeight = 200;
    }

    // Update the span count
    public void updateSpanCount(int spanCount) {
        this.spanCount = spanCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Adjust image height and width based on the span count
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = calculateImageWidth();
        layoutParams.height = calculateImageHeight();
        holder.imageView.setLayoutParams(layoutParams);

        int targetWidth = calculateImageWidth();
        int targetHeight = calculateImageHeight();

        animateItemSize(holder.imageView, layoutParams.width, layoutParams.height, targetWidth, targetHeight);

        // Load image using a library like Glide or Picasso
        // For example using Glide:
        Glide.with(context)
                .load(imagesData.get(position).getImagePath())
                .into(holder.imageView);
        int pos = position;
//
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> imagePaths = new ArrayList<>();
                for (ImagesData imageData : imagesData) {
                    imagePaths.add(imageData.getImagePath()); // Assuming getImagePath() returns the image path as a string
                }

                Intent intent = new Intent(context.getApplicationContext(), ViewPagerActivity.class);
                //intent.putExtra("imagePath",imagesData.getImagePath());
                intent.putStringArrayListExtra("imagePath",new ArrayList<>(imagePaths));
                intent.putExtra("position",pos);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
        }
    }

    // Calculate the width of the image based on the span count
    private int calculateImageWidth() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        return screenWidth / spanCount;
    }

    // Calculate the height of the image based on the span count (modify this according to your requirements)
    private int calculateImageHeight() {
        // This is just an example, modify this calculation based on your desired logic
        return calculateImageWidth(); // You can adjust the height proportionally if needed
    }

    private void animateItemSize(final View view, int startWidth, int startHeight, int targetWidth, int targetHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(startWidth, targetWidth);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = value;
            layoutParams.height = startHeight * targetWidth / startWidth; // Maintain aspect ratio
            view.setLayoutParams(layoutParams);
        });
        animator.setDuration(100); // Set the duration of the animation
        animator.start();
    }
}
