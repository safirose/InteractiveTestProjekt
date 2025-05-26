package org.beginningandroid.interactivetest;
//Bruges til at skifte mellem vores aktiviteter
import android.content.Intent;
//Gemmer midlertidige data ved rotation mellem skærmene
import android.os.Bundle;
//Importere grundklasse for vores app
import androidx.appcompat.app.AppCompatActivity;
//Definerer vores MainActivity, der arver fra AppCompatActivity
public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Starter LoginActivity som MainActivity, da dette er vores første aktivitet
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        finish(); // Luk MainActivity så den ikke ligger i baggrunden,
    }
}
