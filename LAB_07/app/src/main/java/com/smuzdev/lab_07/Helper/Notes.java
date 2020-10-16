package com.smuzdev.lab_07.Helper;

import android.util.Log;

import com.smuzdev.lab_07.Models.Note;

import java.util.ArrayList;

public class Notes {
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
}
