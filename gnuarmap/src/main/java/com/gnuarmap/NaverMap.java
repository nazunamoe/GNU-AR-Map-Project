package com.gnuarmap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;

import com.gnuarmap.State;
import com.gnuarmap.R;
import com.nhn.android.maps.maplib.NGeoPoint;

public class NaverMap extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "mUusvsrwEZf9uxFtJ5Se";// 애플리케이션 클라이언트 아이디 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        State state = (State)getApplicationContext();
        super.onCreate(savedInstanceState);
        mMapView = new NMapView(this);
        NMapController mMapController=mMapView.getMapController();
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        if(state.getNMapState()==0){
            mMapController.setMapViewMode(mMapView.VIEW_MODE_SATELLITE);

        }else if(state.getNMapState()==1){
            mMapController.setMapViewMode(mMapView.VIEW_MODE_VECTOR);
        }
        mMapController.setMapCenter(new NGeoPoint(128.098160, 35.154008), 11);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Context ctx;
            ctx = this;
            startActivity(new Intent(ctx, MenuActivity.class));
            finish();
        }
        return false;
    }

}
