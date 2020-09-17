package com.smuzdev.lab_01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    TextView absoluteTextView;
    TextView nameTextView;
    TextView pathTextView;
    TextView rwTextView;
    TextView externalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        absoluteTextView = findViewById(R.id.absoluteTextView);
        nameTextView = findViewById(R.id.nameTextView);
        pathTextView = findViewById(R.id.pathTextView);
        rwTextView = findViewById(R.id.rwTextView);
        externalTextView = findViewById(R.id.externalTextView);
    }

    public void onGetFilesDirButtonClick(View view) {
        File getFilesDir = super.getFilesDir();
        absoluteTextView.setText("Absolute: " + getFilesDir.getAbsolutePath());
        nameTextView.setText("Name: " + getFilesDir.getName());
        pathTextView.setText("Path: " + getFilesDir.getPath());
        rwTextView.setText("Read/Write" + getFilesDir.canRead() + "/" + getFilesDir.canWrite());
        externalTextView.setText("External: " + Environment.getExternalStorageState());
    }

    public void onGetCacheDirButtonClick(View view) {
        File getCacheDir = super.getCacheDir();
        absoluteTextView.setText("Absolute: " + getCacheDir.getAbsolutePath());
        nameTextView.setText("Name: " + getCacheDir.getName());
        pathTextView.setText("Path: " + getCacheDir.getPath());
        rwTextView.setText("Read/Write" + getCacheDir.canRead() + "/" + getCacheDir.canWrite());
        externalTextView.setText("External: " + Environment.getExternalStorageState());
    }

    public void onGetExtFilesDirButtonClick(View view) {
        File getExternalFilesDir = super.getExternalFilesDir("text.txt");
        absoluteTextView.setText("Absolute: " + getExternalFilesDir.getAbsolutePath());
        nameTextView.setText("Name: " + getExternalFilesDir.getName());
        pathTextView.setText("Path: " + getExternalFilesDir.getPath());
        rwTextView.setText("Read/Write" + getExternalFilesDir.canRead() + "/" + getExternalFilesDir.canWrite());
        externalTextView.setText("External: " + Environment.getExternalStorageState());
    }

    public void onGetExtCacheDirButtonClick(View view) {
        File getExternalCacheDir = super.getExternalCacheDir();
        absoluteTextView.setText("Absolute: " + getExternalCacheDir.getAbsolutePath());
        nameTextView.setText("Name: " + getExternalCacheDir.getName());
        pathTextView.setText("Path: " + getExternalCacheDir.getPath());
        rwTextView.setText("Read/Write" + getExternalCacheDir.canRead() + "/" + getExternalCacheDir.canWrite());
        externalTextView.setText("External: " + Environment.getExternalStorageState());
    }

    public void onGetExtStorageDirectoryButtonClick(View view) {
        File getExternalStorageDirectory = Environment.getExternalStorageDirectory();
        absoluteTextView.setText("Absolute: " + getExternalStorageDirectory.getAbsolutePath());
        nameTextView.setText("Name: " + getExternalStorageDirectory.getName());
        pathTextView.setText("Path: " + getExternalStorageDirectory.getPath());
        rwTextView.setText("Read/Write" + getExternalStorageDirectory.canRead() + "/" + getExternalStorageDirectory.canWrite());
        externalTextView.setText("External: " + Environment.getExternalStorageState());
    }

    public void onGetExtStoragePublicDirectoryButtonClick(View view) {
        File getExternalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory("text.txt");
        absoluteTextView.setText("Absolute: " + getExternalStoragePublicDirectory.getAbsolutePath());
        nameTextView.setText("Name: " + getExternalStoragePublicDirectory.getName());
        pathTextView.setText("Path: " + getExternalStoragePublicDirectory.getPath());
        rwTextView.setText("Read/Write" + getExternalStoragePublicDirectory.canRead() + "/" + getExternalStoragePublicDirectory.canWrite());
        externalTextView.setText("External: " + Environment.getExternalStorageState());
    }
}