package org.beginningandroid.interactivetest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SaldoActivity extends BaseActivity {

    private TextView userNameText, saldoText;
    private ListView receiptList;
    private MyDatabaseHelper dbHelper;
    private String brugerNavn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        // Kalder bundmenu til saldoikonet
        setupBottomNavigation(R.id.nav_saldo);

        userNameText = findViewById(R.id.userNameText);
        saldoText = findViewById(R.id.saldoText);
        receiptList = findViewById(R.id.receiptList);

        dbHelper = new MyDatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("pantapp", MODE_PRIVATE);
        brugerNavn = prefs.getString("brugernavn", "Ukendt");


        userNameText.setText(brugerNavn + "s Saldo");

        // Henter alle kvitteringer
        List<Kvittering> kvitteringer = dbHelper.hentAlleKvitteringer();
        double total = dbHelper.beregnTotalSaldo();

        // Konverterer kvitteringer til tekst til visning
        List<String> visning = new ArrayList<>();
        for (Kvittering k : kvitteringer) {
            visning.add(k.toDisplayString());
        }

        // Viser kvitteringer i ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, visning);
        receiptList.setAdapter(adapter);

        // Viser samlet saldo
        saldoText.setText("Saldo: " + total + " kr.");
    }
}
