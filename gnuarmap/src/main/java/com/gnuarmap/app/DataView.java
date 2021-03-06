/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.gnuarmap.app;

import static android.view.KeyEvent.KEYCODE_CAMERA;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.ScreenLine;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.render.Camera;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.gnuarmap.R;

/**
 * This class is able to update the markers and the radar. It also handles some
 * user events
 *
 * @author daniele
 */

/**
 * MixView위에 띄워져서 AR 마커를 띄워주는 주체
 */
public class DataView {

    /**
     * current context
     */
    private MixContext mixContext;
    /**
     * is the view Inited?
     */
    private boolean isInit;

    /**
     * width and height of the view
     */
    private int width, height;

    /**
     * _NOT_ the android camera, the class that takes care of the transformation
     */
    private Camera cam;

    private State state = State.getInstance();

    /**
     * The view can be "frozen" for debug purposes
     */
    private boolean frozen;

    /**
     * how many times to re-attempt download
     */
    private int retry;

    private Location curFix;
    private DataHandler dataHandler = new DataHandler();
    private float radius = 20;

    /**
     * timer to refresh the browser
     */
    private Timer refresh = null;
    private final long refreshDelay = 10 * 1000; // refresh every 10 seconds

    private boolean isLauncherStarted;

    private ArrayList<UIEvent> uiEvents = new ArrayList<UIEvent>();
    private ScreenLine lrl = new ScreenLine();
    private ScreenLine rrl = new ScreenLine();
    private float rx = 10, ry = 20;
    private float addX = 0, addY = 0;

    private List<Marker> markers;

    /**
     * Constructor
     */
    public DataView(MixContext ctx) {
        this.mixContext = ctx;
    }

    public MixContext getContext() {
        return mixContext;
    }

    public boolean isLauncherStarted() {
        return isLauncherStarted;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public boolean isDetailsView() {
        return state.isDetailsView();
    }

    public void setDetailsView(boolean detailsView) {
        state.setDetailsView(detailsView);
    }

    public void doStart() {
        state.nextLStatus = State.NOT_STARTED;
        mixContext.getLocationFinder().setLocationAtLastDownload(curFix);
    }

    public boolean isInited() {
        return isInit;
    }

    public void init(int widthInit, int heightInit) {
        try {
            width = widthInit;
            height = heightInit;
            Log.d("debug", "init");
            cam = new Camera(width, height, true);
            cam.setViewAngle(Camera.DEFAULT_VIEW_ANGLE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frozen = false;
        isInit = true;
    }

    public void draw(PaintScreen dw) {
        Log.d("debug", "getRM");

        mixContext.getRM(cam.transform);
        curFix = mixContext.getLocationFinder().getCurrentLocation();

        // Load Layer
        if (state.nextLStatus == State.NOT_STARTED && !frozen) {
            loadDrawLayer();
            markers = new ArrayList<Marker>();
        } else if (state.nextLStatus == State.PROCESSING) {
            retry = 0;
            state.nextLStatus = State.DONE;

            dataHandler = new DataHandler();
            dataHandler.addMarkers(markers); //마커 정보를 불러와서 추가
            dataHandler.onLocationChanged(curFix);

            if (refresh == null) { // start the refresh timer if it is null
                refresh = new Timer(false);
                Date date = new Date(System.currentTimeMillis()
                        + refreshDelay);
                refresh.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        callRefreshToast();
                        refresh();
                    }
                }, date, refreshDelay);
            }
        }

        // Update markers
        dataHandler.updateActivationStatus(mixContext);
        for (int i = dataHandler.getMarkerCount() - 1; i >= 0; i--) {
            Marker ma = dataHandler.getMarker(i);
            if (ma.isActive() && (ma.getDistance() / 1000f < radius)) {

                // To increase performance don't recalculate position vector
                // for every marker on every draw call, instead do this only
                // after onLocationChanged and after downloading new marker
                // if (!frozen)
                // ma.update(curFix);
                Random random = new Random();
                if (!frozen)
                    ma.calcPaint(cam, addX, addY);
                ma.draw(dw);
            }
        }


        // Get next event
        UIEvent evt = null;
        synchronized (uiEvents) {
            if (uiEvents.size() > 0) {
                evt = uiEvents.get(0);
                uiEvents.remove(0);
            }
        }
        if (evt != null) {
            switch (evt.type) {
                case UIEvent.KEY:
                    handleKeyEvent((KeyEvent) evt);
                    break;
                case UIEvent.CLICK:
                    handleClickEvent((ClickEvent) evt);
                    break;
            }
        }
        state.nextLStatus = State.PROCESSING;
    }

    /**
     * Part of draw function, loads the layer.
     */
    private void loadDrawLayer() {
        if (mixContext.getStartUrl().length() > 0) {
            state.nextLStatus = State.PROCESSING;
            isLauncherStarted = true;
        } else {
            //double lat = curFix.getLatitude(), lon = curFix.getLongitude(), alt = curFix
            //			.getAltitude();
            state.nextLStatus = State.PROCESSING;
        }

        // if no datasources are activated
        if (state.nextLStatus == State.NOT_STARTED)
            state.nextLStatus = State.DONE;
    }

    private void handleKeyEvent(KeyEvent evt) {
        /** Adjust marker position with keypad */
        final float CONST = 10f;
        switch (evt.keyCode) {
            case KEYCODE_DPAD_LEFT:
                addX -= CONST;
                break;
            case KEYCODE_DPAD_RIGHT:
                addX += CONST;
                break;
            case KEYCODE_DPAD_DOWN:
                addY += CONST;
                break;
            case KEYCODE_DPAD_UP:
                addY -= CONST;
                break;
            case KEYCODE_DPAD_CENTER:
                frozen = !frozen;
                break;
            case KEYCODE_CAMERA:
                frozen = !frozen;
                break; // freeze the overlay with the camera button
            default: //if key is set, then ignore event
                break;
        }
    }

    boolean handleClickEvent(ClickEvent evt) {
        boolean evtHandled = false;

        // Handle event
        if (state.nextLStatus == State.DONE) {
            // the following will traverse the markers in ascending order (by
            // distance) the first marker that
            // matches triggers the event.
            //TODO handle collection of markers. (what if user wants the one at the back)
            for (int i = 0; i < dataHandler.getMarkerCount() && !evtHandled; i++) {
                Marker pm = dataHandler.getMarker(i);

                evtHandled = pm.fClick(evt.x, evt.y, mixContext, state);
            }
        }
        return evtHandled;
    }

    private void radarText(PaintScreen dw, String txt, float x, float y, boolean bg) {
        float padw = 4, padh = 2;
        float w = dw.getTextWidth(txt) + padw * 2;
        float h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
        if (bg) {
            dw.setColor(Color.rgb(0, 0, 0));
            dw.setFill(true);
            dw.paintRect(x - w / 2, y - h / 2, w, h);
            dw.setColor(Color.rgb(255, 255, 255));
            dw.setFill(false);
            dw.paintRect(x - w / 2, y - h / 2, w, h);
        }
        dw.paintText(padw + x - w / 2, padh + dw.getTextAsc() + y - h / 2, txt,
                false);
    }

    public void clickEvent(float x, float y) {
        synchronized (uiEvents) {
            uiEvents.add(new ClickEvent(x, y));
        }
    }

    public void keyEvent(int keyCode) {
        synchronized (uiEvents) {
            uiEvents.add(new KeyEvent(keyCode));
        }
    }

    public void clearEvents() {
        synchronized (uiEvents) {
            uiEvents.clear();
        }
    }

    public void cancelRefreshTimer() {
        if (refresh != null) {
            refresh.cancel();
        }
    }

    /**
     * Re-downloads the markers, and draw them on the map.
     */
    public void refresh() {
        state.nextLStatus = State.NOT_STARTED;
    }

    private void callRefreshToast() {
        mixContext.getActualMixView().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(
                        mixContext,
                        mixContext.getResources()
                                .getString(R.string.refreshing),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Created by nazunamoe on 2017-12-01.
     */
    static class ClickEvent extends UIEvent {
        public float x, y;

        public ClickEvent(float x, float y) {
            this.type = CLICK;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    /**
     * Created by nazunamoe on 2017-12-01.
     */
    static class KeyEvent extends UIEvent {
        public int keyCode;

        public KeyEvent(int keyCode) {
            this.type = KEY;
            this.keyCode = keyCode;
        }

        @Override
        public String toString() {
            return "(" + keyCode + ")";
        }
    }

    static class UIEvent {
        public static final int CLICK = 0;
        public static final int KEY = 1;

        public int type;
    }


}


