package org.beginningandroid.interactivetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// vores DB-helper arver fra superklassen SQLiteOpenHelper
public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Navn og version på Databasen
    private static final String DATABASE_NAME = "MinPantAppDatabase.db";
    private static final int DATABASE_VERSION = 3;

    // Tabel- og kolonnenavne: Tabellen "Bruger" indholder brugernavn og unikt bruger_id.
    public static final String TABLE_BRUGER = "Bruger";
    public static final String COLUMN_ID = "bruger_id";
    public static final String COLUMN_NAME = "brugernavn";

    // Constructor som kaldes ved oprettelse af databasen
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Kaldes når databasen oprettes første gang
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_BRUGER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // PK og Autoincrement
                COLUMN_NAME + " TEXT UNIQUE)"; // Sørger for unikt brugernavn
        db.execSQL(CREATE_TABLE);

        // Opretter tabel til kvitteringer
        String CREATE_KVITTERING_TABLE = "CREATE TABLE IF NOT EXISTS Kvittering (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lokation_id INTEGER, " +
                "antal_type_a INTEGER, " +
                "antal_type_b INTEGER, " +
                "antal_type_c INTEGER, " +
                "tidspunkt TEXT, " +
                "dato TEXT)";
        db.execSQL(CREATE_KVITTERING_TABLE);
    }

    // Kaldes ved versionsændring (f.eks. opgradering)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRUGER);
        db.execSQL("DROP TABLE IF EXISTS Kvittering");
        onCreate(db);
    }

    // Indsæt en ny bruger (hvis ikke eksisterende)
    public long insertUser(String brugernavn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, brugernavn);
        return db.insert(TABLE_BRUGER, null, values); // Returnerer ID eller -1 hvis fejl
    }

    // Hent brugerens ID ud fra brugernavn
    public int getUserId(String brugernavn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BRUGER,
                new String[]{COLUMN_ID},
                COLUMN_NAME + "=?",
                new String[]{brugernavn},
                null, null, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        return id;
    }

    // Tjek om bruger eksisterer
    public boolean userExists(String brugernavn) {
        return getUserId(brugernavn) != -1;
    }

    // Indsæt ny kvittering i databasen
    public void insertKvittering(Kvittering k) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lokation_id", k.lokationId);
        values.put("antal_type_a", k.antalTypeA);
        values.put("antal_type_b", k.antalTypeB);
        values.put("antal_type_c", k.antalTypeC);
        values.put("tidspunkt", k.tidspunkt);
        values.put("dato", k.dato);
        db.insert("Kvittering", null, values);
        db.close();
    }

    // Hent alle kvitteringer fra databasen
    public List<Kvittering> hentAlleKvitteringer() {
        List<Kvittering> kvitteringer = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Kvittering", null);

        if (cursor.moveToFirst()) {
            do {
                Kvittering k = new Kvittering();
                k.lokationId = cursor.getInt(cursor.getColumnIndexOrThrow("lokation_id"));
                k.antalTypeA = cursor.getInt(cursor.getColumnIndexOrThrow("antal_type_a"));
                k.antalTypeB = cursor.getInt(cursor.getColumnIndexOrThrow("antal_type_b"));
                k.antalTypeC = cursor.getInt(cursor.getColumnIndexOrThrow("antal_type_c"));
                k.tidspunkt = cursor.getString(cursor.getColumnIndexOrThrow("tidspunkt"));
                k.dato = cursor.getString(cursor.getColumnIndexOrThrow("dato"));
                kvitteringer.add(k);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return kvitteringer;
    }

    // Udregner samlet beløb (saldo)
    public double beregnTotalSaldo() {
        double total = 0.0;
        List<Kvittering> kvitteringer = hentAlleKvitteringer();
        for (Kvittering k : kvitteringer) {
            total += k.getSamletBeloeb();
        }
        return total;
    }
}
