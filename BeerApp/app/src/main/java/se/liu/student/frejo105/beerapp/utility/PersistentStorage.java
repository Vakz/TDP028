package se.liu.student.frejo105.beerapp.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PersistentStorage extends SQLiteOpenHelper {

    /* https://developer.android.com/guide/topics/data/data-storage.html */
    private static final String DATABASE_NAME = "BeerStorage";
    private static final int DATABASE_VERSION = 2;
    private static final String TESTED_TABLE_NAME = "Tested";
    private static final String TESTED_CREATE = "CREATE TABLE " + TESTED_TABLE_NAME
        + " (id INTEGER PRIMARY KEY);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TESTED_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public PersistentStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean writeId(int id) {
        boolean success = false;
        if (id < 0) return success;
        SQLiteDatabase sqld = getWritableDatabase();
        try {
            sqld.execSQL("INSERT OR IGNORE INTO " + TESTED_TABLE_NAME + " VALUES (" + id + ")");
            success = true;
        }
        catch (Exception e) {

        }
        sqld.close();
        return success;
    }

    public boolean deleteId(int id) {
        SQLiteDatabase sqld = getWritableDatabase();
        if (id < 0) return false;
        boolean success = sqld.delete(TESTED_TABLE_NAME, "id = " + id, null) == 1;
        sqld.close();
        return success;
    }

    public List<Integer> getTestedIds()  {
        ArrayList<Integer> ids = new ArrayList<>();
        SQLiteDatabase sqld = getReadableDatabase();
        Cursor c = sqld.query(TESTED_TABLE_NAME, null, null, null, null, null, null, null);
        while (c.moveToNext()) {
            ids.add(c.getInt(0));
        }
        c.close();
        sqld.close();
        return ids;
    }

    public boolean isTested(int id) {
        SQLiteDatabase sqld = getReadableDatabase();
        Cursor c = sqld.query(TESTED_TABLE_NAME, null, "id = " + id, null, null, null, null, null);
        boolean contained = c.getCount() == 1;
        c.close();
        sqld.close();
        return contained;
    }
}
