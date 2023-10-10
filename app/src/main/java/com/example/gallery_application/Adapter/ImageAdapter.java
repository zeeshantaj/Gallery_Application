package com.example.gallery_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery_application.ImageShowActivity;
import com.example.gallery_application.Model.ImagesData;
import com.example.gallery_application.R;
import com.example.gallery_application.ViewPagerActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<ImagesData> imagesDataList;
    private Context context;

    public ImageAdapter(List<ImagesData> imagesDataList, Context context) {
        this.imagesDataList = imagesDataList;
        this.context = context;
    }
    public void addImages(List<ImagesData> newImagesData) {
        imagesDataList.addAll(newImagesData);
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        ImagesData imagesData = imagesDataList.get(position);
        Glide.with(context)
                .load(imagesData.getImagePath())
                .into(holder.imageView);
        int pos = position;

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> imagePaths = new ArrayList<>();
                for (ImagesData imageData : imagesDataList) {
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
        return imagesDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
        }
    }
}
