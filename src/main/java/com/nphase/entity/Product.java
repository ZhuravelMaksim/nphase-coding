package com.nphase.entity;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final String category;
    private final BigDecimal pricePerUnit;
    private final int quantity;

    public Product(String name, BigDecimal pricePerUnit, int quantity, String category) {
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }
}
