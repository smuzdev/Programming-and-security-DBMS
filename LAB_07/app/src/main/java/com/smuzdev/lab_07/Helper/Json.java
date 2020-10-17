package com.smuzdev.lab_07.Helper;

import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.smuzdev.lab_07.Models.Note;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Json {
    private static final String TAG = "LAB_07_D";
    static File file = new File(Environment.getExternalStorageDirectory(), "Notes.txt");


    public static void Serialize(Notes notes) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        try {
            objectMapper.writeValue(file, notes);
            Log.d(TAG, "Serialized successful.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Notes Deserialize() {

        Notes notes = new Notes();
        ObjectMapper objectMapper = new ObjectMapper();

        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        try {
            FileReader fr = new FileReader(file);
            notes = objectMapper.readValue(fr, Notes.class);
            Log.d(TAG, "DeSerialized successful. Result:");
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return notes;
    }
}
