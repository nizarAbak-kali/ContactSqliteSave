package fr.p8.m2ise.contactsqlitedb;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView lolo;
    List<Map<String, Object>> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lolo = (ListView) findViewById(R.id.listcontact);
        contacts = getContact(this.getContentResolver());

    }

    private void afficheContact() {
        List<Map<String, Object>> contacts = getContact(this.getContentResolver());
        ListView list = (ListView) findViewById(R.id.listcontact);
        if (contacts != null) {
            SimpleAdapter adapter = new SimpleAdapter(this, contacts, R.layout.contact_layout, new String[]{"name", "photo"}, new int[]{R.id.name, R.id.photo});
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                        ImageView image = (ImageView) view;
                        Bitmap photo = (Bitmap) data;
                        image.setImageBitmap(photo);
                        return true;
                    }
                    return false;
                }
            });
            list.setAdapter(adapter);
        }
    }


    private List<Map<String, Object>> getContact(ContentResolver contentResolver) {
        ArrayList<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data._ID, ContactsContract.Data.HAS_PHONE_NUMBER}, null, null, null);
        if (cursor == null) {
            Log.e("getContact", "Impossible d'acceder aux contacts");
        }

        while (cursor.moveToNext()) {
            Map<String, Object> contact = new HashMap<String, Object>();
            Bitmap photo = null;

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            int phoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));
            Log.d("contact", "nom: " + name + " id: " + id + "telephone: " + phoneNumber);
            photo = getPhoto(contentResolver, id);

            if (phoneNumber > 0) {
                contact.put("name", name);
                contact.put("photo", photo);
                contacts.add(contact);
            }


        }
        if (cursor.isClosed() == false)
            cursor.close();

        return contacts;
    }


    private Bitmap getPhoto(ContentResolver cr, long id) {
        Uri contact = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        Uri photoUri = Uri.withAppendedPath(contact, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = cr.query(photoUri, new String[]{ContactsContract.Contacts.Photo.DATA15}, null, null, null);
        Bitmap photo = null;
        if (cursor == null) {
            Log.e("getPhote", "Impossible d'accéder aux photo");
        }

        if (cursor.moveToFirst() == true) {
            byte[] data = cursor.getBlob(0);
            if (data != null) {
                photo = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            }
        }
        cursor.close();
        return photo;
    }

}
