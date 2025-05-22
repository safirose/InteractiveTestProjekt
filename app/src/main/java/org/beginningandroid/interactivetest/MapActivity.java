package org.beginningandroid.interactivetest;

import android.content.Intent;
// Biblotek til at implementere bottom navigation
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
// Bruges til at lave en Android Activity
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
// Bruges til at danne vores OSM kort bibliotek
import org.osmdroid.config.Configuration;
// Indholder forskellige kort layouts
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
// Klasse til at håndtere geopoint (tilføje punkter til kortet)
import org.osmdroid.util.GeoPoint;
// Klasse til at fremvise kortet i appen
import org.osmdroid.views.MapView;
// Klasse til at tilføje markører til kortet
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends BaseActivity {

    // Deklarerer et kort objekt
    private MapView map;

  /*  Intent intent = new Intent(LoginActivity.this, MapActivity.class);
    intent.putExtra("brugernavn", brugernavn);
    intent.putExtra("brugerid", brugerId);
    startActivity(intent);
    finish();

   */
    @Override
    // onCreate – se lifecycle for app activities!
    public void onCreate(Bundle savedInstanceState) {
        // Kalder vores superklasse
        super.onCreate(savedInstanceState);
        // Indlæser layout filen fra res/layout/activity_map.xml
        setContentView(R.layout.activity_map);

        // Kalder den fælles metode fra BaseActivity for at aktivere bundmenu
        setupBottomNavigation(R.id.nav_home);

        // Konfigurer osmdroid (OpenStreetMap biblioteket)
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Initialiser kortet
        map = findViewById(R.id.map); // Finder kortet i layout filen (fra activity_map.xml), gennem ID
        map.setTileSource(TileSourceFactory.MAPNIK); // Sætter layout til Standard OpenStreetMap layout
        map.setMultiTouchControls(true); // Aktiver zoom og panorering, gennem multitouch

        // Sæt kortets startposition til København
        GeoPoint startPoint = new GeoPoint(55.68617, 12.55077);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);

        // Test, prøver at tilføje flere markører/punkter til kortet
        GeoPoint supermarket1 = new GeoPoint(55.68546, 12.55686);

        // Tilføj markører
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle("Netto, Blågårdsgade 26");
        //marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(resizeDrawable(R.drawable.netto_logo, 80, 80));
        map.getOverlays().add(marker);

        Marker marker2 = new Marker(map);
        marker2.setPosition(supermarket1);
        marker2.setTitle("Netto, Rantzausgade 21");
        //marker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker2.setIcon(resizeDrawable(R.drawable.netto_logo, 80, 80));
        map.getOverlays().add(marker2);

        // Gør markører interaktive
        marker.setOnMarkerClickListener((clickedMarker, mapView) -> {
            Intent intent = new Intent(MapActivity.this, MarkerDetailActivity.class);
            intent.putExtra("location_name", "Netto, Rantzausgade 21");
            startActivity(intent);
            return true;
        });

        marker2.setOnMarkerClickListener((clickedMarker, mapView) -> {
            Intent intent = new Intent(MapActivity.this, MarkerDetailActivity.class);
            intent.putExtra("location_name", "Netto, Blågårdsgade 26");
            startActivity(intent);
            return true;
        });
    }
    private Drawable resizeDrawable (int drawableId, int width, int height) {
        Drawable original = ContextCompat.getDrawable(this, drawableId);

        if (original == null) return null;

        Bitmap originalBitmap = Bitmap.createBitmap(
                original.getIntrinsicWidth(),
                original.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(originalBitmap);
        original.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        original.draw(canvas);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

        return new BitmapDrawable(getResources(), scaledBitmap);
    }
}



