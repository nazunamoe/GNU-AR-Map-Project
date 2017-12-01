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
package com.gnuarmap.mixare.app;

import static android.view.KeyEvent.KEYCODE_CAMERA;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gnuarmap.R;
import com.gnuarmap.observer.Activity.Filtering;

import org.mixare.lib.HtmlUnescape;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.ScreenLine;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.render.Camera;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is able to update the markers and the radar. It also handles some
 * user events
 * 
 * @author daniele
 * 
 */
public class DataView {

	/** current context */
	private MixContext mixContext;
	/** is the view Inited? */
	private boolean isInit;

	/** width and height of the view */
	private int width, height;

	/**
	 * _NOT_ the android camera, the class that takes care of the transformation
	 */
	private Camera cam;

	private State state = State.getInstance();

	/** The view can be "frozen" for debug purposes */
	private boolean frozen;

	/** how many times to re-attempt download */
	private int retry;

	private Location curFix;
	private Data.DataHandler dataHandler = new Data.DataHandler();
	private float radius = 20;

	/** timer to refresh the browser */
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

	public Data.DataHandler getDataHandler() {
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

			cam = new Camera(width, height, true);
			cam.setViewAngle(Camera.DEFAULT_VIEW_ANGLE);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		frozen = false;
		isInit = true;
	}

	public void draw(PaintScreen dw) {
		mixContext.getRM(cam.transform);
		curFix = mixContext.getLocationFinder().getCurrentLocation();
		Data dataclass = new Data();
		state.calcPitchBearing(cam.transform);

		// Load Layer
		if (state.nextLStatus == State.NOT_STARTED && !frozen) {
			loadDrawLayer();
			markers = new ArrayList<Marker>();
		}
		else if (state.nextLStatus == State.PROCESSING) {
			retry = 0;
			state.nextLStatus = State.DONE;

			dataHandler = new Data.DataHandler();
			dataHandler.addMarkers(markers);
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
	private void loadDrawLayer(){
		if (mixContext.getStartUrl().length() > 0) {
			state.nextLStatus = State.PROCESSING;
			isLauncherStarted = true;
		}

		else {
			double lat = curFix.getLatitude(), lon = curFix.getLongitude(), alt = curFix
					.getAltitude();
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
	public void refresh(){
		state.nextLStatus = State.NOT_STARTED;
	}
	
	private void callRefreshToast(){
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

    /**
     * Created by nazunamoe on 2017-12-01.
     */
    static class UIEvent {
        public static final int CLICK = 0;
        public static final int KEY = 1;

        public int type;
    }

    /**
     * Created by nazunamoe on 2017-11-11.
     */

    public static class Data {
        public ArrayList<com.gnuarmap.mixare.app.Marker.SocialMarker> WholeList = new ArrayList<com.gnuarmap.mixare.app.Marker.SocialMarker>();
        public ArrayList<com.gnuarmap.mixare.app.Marker.SocialMarker> List = new ArrayList<com.gnuarmap.mixare.app.Marker.SocialMarker>();
        public static Bitmap basic;
        private String Filtering1;
        private String[] Filtering2;
        State state = State.getInstance();

        public int getIntFromColor(float Red, float Green, float Blue){
            int R = Math.round(255 * Red);
            int G = Math.round(255 * Green);
            int B = Math.round(255 * Blue);

            R = (R << 16) & 0x00FF0000;
            G = (G << 8) & 0x0000FF00;
            B = B & 0x000000FF;

            return 0xFF000000 | R | G | B;
        }

        public void addItem(String num, String name, String url, double Latitude, double Longitude, String type, String Filtering1, String[] Filtering2, int no){
            int value = 0;
            this.Filtering1 = Filtering1;
            this.Filtering2 = Filtering2;
            int number = List.size();
            WholeList.add(new com.gnuarmap.mixare.app.Marker.SocialMarker(
                    num,
                    HtmlUnescape.unescapeHTML((name), 0),
                    Latitude,
                    Longitude,
                    0, // 소셜 마커이므로 고도에 구애받지 않는다.
                    url,
                    1,
                    value,
                    type,
                    Filtering1,
                    Filtering2,
                    no)
            );
            if(state.AllBuilding){
                secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
            }else{
                if(state.Agriculture){
                    if(Filtering1=="agriculture"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Business){
                    if(Filtering1=="business"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Engnieering){
                    if(Filtering1=="engnieering"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Dormitory){
                    if(Filtering1=="dormitory"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.ETC){
                    if(Filtering1=="etc"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.University){
                    if(Filtering1=="university"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Club){
                    if(Filtering1=="club"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Door){
                    if(Filtering1=="door"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Law){
                    if(Filtering1=="law"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Education){
                    if(Filtering1=="education"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Social){
                    if(Filtering1=="social"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Veterinary){
                    if(Filtering1=="veterinary"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Leisure){
                    if(Filtering1=="leisure"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Humanities){
                    if(Filtering1=="humanities"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
                if(state.Science){
                    if(Filtering1=="science"){
                        secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2,no);
                    }
                }
            }
            // 건물 필터링, 이 다음에 2차 필터링도 넣어야 함.
        }

        public String[] vending = {"vending"};
        public String[] printer = {"printer"};
        public String[] cvs = {"cvs"};
        public String[] atm = {"atm"};

        public String[] printer_cvs = {"printer","cvs"};
        public String[] printer_atm = {"printer","atm"};
        public String[] vending_atm = {"vending","atm"};
        public String[] vending_printer = {"vending","printer"};
        public String[] cvs_atm = {"cvs","atm"};

        public String[] vending_cvs_atm = {"vending","cvs","atm"};
        public String[] printer_cvs_atm = {"printer","cvs","atm"};

        public String[] nothing = {};

        private void secondFiltering(String num, Double Latitude, Double Longitude, String url, int value, String type, String name, String[] Filtering2, int ID){

            if(state.All){
                addMarker(num,Latitude,Longitude,url,value,type,name,ID);}
            else if(state.Vending){
                if(state.ATM){
                    if(Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("atm")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }else if(state.Printer){
                    if(Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("printer")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }else if(state.CVS) {
                    if(Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("cvs")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }else{
                    if(Arrays.asList(Filtering2).contains("vending")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }
            }else if(state.Printer){
                if(state.CVS){
                    if(state.ATM){
                        if(Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("cvs") && Arrays.asList(Filtering2).contains("atm")) {
                            addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                        }
                    }else{
                        if(Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("cvs")){
                            addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                        }else{
                        }
                    }
                }else if(state.ATM){
                    if(Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("atm")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }else{
                    if(Arrays.asList(Filtering2).contains("printer")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }
            }else if(state.CVS){
                if(state.ATM){
                    if(Arrays.asList(Filtering2).contains("cvs") && Arrays.asList(Filtering2).contains("atm")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }else{
                    if(Arrays.asList(Filtering2).contains("cvs")){
                        addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                    }
                }
            }else if(state.ATM){
                if(Arrays.asList(Filtering2).contains("atm")){
                    addMarker(num,Latitude,Longitude,url,value,type,name,ID);
                }
            }else{
            }
        }


        private void addMarker(String num, Double Latitude, Double Longitude, String url, int value, String type, String name, int ID){
            com.gnuarmap.mixare.app.Marker.SocialMarker item = new com.gnuarmap.mixare.app.Marker.SocialMarker(
                    num,
                    HtmlUnescape.unescapeHTML((name), 0),
                    Latitude,
                    Longitude,
                    0, // 소셜 마커이므로 고도에 구애받지 않는다.
                    url,
                    1,
                    value,
                    type,
                    Filtering1,
                    Filtering2,
                    ID);
            List.add(item);
        }

        public com.gnuarmap.mixare.app.Marker.SocialMarker getMarker(String no){
            int b=0;

            for(int a = 0; a<WholeList.size(); a++){
                if(no.equals(WholeList.get(a).NUM)){
                    b = a;
                }
            }
            return List.get(b);
        }

        public int getSize(){
            return List.size();
        }

        public int getWholeSize() { return WholeList.size();}

        public com.gnuarmap.mixare.app.Marker.SocialMarker getData(int index){
            return List.get(index);
        }

        public com.gnuarmap.mixare.app.Marker.SocialMarker getWholeData(int index){
            return WholeList.get(index);
        }

        public String getFilter1(int index){
            return List.get(index).filter1;
        }

        public String[] getFilter2(int index){
            return List.get(index).filter2;
        }

        /**
         * DataHandler is the model which provides the Marker Objects with its data.
         * DataHandler is also the Factory for new Marker objects.
         */
        public static class DataHandler {

            // 완성된 마커 리스트
            private java.util.List<Marker> markerList = new ArrayList<Marker>();

            public void addMarkers(List<Marker> markers) {
                Filtering.DataBase data = new Filtering.DataBase();

                data.Initialize();

                for(int i=0; i<data.data.getSize(); i++){
                    markerList.add(data.data.List.get(i));
                }
                /**
                 * 독립된 데이터 프로세서 대신 이 부분에 marker 배열에 필요한 마커 데이터를 직접 등록한다.
                 */

                Log.d(MixView.TAG, "Marker count: "+markerList.size());
            }

            // 마커 리스트 정렬
            public void sortMarkerList() {
                Collections.sort(markerList);
            }

            // 위치를 이용해서 현재 위치와의 거리 측정
            public void updateDistances(Location location) {
                for(Marker ma: markerList) {
                    float[] dist=new float[3];
                    Location.distanceBetween(ma.getLatitude(), ma.getLongitude(), location.getLatitude(), location.getLongitude(), dist);
                    ma.setDistance(dist[0]);
                }
            }

            // 활성화 상태 업데이트...??
            public void updateActivationStatus(MixContext mixContext) {

                Hashtable<Class, Integer> map = new Hashtable<Class, Integer>();

                for(Marker ma: markerList) {

                    Class<? extends Marker> mClass=ma.getClass();
                    map.put(mClass, (map.get(mClass)!=null)?map.get(mClass)+1:1);

                    boolean belowMax = (map.get(mClass) <= ma.getMaxObjects());
                    //boolean dataSourceSelected = mixContext.isDataSourceSelected(ma.getDatasource());

                    ma.setActive((belowMax));
                }
            }

            // 장소가 변함에 따라서 마커 리스트를 정렬함
            public void onLocationChanged(Location location) {
                updateDistances(location);
                sortMarkerList();
                for(Marker ma: markerList) {
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
            public void setMarkerList(List<Marker> markerList) {
                this.markerList = markerList;
            }

            public int getMarkerCount() {
                return markerList.size();
            }

            public Marker getMarker(int index) {
                return markerList.get(index);
            }
        }

        /**
         * The DataSource class is able to create the URL where the information about a
         * place can be found.
         *
         * @author hannes
         *
         */

         // 이건 마커 정보에 따라서 URL을 생성하는 부분
        public static class DataSource {

            public static Bitmap schoolicon; // 학교 아이콘
            public static Bitmap dormicon;
            public static Bitmap dorm2icon;
            public static Bitmap elecicon;
            public static Bitmap forlangicon; // 국제어학원
            public static Bitmap gateicon; // 정문이나 후문
            public static Bitmap libraryicon; // 도서관
            public static Bitmap lifesci; // 농생대
            public static Bitmap manageinticon; // 인문대
            public static Bitmap multi; // 학생회관
            public static Bitmap student; // 따로 만들어야함, 경상대 아이콘
            public static Bitmap viliage; // 기숙사
            public static Bitmap panpacific; // ??
            public static Bitmap basic; // 기본 아이콘
            public static Bitmap engine; // 공대
            public static Bitmap physical; // 체육관, 운동시설

            // 아이콘이 있는 것 : 농생대, 인문대, 공대
            // 만들어야 하는 것 : 사회과학대학, 자연과학대학, 경영대학, 법과대학, 사법대학, 수의과대학, 약학대학
            // res/drawable 폴더를 참조해서 제작

            public static Bitmap SCHOOLRestaurant;
            public static Bitmap cafeIcon;
            public static Bitmap restraurantIcon;
            public static Bitmap convenienceIcon;
            public static Bitmap printer;

            // 프린터 아이콘 필요
            // 카페(매점), 편의점, 식당 아이콘은 이미 있음

            private String name;
            private String url;

            public enum TYPE {
                WIKIPEDIA, BUZZ, TWITTER, OSM, MIXARE, ARENA
            };

            public enum DISPLAY {
                CIRCLE_MARKER, NAVIGATION_MARKER, IMAGE_MARKER
            };

            public static String Building_Type;

            private boolean enabled;
            private TYPE type;
            private DISPLAY display;

            public static void createIcons(Resources res) {
                cafeIcon = BitmapFactory.decodeResource(res, R.drawable.icon_cafe);
                restraurantIcon = BitmapFactory.decodeResource(res,R.drawable.icon_store);
                convenienceIcon = BitmapFactory.decodeResource(res,R.drawable.icon_conveni);
                SCHOOLRestaurant = BitmapFactory.decodeResource(res,R.drawable.restraunticon);
                dormicon = BitmapFactory.decodeResource(res,R.drawable.school_dorm);
                dorm2icon = BitmapFactory.decodeResource(res,R.drawable.school_dorm2);
                elecicon = BitmapFactory.decodeResource(res,R.drawable.school_elecinfo);
                forlangicon = BitmapFactory.decodeResource(res,R.drawable.school_forlang);
                gateicon = BitmapFactory.decodeResource(res,R.drawable.school_gate);
                libraryicon = BitmapFactory.decodeResource(res,R.drawable.school_library);
                lifesci = BitmapFactory.decodeResource(res,R.drawable.school_lifesci);
                manageinticon = BitmapFactory.decodeResource(res,R.drawable.school_manageint);
                multi = BitmapFactory.decodeResource(res,R.drawable.school_multi);
                panpacific = BitmapFactory.decodeResource(res,R.drawable.school_panpacific);
                engine = BitmapFactory.decodeResource(res,R.drawable.school_engine);
                physical = BitmapFactory.decodeResource(res,R.drawable.school_physical);
                viliage = BitmapFactory.decodeResource(res,R.drawable.school_village);
                basic = BitmapFactory.decodeResource(res,R.drawable.school_default);
                // routeIcon;
            }

            public static Bitmap getBitmap(String ds) {
                Bitmap bitmap = null;
                switch (ds) {

                    case "SCHOOLRestaurant":
                         bitmap = cafeIcon;
                         break;
                    case "dorm":
                        bitmap = dormicon;
                        break;
                    case "dorm2":
                        bitmap = dorm2icon;
                        break;
                    case "elecinfo":
                        bitmap = elecicon;
                        break;
                    case "forlang":
                        bitmap = forlangicon;
                        break;
                    case "gate":
                        bitmap = gateicon;
                        break;
                    case "library":
                        bitmap = libraryicon;
                        break;
                    case "lifesci":
                        bitmap = lifesci;
                        break;
                    case "manageint":
                        bitmap = manageinticon;
                        break;
                    case "student":
                        bitmap = multi;
                        break;
                    case "panpacific":
                        bitmap = panpacific;
                        break;
                    case "engine":
                        bitmap = engine;
                        break;
                    case "physical":
                        bitmap = physical;
                        break;
                    case "default":
                        bitmap = basic;
                        break;
                    case "CAFE":
                        bitmap = cafeIcon;
                        break;
                    case "CONVENICE":
                        bitmap = convenienceIcon;
                        break;
                    case "RESTRAUNT":
                        bitmap = restraurantIcon;
                        break;
                }
                return bitmap;
            }


            public DataSource(String name, String url, TYPE type, DISPLAY display,
                              boolean enabled) {
                this.name = name;
                this.url = url;
                this.type = type;
                this.display = display;
                this.enabled = enabled;
                Log.d("mixare", "New Datasource!" + name + " " + url + " " + type + " "
                        + display + " " + enabled);
            }

            public DataSource(String name, String url, int typeInt, int displayInt,
                    boolean enabled) {
                TYPE typeEnum = TYPE.values()[typeInt];
                DISPLAY displayEnum = DISPLAY.values()[displayInt];
                this.name = name;
                this.url = url;
                this.type = typeEnum;
                this.display = displayEnum;
                this.enabled = enabled;
            }

            public DataSource(String name, String url, String typeString,
                    String displayString, String enabledString) {
                TYPE typeEnum = TYPE.values()[Integer.parseInt(typeString)];
                DISPLAY displayEnum = DISPLAY.values()[Integer.parseInt(displayString)];
                Boolean enabledBool = Boolean.parseBoolean(enabledString);
                this.name = name;
                this.url = url;
                this.type = typeEnum;
                this.display = displayEnum;
                this.enabled = enabledBool;
            }

            public int getDisplayId() {
                return this.display.ordinal();
            }

            public int getTypeId() {
                return this.type.ordinal();
            }

            public DISPLAY getDisplay() {
                return this.display;
            }

            public TYPE getType() {
                return this.type;
            }

            public boolean getEnabled() {
                return this.enabled;
            }

            public String getName() {
                return this.name;
            }

            public String getUrl() {
                return this.url;
            }

            public String serialize() {
                return this.getName() + "|" + this.getUrl() + "|" + this.getTypeId()
                        + "|" + this.getDisplayId() + "|" + this.getEnabled();
            }

            public void setEnabled(boolean isChecked) {
                this.enabled = isChecked;
            }

            @Override
            public String toString() {
                return "DataSource [name=" + name + ", url=" + url + ", enabled="
                        + enabled + ", type=" + type + ", display=" + display + "]";
            }

            /**
             * Check the minimum required data
             *
             * @return boolean
             */
            public boolean isWellFormed() {
                boolean out = false;
                if (isUrlWellFormed() || getName() != null || !getName().isEmpty()) {
                    out = true;
                }
                return out;
            }

            public boolean isUrlWellFormed() {
                return getUrl() != null || !getUrl().isEmpty()
                        || "http://".equalsIgnoreCase(getUrl());
            }

        }
    }
}

