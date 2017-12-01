package com.gnuarmap.observer.Activity;

import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;

/**
 * 현재위치를 나타내는 클래스
 */

public class GLocation_Setting extends NMapActivity{
    public void startMyLocation() {
        if (NaverMapActivity.mMyLocationOverlay != null) {
            if (!NaverMapActivity.mOverlayManager.hasOverlay(NaverMapActivity.mMyLocationOverlay)) {
                NaverMapActivity.mOverlayManager.addOverlay(NaverMapActivity.mMyLocationOverlay);
            }
            if (NaverMapActivity.mMapLocationManager.isMyLocationEnabled()) {
                NaverMapActivity.mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = NaverMapActivity.mMapLocationManager.enableMyLocation(true);
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
        NaverMapActivity.mMapView.setClickable(true);
        NaverMapActivity.mMapView.setEnabled(true);
        NaverMapActivity.mMapView.setFocusable(true);
        NaverMapActivity.mMapController.setZoomLevel(2);
        NaverMapActivity.mMapView.setFocusableInTouchMode(true);
        NaverMapActivity.mMapView.requestFocus();
    }
}
