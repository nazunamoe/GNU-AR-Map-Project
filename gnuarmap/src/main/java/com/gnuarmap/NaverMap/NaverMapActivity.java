package com.gnuarmap.NaverMap;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import com.gnuarmap.data.convert.DataBase;
import com.gnuarmap.Filtering;
import com.gnuarmap.FilteringState;
import com.gnuarmap.MenuActivity;
import com.gnuarmap.MixView;
import com.gnuarmap.R;
import com.gnuarmap.Search;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

public class NaverMapActivity extends NMapActivity {
    public final static String CLIENT_ID = "mUusvsrwEZf9uxFtJ5Se";
    public static final String LOG_TAG = "NMapViewer";
    public static MapContainerView mMapContainerView;
    public static NMapView mMapView;// 지도 화면 View
    public static NMapOverlayManager mOverlayManager;//Overlay 아이템을 관리하는 매니저 변수
    public static NMapViewerResourceProvider mMapViewerResourceProvider; //Resource를 관리하는 변수
    public static NMapLocationManager mMapLocationManager;
    public static NMapMyLocationOverlay mMyLocationOverlay;
    public static NMapCompassManager mMapCompassManager;
    public static NMapController mMapController;

    public static final boolean DEBUG = false;

    public static boolean Print = false;
    public static boolean Market = false;
    public static boolean Controller = false;
    public static boolean Vending = false;
    public static boolean atm = false;
    public static boolean Restaurant = false;


    public static DataBase db = new DataBase();
    public GLocation_Setting gLocation_setting = new GLocation_Setting();
    public Filtering filtering = new Filtering(this);
    public static Search search = new Search();
    private Context context;
    public String name="";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FilteringState state = FilteringState.getInstance();
        mMapView = findViewById(R.id.mapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapController = mMapView.getMapController();
        if(state.NMapState){
            mMapController.setMapViewMode(mMapView.VIEW_MODE_SATELLITE);
        }else{
            mMapController.setMapViewMode(mMapView.VIEW_MODE_VECTOR);
        }
        Intent intent = getIntent();
        name = intent.getStringExtra("num");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("지도화면");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                               @Override
                                               public boolean onMenuItemClick(MenuItem item) {
                                                   switch (item.getItemId()) {
                                                       case R.id.action_location:
                                                           mMapController.setZoomLevel(11);
                                                           gLocation_setting.startMyLocation();
                                                           Toast.makeText(getApplicationContext(),"현재 위치로 이동",Toast.LENGTH_SHORT).show();
                                                           return true;
                                                   }
                                                   return false;
                                               }
                                           });

        gLocation_setting.initialize();
        mMapController.setZoomLevel(11);
        mMapController.setMapCenter(128.103959,35.152751);

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        super.setMapDataProviderListener(onDataProviderListener);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mMapCompassManager = new NMapCompassManager(this);
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
        if(name == null){
            filtering.GMarker();
        }else{
            int a = Integer.parseInt(name);
            filtering.Searching(a);
        }
    }


    private void invalidateMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        Intent intent = getIntent();
        int ret = intent.getIntExtra("return",1);
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Log.d("FAL",""+ret);
            if(ret == 2){
                Context ctx;
                ctx = this;
                startActivity(new Intent(ctx, Search.class));
                finish();
            }
            else if(ret == 1){
                Context ctx;
                ctx = this;
                startActivity(new Intent(ctx, MixView.class));
                finish();
            }
            else if (ret == 0){
                Context ctx;
                ctx = this;
                startActivity(new Intent(ctx, MenuActivity.class));
                finish();
            }
        }
        return false;
    }

    private class MapContainerView extends ViewGroup {
        public MapContainerView(Context context) {
            super(context);
        }
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            final int width = getWidth();
            final int height = getHeight();
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                final int childLeft = (width - childWidth) / 2;
                final int childTop = (height - childHeight) / 2;
                view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }
            if (changed) {
                mOverlayManager.onSizeChanged(width, height);
            }
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int sizeSpecWidth = widthMeasureSpec;
            int sizeSpecHeight = heightMeasureSpec;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);

                if (view instanceof NMapView) {
                    if (mMapView.isAutoRotateEnabled()) {
                        int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
                        sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
                        sizeSpecHeight = sizeSpecWidth;
                    }
                }
                view.measure(sizeSpecWidth, sizeSpecHeight);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint myLocation) {
            if(mMapController != null){
                mMapController.setMapCenter(myLocation);
            }
            else{
            }
            return true;
        }
        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
        }
        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
        }
    };
    private final OnDataProviderListener onDataProviderListener = new OnDataProviderListener() {
        @Override
        public void onReverseGeocoderResponse(NMapPlacemark nMapPlacemark, NMapError nMapError) {
        }
    };
}

