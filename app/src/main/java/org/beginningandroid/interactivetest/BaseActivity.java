package org.beginningandroid.interactivetest;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    public void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (bottomNav != null) {
            bottomNav.setSelectedItemId(selectedItemId);

            // Hent bruger-id fra den aktivitet, vi står i nu
            int brugerId = getIntent().getIntExtra("brugerid", -1);

            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == selectedItemId) return true;

                Intent intent = null;

                if (id == R.id.nav_home) {
                    intent = new Intent(this, MapActivity.class);
                } else if (id == R.id.nav_scan) {
                    intent = new Intent(this, CameraActivity.class);
                } else if (id == R.id.nav_saldo) {
                    intent = new Intent(this, SaldoActivity.class);
                }

                if (intent != null) {
                    intent.putExtra("brugerid", brugerId); // ✅ Send bruger-ID videre
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

                return false;
            });
        }
    }
}
