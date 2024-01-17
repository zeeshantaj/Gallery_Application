package com.example.gallery_application.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.gallery_application.Adapter.FolderRecyclerAdapter;
import com.example.gallery_application.Model.ImageData;
import com.example.gallery_application.Model.ImagesData;
import com.example.gallery_application.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Image_Retrivel_Activity extends AppCompatActivity {


    //private List<ImagesData> imagesData;
    private CardView cardViewFolders;
    private ImageView thumbnail1,thumbnail2,thumbnail3,thumbnail4;
    private Set<String> processedPaths = new HashSet<>();
    private List<ImagesData> imagesData;
    private RecyclerView recyclerView;
    private FolderRecyclerAdapter adapter;

    List<String> modifiedPathArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_retrivel);

        imagesData = new ArrayList<>();

        Set<String> uniquePathsSet = new HashSet<>(modifiedPathArray);
        modifiedPathArray.clear();
        modifiedPathArray.addAll(uniquePathsSet);

        Log.e("MyApp","modifiedArraySize"+modifiedPathArray.size());
        retrieveImages();
        retrieveLast4Images(modifiedPathArray);

    }

private void retrieveLast4Images(List<String> paths) {
    int batchSize = 4; // Set the number of images to retrieve for each path

    for (String path : paths) {
        // Define the columns you want to retrieve
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        // Define the sorting order (DATE_ADDED in descending order)
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        // Create a cursor to query the images in the MediaStore with pagination
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Media.DATA + " LIKE ?",
                new String[]{path + "%"}, // Filter by the specific path
                sortOrder
        );

        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            int count = 0;
            if (cursor.moveToFirst()) {
                do {
                    long imageId = cursor.getLong(idColumnIndex);
                    String imagePath = cursor.getString(dataColumnIndex);
                    Log.e("MyApp", "imagePath:count " +count+ imagePath);
                    modifiedPathArray.add(imagePath);
                    // Load images in batches of batchSize
                    count++;
                    if (count >= batchSize) {
                        break;
                    }
                } while (cursor.moveToNext());
            }

            cursor.close(); // Close the cursor after retrieving images for a path
        }
    }
}
    private void createFolders(List<ImagesData> imagesDataList) {
        for (ImagesData imageData : imagesDataList) {
            String imagePath = imageData.getImagePath();
            File imageFile = new File(imagePath);
            String folderPath = imageFile.getParent(); // Get the parent folder path



            if (folderPath != null) {

                String modifiedFolderPath = modifyPath(folderPath);
                modifiedPathArray.add(modifiedFolderPath);
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

    }
    private String modifyPath(String path) {
        // Split the path by slashes
        String[] parts = path.split("/");
        // If there are at least two parts, remove everything except the first and last parts
        int numberOfSlashes = countSlashes(path);

        if (numberOfSlashes >= parts.length) {
            String modifiedPath = "/" + parts[parts.length - 5] +
                    parts[parts.length - 4] + "/" +
                    parts[parts.length - 3] + "/" +
                    parts[parts.length - 2] + "/" +
                    parts[parts.length - 1];

            // Check if the modified path is already processed
            if (!processedPaths.contains(modifiedPath)) {
                processedPaths.add(modifiedPath);
                Log.e("MyApp", "modifiedFolderPath: " + processedPaths.size());
                int numberOfSlashesInModifiedPath = countSlashes(modifiedPath);
                System.out.println("Number of slashes in modified path: " + numberOfSlashesInModifiedPath);

                return modifiedPath;
            }
        }


        return path;
    }
    private static int countSlashes(String input) {
        // Split the input string based on the slash character
        String[] parts = input.split("/");

        // Return the count of slashes
        return parts.length - 1; // Subtract 1 to get the count of slashes
    }
    private void retrieveImages() {
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
            if (cursor.moveToNext()) {
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
//                    if (count >= batchSize) {
//                        break;
//                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

}