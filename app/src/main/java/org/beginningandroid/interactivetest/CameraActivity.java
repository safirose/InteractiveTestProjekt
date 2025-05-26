package org.beginningandroid.interactivetest;
//Import af android klasser + ML-kit klasser
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Arver fra BaseActivity mht. bundmenu
public class CameraActivity extends BaseActivity {

    //Viser live kamera
    private PreviewView previewView;
    //Barcode scanner
    private BarcodeScanner scanner;
    //Boolean til kun 1 scanning, så man ikke kan gentage samme scanning
    private boolean scanned = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //Kalder bundmenu til scan iconet
        setupBottomNavigation(R.id.nav_scan);

        previewView = findViewById(R.id.previewView);

        // initalisere ML Kit scanner
        scanner = BarcodeScanning.getClient();

        //tjekker hvorvidt brugeren har givet adgang til kameraet
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    100);
            // return; // Vent med at starte kamera
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Billedeanalyse --> stregkodescanner
                ImageAnalysis analysis = new ImageAnalysis.Builder().build();
                analysis.setAnalyzer(getExecutor(), this::analyzeImage);

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                );

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    // Analyze af billede for stregkoder
    private void analyzeImage(@NonNull ImageProxy imageProxy) {
        // Hvis allerede scannet, lukkes billedet og retuneres
        if (scanned) {
            imageProxy.close();
            return;
        }

        // konventere kameraet billede til InputImage
        @SuppressWarnings("UnsafeOptInUsageError")
        InputImage image = InputImage.fromMediaImage(
                imageProxy.getImage(),
                imageProxy.getImageInfo().getRotationDegrees()
        );

        // Start scanning af stregkode
        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (!barcodes.isEmpty()) {
                        // Fik scannet noget, hentes data
                        String raw = barcodes.get(0).getRawValue();
                        scanned = true;

                        runOnUiThread(() -> {
                            // 1. Parser stregkoden
                            Kvittering kvit = parseKvitteringskode(raw);


                            // 2. Gemmer i SQLite-databasen
                            MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
                            int brugerId = getIntent().getIntExtra("brugerid", -1);
                            dbHelper.insertKvittering(kvit, brugerId);


                            // 3. Giver feedback til brugeren
                            Toast.makeText(this,
                                    "Kvittering gemt: " + kvit.getSamletBeloeb() + " kr",
                                    Toast.LENGTH_LONG).show();

                            //Måske slette nedstående?
                            Log.d("SCANNING", "Gemte kvittering:\n" + kvit.toDisplayString());
                        });
                    }
                    imageProxy.close();
                });

    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    // Parser stregkoden og returnerer en Kvittering
    private Kvittering parseKvitteringskode(String kode) {
        Kvittering k = new Kvittering();

        // 1. Lokation ID (første tegn)
        k.lokationId = Character.getNumericValue(kode.charAt(0));

        // 2. Flaskekode (mellem index 1 og tidspunkt start)
        Pattern flaskemønster = Pattern.compile("(\\d+[A-C])+");
        Matcher matcher = flaskemønster.matcher(kode.substring(1));

        int flaskeslut = -1;
        if (matcher.find()) {
            flaskeslut = matcher.end() + 1; // matcher arbejder på substring(1)
            String flaskekode = kode.substring(1, flaskeslut);
            k.antalTypeA = extractAntal(flaskekode, 'A');
            k.antalTypeB = extractAntal(flaskekode, 'B');
            k.antalTypeC = extractAntal(flaskekode, 'C');
        }

        // 3. Tidspunkt og dato fra substring(flaskeslut)
        String tidOgDato = kode.substring(flaskeslut); // fx "09:40140425"

        if (!tidOgDato.contains(":") || tidOgDato.length() < 9) {
            k.tidspunkt = "ukendt";
            k.dato = "ukendt";
            return k;
        }

        String[] dele = tidOgDato.split(":");
        if (dele.length != 2) {
            k.tidspunkt = "ukendt";
            k.dato = "ukendt";
            return k;
        }

        // Tager tid og dato fra tekst
        String timer = dele[0];                // "09"
        String minOgDato = dele[1];            // "40140425"

        String minutter = minOgDato.substring(0, 2); // "40"
        String dag = minOgDato.substring(2, 4);      // "14"
        String måned = minOgDato.substring(4, 6);    // "04"
        String år = "20" + minOgDato.substring(6, 8);// "2025"

        k.tidspunkt = timer + ":" + minutter;
        k.dato = dag + "-" + måned + "-" + år;

        return k;
    }

    // Henter antal flasker af en bestemt type (A, B, C)
    private int extractAntal(String str, char type) {
        Pattern p = Pattern.compile("(\\d+)" + type);
        Matcher m = p.matcher(str);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
}
