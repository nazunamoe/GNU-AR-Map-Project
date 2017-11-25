package com.gnuarmap;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Toast;

import com.gnuarmap.data.convert.DataBase;
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
    public Filtering filtering = new Filtering();
    public static Search search = new Search();
    private Context context;
    public String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = findViewById(R.id.mapView);
        mMapView.setClientId(CLIENT_ID.CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapController = mMapView.getMapController();
        Intent intent = getIntent();
        name = intent.getStringExtra("num");
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("지도화면");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setLogo(R.drawable.icon);
        toolbar.inflateMenu(R.menu.menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_vector:
                        invalidateMenu();
                        mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
                        Toast.makeText(getApplicationContext(),"일반지도 호출",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_satellite:
                        invalidateMenu();
                        mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
                        Toast.makeText(getApplicationContext(),"위성지도 호출",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_traffic:
                        invalidateMenu();
                        mMapController.setMapViewTrafficMode(!mMapController.getMapViewTrafficMode());
                        Toast.makeText(getApplicationContext(),"실시간교통지도 호출",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_bicycle:
                        invalidateMenu();
                        mMapController.setMapViewBicycleMode(!mMapController.getMapViewBicycleMode());
                        Toast.makeText(getApplicationContext(),"자전거 도로 지도 호출",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_location:
                        mMapController.setZoomLevel(11);
                        gLocation_setting.startMyLocation();
                        Toast.makeText(getApplicationContext(),"현재 위치로 이동",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_search:
                        Intent intent = new Intent(getApplicationContext(),Search.class);
                        startActivityForResult(intent,101);
                        return true;
                    case R.id.action_navermap:
                        mOverlayManager.clearOverlays();
                        atm = false;
                        Vending = false;
                        Print = false;
                        Market = false;
                        Controller = false;
                        Restaurant = false;
                        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.action_poi_data:
                        if(Controller == true){
                            Controller = false;
                        }
                        Toast.makeText(getApplicationContext(),"마커 표시하기",Toast.LENGTH_SHORT).show();
                        filtering.GMarker();
                        return true;

                    case R.id.action_filter_print:
                        if(Controller == false && Print == false){
                            mOverlayManager.clearOverlays();
                            filtering.print();
                            Print = true;
                        }else if(Print == false && Controller == true){
                            Print = true;
                            filtering.GMarker();
                        }
                        else{
                            atm = false;
                            Vending = false;
                            Print = false;
                            Restaurant = false;
                            Market = false;
                            Controller = false;
                            filtering.GMarker();
                        }
                        Toast.makeText(getApplicationContext(),"프린트가 있는 건물의 마커만 보여줌",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.action_filter_market:
                        if(Controller == false&&Market == false) {
                            mOverlayManager.clearOverlays();
                            filtering.market();
                            Market = true;
                        }else if(Market == false && Controller == true){
                            Market = true;
                            filtering.market();
                        }
                        else{
                            atm = false;
                            Vending = false;
                            Market = false;
                            Print = false;
                            Restaurant = false;
                            Controller = false;
                            mOverlayManager.clearOverlays();
                            filtering.GMarker();
                        }
                        Toast.makeText(getApplicationContext(),"매점이 있는 건물의 마커만 보여줌",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.action_filter_vending:
                        if(Controller == false&&Vending == false) {
                            mOverlayManager.clearOverlays();
                            filtering.vending();
                            Vending = true;
                        }else if(Vending == false && Controller == true){
                            Vending = true;
                            filtering.vending();
                        }
                        else{
                            Vending = false;
                            Market = false;
                            Print = false;
                            atm = false;
                            Controller = false;
                            Restaurant = false;
                            mOverlayManager.clearOverlays();
                            filtering.GMarker();
                        }
                        Toast.makeText(getApplicationContext(),"매점이 있는 건물의 마커만 보여줌",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.action_filter_ATM:
                        if(Controller == false&&atm == false) {
                            mOverlayManager.clearOverlays();
                            filtering.ATM();
                            atm = true;
                        }else if(atm == false && Controller == true){
                            atm = true;
                            filtering.ATM();
                        }
                        else{
                            atm = false;
                            Vending = false;
                            Market = false;
                            Print = false;
                            Controller = false;
                            Restaurant = false;
                            mOverlayManager.clearOverlays();
                            filtering.GMarker();
                        }
                        Toast.makeText(getApplicationContext(),"매점이 있는 건물의 마커만 보여줌",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_filter_restaurant:
                        if(Controller == false&&Restaurant == false) {
                            mOverlayManager.clearOverlays();
                            filtering.Restaurant();
                            Restaurant = true;
                        }else if(Restaurant == false && Controller == true){
                            Restaurant = true;
                            filtering.Restaurant();
                        }
                        else{
                            Restaurant = false;
                            atm = false;
                            Vending = false;
                            Market = false;
                            Print = false;
                            Controller = false;
                            mOverlayManager.clearOverlays();
                            filtering.GMarker();
                        }
                        Toast.makeText(getApplicationContext(),"식당이 있는 건물",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });*/

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
            //filtering.Searching(a);
        }
    }


    private void invalidateMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            invalidateOptionsMenu();
        }
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

