package com.smuzdev.lab_04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!IsFileExist("BaseLab.txt"))
            ShowDialog("BaseLab.txt");
    }

    private boolean IsFileExist(String fname) {
        boolean isFileExist = false;
        File f = new File(Environment.getExternalStorageDirectory(), fname);
        if (isFileExist = f.exists()) Log.d("Log_02", "File " + fname + " exists.");
        else Log.d("Log_02", "File " + fname + " not found.");
        return isFileExist;
    }

    public void ShowDialog(final String fname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File " + fname + " does not exist. Do you want to create one?").setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!permissionGranted){
                            checkPermissions();
                            return;
                        }
                        CreateFile(fname);
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void CreateFile(String fname) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fname);
            file.createNewFile();
            Log.d("Log_02", "File " + fname + " has been successfully created.");
        } catch (IOException e) {
            Log.d("Log_02", "Error: " + e.getMessage() + ". File " + fname + " was not created.");
        }
    }

    public void onClickEnterButton(View view) {
        TextView FirstName = findViewById(R.id.firstName);
        TextView LastName = findViewById(R.id.lastName);

        File file = new File(Environment.getExternalStorageDirectory(), "BaseLab.txt");
        Log.d("Log_02", "Path:" + Environment.getExternalStorageDirectory());

        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(FirstName.getText().toString() + " " + LastName.getText().toString() + "\r\n");
            bw.close();
            fw.close();
        } catch (IOException e) {
            Log.d("Log_02", "File entry error.");
        }

    }

    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private boolean checkPermissions(){
        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true;
                    Toast.makeText(this, "Permissions received", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Permissions must be granted", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}