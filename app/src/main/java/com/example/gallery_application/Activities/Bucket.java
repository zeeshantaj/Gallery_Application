package com.example.gallery_application.Activities;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private String name;

    private List<String> imagePaths;
    //private List<String> bucketPaths;
    private String bucketPath;

    public Bucket(String name,String bucketPath) {
        this.name = name;
        this.bucketPath = bucketPath;
        this.imagePaths = new ArrayList<>();
    //    this.bucketPaths = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public String getBucketPath() {
        return bucketPath;
    }

    public void addImagePath(String path) {
        imagePaths.add(path);
    }
}
