package org.beginningandroid.interactivetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
public class ReceiptDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "recepits.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "pantbon";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_BOTTLES = "bottles";

    private static final String TABLE_CREATE = "CREATE TABLE"+TABLE_NAME+"("+COLUMN_ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+ COLUMN_TOTAL+"TEXT,"+COLUMN_DATE+"TEXT,"+COLUMN_TIME
}
