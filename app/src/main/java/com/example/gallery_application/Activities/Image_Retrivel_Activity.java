package com.example.gallery_application.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gallery_application.Adapter.BucketRecyclerAdapter;
import com.example.gallery_application.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Image_Retrivel_Activity extends AppCompatActivity {


    private List<Bucket> bucketList;
    private BucketRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout splashLayout;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String readMediaImageIn_13 = Manifest.permission.READ_MEDIA_IMAGES;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_retrivel);

        showSplashScreen();

        if (Build.VERSION.SDK_INT >= 33){
            ActivityCompat.requestPermissions(this,new String[]{readMediaImageIn_13},REQUEST_READ_EXTERNAL_STORAGE);
        } else if (Build.VERSION.SDK_INT <= 30) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                // Permission already granted, proceed with loading the image
                String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
                new ImageDirectoriesAsyncTask().execute(new File(ROOT_DIR));
            }
        }
        else {
            // For versions below Android 6.0, permission is granted in the manifest
            String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
            new ImageDirectoriesAsyncTask().execute(new File(ROOT_DIR));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
//            } else {
//                // Permission already granted, proceed with loading the image
//                String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
//                new ImageDirectoriesAsyncTask().execute(new File(ROOT_DIR));
//            }
//        } else {
//            // For versions below Android 6.0, permission is granted in the manifest
//            String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
//            new ImageDirectoriesAsyncTask().execute(new File(ROOT_DIR));
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with loading the image
                String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
                new ImageDirectoriesAsyncTask().execute(new File(ROOT_DIR));
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Permission is required to access the images ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class ImageDirectoriesAsyncTask extends AsyncTask<File, Void, List<Bucket>> {
        @Override
        protected List<Bucket> doInBackground(File... params) {
            File rootDirectory = params[0];
            List<Bucket> result = findImageDirectories(rootDirectory);

            return result;
//            return findImageDirectories(rootDirectory);
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


            // Load the remaining 75% of the data after a delay (you can adjust the delay as needed)
        }
    }
    private List<Bucket> findImageDirectories(File directory) {
        List<Bucket> imageDirectories = new ArrayList<>();
        File[] subDirectories = directory.listFiles(File::isDirectory);

        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                // Define your criteria for identifying image directories
                if (containsImages(subDirectory)) {
                    //imageDirectories.add(new Bucket(subDirectory.getName()));

                    Bucket bucket = new Bucket(subDirectory.getName(),subDirectory.getAbsolutePath());
                    List<String> lastFourImages = getLastFourImages(subDirectory);
                    bucket.getImagePaths().addAll(lastFourImages);
                    imageDirectories.add(bucket);

                }

                // Recursively search in subdirectories
                imageDirectories.addAll(findImageDirectories(subDirectory));
            }
        }
        return imageDirectories;
    }

    private List<String> getLastFourImages(File directory) {
        List<String> imagePaths = new ArrayList<>();
        FilenameFilter imageFilter = (dir, name) -> name.toLowerCase().endsWith(".jpg")
                || name.toLowerCase().endsWith(".jpeg")
                || name.toLowerCase().endsWith(".png")
                || name.toLowerCase().endsWith("..avif");

        File[] files = directory.listFiles(imageFilter);

        if (files != null) {
            int count = Math.min(4, files.length); // Limit to the last four images
            for (int i = files.length - 1; i >= files.length - count; i--) {
                imagePaths.add(files[i].getAbsolutePath());
                Log.e("MyApp","Images -> "+files[i].getAbsolutePath());
            }
        }
        return imagePaths;
    }
    public static boolean containsImages(File directory) {
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