package com.example.gallery_application.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.gallery_application.Model.ImagesData;
import com.example.gallery_application.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Image_Retrivel_Activity extends AppCompatActivity {


    private List<ImagesData> imagesData;
    private CardView cardViewFolders;
    private ImageView thumbnail1,thumbnail2,thumbnail3,thumbnail4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_retrivel);

        cardViewFolders = findViewById(R.id.cardViewFolder);
        thumbnail1 = findViewById(R.id.imageView1);
        thumbnail2 = findViewById(R.id.imageView2);
        thumbnail3 = findViewById(R.id.imageView3);
        thumbnail4 = findViewById(R.id.imageView4);
        imagesData = new ArrayList<>();
        retrieveImages(0,30);
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
                    Log.e("MyApp","imagePath"+imagePath);

                    ImagesData imagesData1 = new ImagesData(imagePath);
                    imagesData.add(imagesData1);

                    Log.e("MyApp","imageDataSize"+imagesData.size());
                    createFolders(imagesData);
                  //  openImagesInFolder(imagesData);


                    count++;

                    // Load images in batches of batchSize
                    if (count >= batchSize) {
                        break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }
    private void createFolders(List<ImagesData> imagesDataList) {
        for (ImagesData imageData : imagesDataList) {
            String imagePath = imageData.getImagePath();
            File imageFile = new File(imagePath);
            String folderPath = imageFile.getParent(); // Get the parent folder path

            Log.e("MyApp","folderPath"+folderPath);
            Log.e("MyApp","folderPathParentAbsu"+imageFile.getAbsolutePath());

            //path same as  absolutepath
            //Log.e("MyApp","folderPathParent"+imageFile.getPath());

            String modifiedFolderPath = modifyPath(folderPath);
            Log.e("MyApp", "modifiedFolderPath: " + modifiedFolderPath);
            // Create folder if it doesn't exist
            File folder = new File(folderPath);
           // loadThumbnailImages(folderPath);
            if (!folder.exists()) {

                folder.mkdirs(); // Create parent directories as well

                boolean created = folder.mkdirs(); // Create parent directories as well
                Log.e("MyApp", "Folder Created: " + created);

                // Load and display 4 images as thumbnails

            }
        }
    }
    private String modifyPath(String path) {
        // Split the path by slashes
        String[] parts = path.split("/");

        // If there are at least two parts, remove everything except the first and last parts
        if (parts.length >= 2) {
            return "/" + parts[parts.length -5] + parts[parts.length - 4] +"/" + parts[parts.length -3] + "/"+ parts[parts.length -2] + "/" + parts[parts.length - 1];
        }

        // If there are less than two parts, return the original path
        return path;
    }
    private void loadThumbnailImages(String folderPath) {
        // List all files in the folder
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null && files.length > 0) {
            // Load and display up to 4 images as thumbnails
            int count = Math.min(4, files.length);
            for (int i = 0; i < count; i++) {
                ImageView thumbnailView = new ImageView(this);
                // Use a library like Glide to load and display the image as a thumbnail
                Glide.with(this).load(files[i]).thumbnail(0.1f).into(thumbnailView);

                // Add the ImageView to your layout or save it for later use
                // For example, if you have a LinearLayout with id 'thumbnailContainer':
                RelativeLayout thumbnailContainer = findViewById(R.id.containerRelative);
                thumbnailContainer.addView(thumbnailView);
            }
        }
    }
    private void openImagesInFolder(List<ImagesData> imagesDataList) {
        for (ImagesData imageData : imagesDataList) {
            String imagePath = imageData.getImagePath();

            // Load and display the image using Glide or Picasso
            ImageView imageView = new ImageView(this);
            // Use a library like Glide to load and display the image
            Glide.with(this).load(new File(imagePath)).into(imageView);

            // Add the ImageView to your layout
            // For example, if you have a LinearLayout with id 'container':
             RelativeLayout container = findViewById(R.id.containerRelative);
            container.addView(imageView);
        }
    }
    private void createFoldersAndSetImages(List<ImagesData> imagesDataList, RelativeLayout containerRelative) {
        for (ImagesData imageData : imagesDataList) {
            String imagePath = imageData.getImagePath();
            File imageFile = new File(imagePath);
            String folderPath = imageFile.getParent(); // Get the parent folder path

            // Create folder if it doesn't exist
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // Create parent directories as well
            }

            // Set the last 4 images in the ImageView elements
            setLastFourImages(folderPath, containerRelative);
        }
    }
    private void setLastFourImages(String folderPath, RelativeLayout containerRelative) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null && files.length > 0) {
            // Sort files by last modified time in descending order
            Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));

            // Load and set the last 4 images in ImageView elements
            for (int i = 0; i < Math.min(4, files.length); i++) {
                ImageView imageView = containerRelative.findViewWithTag("imageView" + (i + 1));
                if (imageView != null) {
                    // Use a library like Glide or Picasso to load and display the image
                    Glide.with(this).load(files[i]).into(imageView);
                }
            }
        }
    }
}