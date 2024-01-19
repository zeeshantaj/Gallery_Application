package com.example.gallery_application.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.gallery_application.Activities.Bucket;
import com.example.gallery_application.Activities.Bucket_Show_Image;
import com.example.gallery_application.R;

import java.io.File;
import java.util.List;

public class BucketRecyclerAdapter extends RecyclerView.Adapter<BucketRecyclerAdapter.ViewHolder> {

    private List<Bucket> bucketList;

    public BucketRecyclerAdapter(List<Bucket> folderModels) {
        this.bucketList = folderModels;
    }
    public void setBucketList(List<Bucket> bucketList) {
        this.bucketList = bucketList;
        notifyDataSetChanged();
    }
    public void addData(List<Bucket> newData) {
        int startPosition = bucketList.size();
        bucketList.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }
    @NonNull
    @Override
    public BucketRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketRecyclerAdapter.ViewHolder holder, int position) {
       Bucket bucket = bucketList.get(position);
       holder.bucketName.setText(bucket.getName());

        for (int i = 0; i < Math.min(4, bucket.getImagePaths().size()); i++) {
            String imagePath = bucket.getImagePaths().get(i);

            Uri contentUri = Uri.fromFile(new File(imagePath));

            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .load(contentUri)
                    .into(holder.imageViews[i]);
        }
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), Bucket_Show_Image.class);
            intent.putExtra("BucketPath",bucket.getBucketPath());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bucketName;
        ImageView[] imageViews = new ImageView[4];

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
         bucketName = itemView.findViewById(R.id.bucketName);
            imageViews[0] = itemView.findViewById(R.id.imageView1);
            imageViews[1] = itemView.findViewById(R.id.imageView2);
            imageViews[2] = itemView.findViewById(R.id.imageView3);
            imageViews[3] = itemView.findViewById(R.id.imageView4);

        }
    }
}
