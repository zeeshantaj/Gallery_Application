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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

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
    private int initialSpanCount = 4;
    private ScaleGestureDetector scaleGestureDetector;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.imageRecycler);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        layoutManager = new GridLayoutManager(this, initialSpanCount);
        recyclerView.setLayoutManager(layoutManager);
        imagesData = new ArrayList<>();
        //recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        adapter=new ImageAdapter(imagesData,this,initialSpanCount);
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
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(MainActivity.this
//                    , new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//                @Override
//                public boolean onScale(ScaleGestureDetector detector) {
//                    float scaleFactor = detector.getScaleFactor();
//
//                    if (scaleFactor > 1.0f) {
//                        // Zoom in
//                        layoutManager.setSpanCount(layoutManager.getSpanCount() + 1);
//                        recyclerView.getAdapter().notifyDataSetChanged();
//                        Log.e("MyApp","zoomIn");
//                    } else {
//                        // Zoom out
//                        Log.e("MyApp","zoomOut");
//                        layoutManager.setSpanCount(layoutManager.getSpanCount() - 1);
//                        recyclerView.getAdapter().notifyDataSetChanged();
//                    }
//
//                    return true;
//                }
//            });
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                return false;
//            }
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                scaleGestureDetector.onTouchEvent(e);
//            }
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//                // Not needed in this case
//            }
//        });
        retrieveImages();
    }
    private void updateSpanCount(int newSpanCount) {
        final int oldSpanCount = layoutManager.getSpanCount();

        // Notify adapter about the new span count
        adapter.updateSpanCount(newSpanCount);

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
    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
//            float scaleFactor = detector.getScaleFactor();
//
//            if (scaleFactor > 1.0f && layoutManager.getSpanCount() < 14) {
//                // Zoom in - Limit the maximum span count to 8 (adjust as needed)
//                layoutManager.setSpanCount(layoutManager.getSpanCount() + 1);
//                adapter.updateSpanCount(layoutManager.getSpanCount() );
//                adapter.notifyDataSetChanged();
//                return true;
//            } else if (scaleFactor < 1.0f && layoutManager.getSpanCount() > 1) {
//                // Zoom out - Limit the minimum span count to 1 (adjust as needed)
//                layoutManager.setSpanCount(layoutManager.getSpanCount() - 1);
//                adapter.updateSpanCount(layoutManager.getSpanCount() );
//                adapter.notifyDataSetChanged();
//                return true;
//            }
//
//            return false;
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

}