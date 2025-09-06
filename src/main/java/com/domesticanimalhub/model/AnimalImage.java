package com.domesticanimalhub.model;

import java.sql.Timestamp;

public class AnimalImage {
    private Integer imageId;
    private Integer animalId;
    private byte[] imageData;    
    private Timestamp uploadedAt;

    public AnimalImage() {}

    public AnimalImage(Integer imageId, Integer animalId, byte[] imageData, Timestamp uploadedAt) {
        this.imageId = imageId;
        this.animalId = animalId;
        this.imageData = imageData;
        this.uploadedAt = uploadedAt;
    }

    public Integer getImageId() { return imageId; }
    public void setImageId(Integer imageId) { this.imageId = imageId; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }
}
