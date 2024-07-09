package com.example.centermedic.datahelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "search.db";
    private static final String TABLE_NAME = "search_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "SEARCH_TEXT";

    public SearchDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SEARCH_TEXT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSearchText(String searchText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, searchText);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateSearchText(String searchText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, searchText);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{"1"});
        return result > 0;
    }

    public String getLastSearchText() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SEARCH_TEXT FROM " + TABLE_NAME + " WHERE ID = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String searchText = cursor.getString(cursor.getColumnIndex(COL_2));
            cursor.close();
            return searchText;
        }
        return "";
    }
}
