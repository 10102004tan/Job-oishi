package com.example.joboishi.Models.data;


import com.google.gson.annotations.SerializedName;

public class FileCV {
    @SerializedName("id")
    private int id;
    @SerializedName("file_name")
    private String fileName;

    @SerializedName("file_size")
    private String fileSize;

    @SerializedName("upload_at")
    private String uploadAt;

    public FileCV(int id, String fileName, String fileSize, String uploadAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadAt = uploadAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(String uploadAt) {
        this.uploadAt = uploadAt;
    }
}
