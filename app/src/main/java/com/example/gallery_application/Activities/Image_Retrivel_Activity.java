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


    private List<ImagesData> imagesData;
    private CardView cardViewFolders;
    private ImageView thumbnail1,thumbnail2,thumbnail3,thumbnail4;
    private Set<String> processedPaths = new HashSet<>();

    List<String> modifiedPathArray = new ArrayList<>();
    private RecyclerView recyclerView;
    private FolderRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_retrivel);

//        cardViewFolders = findViewById(R.id.cardViewFolder);
//        thumbnail1 = findViewById(R.id.imageView1);
//        thumbnail2 = findViewById(R.id.imageView2);
//        thumbnail3 = findViewById(R.id.imageView3);
//        thumbnail4 = findViewById(R.id.imageView4);
        imagesData = new ArrayList<>();
        //retrieveImages();
        Set<String> uniquePathsSet = new HashSet<>(modifiedPathArray);
        modifiedPathArray.clear();
        modifiedPathArray.addAll(uniquePathsSet);

        retrieveLast4Images(modifiedPathArray);

        recyclerView = findViewById(R.id.imageFolderRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Replace this with your actual list of ImageCard objects
        List<ImageData> imageCards = generateImageCards();
        adapter = new FolderRecyclerAdapter(imageCards);

        recyclerView.setAdapter(adapter);

    }

//    private void retrieveLast$Images(List<String> paths,int batchSize,int startIndex) {
//
//        for (String path : paths) {
//            // Define the columns you want to retrieve
//            String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
//            // Define the sorting order (DATE_ADDED in descending order)
//            String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
//            // Create a cursor to query the images in the MediaStore with pagination
//            Cursor cursor = getContentResolver().query(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    projection,
//                    MediaStore.Images.Media.DATA + " LIKE ?",
//                    new String[]{path + "%"}, // Filter by the specific path
//                    sortOrder
//            );
//
//            if (cursor != null) {
//                int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//
//
//                if (cursor.moveToPosition(startIndex)) {
//
//                    int count = 0;
//
//                    do {
//                        long imageId = cursor.getLong(idColumnIndex);
//                        String imagePath = cursor.getString(dataColumnIndex);
//                        Log.e("MyApp", "imagePath: " + imagePath);
//
//                        // Load images in batches of batchSize
//                        count++;
//                        if (count >= batchSize) {
//                            break;
//                        }
//                    } while (cursor.moveToNext());
//                }
//
//                cursor.close(); // Close the cursor after retrieving images for a path
//            }
//        }
//    }
private List<ImageData> generateImageCards() {
    // Replace this with your logic to generate ImageCard objects
    // Each ImageCard object should contain a list of image paths (strings)
    // For simplicity, using sample data here
    List<ImageData> imageCards = new ArrayList<>();

    // Example: card 1
    List<String> card1ImagePaths = modifiedPathArray;
    imageCards.add(new ImageData(card1ImagePaths));


    // Example: card 2 (and so on...)
    // ...

    return imageCards;
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
                    Log.e("MyApp", "imagePath: -> " +count+ imagePath);

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