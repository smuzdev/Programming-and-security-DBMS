package com.smuzdev.lab_07.Models;

import java.io.Serializable;

public class Note implements Serializable {
    public Integer id;
    public String date;
    public String title;
    public String description;
    public String category;

    public Note(Integer id, String date, String title, String description) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public Note(Integer id, String date, String title, String description, String category) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public Note() {

    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description +
                ", category='" + description +
                '}';
    }
}
