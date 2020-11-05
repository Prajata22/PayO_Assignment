package com.applex.payo_assignment.Helpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="PayO_database.db";
    private static final String TABLE_NAME = "PayO_list_data";
    private static final String col2 = "NAME";
    private static final String col3 = "EMAIL";
    private static final String col4 = "CONTACT";
    private static final String col5 = "ADDRESS";
    private static final String col6 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + col2 + " TEXT, " + col3 + " TEXT, " + col4 + " TEXT, " + col5 + " TEXT, " + col6 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addData(String name, String email, String contact, String address, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, name);
        contentValues.put(col3, email);
        contentValues.put(col4, contact);
        contentValues.put(col5, address);
        contentValues.put(col6, password);

        long result = db.insert(TABLE_NAME,null, contentValues);
        return result != -1;
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
    }

    public ArrayList<String> getData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.query(TABLE_NAME, null, "EMAIL=?", new String[]{email}, null, null, null);
        if(cursor.getCount() < 1)
            return null;
        cursor.moveToFirst();

        ArrayList<String> data = new ArrayList<>();
        data.add(0, cursor.getString(cursor.getColumnIndex(col2)));
        data.add(1, cursor.getString(cursor.getColumnIndex(col3)));
        data.add(2, cursor.getString(cursor.getColumnIndex(col4)));
        data.add(3, cursor.getString(cursor.getColumnIndex(col5)));
        data.add(4, cursor.getString(cursor.getColumnIndex(col6)));

        return data;
    }
}