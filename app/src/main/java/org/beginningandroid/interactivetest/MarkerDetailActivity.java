package org.beginningandroid.interactivetest;

import android.os.Bundle;
import android.widget.TextView;

public class MarkerDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        // Hent data fra Intent (det navn, vi sendte fra markøren)
        String locationName = getIntent().getStringExtra("location_name");

        // Find TextView og opdater teksten med markørens navn
        TextView textView = findViewById(R.id.textView);
        textView.setText("Adresse: " + locationName + "\nÅbningstiderne er: 7:00-22");
    }
}
