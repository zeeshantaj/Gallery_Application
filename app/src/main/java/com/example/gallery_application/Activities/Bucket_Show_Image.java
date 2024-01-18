package com.example.gallery_application.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.gallery_application.R;

public class Bucket_Show_Image extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_show_image);

        recyclerView = findViewById(R.id.bucketRecycler);
        Intent intent = getIntent();
        String bucketPath = intent.getStringExtra("BucketPath");
        Log.e("MyApp","bucketPath "+bucketPath);

    }
}