package com.gnuarmap;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gnuarmap.NaverMap.NMapCalloutCustomOverlayView;
import com.gnuarmap.NaverMap.NMapPOIflagType;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.gnuarmap.data.convert.DataBase;

import static com.gnuarmap.NaverMap.NaverMapActivity.DEBUG;
import static com.gnuarmap.NaverMap.NaverMapActivity.LOG_TAG;
import static com.gnuarmap.NaverMap.NaverMapActivity.db;
import static com.gnuarmap.NaverMap.NaverMapActivity.mMapViewerResourceProvider;
import static com.gnuarmap.NaverMap.NaverMapActivity.mOverlayManager;


/**
 * 필터링기능을 제공하는 클래스
 */

public class Filtering {
    int markerId = NMapPOIflagType.PIN;
    Context context;
    public static NMapPOIdata poiData;
    public static NMapPOIdataOverlay poiDataOverlay;
    public static NMapPOIdataOverlay poiDataOverlay1;
    DataBase database = new DataBase();

    public Filtering(Context context){
        this.context = context;
    }
    public Filtering(){}

    public void GMarker() {
        database.Initialize();
        int d = db.data.getSize();
        poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        for(int i=0;i<database.data.getSize();i++) {
            poiData.addPOIitem(new NGeoPoint(database.data.getData(i).getLongitude(), database.data.getData(i).getLatitude()),database.data.getData(i).getTitle(),markerId,0);
        }
        poiData.endPOIdata();
        poiDataOverlay1 = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay1.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    public final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle() + item.getTitle());
            }
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

    public void Searching(int num){
        int d = database.data.getSize();
        NMapPOIdata poiData = new NMapPOIdata(d, mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        String number = Integer.toString(num);
        SocialMarker marker = database.data.getMarker(number);
        Log.d("UJungMother",""+num);
        poiData.addPOIitem(new NGeoPoint(marker.getLongitude(), marker.getLatitude()), marker.getTitle() ,markerId, 0);

        poiData.endPOIdata();
        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }


}
