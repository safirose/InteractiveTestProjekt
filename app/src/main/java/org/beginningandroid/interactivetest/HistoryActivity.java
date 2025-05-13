package org.beginningandroid.interactivetest;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity{
ReceiptDatabaseHelper dbHelper;
TextView historyText;

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);

    dbHelper = new ReceiptDatabaseHelper(this);
    historyText = findViewById(R.id.history_text);

    StringBuilder history = new StringBuilder();
    Cursor cursor = dbHelper.getAllReceipts();

    while (cursor.moveToNext()){
        history.append("Dato:").append(cursor.getString(2)).append("Tid:").append(cursor.getString(3)).append("\nTotal:").append(cursor.getString(1)).append("\nFlasker:\n").append(cursor.getString(4)).append("\n---\n");
    }
        cursor.close();
    historyText.setText(history.toString());

    }
}

