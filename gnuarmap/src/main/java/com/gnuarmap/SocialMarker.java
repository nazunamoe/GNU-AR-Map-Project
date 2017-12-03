package com.gnuarmap;

import android.graphics.Bitmap;

import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.TextObj;

import java.text.DecimalFormat;

/**
 * The SocialMarker class represents a marker, which contains data from
 * sources like twitter etc. Social markers appear at the top of the screen
 * and show a small logo of the source.
 *
 * @author hannes
 *
 */
public class SocialMarker extends Marker {

    public String NUM;
    public String filter1;
    public String filter2[];
    public int id;

    public String getFlag;
    public SocialMarker(String id, String title, double latitude, double longitude,
                        double altitude, String URL, int type, int color ,String flag, String filtering1, String filtering2[], int IDW) {
        super(id, title, latitude, longitude, altitude, URL, type, color, id);
        this.NUM = id;
        this.id = IDW;
        this.getFlag = flag;
        this.filter1 = filtering1;
        this.filter2 = filtering2;
    }

    @Override
    public void update(android.location.Location curGPSFix) {

        //0.35 radians ~= 20 degree
        //0.85 radians ~= 45 degree
        //minAltitude = sin(0.35)
        //maxAltitude = sin(0.85)

        // we want the social markers to be on the upper part of
        // your surrounding sphere
        double altitude = curGPSFix.getAltitude();
        //+Math.sin(0.35)*distance+Math.sin(0.4)*(distance/(MixView.getDataView().getRadius()*1000f/distance));
        mGeoLoc.setAltitude(altitude);
        super.update(curGPSFix);

    }

    @Override
    public void draw(PaintScreen dw) {

        // 텍스트 블록을 그린다
        drawTextBlock(dw);

        // 보여지는 상황이라면
        if (isVisible) {
            float maxHeight = Math.round(dw.getHeight() / 10f) + 1;	// 최대 높이 계산
            // 데이터 소스의 비트맵 파일을 읽어온다

            Bitmap bitmap = Dataclass.getBitmap(getFlag);
            // 비트맵 파일이 읽혔다면 적절한 위치에 출력
            if(bitmap!=null) {
                dw.paintBitmap(bitmap, cMarker.x - maxHeight/1.5f, cMarker.y - maxHeight/0.6f);
            }
            else {	// 비트맵 파일을 갖지 않는 마커의 경우

                dw.setStrokeWidth(maxHeight / 10f);
                dw.setFill(false);
                dw.paintCircle(cMarker.x, cMarker.y, maxHeight / 1.5f);
            }
        }
    }

    public void drawTextBlock(PaintScreen dw) {
       super.drawTextBlock(dw);
    }

    @Override
    public int getMaxObjects() {
        State state = State.getInstance();
        int max = 0;
        if(state.MoreView){
            max = 30;
        }else{
            max = 15;
        }
        return max;
    }

}