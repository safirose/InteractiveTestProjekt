package org.beginningandroid.interactivetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
public class ReceiptDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recepits.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "pantbon";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_BOTTLES = "bottles";

    private static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TOTAL + "TEXT," +
                    COLUMN_DATE + "TEXT," +
                    COLUMN_TIME + "TEXT," +
                    COLUMN_BOTTLES + "TEXT);";

    public ReceiptDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public void insertReceipt(String total, String date, String time, String bottles){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL,total);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME,time);
        values.put(COLUMN_BOTTLES,bottles);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Cursor getAllReceipts(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,null,null,null,null,null,COLUMN_ID+"DESC");
    }
}
