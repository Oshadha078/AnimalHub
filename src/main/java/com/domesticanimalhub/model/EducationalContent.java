// src/main/java/com/domesticanimalhub/model/EducationalContent.java
package com.domesticanimalhub.model;

import java.sql.Timestamp;

public class EducationalContent {
    private int id;                 // maps to content_id
    private String title;
    private String description;
    private byte[] image;           // maps to image_data
    private Integer postedBy;
    private Timestamp createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public Integer getPostedBy() { return postedBy; }
    public void setPostedBy(Integer postedBy) { this.postedBy = postedBy; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
