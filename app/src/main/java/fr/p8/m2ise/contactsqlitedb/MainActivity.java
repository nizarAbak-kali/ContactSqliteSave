package fr.p8.m2ise.contactsqlitedb;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    Cursor cursor;
    Context context;
    MySQLiteHelper mydb;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("OnCreate","pré creation de la db");
        mydb = new MySQLiteHelper(this);
        Log.d("OnCreate","post creation de la db");

        final ListView list = (ListView) findViewById(android.R.id.list);
        final List<Map<String, Object>> contacts = retrieveContacts(getContentResolver());
        Log.d("OnCreate","post recuperation des contacts");



        if (contacts != null)
        {
            SimpleAdapter adapter = new SimpleAdapter(this,contacts,R.layout.contact_layout,new String[] { "name", "photo" }, new int[] { R.id.name,
                    R.id.photo });
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if ((view instanceof ImageView) & (data instanceof Bitmap))
                    {
                        final ImageView image = (ImageView) view;
                        final Bitmap photo = (Bitmap) data;
                        image.setImageBitmap(photo);
                        return true;
                    }
                    return false;                }
            });


            list.setAdapter(adapter);
        }
    }

    private List<Map<String, Object>> retrieveContacts(ContentResolver contentResolver)
    {
        final List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
        final Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER }, null, null, null);

        if (cursor == null)
        {
            Log.e("retrieveContacts", "Cannot retrieve the contacts");
            return null;
        }

        if (cursor.moveToFirst() == true)
        {
            do
            {
                final long id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID)));
                final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                final int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));


                if (hasPhoneNumber > 0)
                {
                    final Bitmap photo = getPhoto(contentResolver, id);

                    final Map<String, Object> contact = new HashMap<String, Object>();
                    contact.put("name", name);
                    contact.put("photo", photo);

                    Contact c = new Contact();
                    c.setName(name);

                    if(photo != null) {
                        int bytes = photo.getByteCount();


                        if (bytes != 0) {
                            ByteBuffer buffer = ByteBuffer.allocate(bytes);
                            photo.copyPixelsToBuffer(buffer);
                            c.setThumbnail(buffer.array());

                        }
                    }

                    mydb.addContacts(c);

                    //Log.d("retriveContacts",c.toString());
                    contacts.add(contact);
                }
            }
            while (cursor.moveToNext() == true);
        }

        if (cursor.isClosed() == false)
        {
            cursor.close();
        }

        return contacts;
    }

    private Bitmap getPhoto(ContentResolver contentResolver, long contactId)
    {
        Bitmap photo = null;
        final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        final Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        final Cursor cursor = contentResolver.query(photoUri, new String[] { ContactsContract.Contacts.Photo.DATA15 }, null, null, null);

        if (cursor == null)
        {
            Log.e("getPhoto", "Cannot retrieve the photo of the contact with id '" + contactId + "'");
            return null;
        }

        if (cursor.moveToFirst() == true)
        {
            final byte[] data = cursor.getBlob(0);

            if (data != null)
            {
                photo = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            }
        }

        if (cursor.isClosed() == false)
        {
            cursor.close();
        }

        return photo;
    }
    public void toJSON(){

    }

}
