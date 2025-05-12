package org.beginningandroid.interactivetest;

import static androidx.core.content.ContextCompat.startActivity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
//Bruges til at lave en Android Activity
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

//Definerer vores hoved aktivitet
public class MainActivity extends AppCompatActivity {
 private static final int REQUEST_CODE = 22;
    Button bntpicture;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bntpicture = findViewById(R.id.btnkamera_id);
        imageView = findViewById(R.id.imageview1);

        bntpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bitmap photo  = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        else {
            Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
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

