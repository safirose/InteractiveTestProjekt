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
                    Intent intent = new Intent(this, MapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); // kun hvis ny aktivitet blev startet
                    return true;
                } else if (id == R.id.nav_scan) {
                    startActivity(new Intent(this, CameraActivity.class));
                    finish(); // kun hvis ny aktivitet blev startet
                    return true;
                }
                else if (id == R.id.nav_saldo) {
                    startActivity(new Intent(this, SaldoActivity.class));
                    finish();
                    return true;
                }
                return false;

            });

        }
    }
}
