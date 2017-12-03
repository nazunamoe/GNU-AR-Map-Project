package com.gnuarmap;

/**
 * Created by nazunamoe on 2017-12-02.
 */

import android.util.Log;

import org.mixare.lib.marker.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * DataHandler is the model which provides the Marker Objects with its data.
 * DataHandler is also the Factory for new Marker objects.
 */
public class DataHandler {

    // 완성된 마커 리스트
    private java.util.List<org.mixare.lib.marker.Marker> markerList = new ArrayList<org.mixare.lib.marker.Marker>();

    public void addMarkers(java.util.List<org.mixare.lib.marker.Marker> markers) {
        Dataclass data = new Dataclass();

        for (int i = 0; i < data.getSize(); i++) {
            markerList.add(data.List.get(i));
        }
        /**
         * 독립된 데이터 프로세서 대신 이 부분에 marker 배열에 필요한 마커 데이터를 직접 등록한다.
         */

        Log.d(MixView.TAG, "Marker count: " + markerList.size());
    }

    // 마커 리스트 정렬
    public void sortMarkerList() {
        Collections.sort(markerList);
    }

    // 위치를 이용해서 현재 위치와의 거리 측정
    public void updateDistances(android.location.Location location) {
        for (org.mixare.lib.marker.Marker ma : markerList) {
            float[] dist = new float[3];
            android.location.Location.distanceBetween(ma.getLatitude(), ma.getLongitude(), location.getLatitude(), location.getLongitude(), dist);
            ma.setDistance(dist[0]);
        }
    }

    // 활성화 상태 업데이트...??
    public void updateActivationStatus(MixContext mixContext) {

        Hashtable<Class, Integer> map = new Hashtable<Class, Integer>();

        for (org.mixare.lib.marker.Marker ma : markerList) {

            Class<? extends org.mixare.lib.marker.Marker> mClass = ma.getClass();
            map.put(mClass, (map.get(mClass) != null) ? map.get(mClass) + 1 : 1);

            boolean belowMax = (map.get(mClass) <= ma.getMaxObjects());
            //boolean dataSourceSelected = mixContext.isDataSourceSelected(ma.getDatasource());

            ma.setActive((belowMax));
        }
    }

    // 장소가 변함에 따라서 마커 리스트를 정렬함
    public void onLocationChanged(android.location.Location location) {
        updateDistances(location);
        sortMarkerList();
        for (org.mixare.lib.marker.Marker ma : markerList) {
            ma.update(location);
        }
    }

    /**
     * @deprecated Nobody should get direct access to the list
     */
    public List<Marker> getMarkerList() {
        return markerList;
    }

    /**
     * @deprecated Nobody should get direct access to the list
     */
    public void setMarkerList(List<org.mixare.lib.marker.Marker> markerList) {
        this.markerList = markerList;
    }

    public int getMarkerCount() {
        return markerList.size();
    }

    public org.mixare.lib.marker.Marker getMarker(int index) {
        return markerList.get(index);
    }
}