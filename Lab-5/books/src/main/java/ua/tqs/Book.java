package ua.tqs;

import java.util.Date;

import lombok.Data;

@Data
public class Book {
    private final String title;
    private final String author;
    private final Date published;

    public Book(String title, String author, Date pub) {
        this.title = title;
        this.author = author;
        this.published = pub;
    }
}
