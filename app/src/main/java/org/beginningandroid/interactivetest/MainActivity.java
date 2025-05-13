package org.beginningandroid.interactivetest;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
//Bruges til at lave en Android Activity
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//Bruges til at danne vores OSM kort bibliotek
import org.osmdroid.config.Configuration;
//Indholder forskellige kort layouts
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//Klasse til at håndtere geopoint (tilføje punkter til kortet)
import org.osmdroid.util.GeoPoint;
//Klasse til at fremvise kortet i appen
import org.osmdroid.views.MapView;
//Klasse til at tilføje markører til kortet
import org.osmdroid.views.overlay.Marker;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.mlkit.vision.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Definerer vores hoved aktivitet
public class MainActivity extends AppCompatActivity {
 private static final int REQUEST_CODE = 22;
    Button bntScan, btnHistory;
    ImageView imageView;
    TextView resultText;
    ReceiptDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bntScan = findViewById(R.id.btnkamera_id);
        btnHistory = findViewById(R.id.btn_history);
        imageView = findViewById(R.id.imageview1);
        resultText = findViewById(R.id.result_text);
        dbHelper = new ReceiptDatabaseHelper(this);


        bntScan.setOnClickListener(v ->  {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,REQUEST_CODE);

        });
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
        startActivity(intent);
    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Bitmap photo  = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            scanTextFromImage(photo);
        }  else {
            Toast.makeText(this,"Annuleret",Toast.LENGTH_SHORT).show();
        }
            super.onActivityResult(requestCode, resultCode, data);
    }
    private void scanTextFromImage(Bitmap bitmap){
        InputImage image  = InputImage.fromBitmap(bitmap,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image).addOnSuccessListener(visionText->{
            String rawText = visionText.getText();
            parseAndSavePantbon (rawText);
        }).addOnFailureListener(e->{
            Toast.makeText(this,"Fejl i scanning",Toast.LENGTH_SHORT).show();
        });

    }
    private void parseAndSavePantbon(String rawText){
        Pattern totalPattern = Pattern.compile("\"(?m)^\\\\s*(\\\\d{1,4}[.,]\\\\d{2})\\\\s*$\"");
        Matcher totalMatcher = totalPattern.matcher(rawText);

        String total = "Ukendt";
        while (totalMatcher.find()){
            total = totalMatcher.group(1).replace(",",".");
        }


        Pattern bottlePattern  = Pattern.compile("(\\d+)\\s+Flasker?\\s+à\\s+(\\d+[.,]\\d{2})");
        Matcher bottleMatcher = bottlePattern.matcher(rawText);
        StringBuilder bottleSummary = new StringBuilder();
        while (bottleMatcher.find()){
            bottleSummary.append(bottleMatcher.group(1)).append("x").append(bottleMatcher.group(2).replace(",",".")).append("\n");
        }
        Pattern datePattern  = Pattern.compile("(\\d{2}:\\d{2})\\s+(\\d{2}-[A-Z]{3}-\\d{4})");
        Matcher dateMatcher = datePattern.matcher(rawText);
        String time = "Ukendt", date = "Ukendt";
        if(dateMatcher.find()){
            time = dateMatcher.group(1);
            date = dateMatcher.group(2);
        }
        dbHelper.insertReceipt(total,date,time,bottleSummary.toString());

        resultText.setText("Dato:"+date+"\nTid: " + time + "\nTotal: " + total + "\n\nFlasker:\n" + bottleSummary);

    }

}

//deklarer et kort objekt
 /*   private MapView map;
    @Override
    //onCreate se lifecycle for app activities!
    protected void onCreate(Bundle savedInstanceState) {
        //Kalder vores superklasse
        super.onCreate(savedInstanceState);
        //Indlæser layout filen fra res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        // Konfigurer osmdroid (OpenStreetMap biblioteket)
        Configuration.getInstance().setUserAgentValue(NOTIFICATION_SERVICE);

        // Initialiser kortet
        map = findViewById(R.id.map); //Finder kortet i layout filen (fra activity_main.xml), gennem ID
        map.setTileSource(TileSourceFactory.MAPNIK); // Sætter layout til Standard OpenStreetMap layout
        map.setMultiTouchControls(true); // Aktiver zoom og panorering, gennem multitouch

        //  Sæt kortets startposition til København
        GeoPoint startPoint = new GeoPoint(55.6761, 12.5683);
        //Kan ændre zoom i starten
        map.getController().setZoom(15.0);
        //Sætter centrum på startpunktet
        map.getController().setCenter(startPoint);

        //Test, prøver at tilføje flere markører/punkter til kortet
        GeoPoint supermarket1 = new GeoPoint(55.6760, 12.5680);

        // Tilføj et markør til kortet
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle("København");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        Marker marker2 = new Marker(map);
        marker2.setPosition(supermarket1);
        marker2.setTitle("Aarhus");
        marker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker2);


        // Gør København-markøren interaktiv
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Intent intent = new Intent(MainActivity.this, MarkerDetailActivity.class);
                intent.putExtra("location_name", "København");
                startActivity(intent);
                return true; // Returnerer true for at indikere, at vi håndterer klik-handlingen
            }
        });

        marker2.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Intent intent = new Intent(MainActivity.this, MarkerDetailActivity.class);
                intent.putExtra("location_name", "Aarhus");
                startActivity(intent);
                return true;
            }
        });


    }

}*/

