package org.beginningandroid.interactivetest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//vores DB-helper nedarver fra super klassene SQLiteOpenHelper
public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Database-navn og version
    private static final String DATABASE_NAME = "MinPantAppDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Tabel- og kolonnenavne: Tabellen "Bruger" indholder brugernavn og unikt bruger_id.
    public static final String TABLE_BRUGER = "Bruger";
    public static final String COLUMN_ID = "bruger_id";
    public static final String COLUMN_NAME = "brugernavn";

    //Constructor som kaldes ved oprettelse af databasen
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Kaldes når databasen oprettes første gang, vi onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_BRUGER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //PK og Autoincrement
                COLUMN_NAME + " TEXT UNIQUE)"; //Sørger for unikt brugernavn
        db.execSQL(CREATE_TABLE);
    }

    // Kaldes ved versionsændring (f.eks. opgradering)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRUGER);
        onCreate(db);
    }

    // Indsæt en ny bruger (hvis ikke eksisterende)
    public long insertUser(String brugernavn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, brugernavn);
        return db.insert(TABLE_BRUGER, null, values); // Returnerer ID, eller -1 hvis fejl
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
}
