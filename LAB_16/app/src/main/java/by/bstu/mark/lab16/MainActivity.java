package by.bstu.mark.lab16;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.bstu.mark.lab16.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_CONTACTS = 100;
    private static boolean READ_CONTACTS_GRANTED = false;

    ListView LV_contacts;
    EditText phoneNumber;
    Cursor ContactsCursor;
    SimpleCursorAdapter ContactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = findViewById(R.id.phoneNumberEditText);
        LV_contacts = findViewById(R.id.LV_contacts);
        LV_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ManipulationActivity.class);
                intent.putExtra("contact_id", id);
                startActivity(intent);
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                if (LV_contacts.getCount() == 0)
                    loadContacts();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (phoneNumber.getText().toString().length() > 0)
                    FindContact(phoneNumber.getText().toString());
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadContacts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    READ_CONTACTS_GRANTED = true;
        }

        if (READ_CONTACTS_GRANTED)
            loadContacts();
        else
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
    }

    private void loadContacts()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.READ_CONTACTS }, REQUEST_CODE_READ_CONTACTS);
        } else {
            ContactsCursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts._ID },
                    null,
                    null,
                    null
            );

            String[] headers = new String[]{ ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts._ID };
            for (String str: headers) {
                Log.d("LAB_15_DEBUG", "loadContacts: " + str);
            }

            ContactsAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    ContactsCursor,
                    headers,
                    new int[]{ android.R.id.text1, android.R.id.text2 },
                    0
            );

            if(!ContactsAdapter.isEmpty())
                LV_contacts.setAdapter(ContactsAdapter);
        }
    }

    public void AddContact(View v)
    {
        Intent intent = new Intent(this, ManipulationActivity.class);
        intent.putExtra("contact_id", 0);
        startActivity(intent);
    }

    public void FindContact(String phone)
    {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));

        ContactsCursor = getContentResolver().query(
                contactUri,
                new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                        ContactsContract.PhoneLookup._ID,
                        ContactsContract.PhoneLookup.NUMBER
                },
                null,
                null,
                null
        );

        String[] headers = new String[]{ ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.PhoneLookup.NUMBER };

        ContactsAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.two_line_list_item,
                ContactsCursor,
                headers,
                new int[]{ android.R.id.text1, android.R.id.text2 },
                0
        );

        LV_contacts.setAdapter(ContactsAdapter);
    }
}
