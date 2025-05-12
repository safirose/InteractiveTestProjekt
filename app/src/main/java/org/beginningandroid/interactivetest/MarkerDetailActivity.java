package org.beginningandroid.interactivetest;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MarkerDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        // Hent data fra Intent (det navn, vi sendte fra markøren)
        String locationName = getIntent().getStringExtra("location_name");

        // Find TextView og opdater teksten med markørens navn
        TextView textView = findViewById(R.id.textView);
        textView.setText("Du klikkede på: " + locationName + "\n Åbningstiderne er: 7:00-22");

    }
}
