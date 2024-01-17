package com.example.gallery_application.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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


        Glide.with(holder.itemView.getContext())
                .load(imageCard.getImagePaths())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return folderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        List<ImageView> imageViews;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViews = new ArrayList<>();
            imageViews.add(itemView.findViewById(R.id.imageView1));
            imageViews.add(itemView.findViewById(R.id.imageView2));
            imageViews.add(itemView.findViewById(R.id.imageView3));
            imageViews.add(itemView.findViewById(R.id.imageView4));
            img = itemView.findViewById(R.id.imageView1);

        }
    }
}
