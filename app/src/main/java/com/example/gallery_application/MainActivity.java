package com.example.gallery_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.gallery_application.Adapter.ImageAdapter;
import com.example.gallery_application.Model.ImagesData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 1001;
    private RecyclerView recyclerView;

    private List<ImagesData> imagesData = new ArrayList<>();
    private int batchSize = 10;
    private int startIndex = 0;
    private ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.imageRecycler);
        imagesData = new ArrayList<>();

        //recyclerView.setLayoutManager(new GridLayoutManager(this,4));


        adapter=new ImageAdapter(imagesData,this);
        recyclerView.setAdapter(adapter);


        // Load the initial batch of images
        retrieveImages(startIndex, batchSize);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                return;
            }
        }

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
                    startIndex += batchSize;
                    retrieveImages(startIndex, batchSize);

                }
            }
        });
        retrieveImages();
    }
    private void retrieveImages() {
        // Define the columns you want to retrieve
//        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
//
//        // Create a cursor to query the images in the MediaStore
//        Cursor cursor = getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                null,
//                null,
//                null
//        );
//
//        // Check if the cursor is not null and has data
//        if (cursor != null && cursor.moveToFirst()) {
//            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//
//            do {
//                // Retrieve image details
//                long imageId = cursor.getLong(idColumnIndex);
//                String imagePath = cursor.getString(dataColumnIndex);
//                // Do something with the image data, e.g., display, store, or process it
//                // Here, you can display or process each image using its path (imagePath)
//                ImagesData imagesData1 = new ImagesData(imagePath);
//                imagesData.add(imagesData1);
//                adapter = new ImageAdapter(imagesData,MainActivity.this);
//
//
//            } while (cursor.moveToNext());
//
//            // Close the cursor when done
//            cursor.close();
//        }
//        recyclerView.setAdapter(adapter);
    }
    private void retrieveImages(int startIndex, int batchSize) {
        // Define the columns you want to retrieve
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};

        // Define the sorting order (DATE_ADDED in descending order)
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";


        // Create a cursor to query the images in the MediaStore with pagination
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            // Move the cursor to the startIndex
            if (cursor.moveToPosition(startIndex)) {
                int count = 0;
                do {
                    long imageId = cursor.getLong(idColumnIndex);
                    String imagePath = cursor.getString(dataColumnIndex);
                    ImagesData imagesData1 = new ImagesData(imagePath);
                    imagesData.add(imagesData1);


                    count++;


                    // Load images in batches of batchSize
                    if (count >= batchSize) {

                        break;
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000)); // Convert seconds to milliseconds
    }

}