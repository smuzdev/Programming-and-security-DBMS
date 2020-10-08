
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

// ПРИВЕТ, ДРУГ! ЕСЛИ ПОЛУЧИЛОСЬ ТАК, ЧТО КАК ПРИМЕР ТЫ ВЗЯЛ ЭТУ ЛАБУ, ТО ХОЧУ ТЕБЕ СКАЗАТЬ, ЧТО
// НЕ СМОТРЯ НА КУЧУ ЧАСОВ, ОТТДАННЫХ НА ПОПЫТКУ РАЗОБРАТЬСЯ С RandomAccessFile, Я ТАК И НЕ СМОГ С НИМ
// РАЗОБРАТЬСЯ. КАКИЕ-ТО ПОПЫТКИ СПРЯТАНЫ В РЕГИОНАХ И ЗАКОМЕНЧЕНЫ, ПОЭТОМУ ЕСЛИ ЕСТЬ ЖЕЛАНИЕ, ТО
// МОЖЕШЬ ПОПРОБОВАТЬ ДОВЕСТИ ЭТО ДЕЛО ДО КОНЦА. В МОЕМ ВАРИАНТЕ ПОИСК ИДЕТ НЕ В ФАЙЛЕ, КАК ПРОСЯТ
// В УСЛОВИИ, А В ОПЕРАТИВНОЙ ПАМЯТИ. УДАЧИ!

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

package com.smuzdev.lab_05;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {

    public String filename = "Lab.txt";
    public String TAG = "Events";
    public static final int PACKAGE_SIZE = 17;
    Button saveToHashtableButton;
    Button getValueButton;
    Button printHashtableButton;

    EditText inputKey;
    EditText inputValue;
    EditText findByKey;
    EditText outputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveToHashtableButton = findViewById(R.id.saveToHashtableButton);
        getValueButton = findViewById(R.id.getValueButton);
        printHashtableButton = findViewById(R.id.printHashtableButton);
        inputKey = findViewById(R.id.inputKey);
        inputValue = findViewById(R.id.inputValue);
        findByKey = findViewById(R.id.findByKey);
        outputValue = findViewById(R.id.outputValue);

        if(!IsFileExist(filename)) {
            ShowDialog(filename, this, this);
        }

        final CustomHashtable HashTable = CreateHashTable(10);
        saveToHashtableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = inputKey.getText().toString();
                String value = inputValue.getText().toString();

                if(isKeyCorrect(key) && isValueCorrect(value)) {
                    HashTable.put(key, value);
                    Toast.makeText(MainActivity.this, "A new key-value pair has been added to the hashtable", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error: unable to add new key-value pair to the hashtable", Toast.LENGTH_LONG).show();
                }
            }
        });

        //region Search
//        getValueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(findByKey.getText().toString().length() > 0){
//                    outputValue.setText(SearchThis(findByKey.getText().toString()));
//                }
//                String key = findByKey.getText().toString();
//
//                if(isKeyCorrect(key)) {
//                    String returnValue =  (String)HashTable.get(key);
//                    Log.d("LAB_5", "Trying to find value with key: " + key);
//                    findByKey.setText(returnValue);
//                    Log.d("LAB_5", " Value: " + returnValue);
//                }
//            }
//        });
        //endregion

        getValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = findByKey.getText().toString();

                if(isKeyCorrect(key)) {
                    String returnValue =  (String)HashTable.get(key);
                    Log.d(TAG, "Trying to find value with key: " + key);
                    outputValue.setText(returnValue);
                    Log.d(TAG, "Value: " + returnValue);
                }
            }
        });


        printHashtableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashTable.printTable();
            }
        });
    }

    private boolean IsFileExist(String filename) {
        boolean isFileExist = false;
        File file = new File (Environment.getExternalStorageDirectory(), filename);
        if(isFileExist = file.exists()) {
            Log.d(TAG, "File " + filename + " " + "exists.");
        } else {
            Log.d(TAG, "File " + filename + " " + "not found.");
        }
        return isFileExist;
    }

    public void ShowDialog(final String filename, final Context context, final Activity activity)  {
        final RequestPermission requestPermission = new RequestPermission();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File " + filename + " does not exist. Do you want to create one?").setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!requestPermission.permissionGranted){
                            requestPermission.checkPermissions(context, activity);
                        }
                        CreateFile(filename);
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void CreateFile(String filename) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            file.createNewFile();
            Log.d(TAG, "File " + filename + " has been successfully created.");
            Toast.makeText(this, "New file has been created.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.d(TAG, "Error: " + e.getMessage() + ". File " + filename + " was not created.");
        }
    }

    CustomHashtable<String, String> CreateHashTable(int capacity) {
        return new CustomHashtable<String, String>(capacity);
    }

    boolean isKeyCorrect(String key){
        if(key.length() <= 5 ) {
            return true;
        } else {
            Log.d(TAG, "Cannot be added to hashtable, key is incorrect");
            return false;
        }
    }

    boolean isValueCorrect(String value) {
        if (value.length() <= 5) {
            return true;
        } else {
            Log.d(TAG, "Cannot be added to hashtable, value is incorrect");
            return false;
        }
    }

    //region RandomAccessFile
    //    public void WriteToTable(String value, int pos, int hash, String key){
//        try {
//            File file = new File(getFilesDir() + "/" + File.separator + filename);
//            RandomAccessFile file1 = new RandomAccessFile(file, "rw");
//            Log.i("HASH", hash+"");
//            Log.i("FILESIZE", file1.length()+"");
//            if(file1.length()  == 0){
//                //Если файл пустой, то мы пишем сразу с N позиции
//                file1.seek(pos);
//                file1.writeBytes(key+value);
//                file1.seek(pos+15);
//                file1.writeBytes("00");
//                file1.close();
//            }else{
//                //Если файл не пустой то сразу двигаемся куда надо
//                file1.seek(pos);
//                byte[] arr = new byte[5];
//                file1.read(arr);
//                String result = new String(arr).trim();
//                Log.i("INFO", result);
//                if(key.contains(result)){
//                    //Если ключи совпадают то мы просто перезаписываем
//                    byte[] pos_ = new byte[2];
//                    file1.seek(pos+15);
//                    file1.read(pos_);
//                    String result_ = new String(pos_).trim();
//                    file1.seek(pos);
//                    file1.writeBytes("                 ");
//                    file1.seek(pos);
//                    file1.writeBytes(key+value);
//                    file1.seek(pos+15);
//                    file1.writeBytes(result_.length() > 0?result_+"":"00");
//                    file1.close();
//                }
//                else if(key.length() == 0){
//                    file1.seek(pos);
//                    file1.writeBytes(key+value);
//                    file1.seek(pos+15);
//                    file1.writeBytes("00");
//                    file1.close();
//                }
//                else{
//                    //Ключи разные, коллизия
//                    int tempnum = (int)file1.length()/ PACKAGE_SIZE;
//                    file1.seek(pos+15);
//                    byte[] arr_ = new byte[2];
//                    file1.read(arr_);
//                    String res_2 = new String(arr_).trim();
//                    Log.i("RES_2", res_2);
//                    if(res_2.contains("00")){
//                        file1.seek(pos+15);
//                        file1.writeBytes(tempnum < 10? "0" + tempnum:tempnum + "");
//                        int temp_len = (int)file1.length();
//                        file1.seek(temp_len);
//                        file1.writeBytes(key+value);
//                        file1.seek(temp_len+15);
//                        file1.writeBytes("00");
//                        file1.close();
//                    }else{
//                        int currpos = Integer.parseInt(res_2);
//                        while(!res_2.contains("00") && res_2.length()>0){
//                            currpos = Integer.parseInt(res_2);
//                            file1.seek(currpos* PACKAGE_SIZE +15);
//                            arr_ = new byte[2];
//                            file1.read(arr_);
//                            res_2 = new String(arr_).trim();
//                            Log.i("THUIS", currpos+"");
//                            if(currpos == 0)
//                                break;
//                        }
//                        int len = (int)file1.length();
//                        file1.seek(currpos* PACKAGE_SIZE +15);
//                        Log.i("TEMPNUM", tempnum+"");
//                        file1.writeBytes(tempnum < 10? "0"+tempnum:tempnum+"");
//                        file1.seek(len);
//                        file1.writeBytes(key+value);
//                        file1.seek(len+15);
//                        file1.writeBytes("00");
//                        file1.close();
//                    }
//
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public int GenerateHash(String key){
//        int count = 0;
//        for(int i = 0;i<key.length(); i++){
//            count += Character.getNumericValue(key.charAt(i));
//        }
//        return Math.abs(count%10);
//    }
//
//    public String SearchThis(String key){
//        try {
//            int hash = GenerateHash(key);
//            File file = new File(getFilesDir() + "/" + File.separator + filename);
//            RandomAccessFile file1 = new RandomAccessFile(file, "rw");
//
//            byte[] array = new byte[5];
//
//            file1.seek(hash* PACKAGE_SIZE);
//            file1.read(array);
//            String result = new String(array).trim();
//            if(result.length() > 0) {
//                if (result.contains(key)) {
//                    byte[] arr = new byte[10];
//                    file1.seek(hash * PACKAGE_SIZE + 5);
//                    file1.read(arr);
//                    return new String(arr).trim();
//                } else {
//                    String next_stage = "";
//                    String loaded_key = "";
//                    String loaded_value = "";
//                    while (!next_stage.contains("00") && !loaded_key.contains(key)) {
//                        byte[] twos = new byte[2];
//                        file1.seek(hash * PACKAGE_SIZE + 15);
//                        file1.read(twos);
//                        next_stage = new String(twos).trim();
//                        hash = Integer.parseInt(next_stage);
//                        array = new byte[5];
//                        file1.seek(hash * PACKAGE_SIZE);
//                        file1.read(array);
//                        loaded_key = new String(array);
//                        byte[] arr = new byte[10];
//                        file1.seek(hash * PACKAGE_SIZE + 5);
//                        file1.read(arr);
//                        loaded_value = new String(arr);
//                    }
//                    if (loaded_key.contains(key)) {
//                        return loaded_value;
//                    } else {
//                        return "-";
//                    }
//                }
//            }else{
//                return "-";
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "-";
//    }
    //endregion
}
