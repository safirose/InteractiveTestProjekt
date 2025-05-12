package org.beginningandroid.interactivetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private Button buttonLogin;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Husk: layoutfil skal hedde activity_login.xml

        // Initialiser komponenter
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonLogin = findViewById(R.id.buttonLogin);
        dbHelper = new MyDatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String brugernavn = editTextUsername.getText().toString().trim();

                if (brugernavn.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Indtast brugernavn", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hvis brugeren ikke findes i databasen, opret den
                if (!dbHelper.userExists(brugernavn)) {
                    long id = dbHelper.insertUser(brugernavn);
                    Toast.makeText(LoginActivity.this, "Ny bruger oprettet med ID: " + id, Toast.LENGTH_SHORT).show();
                } else {
                    int id = dbHelper.getUserId(brugernavn);
                    Toast.makeText(LoginActivity.this, "Bruger fundet. ID: " + id, Toast.LENGTH_SHORT).show();
                }

                // Gå videre til MapActivity og send brugernavn med
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                intent.putExtra("brugernavn", brugernavn); // Du kan også sende brugerID hvis ønsket
                startActivity(intent);
                finish();
            }
        });
    }
}
