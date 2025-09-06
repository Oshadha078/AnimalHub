package com.domesticanimalhub.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private Integer orderId;
    private Integer animalId;
    private Integer buyerId;
    private BigDecimal pricePaid;
    private OrderStatus status = OrderStatus.INITIATED;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Order() {}

    public Order(Integer orderId, Integer animalId, Integer buyerId, BigDecimal pricePaid,
                 OrderStatus status, Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.animalId = animalId;
        this.buyerId = buyerId;
        this.pricePaid = pricePaid;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public Integer getBuyerId() { return buyerId; }
    public void setBuyerId(Integer buyerId) { this.buyerId = buyerId; }

    public BigDecimal getPricePaid() { return pricePaid; }
    public void setPricePaid(BigDecimal pricePaid) { this.pricePaid = pricePaid; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
