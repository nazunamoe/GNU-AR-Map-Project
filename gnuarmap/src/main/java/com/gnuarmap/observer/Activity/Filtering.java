package com.gnuarmap.observer.Activity;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.gnuarmap.data.Database;
import com.gnuarmap.data.Dataclass;
import com.gnuarmap.mixare.app.DataView;
import com.gnuarmap.mixare.app.Marker;
import com.gnuarmap.mixare.app.SocialMarker;
import com.gnuarmap.observer.NaverMap.NMapCalloutCustomOverlayView;
import com.gnuarmap.observer.NaverMap.NMapPOIflagType;
import com.gnuarmap.R;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import com.gnuarmap.mixare.app.State;


/**
 * 필터링기능을 제공하는 클래스
 */

public class Filtering {
    private int markerId = NMapPOIflagType.PIN;
    private int currentMarker = NMapPOIflagType.SPOT;
    private Context context;
    private static NMapPOIdata poiData;
    private static NMapPOIdataOverlay poiDataOverlay;
    private static NMapPOIdataOverlay poiDataOverlay1;
    private Dataclass database = new Dataclass();

    public Filtering(Context context){
        this.context = context;
    }
    public Filtering(){}

    public void GMarker() {
        int d = database.getSize();
        poiData = new NMapPOIdata(d, NaverMapActivity.mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        for(int i=0;i<database.getSize();i++) {
            Log.d("mixare","dddd");
            poiData.addPOIitem(new NGeoPoint(database.getData(i).getLongitude(), database.getData(i).getLatitude()),database.getData(i).getTitle(),markerId,0);
        }
        poiData.endPOIdata();
        poiDataOverlay1 = NaverMapActivity.mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay1.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();
            if (NaverMapActivity.DEBUG) {
                Log.i(NaverMapActivity.LOG_TAG, "onCalloutClick: title=" + item.getTitle() + item.getTitle());
            }
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {

            if (NaverMapActivity.DEBUG) {
                if (item != null) {
                    Log.i(NaverMapActivity.LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(NaverMapActivity.LOG_TAG, "onFocusChanged: ");
                }
            }
        }
    };
    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

        @Override
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

            if (overlayItem != null) {
                // [TEST] 말풍선 오버레이를 뷰로 설정함
                String title = overlayItem.getTitle();
                if (title != null && title.length() > 5) {
                    return new NMapCalloutCustomOverlayView(context, itemOverlay, overlayItem, itemBounds);
                }
            }

            // null을 반환하면 말풍선 오버레이를 표시하지 않음
            return null;
        }

    };

    public void CurrentLocation(Double longitude, Double latitude, Context ctx, int first){
        NMapPOIdata poiData = new NMapPOIdata(1, NaverMapActivity.mMapViewerResourceProvider, true);
        if(first == 0){
            poiData.addPOIitem(new NGeoPoint(longitude,latitude),ctx.getString(R.string.My_location),currentMarker,0);
            poiData.endPOIdata();
            poiDataOverlay = NaverMapActivity.mOverlayManager.createPOIdataOverlay(poiData, null);
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        }else{
            poiDataOverlay.removeAllPOIdata();
            this.CurrentLocation(longitude,latitude,ctx,0);
        }
    }

    public void Searching(int num){
        int d = database.getSize();
        NMapPOIdata poiData = new NMapPOIdata(d, NaverMapActivity.mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        String number = Integer.toString(num);
        SocialMarker marker = database.getMarker(number);
        poiData.addPOIitem(new NGeoPoint(marker.getLongitude(), marker.getLatitude()), marker.getTitle() ,markerId, 0);
        State state = State.getInstance();
        state.marker = marker;
        poiData.endPOIdata();
        poiDataOverlay = NaverMapActivity.mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }
}
