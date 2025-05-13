package org.beginningandroid.interactivetest;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (bottomNav != null) {
            bottomNav.setSelectedItemId(selectedItemId);

            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == selectedItemId) return true;

                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, MapActivity.class));
                } else if (id == R.id.nav_scan) {
                    startActivity(new Intent(this, CameraActivity.class));
                }
                // Tilføjer SaldoActivity senere når denne laves.

                finish(); // Luk aktiviteten
                return true;
            });
        }
    }
}
