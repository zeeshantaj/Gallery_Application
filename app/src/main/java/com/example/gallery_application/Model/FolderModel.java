package com.example.gallery_application.Model;

import java.util.List;

public class FolderModel {
    private String imagesFolder;

    public FolderModel(String imagesFolder) {
        this.imagesFolder = imagesFolder;
    }

    public String getImagesFolder() {
        return imagesFolder;
    }

    public void setImagesFolder(String imagesFolder) {
        this.imagesFolder = imagesFolder;
    }
}
