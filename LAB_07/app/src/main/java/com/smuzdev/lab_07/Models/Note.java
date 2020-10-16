package com.smuzdev.lab_07.Models;

import java.io.Serializable;

public class Note implements Serializable {
    String date;
    String title;
    String description;

    public Note(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public Note() {

    }

    @Override
    public String toString() {
        return "Note{" +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description +
                '}';
    }
}
