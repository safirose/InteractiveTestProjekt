package org.beginningandroid.interactivetest;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Abstrak klasse som andre klasse kan arve fra fx. MapActivity
public abstract class BaseActivity extends AppCompatActivity {

    // Metode til opsætte bundmenu
    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Tjekke om knappen findes i layoutet
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(selectedItemId);
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                // Hvis bruger allerede er på den valgte side, sker ingenting
                if (id == selectedItemId) return true;

                // Brugerne trykker på hjem til kortvisning
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
                    finish(); // kun hvis ny aktivitet blev startet
                    return true;
                }
                return false;

            });

        }
    }
}
