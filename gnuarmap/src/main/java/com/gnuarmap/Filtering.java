package com.gnuarmap;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import static com.gnuarmap.data.convert.DB.Hardness;
import static com.gnuarmap.data.convert.DB.Latitude;
import static com.gnuarmap.data.convert.DB.Title;
import static com.gnuarmap.NaverMapActivity.DEBUG;
import static com.gnuarmap.NaverMapActivity.LOG_TAG;
import static com.gnuarmap.NaverMapActivity.db;
import static com.gnuarmap.NaverMapActivity.mMapViewerResourceProvider;
import static com.gnuarmap.NaverMapActivity.mOverlayManager;


/**
 * 필터링기능을 제공하는 클래스
 */

public class Filtering {
    int markerId = NMapPOIflagType.PIN;
    Context context;
    int d = db.Count();
    public static NMapPOIdata poiData;
    public static NMapPOIdataOverlay poiDataOverlay;
    public static NMapPOIdataOverlay poiDataOverlay1;

    public Filtering(Context context){
        this.context = context;
    }

    public void GMarker() {
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        for(int i=0;i<d;i++) {
            poiData.addPOIitem(new NGeoPoint(Hardness.get(i), Latitude.get(i)),Title.get(i),markerId,0);
        }
        poiData.endPOIdata();
        poiDataOverlay1 = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay1.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    public void print() {
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(9), Latitude.get(9)), Title.get(9),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(15), Latitude.get(15)), Title.get(15),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(19), Latitude.get(19)), Title.get(19),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(34), Latitude.get(34)), Title.get(34),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(40), Latitude.get(40)), Title.get(40),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(50), Latitude.get(50)), Title.get(50),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(66), Latitude.get(66)), Title.get(66),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(71), Latitude.get(71)), Title.get(71),markerId,0);
            poiData.addPOIitem(new NGeoPoint(Hardness.get(73), Latitude.get(73)), Title.get(73),markerId,0);
        poiData.endPOIdata();
        poiDataOverlay1 = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay1.setOnStateChangeListener(onPOIdataStateChangeListener);
    }


    public void market() {
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        NMapPOIdata poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(3), Latitude.get(3)), Title.get(3),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(32), Latitude.get(32)), Title.get(32),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(44), Latitude.get(44)), Title.get(44),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(69), Latitude.get(69)), Title.get(69),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(75), Latitude.get(75)), Title.get(75),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(76), Latitude.get(76)), Title.get(76),markerId,0);
        poiData.endPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    public void vending() {
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        NMapPOIdata poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        for(int i=9;i==12;i++) {
            poiData.addPOIitem(new NGeoPoint(Hardness.get(i), Latitude.get(i)), Title.get(i), markerId, 0);
        }
        poiData.addPOIitem(new NGeoPoint(Hardness.get(16), Latitude.get(16)), Title.get(16),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(17), Latitude.get(17)), Title.get(17),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(24), Latitude.get(24)), Title.get(24),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(25), Latitude.get(25)), Title.get(25),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(26), Latitude.get(26)), Title.get(26),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(33), Latitude.get(33)), Title.get(33),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(34), Latitude.get(34)), Title.get(34),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(40), Latitude.get(40)), Title.get(40),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(43), Latitude.get(43)), Title.get(43),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(44), Latitude.get(44)), Title.get(44),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(53), Latitude.get(53)), Title.get(53),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(58), Latitude.get(58)), Title.get(58),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(65), Latitude.get(65)), Title.get(65),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(80), Latitude.get(80)), Title.get(80),markerId,0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(81), Latitude.get(81)), Title.get(81),markerId,0);
        poiData.endPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }


    public void ATM() {
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        NMapPOIdata poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(3), Latitude.get(3)), Title.get(3) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(21), Latitude.get(21)), Title.get(21) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(35), Latitude.get(35)), Title.get(35) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(43), Latitude.get(43)), Title.get(43) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(44), Latitude.get(44)), Title.get(44) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(69), Latitude.get(69)), Title.get(69) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(84), Latitude.get(84)), Title.get(84) ,markerId, 0);
        poiData.endPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    public void Restaurant() {
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        NMapPOIdata poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(3), Latitude.get(3)), Title.get(3) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(21), Latitude.get(21)), Title.get(21) ,markerId, 0);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(44), Latitude.get(44)), Title.get(44) ,markerId, 0);

        poiData.endPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    public void Searching(int num){
        Double a = db.Latitude(0);
        Double b = db.Hardness(0);
        String c = db.Title(0);
        int d = db.Count();
        NMapPOIdata poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        poiData.addPOIitem(new NGeoPoint(Hardness.get(num), Latitude.get(num)), Title.get(num) ,markerId, 0);

        poiData.endPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }
    public final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle() + item.getTitle());
            }

            // [[TEMP]] handle a click event of the callout
            Toast.makeText(context, "onCalloutClick: " +  item.getTitle() , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {

            if (DEBUG) {
                if (item != null) {
                    Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(LOG_TAG, "onFocusChanged: ");
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
}
