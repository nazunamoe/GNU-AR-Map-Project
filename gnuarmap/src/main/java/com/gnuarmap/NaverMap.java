package com.gnuarmap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;

import com.gnuarmap.State;
import com.gnuarmap.R;
import com.nhn.android.maps.maplib.NGeoPoint;

public class NaverMap extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "mUusvsrwEZf9uxFtJ5Se";// 애플리케이션 클라이언트 아이디 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String set;
        NMapLocationManager location = new NMapLocationManager(this);

        Intent intent = getIntent();
        try{
            set = intent.getExtras().getString("set");
        }
        catch(NullPointerException e){
            set = "False";
        }
        State state = (State)getApplicationContext();
        super.onCreate(savedInstanceState);
        mMapView = new NMapView(this);
        NMapController mMapController=mMapView.getMapController();
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapController.setMapViewMode(mMapView.VIEW_MODE_SATELLITE);
        if(state.getNMapState()==0){
            mMapController.setMapViewMode(mMapView.VIEW_MODE_SATELLITE);
        }else if(state.getNMapState()==1){
            mMapController.setMapViewMode(mMapView.VIEW_MODE_VECTOR);
        }

        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        mMapController.setMapCenter(new NGeoPoint(128.098160, 35.154008), 11);
        switch(set){
            case "True":{
                mMapController.setMapCenter(new NGeoPoint(128.606599, 35.257661), 11);
                NGeoPoint building = new NGeoPoint(128.098160,35.154008);
                NMapOverlayItem marker = new NMapOverlayItem(building, "Test","Test",getResources().getDrawable(R.drawable.school_default));
                marker.setMarker(getResources().getDrawable(R.drawable.school_default));
                Log.v("mixare","Marker!");
                marker.setVisibility(NMapOverlayItem.VISIBLE);
               // 마커 액티비티에서 네이버 지도로 넘어왔을 때 핸들러, 해당 좌표만 따와서 보여준다.
            }
            case "False":{
                // 메뉴에서 네이버 지도로 넘어왔을 때 핸들러. 필터링에 의거한 마커를 전부 보여준다.
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        Intent intent = getIntent();
        String value = "";
        try{
            value = intent.getExtras().getString("Return");
        }
        catch (NullPointerException e){
            value = "";
        }
        Log.v("mixare",value);
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Context ctx;
            ctx = this;
            switch(value) {
                case "True": {
                    finish();
                    return false;
                }
                default: {
                    startActivity(new Intent(ctx, MenuActivity.class));
                    finish();
                    return false;
                }

            }

        }
        return false;
    }
}
