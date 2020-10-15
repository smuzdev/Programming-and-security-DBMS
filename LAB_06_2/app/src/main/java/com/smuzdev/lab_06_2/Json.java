package com.smuzdev.lab_06_2;

import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Json {
    static File file = new File(Environment.getExternalStorageDirectory(), "Lab.txt");

    //Serialize to default file (getExternalStorageDirectory)
    public static void Serialize(Person person) {

        ArrayList<Person> arrayList = new ArrayList<>();
        arrayList = Json.Deserialize();

        ObjectMapper objectMapper = new ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //Define map which will be converted to JSON
        arrayList.add(person);

        //1. Convert List of Person objects to JSON
        try {
            objectMapper.writeValue(file, arrayList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("LAB_06" , "Serialized successful.");

    }

    //Serialize method overloading (+file in params)
    public static void Serialize(File file, Person person) {

        ArrayList<Person> arrayList = new ArrayList<>();
        arrayList = Json.Deserialize();

        ObjectMapper objectMapper = new ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //Define map which will be converted to JSON
        arrayList.add(person);

        //1. Convert List of Person objects to JSON
        try {
            objectMapper.writeValue(file, arrayList);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("LAB_06", "Serialized to file:" + file.getAbsolutePath() +  "successful.");


    }

    public static ArrayList<Person> Deserialize() {

        ArrayList<Person> arrayList = new ArrayList<Person>();
        ObjectMapper objectMapper = new ObjectMapper();

        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        TypeReference<ArrayList<Person>> mapType = new TypeReference<ArrayList<Person>>() {};

        try {
            FileReader fr = new FileReader(file);
            arrayList = objectMapper.readValue(fr, mapType);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("LAB_06", "DeSerialized successful. Result:");
        for (Person person:arrayList) {
            Log.d("LAB_06", person.toString());
        }

        return arrayList;
    }
}
