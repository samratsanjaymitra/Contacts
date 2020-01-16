package com.example.contactsapplication;

/**
 * Created by samrat on 14/1/20.
 */


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DbCreation extends SQLiteOpenHelper {


    SQLiteDatabase db = this.getWritableDatabase();


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MyDB";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String PHONE = "phone";


    public DbCreation(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                + NAME + " varchar(200) /*PRIMARY KEY*/," + IMAGE + " varchar(400),"
                + PHONE + " varchar(12)" + ")";

        db.execSQL(create_table);


    }

    public String recordsNumber() {


        String records = "";

        String selectQuery = "SELECT  count(*) FROM " + TABLE_CONTACTS;


        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                records = cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return records;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }


    public ArrayList<ContactsPOJO> getContacts() {


        ArrayList<ContactsPOJO> contacts_list = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                ContactsPOJO contacts = new ContactsPOJO();
                contacts.setName(cursor.getString(0));
                contacts.setImage(cursor.getString(1));
                contacts.setPhone(cursor.getString(2));

                contacts_list.add(contacts);
            } while (cursor.moveToNext());
        }


        return contacts_list;


    }

    public void addContact(String name, String image, String phone) {

        String add = "INSERT INTO " + TABLE_CONTACTS + "\n" +
                "(name,image,phone)\n" +
                "VALUES \n" +
                "(?, ?, ?);";


        db.execSQL(add, new String[]{name, image, phone});


    }

    public void deleteContact(String name) {

        String deleteData = "delete from " + TABLE_CONTACTS + " where name='" + name +
                "';";


        db.execSQL(deleteData);


    }

    public void deleteAllContact() {

        String deleteData = "delete from " + TABLE_CONTACTS;


        db.execSQL(deleteData);


    }


}


