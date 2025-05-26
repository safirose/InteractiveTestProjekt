package org.beginningandroid.interactivetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Database navn og version
    public static final String DATABASE_NAME = "MinPantAppDatabase.db";
    public static final int DATABASE_VERSION = 3;

    // Bruger-tabel
    public static final String TABLE_BRUGER = "Bruger";
    public static final String COLUMN_BRUGER_ID = "bruger_id";
    public static final String COLUMN_BRUGERNAVN = "brugernavn";

    // Kvittering-tabel
    public static final String TABLE_KVITTERING = "Kvittering";
    public static final String COLUMN_KVITTERING_ID = "id";
    public static final String COLUMN_KVITTERING_BRUGER_ID = "bruger_id";
    public static final String COLUMN_LOKATION_ID = "lokation_id";
    public static final String COLUMN_ANTAL_A = "antal_type_a";
    public static final String COLUMN_ANTAL_B = "antal_type_b";
    public static final String COLUMN_ANTAL_C = "antal_type_c";
    public static final String COLUMN_TIDSPUNKT = "tidspunkt";
    public static final String COLUMN_DATO = "dato";

    // Udbetaling-tabel
    public static final String TABLE_UDBETALING = "Udbetaling";
    public static final String COLUMN_UDBETALING_ID = "id";
    public static final String COLUMN_UDBETALING_BRUGER_ID = "bruger_id";
    public static final String COLUMN_BELOEB = "beløb";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBruger = "CREATE TABLE IF NOT EXISTS " + TABLE_BRUGER + " (" +
                COLUMN_BRUGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRUGERNAVN + " TEXT UNIQUE)";
        db.execSQL(createBruger);

        String createKvittering = "CREATE TABLE IF NOT EXISTS " + TABLE_KVITTERING + " (" +
                COLUMN_KVITTERING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_KVITTERING_BRUGER_ID + " INTEGER, " +
                COLUMN_LOKATION_ID + " INTEGER, " +
                COLUMN_ANTAL_A + " INTEGER, " +
                COLUMN_ANTAL_B + " INTEGER, " +
                COLUMN_ANTAL_C + " INTEGER, " +
                COLUMN_TIDSPUNKT + " TEXT, " +
                COLUMN_DATO + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_KVITTERING_BRUGER_ID + ") REFERENCES " + TABLE_BRUGER + "(" + COLUMN_BRUGER_ID + ") ON DELETE CASCADE)";
        db.execSQL(createKvittering);

        String createUdbetaling = "CREATE TABLE IF NOT EXISTS " + TABLE_UDBETALING + " (" +
                COLUMN_UDBETALING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_UDBETALING_BRUGER_ID + " INTEGER, " +
                COLUMN_BELOEB + " REAL, " +
                COLUMN_DATO + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_UDBETALING_BRUGER_ID + ") REFERENCES " + TABLE_BRUGER + "(" + COLUMN_BRUGER_ID + ") ON DELETE CASCADE)";
        db.execSQL(createUdbetaling);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KVITTERING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UDBETALING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRUGER);
        onCreate(db);
    }

    public long insertUser(String brugernavn) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRUGERNAVN, brugernavn);
        return db.insert(TABLE_BRUGER, null, values);
    }

    public int getUserId(String brugernavn) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BRUGER,
                new String[]{COLUMN_BRUGER_ID},
                COLUMN_BRUGERNAVN + "=?",
                new String[]{brugernavn},
                null, null, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRUGER_ID));
        }
        cursor.close();
        return id;
    }

    public boolean userExists(String brugernavn) {
        return getUserId(brugernavn) != -1;
    }

    public void insertKvittering(Kvittering k, int brugerId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KVITTERING_BRUGER_ID, brugerId);
        values.put(COLUMN_LOKATION_ID, k.lokationId);
        values.put(COLUMN_ANTAL_A, k.antalTypeA);
        values.put(COLUMN_ANTAL_B, k.antalTypeB);
        values.put(COLUMN_ANTAL_C, k.antalTypeC);
        values.put(COLUMN_TIDSPUNKT, k.tidspunkt);
        values.put(COLUMN_DATO, k.dato);
        db.insert(TABLE_KVITTERING, null, values);
        db.close();
    }

    public List<Kvittering> hentAlleKvitteringer() {
        List<Kvittering> kvitteringer = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KVITTERING, null);

        if (cursor.moveToFirst()) {
            do {
                Kvittering k = new Kvittering();
                k.lokationId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOKATION_ID));
                k.antalTypeA = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANTAL_A));
                k.antalTypeB = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANTAL_B));
                k.antalTypeC = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANTAL_C));
                k.tidspunkt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIDSPUNKT));
                k.dato = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATO));
                kvitteringer.add(k);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return kvitteringer;
    }

    public double beregnTotalSaldo() {
        double total = 0.0;
        for (Kvittering k : hentAlleKvitteringer()) {
            total += k.getSamletBeloeb();
        }
        return total;
    }

    public void insertUdbetaling(double beloeb, int brugerId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UDBETALING_BRUGER_ID, brugerId);
        values.put(COLUMN_BELOEB, beloeb);
        values.put(COLUMN_DATO, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_UDBETALING, null, values);
        db.close();
    }

    public double beregnTotalUdbetaling() {
        double total = 0.0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_BELOEB + ") FROM " + TABLE_UDBETALING, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public String hentBrugernavnFraId(int brugerId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_BRUGER,
                new String[]{COLUMN_BRUGERNAVN},
                COLUMN_BRUGER_ID + "=?",
                new String[]{String.valueOf(brugerId)},
                null, null, null);

        String brugernavn = "Ukendt";
        if (cursor.moveToFirst()) {
            brugernavn = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRUGERNAVN));
        }
        cursor.close();
        return brugernavn;
    }
}
