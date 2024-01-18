package com.example.gallery_application.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery_application.Activities.Bucket;
import com.example.gallery_application.Activities.Bucket_Show_Image;
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

        for (int i = 0; i < Math.min(4, bucket.getImagePaths().size()); i++) {
            String imagePath = bucket.getImagePaths().get(i);
            // Use an image loading library or your preferred method to load and display the images
            // For example, you can use Glide or Picasso
            // Glide.with(holder.itemView.getContext()).load(imagePath).into(imageViews[i]);
            Glide.with(holder.itemView.getContext())
                    .load(imagePath)
                    .into(holder.imageViews[i]);
        }
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "path -> "+bucket.getBucketPath(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), Bucket_Show_Image.class);
            intent.putExtra("BucketPath",bucket.getBucketPath());
            v.getContext().startActivity(intent);

            //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());

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
