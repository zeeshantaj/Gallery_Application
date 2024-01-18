package com.example.gallery_application.Activities;

import static com.example.gallery_application.Activities.Image_Retrivel_Activity.containsImages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.gallery_application.Adapter.BucketRecyclerAdapter;
import com.example.gallery_application.Adapter.Bucket_ImagesAdapter;
import com.example.gallery_application.MainActivity;
import com.example.gallery_application.Model.ImagesData;
import com.example.gallery_application.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bucket_Show_Image extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ImagesData> bucketList;
    private Bucket_ImagesAdapter adapter;
    private int startIndex = 0;
    private String bucketPath;
    private GridLayoutManager layoutManager;
    private ScaleGestureDetector scaleGestureDetector;
    private int initialSpanCount = 4;
    private int pageSize = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_show_image);


        recyclerView = findViewById(R.id.bucketRecycler);
        Intent intent = getIntent();
        bucketPath = intent.getStringExtra("BucketPath");
        bucketList = new ArrayList<>();

        loadImagesFromDirectory(bucketPath,startIndex,pageSize);

        adapter = new Bucket_ImagesAdapter(bucketList,initialSpanCount);
        layoutManager = new GridLayoutManager(this, initialSpanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    // Load the next batch of images
//                    startIndex += batchSize;
//                    loadImagesFromDirectory(bucketPath);
                    startIndex += pageSize;
                    loadImagesFromDirectory(bucketPath,startIndex,pageSize);
                    Log.e("MyApp","pageSize"+pageSize);
                    Log.e("MyApp","start index "+startIndex);
                }
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new Bucket_Show_Image.ScaleGestureListener());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }
    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();

            if (scaleFactor > 1.0f && layoutManager.getSpanCount() < 14) {
                // Zoom in - Limit the maximum span count to 8 (adjust as needed)
                updateSpanCount(layoutManager.getSpanCount() + 1);
                return true;
            }
            else if (scaleFactor < 1.0f && layoutManager.getSpanCount() > 4) {
                // Zoom out - Limit the minimum span count to 1 (adjust as needed)
                updateSpanCount(layoutManager.getSpanCount() - 1);
                return true;
            }

            return false;
        }
    }
    private void updateSpanCount(int newSpanCount) {
        final int oldSpanCount = layoutManager.getSpanCount();

        // Notify adapter about the new span count
        adapter.updateSpanCountBucket(newSpanCount);

        // Smoothly scroll to the first visible item to maintain the position
        final int firstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition != RecyclerView.NO_POSITION) {
            final int columnWidth = recyclerView.getWidth() / newSpanCount;
            final int offset = (firstVisiblePosition % oldSpanCount) * columnWidth;
            recyclerView.post(() -> {
                recyclerView.scrollBy(offset, 0);
            });
        }

        // Invalidate the layout manager to force RecyclerView to redraw items with new sizes
        recyclerView.postDelayed(() -> {
            layoutManager.setSpanCount(newSpanCount);
            layoutManager.requestLayout();
        }, 100); // Delay added to ensure the RecyclerView layout is updated after the adapter change
    }

    private List<ImagesData> loadImagesFromDirectory(String directoryPath, int startPosition, int pageSize) {
        //List<ImagesData> imageList = new ArrayList<>();
        File directory = new File(directoryPath);

        // Check if the directory exists and is a directory
        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            if (files != null) {
                int endPosition = Math.min(startPosition + pageSize, files.length);
                for (int i = startPosition; i < endPosition; i++) {
                    File file = files[i];
                    // Add the file path to the list if it is an image file
                    if (isImageFile(file)) {
                        ImagesData imagesData = new ImagesData(file.getAbsolutePath());
                        //imageList.add(imagesData);
                        bucketList.add(imagesData);
                    }
                }
            }
        }
        return bucketList;
    }

//    public  List<String> loadImagesFromDirectory(String directoryPath) {
//        List<String> imagePaths = new ArrayList<>();
//        bucketList.clear();
//        File directory = new File(directoryPath);
//
//        // Check if the directory exists and is a directory
//        if (directory.exists() && directory.isDirectory()) {
//            // List all files in the directory
//            File[] files = directory.listFiles();
//
//            if (files != null) {
//                for (File file : files) {
//                    // Add the file path to the list if it is an image file
//                    if (isImageFile(file)) {
//                        imagePaths.add(file.getAbsolutePath());
//                        ImagesData imagesData = new ImagesData(file.getAbsolutePath());
//                        bucketList.add(imagesData);
//                       // adapter.submitData(bucketList);
//
//                    }
//                }
//            }
//        }
//
//        return imagePaths;
//    }

    private boolean isImageFile(File file) {
        // Check if the file has an image file extension
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
    }
}