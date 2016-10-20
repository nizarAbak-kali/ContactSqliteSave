package fr.p8.m2ise.contactsqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nizar on 19/10/2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ContactDB";
    // Books table name
    private static final String TABLE_CONTACTS = "contacts";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "nom";

    private static final String[] COLUMNS = {KEY_ID,KEY_IMAGE,KEY_NAME};


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_CONTACT_TABLE = "CREATE TABLE "+TABLE_CONTACTS+" ( " +
                KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_IMAGE+" BLOB, "+
                KEY_NAME+" TEXT )";

        Log.d("OnCreate",CREATE_CONTACT_TABLE);
        // create books table
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACTS);

        this.onCreate(db);
    }

    public void addContacts(Contact contact){
        Log.d("addContact", contact.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, contact.getThumbnail()); // get title
        values.put(KEY_NAME, contact.getName()); // get author
        db.insert(TABLE_CONTACTS, null,values); // key/value -> keys = column names/ values = column values

        db.close();
    }
    public int updateContact(Contact contact){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, contact.getThumbnail()); // get title
        values.put(KEY_NAME, contact.getName()); // get author
        String s  = KEY_NAME;
        int i  = db.update(TABLE_CONTACTS, values,s+=" = ?", new String[]{contact.getName()}); // key/value -> keys = column names/ values = column values

        db.close();
        return i ;
    }

}
