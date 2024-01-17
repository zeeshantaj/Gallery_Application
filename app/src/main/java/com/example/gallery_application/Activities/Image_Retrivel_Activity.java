package com.example.gallery_application.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.gallery_application.Adapter.BucketRecyclerAdapter;
import com.example.gallery_application.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Image_Retrivel_Activity extends AppCompatActivity {


    private List<Bucket> bucketList;
    //private Bucket bucket;
    private BucketRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout splashLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_retrivel);

        showSplashScreen();

        String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
        new ImageDirectoriesAsyncTask().execute(new File(ROOT_DIR));

//        String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
////        findImageDirectories(new File(ROOT_DIR));
//        List<String> imageDirectories = findImageDirectories(new File(ROOT_DIR));
//
//        bucketList = new ArrayList<>();
//
//        // Print the names of image directories
//
//        Log.e("Myapp","BucketSize"+bucketList.size());
//        for (String directoryName : imageDirectories) {
//            System.out.println("Image Directory: " + directoryName);
//            bucket = new Bucket(directoryName);
//            bucketList.add(bucket);
//        }
//        recyclerView = findViewById(R.id.imageFolderRecycler);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        adapter = new BucketRecyclerAdapter(bucketList);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }
    private class ImageDirectoriesAsyncTask extends AsyncTask<File, Void, List<Bucket>> {
        @Override
        protected List<Bucket> doInBackground(File... params) {
            File rootDirectory = params[0];
            return findImageDirectories(rootDirectory);
        }

        @Override
        protected void onPostExecute(List<Bucket> result) {
            super.onPostExecute(result);
            bucketList = result;

            Log.e("Myapp", "BucketSize" + bucketList.size());
            // Hide splash screen
            hideSplashScreen();

            recyclerView = findViewById(R.id.imageFolderRecycler);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(Image_Retrivel_Activity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            adapter = new BucketRecyclerAdapter(bucketList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    private List<Bucket> findImageDirectories(File directory) {
        List<Bucket> imageDirectories = new ArrayList<>();
        File[] subDirectories = directory.listFiles(File::isDirectory);

        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                // Define your criteria for identifying image directories
                if (containsImages(subDirectory)) {
                    imageDirectories.add(new Bucket(subDirectory.getName()));
                }

                // Recursively search in subdirectories
                imageDirectories.addAll(findImageDirectories(subDirectory));
            }
        }
        return imageDirectories;
    }

    private static boolean containsImages(File directory) {
        // Define a custom FilenameFilter to filter files based on their extensions (e.g., jpg, png, etc.)
        FilenameFilter imageFilter = (dir, name) -> name.toLowerCase().endsWith(".jpg")
                || name.toLowerCase().endsWith(".jpeg")
                || name.toLowerCase().endsWith(".png")
                || name.toLowerCase().endsWith(".avif");

        // List files in the directory using the filter
        File[] files = directory.listFiles(imageFilter);

        // If there are image files in the directory, consider it an image directory
        return files != null && files.length > 0;
    }
    private void showSplashScreen() {
        splashLayout = new RelativeLayout(this);
        splashLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        splashLayout.setBackgroundResource(R.color.splashBg);
        setContentView(splashLayout);
    }

    private void hideSplashScreen() {
        setContentView(R.layout.activity_image_retrivel); // Switch back to the main layout
        splashLayout = null; // Release the splashLayout reference
    }
}