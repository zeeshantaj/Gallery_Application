package com.example.gallery_application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery_application.Activities.Bucket;
import com.example.gallery_application.Model.ImageData;
import com.example.gallery_application.R;

import java.util.List;

public class BucketRecyclerAdapter extends RecyclerView.Adapter<BucketRecyclerAdapter.ViewHolder> {

    private List<Bucket> bucketList;

    public BucketRecyclerAdapter(List<Bucket> folderModels) {
        this.bucketList = folderModels;
    }

    @NonNull
    @Override
    public BucketRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketRecyclerAdapter.ViewHolder holder, int position) {
       Bucket bucket = bucketList.get(position);
       holder.bucketName.setText(bucket.getName());

    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bucketName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
         bucketName = itemView.findViewById(R.id.bucketName);

        }
    }
}
