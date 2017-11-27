package com.gnuarmap;

import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;

import static com.gnuarmap.NaverMapActivity.mMapController;
import static com.gnuarmap.NaverMapActivity.mMapLocationManager;
import static com.gnuarmap.NaverMapActivity.mMapView;
import static com.gnuarmap.NaverMapActivity.mMyLocationOverlay;
import static com.gnuarmap.NaverMapActivity.mOverlayManager;

/**
 * 현재위치를 나타내는 클래스
 */

public class GLocation_Setting extends NMapActivity{
    public void startMyLocation() {
        if (mMyLocationOverlay != null) {
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }
            if (mMapLocationManager.isMyLocationEnabled()) {
                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(getApplicationContext(), "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);
                    return;
                }
            }
        }
    }
    public void initialize() {
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapController.setZoomLevel(2);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }
}
