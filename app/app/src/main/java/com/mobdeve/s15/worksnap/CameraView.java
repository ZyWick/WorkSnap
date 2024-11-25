package com.mobdeve.s15.worksnap;

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
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_view);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(textureListener);
        flipCameraButton = findViewById(R.id.flipCamera);
        captureButton = findViewById(R.id.capture);
        retakeButton = findViewById(R.id.btn_retake);
        uploadButton = findViewById(R.id.btn_upload);

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
            }
        });
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
        // Convert Bitmap to ByteArray
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Compress as JPEG
//        byte[] data = baos.toByteArray();
//
//        // Get a reference to Firebase Storage
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference imagesRef = storageRef.child("images/captured_image_" + System.currentTimeMillis() + ".jpg");
//
//        // Upload the image
//        UploadTask uploadTask = imagesRef.putBytes(data);
//        uploadTask.addOnSuccessListener(taskSnapshot -> {
//            // Get the download URL
//            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                // Handle success - e.g., display a toast or log the URL
//                Toast.makeText(CameraActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
//                Log.d("FirebaseStorage", "Download URL: " + uri.toString());
//            });
//        }).addOnFailureListener(e -> {
//            // Handle failure
//            Toast.makeText(CameraActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        });
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