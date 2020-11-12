package com.smuzdev.lab_07.Helper;

import android.util.Log;

import com.smuzdev.lab_07.Models.Note;

import java.io.Serializable;
import java.util.ArrayList;

public class Notes implements Serializable {
    public ArrayList<Note> notesArrayList = new ArrayList<Note>();

    public void addNote(Note note) {
        notesArrayList.add(note);
    }

    public void removeNote(Note note) {
        notesArrayList.remove(note);
    }

    public void printNotesArrayList() {
        for (Note note: notesArrayList) {
            Log.d("LAB_D", note.toString());
        }
    }

    public ArrayList<String> categoriesArrayList = new ArrayList<>();
    //public ArrayList<String> matchedNotes = new ArrayList<>();
}
