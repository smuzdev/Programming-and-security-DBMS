package by.bstu.mark.lab16;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bstu.mark.lab16.R;

import java.util.ArrayList;

public class ManipulationActivity extends AppCompatActivity {
    long contactId = 0;
    EditText textPhone, textName;
    Button del_btn, save_btn;
    String tv_phone_OnStart, tv_name_OnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulation_contacts);

        textPhone = findViewById(R.id.contact_phone);
        textName = findViewById(R.id.contact_name);
        del_btn = findViewById(R.id.del_btn);
        save_btn = findViewById(R.id.btn_save);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
            contactId = extras.getLong("contact_id");
        if (contactId > 0)
        {
            textName.setText(String.valueOf(contactId));

            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ ContactsContract.Contacts.DISPLAY_NAME_PRIMARY },
                    ContactsContract.Contacts._ID + " = " + contactId,
                    null,
                    null
            );

            if (cursor.getCount() > 0)
            {
                /* Найдено */
                while (cursor.moveToNext())
                {
                    String contact = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                    );

                    textName.setText(contact);
                }

                cursor.close();
            }
            else
                textName.setText("nothing"); // не найдено

            Cursor phone_cursor = getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER },
                    ContactsContract.Data.CONTACT_ID + " = " + contactId,
                    null,
                    null
            );

            if (phone_cursor.getCount() > 0)
            {
                /* Найдено */
                while (phone_cursor.moveToNext())
                {
                    String number = phone_cursor.getString(
                            phone_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    );

                    textPhone.setText(number);
                }

                phone_cursor.close();
            }
        }


        tv_phone_OnStart = textPhone.getText().toString();
        tv_name_OnStart = textName.getText().toString();


        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact(textPhone.getText().toString(), textName.getText().toString());
                goHome();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();

                if (contactId == 0)
                {
                    try {
                        op.add(
                                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                        .build()
                        );

                        op.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, textName.getText().toString())
                                        .build()
                        );

                        op.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, textPhone.getText().toString())
                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                        .build()
                        );
                    }

                    catch (Exception e) {
                        Log.d("LAB_16_DEBUG", "SaveContact: " + e.getMessage());
                    }
                }

                else
                {
                    deleteContact(tv_phone_OnStart, tv_name_OnStart);


                    op.add(
                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                    .build()
                    );

                    op.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, textName.getText().toString())
                                    .build()
                    );

                    op.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, textPhone.getText().toString())
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .build()
                    );

                }

                try
                {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
                }
                catch (Exception e)
                {
                    Log.e("Exception: ", e.getMessage());
                }
                finally
                {
                    goHome();
                }
            }
        });

    }




    private void deleteContact(String phone, String name)
    {
        Log.d("--------", phone);
        Log.d("--------", name);
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = getContentResolver().query(contactUri, null, null, null, null);

        try
        {
            if (cur.moveToFirst())
            {
                do
                {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name))
                    {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

                        getContentResolver().delete(uri, null, null);
                    }

                }
                while (cur.moveToNext());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
        finally
        {
            cur.close();
        }
    }


    private void goHome()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
