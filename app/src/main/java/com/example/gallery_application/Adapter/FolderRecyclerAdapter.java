package com.example.gallery_application.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery_application.Model.ImageData;
import com.example.gallery_application.R;

import java.util.ArrayList;
import java.util.List;

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.ViewHolder> {

    private List<ImageData> folderModels;

    public FolderRecyclerAdapter(List<ImageData> folderModels) {
        this.folderModels = folderModels;
    }

    @NonNull
    @Override
    public FolderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderRecyclerAdapter.ViewHolder holder, int position) {
        ImageData imageCard = folderModels.get(position);
        List<String> imagePaths = imageCard.getImagePaths();


        for (int i = 0; i < 4; i++) {
            ImageView imageView = getImageViewById(holder, i + 1);
            if (i < imagePaths.size()) {
                // Load image using your preferred image loading library (e.g., Glide or Picasso)
                // For simplicity, using a placeholder image here

                Glide.with(holder.itemView.getContext())
                                .load(imagePaths)
                                        .into(imageView);
                //imageView.setImageResource(R.drawable.placeholder_image);


                // Replace 'R.drawable.placeholder_image' with the actual image loading code
            } else {
                // If there are fewer than 4 images, hide the remaining ImageViews
                imageView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return folderModels.size();
    }
    private ImageView getImageViewById(ViewHolder holder, int id) {
        switch (id) {
            case 1:
                return holder.imageView1;
            case 2:
                return holder.imageView2;
            case 3:
                return holder.imageView3;
            case 4:
                return holder.imageView4;
            default:
                throw new IllegalArgumentException("Invalid image view ID");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        List<ImageView> imageViews;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);
            imageView4 = itemView.findViewById(R.id.imageView4);

        }
    }
}
