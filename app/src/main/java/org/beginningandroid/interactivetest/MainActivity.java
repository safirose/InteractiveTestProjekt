package org.beginningandroid.interactivetest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    static

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🛠️ Konfigurer osmdroid
        Configuration.getInstance().setUserAgentValue(NOTIFICATION_SERVICE);

        // 🗺️ Initialiser kortet
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // Standard OpenStreetMap layout
        map.setMultiTouchControls(true); // Aktiver zoom og panorering

        // 📍 Sæt kortets startposition til København
        GeoPoint startPoint = new GeoPoint(55.6761, 12.5683);
        map.getController().setZoom(10.0);
        map.getController().setCenter(startPoint);

        // 📌 Tilføj en markør (pin) på kortet
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle("København");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
    }

    private class APPLICATION_ID {
    }
}
