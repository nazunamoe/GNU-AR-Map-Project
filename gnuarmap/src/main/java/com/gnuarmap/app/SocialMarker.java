package com.gnuarmap.app;

import android.graphics.Bitmap;
import android.util.Log;

import org.mixare.lib.MixContextInterface;
import org.mixare.lib.MixStateInterface;
import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.ScreenLine;
import org.mixare.lib.gui.TextObj;
import org.mixare.lib.render.Camera;
import org.mixare.lib.render.MixVector;

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
    private String URL;
    public String NUM;
    public String filter1;
    public String filter2[];
    public int id;
    private ScreenLine pPt = new ScreenLine();
    private MixVector upV = new MixVector(0, 1, 0);
    private MixVector origin = new MixVector(0, 0, 0);
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
        double altitude = curGPSFix.getAltitude()+Math.sin(0.35)*distance+Math.sin(0.4)*(distance/(MixView.getDataView().getRadius()*1000f/distance));
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

    public boolean fClick(float x, float y, MixContext ctx, State state) {
        boolean evtHandled = false;
        Log.d("mixare","Clicked Marker");

        if (isClickValid(x, y)) {
            evtHandled = state.handleEvent(ctx, URL, this);
        }
        return evtHandled;
    }

    public void drawTextBlock(PaintScreen dw) {

        //TODO: grandezza cerchi e trasparenza
        float maxHeight = Math.round(dw.getHeight() / 10f) + 1;

        //TODO: change textblock only when distance changes
        String textStr="";

        double d = distance;
        DecimalFormat df = new DecimalFormat("@#");
        if(d<1000.0) {
            textStr = title + " ("+ df.format(d) + "m)";
        }
        else {
            d=d/1000.0;
            textStr = title + " (" + df.format(d) + "km)";
        }

        textBlock = new TextObj(textStr, Math.round(maxHeight / 2f) + 1, 800, dw, underline);

        if (isVisible) {
            float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y, signMarker.x, signMarker.y);

            txtLab.prepare(textBlock);

            dw.setStrokeWidth(1f);
            dw.setFill(true);
            dw.paintObj(txtLab, signMarker.x - txtLab.getWidth()
                    / 2, signMarker.y + maxHeight, currentAngle + 90, 1);
        }

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

    private void cCMarker(MixVector originalPoint, Camera viewCam, float addX, float addY) {

        // Temp properties
        MixVector tmpa = new MixVector(originalPoint);
        MixVector tmpc = new MixVector(upV);
        tmpa.add(locationVector); //3
        tmpc.add(locationVector); //3
        tmpa.sub(viewCam.lco); //4
        tmpc.sub(viewCam.lco); //4
        tmpa.prod(viewCam.transform); //5
        tmpc.prod(viewCam.transform); //5

        MixVector tmpb = new MixVector();
        viewCam.projectPoint(tmpa, tmpb, addX, addY); //6
        cMarker.set(tmpb); //7
        viewCam.projectPoint(tmpc, tmpb, addX, addY); //6
        signMarker.set(tmpb); //7
    }

    private void calcV(Camera viewCam) {
        isVisible = false;
//		isLookingAt = false;
//		deltaCenter = Float.MAX_VALUE;

        if (cMarker.z < -1f) {
            isVisible = true;
        }
    }

    public void calcPaint(Camera viewCam, float addX, float addY) {
        cCMarker(origin, viewCam, addX, addY);
        calcV(viewCam);
    }

    private boolean isClickValid(float x, float y) {

        //if the marker is not active (i.e. not shown in AR view) we don't have to check it for clicks
        if (!isActive() && !this.isVisible)
            return false;

        float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,
                signMarker.x, signMarker.y);
        //TODO adapt the following to the variable radius!
        pPt.x = x - signMarker.x;
        pPt.y = y - signMarker.y;
        pPt.rotate((float) Math.toRadians(-(currentAngle + 90)));
        pPt.x += txtLab.getX();
        pPt.y += txtLab.getY();

        float objX = txtLab.getX() - txtLab.getWidth() / 2;
        float objY = txtLab.getY() - txtLab.getHeight() / 2;
        float objW = txtLab.getWidth();
        float objH = txtLab.getHeight();

        if (pPt.x > objX && pPt.x < objX + objW && pPt.y > objY
                && pPt.y < objY + objH) {
            return true;
        } else {
            return false;
        }
    }
}