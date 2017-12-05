package com.gnuarmap.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import com.gnuarmap.R;
import com.gnuarmap.naver.NMapCalloutCustomOverlayView;
import com.gnuarmap.naver.NMapPOIflagType;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.Set;


/**
 * 필터링기능을 제공하는 클래스
 */

public class NaverMapMarker extends AppCompatActivity{
    private int markerId = NMapPOIflagType.PIN;
    private int currentMarker = NMapPOIflagType.SPOT;
    private Context context;
    private static NMapPOIdata poiData;
    private static NMapPOIdataOverlay poiDataOverlay;
    private static NMapPOIdataOverlay poiDataOverlay1;
    private Dataclass dataclass = new Dataclass();
    public State state = State.getInstance();
    private NGeoPoint current;
    public NaverMapMarker(Context context, NGeoPoint current){
        this.context = context;
        this.current = current;
    }

    public void GMarker() {
        int d = dataclass.getSize();
        poiData = new NMapPOIdata(d, NaverMapActivity.mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        for(int i = 0; i< dataclass.getSize(); i++) {
            poiData.addPOIitem(new NGeoPoint(dataclass.getData(i).getLongitude(), dataclass.getData(i).getLatitude()), dataclass.getData(i).getTitle(),markerId,0);
        }
        poiData.endPOIdata();
        poiDataOverlay1 = NaverMapActivity.mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay1.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            final String title = item.getTitle();
            final NGeoPoint point = item.getPoint();
            if (NaverMapActivity.DEBUG) {
                Log.i(NaverMapActivity.LOG_TAG, "onCalloutClick: title=" + item.getTitle() + item.getTitle());
            }
            final AlertDialog.Builder ad = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
            ad.setTitle(item.getTitle());
            ad.setTitle(item.getTitle());
            ad.setItems(menus, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(makeURL(current,"current",point,title)));
                        try{
                           context.startActivity(in);
                        }catch (ActivityNotFoundException e){
                            Toast.makeText(context, R.string.kakaomaperror, Toast.LENGTH_SHORT).show();
                            in = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map"));
                            context.startActivity(in);
                        }
                    }
                    else if(which == 1){
                        String url = dataclass.getMarkerviaTitle(title).getURL();
                        url = url.substring(8);
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(in);
                    }
                }
            });
            ad.setNegativeButton(R.string.negetive,null);
            ad.show();
        }

        private String makeURL(NGeoPoint start, String starttitle, NGeoPoint end, String endtitle){
            String result = "";
            switch(state.api){
                case 0:{ //네이버
                   result = "https://m.map.naver.com/directions/?menu=route&sname="+starttitle+"&sx="+start.getLongitude()+"&sy="+start.getLatitude()+"&ename"+endtitle+"&ex="+end.getLongitude()+"&ey="+end.getLatitude()+"&pathType=0&showMap=true";
                    break;
                }
                case 1:{ //카카오앱
                    result = "daummaps://route?sp="+start.getLatitude()+","+start.getLongitude()+"&ep="+end.getLatitude()+","+end.getLongitude()+"&by=FOOT"; // 다음 길찾기 앱 api
                    break;
                }
                case 2:{ //카카오웹
                    result = "http://map.daum.net/link/to/"+endtitle+","+end.getLatitude()+","+end.getLongitude();  // 다음 길찾기 웹 api
                    break;
                }
                default :{
                    result = "https://m.map.naver.com/directions/?menu=route&sname="+starttitle+"&sx="+start.getLongitude()+"&sy="+start.getLatitude()+"&ename"+endtitle+"&ex="+end.getLongitude()+"&ey="+end.getLatitude()+"&pathType=0&showMap=true";
                }
            }
            return result;
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
    final String[] menus = new String[]{"길찾기","건물 정보"};
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
        int d = dataclass.getWholeSize();
        NMapPOIdata poiData = new NMapPOIdata(d, NaverMapActivity.mMapViewerResourceProvider, true);
        poiData.beginPOIdata(d);
        String number = Integer.toString(num);
        SocialMarker marker = dataclass.getMarker(number);
        poiData.addPOIitem(new NGeoPoint(marker.getLongitude(), marker.getLatitude()), marker.getTitle() ,markerId, 0);
        state.marker = marker;
        poiData.endPOIdata();
        poiDataOverlay = NaverMapActivity.mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }
}
