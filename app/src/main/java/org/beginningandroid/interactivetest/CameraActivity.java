package org.beginningandroid.interactivetest;
//Implementere android klasser + ML-kit klasser
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

//Arver fra BaseActivity mht. bundmenu
public class CameraActivity extends BaseActivity {

    //Viser live kamera
    private PreviewView previewView;
    //Barcode scanner
    private BarcodeScanner scanner;
    //Boolean til kun 1 scanning
    private boolean scanned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //Kalder bundmenu til scan iconet
        setupBottomNavigation(R.id.nav_scan);

        previewView = findViewById(R.id.previewView);
        scanner = BarcodeScanning.getClient();

        //tjekker hvorvidt brugeren har givet adgang til kameraet
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    100);
            return; // Vent med at starte kamera
        }

        startCamera();
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

                // Analyse (stregkodelæsning)
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

    private void analyzeImage(@NonNull ImageProxy imageProxy) {
        if (scanned) {
            imageProxy.close();
            return;
        }

        @SuppressWarnings("UnsafeOptInUsageError")
        InputImage image = InputImage.fromMediaImage(
                imageProxy.getImage(),
                imageProxy.getImageInfo().getRotationDegrees()
        );

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (!barcodes.isEmpty()) {
                        String raw = barcodes.get(0).getRawValue();
                        scanned = true;

                        runOnUiThread(() -> {
                            Toast.makeText(this, "Scannet: " + raw, Toast.LENGTH_LONG).show();
                            Log.d("SCANNING", "Indhold: " + raw);
                        });
                    }
                    imageProxy.close();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    imageProxy.close();
                });
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }
}
