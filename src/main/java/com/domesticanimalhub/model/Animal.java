package com.domesticanimalhub.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Animal {
    private Integer animalId;
    private Integer userId;
    private AnimalType animalType;
    private String breed;
    private Integer ageYears;
    private Gender gender;
    private BigDecimal price;
    private String location;
    private String description;
    private String healthRecord;
    private boolean vaccinated;
    private byte[] paymentSlip;     // BLOB
    private boolean approved;
    private ListingStatus listingStatus = ListingStatus.PENDING;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Animal() {}

    public Animal(Integer animalId, Integer userId, AnimalType animalType, String breed,
                  Integer ageYears, Gender gender, BigDecimal price, String location,
                  String description, String healthRecord, boolean vaccinated,
                  byte[] paymentSlip, boolean approved, ListingStatus listingStatus,
                  Timestamp createdAt, Timestamp updatedAt) {
        this.animalId = animalId;
        this.userId = userId;
        this.animalType = animalType;
        this.breed = breed;
        this.ageYears = ageYears;
        this.gender = gender;
        this.price = price;
        this.location = location;
        this.description = description;
        this.healthRecord = healthRecord;
        this.vaccinated = vaccinated;
        this.paymentSlip = paymentSlip;
        this.approved = approved;
        this.listingStatus = listingStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public AnimalType getAnimalType() { return animalType; }
    public void setAnimalType(AnimalType animalType) { this.animalType = animalType; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAgeYears() { return ageYears; }
    public void setAgeYears(Integer ageYears) { this.ageYears = ageYears; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHealthRecord() { return healthRecord; }
    public void setHealthRecord(String healthRecord) { this.healthRecord = healthRecord; }

    public boolean isVaccinated() { return vaccinated; }
    public void setVaccinated(boolean vaccinated) { this.vaccinated = vaccinated; }

    public byte[] getPaymentSlip() { return paymentSlip; }
    public void setPaymentSlip(byte[] paymentSlip) { this.paymentSlip = paymentSlip; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public ListingStatus getListingStatus() { return listingStatus; }
    public void setListingStatus(ListingStatus listingStatus) { this.listingStatus = listingStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
