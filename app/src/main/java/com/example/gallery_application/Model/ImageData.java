package com.example.gallery_application.Model;

import java.util.List;

public class ImageData {
    private List<String > imagePaths;

    public ImageData(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }
}
