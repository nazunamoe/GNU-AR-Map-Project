package com.gnuarmap.app;

/**
 * 안드로이드 카메라 2 API를 사용하는 클래스
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2Surface extends CameraSurface {


    private static final int DEFAULT_CAM_WIDTH = 1920;
    private static final int DEFAULT_CAM_HEIGHT = 1080;

    Size[] sizes = null;

    MixView mixViewActivity;
    SurfaceHolder holder;
    CameraDevice camera;
    CameraManager cameraManager;
    private String cameraId;
    private CameraCaptureSession activeSession;


    Camera2Surface(Context context) {
        super(context);
        try {
            mixViewActivity = (MixView) context;

            holder = getHolder();
            holder.addCallback(this);

            cameraManager = (CameraManager) mixViewActivity.getSystemService(Context.CAMERA_SERVICE);
        } catch (Exception ex) {

        }

        cameraId = null;
        sizes = null;
        try {
            String[] ids = cameraManager.getCameraIdList();
            for (String curCameraId : ids) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(curCameraId);
                Integer camLensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (camLensFacing != null && camLensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = curCameraId;
                    StreamConfigurationMap configs = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    sizes = configs.getOutputSizes(SurfaceHolder.class);
                    break;
                }
            }
        } catch (CameraAccessException ex) {
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            if (activeSession != null) {
                activeSession.close();
                activeSession = null;
            }
            camera.close();
            camera = null;
        }
        holder.removeCallback(this);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // preview form factor
        float ff = (float) w / h;

        // holder for the best form factor and size
        float bff = 0;
        int bestw = 0;
        int besth = 0;

        // we look for the best preview size, it has to be the closest
        // to the
        // screen form factor, and be less wide than the screen itself
        // the latter requirement is because the HTC Hero with update
        // 2.1 will
        // report camera preview sizes larger than the screen, and it
        // will fail
        // to initialize the camera
        // other devices could work with previews larger than the screen
        // though
        if (sizes != null) {
            for (Size curSize : sizes) {
                // current form factor
                float cff = (float) curSize.getWidth() / curSize.getHeight();
                // check if the current element is a candidate to replace
                // the best match so far
                // current form factor should be closer to the bff
                // preview width should be less than screen width
                // preview width should be more than current bestw
                // this combination will ensure that the highest resolution
                // will win
                if ((ff - cff <= ff - bff) && (curSize.getWidth() <= w)
                        && (curSize.getWidth() >= bestw)) {
                    bff = cff;
                    bestw = curSize.getWidth();
                    besth = curSize.getHeight();
                }
            }
        }
        // Some Samsung phones will end up with bestw and besth = 0
        // because their minimum preview size is bigger then the screen
        // size.
        // In this case, we use the default values: 480x320
        if ((bestw == 0) || (besth == 0)) {
            bestw = 1080;
            besth = 1920;
        }

        holder.setFixedSize(1920, 1080);
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this.mixViewActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    Camera2Surface.this.camera = cameraDevice;
                    createPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {

                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int i) {

                }
            }, null);
        } catch (CameraAccessException ex) {
        }
    }

    private void createPreviewSession() {
        List<Surface> outputs = new ArrayList<>();
        outputs.add(holder.getSurface());
        try {
            final CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            builder.addTarget(holder.getSurface());
            camera.createCaptureSession(outputs, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.setRepeatingRequest(builder.build(), null, null);
                        Camera2Surface.this.activeSession = cameraCaptureSession;
                    } catch (CameraAccessException ex) {
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                }
            }, null);
        } catch (CameraAccessException ex) {
        }
    }
}