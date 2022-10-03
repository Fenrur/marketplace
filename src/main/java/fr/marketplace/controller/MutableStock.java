package fr.marketplace.controller;

import java.util.UUID;

public class MutableStock {

    private UUID productId;
    private String productName;
    private int actualStock;

    public MutableStock() {
    }

    public MutableStock(UUID productId, String productName, int actualStock) {
        this.productId = productId;
        this.productName = productName;
        this.actualStock = actualStock;
    }

    public UUID getProductId() {
        return productId;
    }

    public MutableStock setProductId(UUID productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public MutableStock setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public int getActualStock() {
        return actualStock;
    }

    public MutableStock setActualStock(int actualStock) {
        this.actualStock = actualStock;
        return this;
    }

    @Override
    public String toString() {
        return "MutableStock{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", actualStock=" + actualStock +
                '}';
    }
}
