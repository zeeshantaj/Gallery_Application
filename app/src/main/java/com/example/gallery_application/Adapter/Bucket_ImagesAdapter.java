package com.example.gallery_application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery_application.Model.ImagesData;
import com.example.gallery_application.R;

import java.util.List;

public class Bucket_ImagesAdapter extends RecyclerView.Adapter<Bucket_ImagesAdapter.ViewHolder> {
    private List<ImagesData> imagesData;
    private int spanCount;
    public Bucket_ImagesAdapter(List<ImagesData> imagesData, int spanCount) {
        this.imagesData = imagesData;
        this.spanCount = spanCount;
    }
    public void updateSpanCountBucket(int spanCount) {
        this.spanCount = spanCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Bucket_ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Bucket_ImagesAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(imagesData.get(position).getImagePath())
                .thumbnail(0.5f)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
        }
    }
}
