package org.beginningandroid.interactivetest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SaldoActivity extends BaseActivity {
    private TextView userNameText, saldoText;
    private ListView receiptList;
    private MyDatabaseHelper dbHelper;
    private String brugernavn;
    private EditText editTextUdbetal;
    private Button buttonUdbetal;
    private double totalSaldo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        // Kalder bundmenu til saldoikonet
        setupBottomNavigation(R.id.nav_saldo);

        // Init views
        userNameText = findViewById(R.id.userNameText);
        saldoText = findViewById(R.id.saldoText);
        receiptList = findViewById(R.id.receiptList);
        editTextUdbetal = findViewById(R.id.editTextUdbetal);
        buttonUdbetal = findViewById(R.id.buttonUdbetal);

        dbHelper = new MyDatabaseHelper(this);

        // Hent brugernavn fra database
        int brugerId = getIntent().getIntExtra("brugerid", -1);
        String brugernavn = dbHelper.hentBrugernavnFraId(brugerId);
        userNameText.setText(brugernavn + "s Saldo");

        // Hent kvitteringer og udregn saldo
        List<Kvittering> kvitteringer = dbHelper.hentAlleKvitteringer();
        double samletIndtjent = dbHelper.beregnTotalSaldo();        // SUM fra Kvittering
        double udbetalt = dbHelper.beregnTotalUdbetaling();         // SUM fra Udbetaling
        totalSaldo = samletIndtjent - udbetalt;

        saldoText.setText("Saldo: " + totalSaldo + " kr.");

        // Vis kvitteringer i ListView
        List<String> visning = new ArrayList<>();
        for (Kvittering k : kvitteringer) {
            visning.add(k.toDisplayString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, visning);
        receiptList.setAdapter(adapter);

        // Håndter klik på udbetal-knap
        buttonUdbetal.setOnClickListener(v -> {
            String input = editTextUdbetal.getText().toString().trim();

            if (input.isEmpty()) {
                Toast.makeText(this, "Indtast et beløb", Toast.LENGTH_SHORT).show();
                return;
            }

            double ønsketBeløb = Double.parseDouble(input);
            if (ønsketBeløb > totalSaldo) {
                Toast.makeText(this, "Beløbet overstiger din saldo!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertUdbetaling(ønsketBeløb, brugerId);
                totalSaldo -= ønsketBeløb;
                saldoText.setText("Saldo: " + totalSaldo + " kr.");
                Toast.makeText(this, "Udbetalt: " + ønsketBeløb + " kr.", Toast.LENGTH_SHORT).show();
                editTextUdbetal.setText("");
            }
        });
    }
}
