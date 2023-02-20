package com.example.toymarket2;

import java.sql.Date;

public class customerData {

    private final Integer customerId;
    private final Integer toyId;
    private final String brand;
    private final String author;
    private final String genre;
    private final Integer quantity;
    private final Double price;
    private final Date date;

    public customerData(Integer customerId, Integer toyId, String brand, String author
            , String genre, Integer quantity, Double price, Date date) {
        this.customerId = customerId;
        this.toyId = toyId;
        this.brand = brand;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getBookId() {
        return toyId;
    }

    public String getTitle() {
        return brand;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

}