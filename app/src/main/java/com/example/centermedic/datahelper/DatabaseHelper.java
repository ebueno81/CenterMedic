package com.example.centermedic.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.centermedic.clases.SedeDTO;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    // Table for login data
    private static final String LOGIN_TABLE_NAME = "login_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";

    // Table for location data
    private static final String LOCATION_TABLE_NAME = "location_table";
    private static final String LOC_COL_1 = "ID";
    private static final String LOC_COL_2 = "LATITUDE";
    private static final String LOC_COL_3 = "LONGITUDE";
    private static final String LOC_COL_4 = "NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create login table
        db.execSQL("CREATE TABLE " + LOGIN_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");

        // Create location table
        db.execSQL("CREATE TABLE " + LOCATION_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LATITUDE TEXT, LONGITUDE TEXT, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
        onCreate(db);
    }

    // Methods for login table
    public boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        long result = db.insert(LOGIN_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + LOGIN_TABLE_NAME, null);
    }

    public boolean updateData(String id, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        db.update(LOGIN_TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LOGIN_TABLE_NAME, "ID = ?", new String[]{id});
    }

    public Cursor getData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + LOGIN_TABLE_NAME + " WHERE USERNAME = ?", new String[]{username});
    }

    // Methods for location table
    public void insertLocation(String latitude, String longitude, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LOCATION_TABLE_NAME); // Clear previous entries
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOC_COL_2, latitude);
        contentValues.put(LOC_COL_3, longitude);
        contentValues.put(LOC_COL_4, name);
        db.insert(LOCATION_TABLE_NAME, null, contentValues);
    }

    public SedeDTO getLastLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCATION_TABLE_NAME + " ORDER BY " + LOC_COL_1 + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String latitude = cursor.getString(cursor.getColumnIndexOrThrow(LOC_COL_2));
            String longitude = cursor.getString(cursor.getColumnIndexOrThrow(LOC_COL_3));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(LOC_COL_4));
            cursor.close();
            return new SedeDTO(latitude, longitude, name);
        }
        cursor.close();
        return null;
    }
}