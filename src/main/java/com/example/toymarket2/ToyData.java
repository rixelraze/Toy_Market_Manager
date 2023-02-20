package com.example.toymarket2;

import java.sql.Date;

public class ToyData {

    private final Integer bookId;
    private final String title;
    private final String author;
    private final String genre;
    private final Date date;
    private final Double price;
    private final String image;

    public ToyData(Integer bookId, String title,
                   String author, String genre,
                   Date date, Double price, String image) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.price = price;
        this.image = image;
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Date getDate() {
        return date;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

}