package com.domesticanimalhub.model;

import java.sql.Timestamp;

public class Complaint {
    private Integer complaintId;
    private Integer userId;
    private String description;
    private ComplaintStatus status = ComplaintStatus.OPEN;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Complaint() {}

    public Complaint(Integer complaintId, Integer userId, String description,
                     ComplaintStatus status, Timestamp createdAt, Timestamp updatedAt) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getComplaintId() { return complaintId; }
    public void setComplaintId(Integer complaintId) { this.complaintId = complaintId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
