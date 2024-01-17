package com.example.gallery_application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery_application.Model.FolderModel;
import com.example.gallery_application.R;

import java.util.List;

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.ViewHolder> {

    private List<FolderModel> folderModels;

    public FolderRecyclerAdapter(List<FolderModel> folderModels) {
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


    }

    @Override
    public int getItemCount() {
        return folderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
