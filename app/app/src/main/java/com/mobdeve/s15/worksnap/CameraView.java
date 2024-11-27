package com.mobdeve.s15.worksnap;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CameraView extends AppCompatActivity {

    private TextureView textureView;
    private ImageButton flipCameraButton;
    private ImageButton captureButton;
    private Button retakeButton;
    private Button uploadButton;
    private boolean isOkarun = true;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private ImageReader imageReader;
    private Bitmap capturedBitmap;
    private FirebaseAuth mAuth;
    private Handler handler = new Handler(); // Declare the handler globally
    private Runnable checkStateRunnable;
    private List<TimePeriod> timePeriods = new ArrayList<>(); ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_view);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        mAuth = FirebaseAuth.getInstance();
        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(textureListener);
        flipCameraButton = findViewById(R.id.flipCamera);
        captureButton = findViewById(R.id.capture);
        retakeButton = findViewById(R.id.btn_retake);
        uploadButton = findViewById(R.id.btn_upload);
        Log.d(TAG, "DEBUG1");
        // Fetch time range from Firestore
        fetchTimeRangeFromFirestore(captureButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        flipCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleImage();
                animateButton(flipCameraButton);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                animateButton(captureButton);
            }
        });

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPreview();
                retakeButton.setVisibility(View.GONE);
                uploadButton.setVisibility(View.GONE);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (capturedBitmap != null) {
                    uploadImageToFirebase(capturedBitmap);
                }
                startPreview();
                retakeButton.setVisibility(View.GONE);
                uploadButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove all pending callbacks to prevent memory leaks
        handler.removeCallbacks(checkStateRunnable);
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
    };

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0]; // Use the first camera
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            Size[] jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG);
            int width = jpegSizes[0].getWidth();
            int height = jpegSizes[0].getHeight();

            imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            imageReader.setOnImageAvailableListener(imageAvailableListener, null);

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                manager.openCamera(cameraId, stateCallback, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void startPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(textureView.getWidth(), textureView.getHeight());
            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    try {
                        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        try {
            if (cameraDevice == null) return;
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());
            cameraCaptureSession.capture(captureRequestBuilder.build(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            image.close();

            capturedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Stop the camera preview if needed
            stopPreview();

            // Display the captured image on the TextureView
            displayCapturedImage(bitmap);
            retakeButton.setVisibility(View.VISIBLE);
            uploadButton.setVisibility((View.VISIBLE));
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraDevice != null) {
            cameraDevice.close();
        }
    }

    private void displayCapturedImage(Bitmap bitmap) {
        if (textureView.isAvailable()) {
            Canvas canvas = textureView.lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(bitmap, 0, 0, null); // Draw the captured image on the TextureView
                textureView.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void stopPreview() {
        if (cameraCaptureSession != null) {
            try {
                cameraCaptureSession.stopRepeating();
                cameraCaptureSession.abortCaptures();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        // Get Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        String imageId = UUID.randomUUID().toString(); // Generates a unique ID
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                // 3. Perform Reverse Geocoding to get the address
                String address = getAddressFromLocation(location);

                if (address != null) {
                    // 4. Convert Bitmap to Byte Array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Compress the image (JPEG, PNG supported)
                    byte[] data = baos.toByteArray();
                    // 5. Get a reference to Firebase Storage
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imageRef = storageRef.child("images/" + imageId + ".jpg");

                    // 6. Upload the image to Firebase Storage
                    UploadTask uploadTask = imageRef.putBytes(data);

                    // Listen for upload success/failure
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // 7. Get the download URL after successful upload
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            // 8. Prepare the data for Firestore
                            Map<String, Object> imageData = new HashMap<>();
                            imageData.put("user_id", uid);
                            imageData.put("imageLink", downloadUrl);
                            imageData.put("location", address);
                            imageData.put("created_at", new Timestamp(new Date()));
                            imageData.put("verified" ,false);
                            imageData.put("rejected", false);
                            // 9. Save the URL and other info to Firestore
                            db.collection("images").document(imageId).set(imageData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Image uploaded with address!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to upload: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }).addOnFailureListener(e -> {
                            // Error: Failed to get the download URL
                            e.printStackTrace();
                        });
                    }).addOnFailureListener(e -> {
                        // Error: Failed to upload the image
                        e.printStackTrace();
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // Compress to JPEG
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Combine the address parts into a single string
                return address.getAddressLine(0); // Full address (e.g., "123 Main St, City, Country")
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if unable to fetch address
    }

    private void fetchTimeRangeFromFirestore(ImageButton button) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get time_start and time_end from Firestore
                        String timeStart = documentSnapshot.getString("work_start"); // e.g., "08:00"
                        String timeEnd = documentSnapshot.getString("work_end");     // e.g., "15:40"
                        // Generate 10-minute intervals
                        generateTimePeriods(timeStart, timeEnd);
                        logTimePeriods();
                        logCurrentTime();
                        // Start the periodic button state check
                        startPeriodicCheck(button);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "DEBUGFAIL");
                    e.printStackTrace();
                });
    }

    private void generateTimePeriods(String timeStart, String timeEnd) {
        if (!timeStart.isEmpty() && !timeEnd.isEmpty()){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date startDate = sdf.parse(timeStart);
                Date endDate = sdf.parse(timeEnd);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);

                timePeriods.clear(); // Clear the list in case it's reused

                while (calendar.getTime().before(endDate)) {
                    Date periodStart = calendar.getTime();

                    // Add 10 minutes for the end of the interval
                    calendar.add(Calendar.MINUTE, 10);
                    Date periodEnd = calendar.getTime();

                    // Add the period to the list
                    timePeriods.add(new TimePeriod(
                            getHour(periodStart), getMinute(periodStart),
                            getHour(periodEnd), getMinute(periodEnd)
                    ));

                    // Skip ahead by at least one hour
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    private void startPeriodicCheck(ImageButton button) {
        checkStateRunnable = new Runnable() {
            @Override
            public void run() {
                checkButtonState(button);
                handler.postDelayed(this, 60000); // Schedule the next check after 60 seconds
            }
        };

        handler.post(checkStateRunnable);
    }

    // Method to check and update button state
    private void checkButtonState(ImageButton button) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Check if the current time falls within any time period
        boolean isWithinTime = false;
        for (TimePeriod period : timePeriods) {
            if (isTimeWithinRange(hour, minute, period.startHour, period.startMinute, period.endHour, period.endMinute)) {
                isWithinTime = true;
                break;
            }
        }

        // Update button state
        button.setEnabled(isWithinTime);
        button.setAlpha(isWithinTime ? 1.0f : 0.5f);
    }

    // Utility method for time range checking
    private boolean isTimeWithinRange(int currentHour, int currentMinute, int startHour, int startMinute, int endHour, int endMinute) {
        int currentTime = currentHour * 60 + currentMinute;
        int startTime = startHour * 60 + startMinute;
        int endTime = endHour * 60 + endMinute;

        return currentTime >= startTime && currentTime <= endTime;
    }

    public void logTimePeriods() {
        for (TimePeriod period : timePeriods) {
            String periodString = String.format("Start: %02d:%02d, End: %02d:%02d",
                    period.startHour, period.startMinute,
                    period.endHour, period.endMinute);
            Log.d("TimePeriods", periodString);
        }
    }

    public void logCurrentTime() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();

        // Format the time (HH:mm format, 24-hour clock)
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(calendar.getTime());

        // Log the current time
        Log.d("CurrentTime", "Current Time: " + currentTime);
    }

    private void toggleImage() {
//        if (isOkarun) {
//            imageView.setImageResource(R.drawable.momo);
//        } else {
//            imageView.setImageResource(R.drawable.okarun);
//        }
//        isOkarun = !isOkarun;
    }

    private void animateButton(View button) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                button,
                PropertyValuesHolder.ofFloat("scaleX", 0.8f),
                PropertyValuesHolder.ofFloat("scaleY", 0.8f));
        scaleDown.setDuration(100);

        scaleDown.setRepeatCount(1);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }
}

// Class to represent a time period
class TimePeriod {
    int startHour, startMinute, endHour, endMinute;

    TimePeriod(int startHour, int startMinute, int endHour, int endMinute) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }
}