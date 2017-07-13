package com.example.android.booklisting;

import java.util.List;

public class Book {

    private String title;
    private List<String> authorList;

    public Book() {
    }

    public Book(String title, List<String> author) {
        this.title = title;
        this.authorList = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        StringBuilder sb = new StringBuilder();
        for (String author : authorList) {
            sb.append(author).append(", ");
        }
        //removes the comma and space after last author
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
