package com.serosoft.academiassu.Modules.AcademiaDrive.Models;

import java.io.Serializable;

public class DriveFile_Dto implements Serializable {

    private String imageName;
    private String path;
    private String fileType;
    private int id;
    private int galleryId;

    public DriveFile_Dto(String imageName, String path, String fileType, int id, int galleryId) {
        this.imageName = imageName;
        this.path = path;
        this.fileType = fileType;
        this.id = id;
        this.galleryId = galleryId;
    }

    public String getImageName() {
        return imageName;
    }

    public String getPath() {
        return path;
    }

    public String getFileType() {
        return fileType;
    }

    public int getId() {
        return id;
    }

    public int getGalleryId() {
        return galleryId;
    }
}
