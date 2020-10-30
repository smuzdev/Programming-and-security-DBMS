package com.smuzdev.lab_07.Helper;

import android.util.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XmlSerialization {
    String TAG = "LAB8_D";
    File file;

    public XmlSerialization(File file) {
        this.file = file;
    }

    public void Serialize(Notes notes) {
        XStream xs = new XStream();
        //Serialize the object

        //Write to a file in the file system
        try {
            FileOutputStream fs = new FileOutputStream(file);
            xs.toXML(notes, fs);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        Log.d(TAG, "Serialize complete.");
    }

    public Notes Deserialize() {

        Notes notes = new Notes();
        XStream xs = new XStream(new DomDriver());

        try {
            FileInputStream fis = new FileInputStream(file);
            xs.fromXML(fis, notes);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        Log.d(TAG, "DeSerialize complete.");

        return notes;
    }
}
