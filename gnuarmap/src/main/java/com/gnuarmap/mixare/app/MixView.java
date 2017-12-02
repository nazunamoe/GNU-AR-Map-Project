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

/**
 * This class is the main application which uses the other classes for different
 * functionalities.
 * It sets up the camera screen and the augmented screen which is in front of the
 * camera screen.
 * It also handles the main sensor events, touch events and location events.
 */

import static android.hardware.SensorManager.SENSOR_DELAY_GAME;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.gnuarmap.data.Dataclass;
import com.gnuarmap.observer.Activity.MenuActivity;
import com.gnuarmap.R;
import com.gnuarmap.observer.Activity.NaverMapActivity;

import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.render.Matrix;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MixView extends Activity implements SensorEventListener, OnTouchListener {

	private CameraSurface camScreen;
	private AugmentedView augScreen;

	private boolean isInited;
	private static PaintScreen dWindow;
	private static DataView dataView;
	private boolean fError;
	public State state = State.getInstance();
	//----------
    private MixViewDataHolder mixViewData  ;
	
	// TAG for logging
	public static final String TAG = "Mixare";

	/* string to name & access the preference file in the internal storage */
	public static final String PREFS_NAME = "MyPrefsFileForMenuItems";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Dataclass.createIcons(getResources());
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		if(state.count == 0){
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage(getString(R.string.GPSWarning));
			builder1.setNegativeButton(getString(R.string.close_button),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			AlertDialog alert1 = builder1.create();
			alert1.setTitle(getString(R.string.GPSWarningTitle));
			alert1.show();
			state.count ++;
		}
		// 경고문은 한번만
		try {
						
			handleIntent(getIntent());

			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			getMixViewData().setmWakeLock(pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag"));

			killOnError();
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			maintainCamera();
			maintainAugmentR();
			maintainZoomBar();
			
			if (!isInited) {
				setdWindow(new PaintScreen());
				setDataView(new DataView(getMixViewData().getMixContext()));

				/* set the radius in data view to the last selected by the user */
				setZoomLevel();
				isInited = true;
			}

			/*Get the preference file PREFS_NAME stored in the internal memory of the phone*/
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		} catch (Exception ex) {
			doError(ex);
		}
	}

	public MixViewDataHolder getMixViewData() {
		if (mixViewData==null){
			mixViewData = new MixViewDataHolder(new MixContext(this));
		}
		return mixViewData;
	}

	@Override
	protected void onPause() {
		super.onPause();

		try {
			this.getMixViewData().getmWakeLock().release();

			try {
				getMixViewData().getSensorMgr().unregisterListener(this,
						getMixViewData().getSensorGrav());
				getMixViewData().getSensorMgr().unregisterListener(this,
						getMixViewData().getSensorMag());
				getMixViewData().setSensorMgr(null);
				
				getMixViewData().getMixContext().getLocationFinder().switchOff();

				if (getDataView() != null) {
					getDataView().cancelRefreshTimer();
				}
			} catch (Exception ignore) {
			}

			if (fError) {
				finish();
			}
		} catch (Exception ex) {
			doError(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * Mixare - Receives results from other launched activities
	 * Base on the result returned, it either refreshes screen or not.
	 * Default value for refreshing is false
	 */
	protected void onActivityResult(final int requestCode,
			final int resultCode, Intent data) {
		Log.d(TAG + " WorkFlow", "MixView - onActivityResult Called");
		// check if the returned is request to refresh screen (setting might be
		// changed)
		try {
			if (data.getBooleanExtra("RefreshScreen", false)) {
				Log.d(TAG + " WorkFlow",
						"MixView - Received Refresh Screen Request .. about to refresh");
				repaint();
				refreshDownload();
			}

		} catch (Exception ex) {
			// do nothing do to mix of return results.
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		try {
			this.getMixViewData().getmWakeLock().acquire();

			killOnError();
			getMixViewData().getMixContext().doResume(this);

			repaint();
			getDataView().doStart();
			getDataView().clearEvents();

			float angleX, angleY;

			int marker_orientation = -90;

			int rotation = Compatibility.getRotation(this);

			// display text from left to right and keep it horizontal
			angleX = (float) Math.toRadians(marker_orientation);
			getMixViewData().getM1().set(1f, 0f, 0f, 0f,
					(float) Math.cos(angleX),
					(float) -Math.sin(angleX), 0f,
					(float) Math.sin(angleX),
					(float) Math.cos(angleX));
			angleX = (float) Math.toRadians(marker_orientation);
			angleY = (float) Math.toRadians(marker_orientation);
			if (rotation == 1) {
				getMixViewData().getM2().set(1f, 0f, 0f, 0f,
						(float) Math.cos(angleX),
						(float) -Math.sin(angleX), 0f,
						(float) Math.sin(angleX),
						(float) Math.cos(angleX));
				getMixViewData().getM3().set((float) Math.cos(angleY), 0f,
						(float) Math.sin(angleY), 0f, 1f, 0f,
						(float) -Math.sin(angleY), 0f,
						(float) Math.cos(angleY));
			} else {
				getMixViewData().getM2().set((float) Math.cos(angleX), 0f,
						(float) Math.sin(angleX), 0f, 1f, 0f,
						(float) -Math.sin(angleX), 0f,
						(float) Math.cos(angleX));
				getMixViewData().getM3().set(1f, 0f, 0f, 0f,
						(float) Math.cos(angleY),
						(float) -Math.sin(angleY), 0f,
						(float) Math.sin(angleY),
						(float) Math.cos(angleY));

			}

			getMixViewData().getM4().toIdentity();

			for (int i = 0; i < getMixViewData().getHistR().length; i++) {
				getMixViewData().getHistR()[i] = new Matrix();
			}

			getMixViewData()
					.setSensorMgr((SensorManager) getSystemService(SENSOR_SERVICE));

			getMixViewData().setSensors(getMixViewData().getSensorMgr().getSensorList(
					Sensor.TYPE_ACCELEROMETER));
			if (getMixViewData().getSensors().size() > 0) {
				getMixViewData().setSensorGrav(getMixViewData().getSensors().get(0));
			}

			getMixViewData().setSensors(getMixViewData().getSensorMgr().getSensorList(
					Sensor.TYPE_MAGNETIC_FIELD));
			if (getMixViewData().getSensors().size() > 0) {
				getMixViewData().setSensorMag(getMixViewData().getSensors().get(0));
			}

			getMixViewData().getSensorMgr().registerListener(this,
					getMixViewData().getSensorGrav(), SENSOR_DELAY_GAME);
			getMixViewData().getSensorMgr().registerListener(this,
					getMixViewData().getSensorMag(), SENSOR_DELAY_GAME);

			try {
				GeomagneticField gmf = getMixViewData().getMixContext().getLocationFinder().getGeomagneticField(); 
				angleY = (float) Math.toRadians(-gmf.getDeclination());
				getMixViewData().getM4().set((float) Math.cos(angleY), 0f,
						(float) Math.sin(angleY), 0f, 1f, 0f,
						(float) -Math.sin(angleY), 0f,
						(float) Math.cos(angleY));
			} catch (Exception ex) {
				Log.d("mixare", "GPS Initialize Error", ex);
			}

			getMixViewData().getMixContext().getLocationFinder().switchOn();
		} catch (Exception ex) {
			doError(ex);
			try {
				if (getMixViewData().getSensorMgr() != null) {
					getMixViewData().getSensorMgr().unregisterListener(this,
							getMixViewData().getSensorGrav());
					getMixViewData().getSensorMgr().unregisterListener(this,
							getMixViewData().getSensorMag());
					getMixViewData().setSensorMgr(null);
				}

				if (getMixViewData().getMixContext() != null) {
					getMixViewData().getMixContext().getLocationFinder().switchOff();
				}
			} catch (Exception ignore) {
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Customize Activity after switching back to it.
	 * Currently it maintain and ensures view creation.
	 */
	protected void onRestart (){
		super.onRestart();
		maintainCamera();
		maintainAugmentR();
		maintainZoomBar();
		
	}
	
	/* ********* Operators ***********/ 

	public void repaint() {
		//clear stored data
		getDataView().clearEvents();
		setDataView(null); //It's smelly code, but enforce garbage collector 
							//to release data.
		setDataView(new DataView(mixViewData.getMixContext()));
		setdWindow(new PaintScreen());
		//setZoomLevel(); //@TODO Caller has to set the zoom. This function repaints only.
	}
	
	/**
	 *  Checks camScreen, if it does not exist, it creates one.
	 */
	private void maintainCamera() {
		if (camScreen == null){

			if(state.Camera2){
				camScreen = new Camera2Surface(this);
			}else{
				camScreen = new CameraSurface(this);
			} // 카메라 2와 구 카메라 API 변환 기능, 오래된 기종에 대한 호환성의 확보 목적
		}
		setContentView(camScreen);
	}
	
	/**
	 * Checks augScreen, if it does not exist, it creates one.
	 */
	private void maintainAugmentR() {
		if (augScreen == null ){
		augScreen = new AugmentedView(this);
		}
		addContentView(augScreen, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Creates a zoom bar and adds it to view.
	 */
	private void maintainZoomBar() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		FrameLayout frameLayout = createZoomBar(settings);
		addContentView(frameLayout, new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.BOTTOM));
	}
	

	private void refreshDownload(){
//		try {
//			if (getMixViewData().getDownloadThread() != null){
//				if (!getMixViewData().getDownloadThread().isInterrupted()){
//					getMixViewData().getDownloadThread().interrupt();
//					getMixViewData().getMixContext().getDownloadManager().restart();
//				}
//			}else { //if no download thread found
//				getMixViewData().setDownloadThread(new Thread(getMixViewData()
//						.getMixContext().getDownloadManager()));
//				//@TODO Syncronize DownloadManager, call Start instead of run.
//				mixViewData.getMixContext().getDownloadManager().run();
//			}
//		}catch (Exception ex){
//		}
	}
	
	public void refresh(){
		dataView.refresh();
	}

	public void setErrorDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.connection_error_dialog));
		builder.setCancelable(false);

		/*Retry*/
		builder.setPositiveButton(R.string.connection_error_dialog_button1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				fError=false;
				//TODO improve
				try {
					maintainCamera();
					maintainAugmentR();
					repaint();
					setZoomLevel();
				}
				catch(Exception ex){
					//Don't call doError, it will be a recursive call.
					//doError(ex);
				}
			}
		});
		/*Open settings*/
		builder.setNeutralButton(R.string.connection_error_dialog_button2, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent1 = new Intent(Settings.ACTION_WIRELESS_SETTINGS); 
				startActivityForResult(intent1, 42);
			}
		});
		/*Close application*/
		builder.setNegativeButton(R.string.connection_error_dialog_button3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				System.exit(0); //wouldn't be better to use finish (to stop the app normally?)
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	
	public float calcZoomLevel(){

		int myZoomLevel = getMixViewData().getMyZoomBar().getProgress();
		float myout = 5;

		if (myZoomLevel <= 26) {
			myout = myZoomLevel / 25f;
		} else if (25 < myZoomLevel && myZoomLevel < 50) {
			myout = (1 + (myZoomLevel - 25)) * 0.38f;
		} else if (25 == myZoomLevel) {
			myout = 1;
		} else if (50 == myZoomLevel) {
			myout = 10;
		} else if (50 < myZoomLevel && myZoomLevel < 75) {
			myout = (10 + (myZoomLevel - 50)) * 0.83f;
		} else {
			myout = (30 + (myZoomLevel - 75) * 2f);
		}


		return myout;
	}

	/**
	 * Create zoom bar and returns FrameLayout. FrameLayout is created to be
	 * hidden and not added to view, Caller needs to add the frameLayout to
	 * view, and enable visibility when needed.
	 *
	 * @return FrameLayout Hidden Zoom Bar
	 */
	private FrameLayout createZoomBar(SharedPreferences settings) {
		getMixViewData().setMyZoomBar(new SeekBar(this));
		getMixViewData().getMyZoomBar().setMax(100);
		getMixViewData().getMyZoomBar()
				.setProgress(settings.getInt("zoomLevel", 65));
		getMixViewData().getMyZoomBar().setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);
		getMixViewData().getMyZoomBar().setVisibility(View.INVISIBLE);

		FrameLayout frameLayout = new FrameLayout(this);

		frameLayout.setMinimumWidth(3000);
		frameLayout.addView(getMixViewData().getMyZoomBar());
		frameLayout.setPadding(10, 0, 10, 10);
		return frameLayout;
	}
	
	/* ********* Operator - Menu ******/
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int base = Menu.FIRST;
		/* define the first */
		MenuItem item1 = menu.add(base, base + 0, base + 0,
				getString(R.string.menu_item_1));
		MenuItem item2 = menu.add(base, base + 1, base + 1,
				getString(R.string.menu_item_2));
		MenuItem item3 = menu.add(base, base + 2, base + 2,
				getString(R.string.menu_item_3));
		MenuItem item4 = menu.add(base, base + 3, base + 3,
				getString(R.string.menu_item_4));

		/* assign icons to the menu items */
		item1.setIcon(android.R.drawable.ic_menu_mapmode);
		item2.setIcon(android.R.drawable.ic_menu_zoom);
		item3.setIcon(android.R.drawable.ic_menu_info_details);
		item4.setIcon(android.R.drawable.ic_menu_share);

		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent2 = new Intent(MixView.this, NaverMapActivity.class);
			intent2.putExtra("return",1);
			startActivityForResult(intent2, 20);
			break;
		case 2:
			getMixViewData().getMyZoomBar().setVisibility(View.VISIBLE);
			getMixViewData().setZoomProgress(getMixViewData().getMyZoomBar()
					.getProgress());
			break;
		case 3:
			Location currentGPSInfo = getMixViewData().getMixContext().getLocationFinder().getCurrentLocation();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.general_info_text) + "\n\n"
					+ getString(R.string.longitude)
					+ currentGPSInfo.getLongitude() + "\n"
					+ getString(R.string.latitude)
					+ currentGPSInfo.getLatitude() + "\n"
					+ getString(R.string.altitude)
					+ currentGPSInfo.getAltitude() + "m\n"
					+ getString(R.string.speed) + currentGPSInfo.getSpeed()
					+ "km/h\n" + getString(R.string.accuracy)
					+ currentGPSInfo.getAccuracy() + "m\n"
					+ getString(R.string.gps_last_fix)
					+ new Date(currentGPSInfo.getTime()).toString() + "\n");
			builder.setNegativeButton(getString(R.string.close_button),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.setTitle(getString(R.string.general_info_title));
			alert.show();
			break;
		case 4:
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage(getString(R.string.license));
			/* Retry */
			builder1.setNegativeButton(getString(R.string.close_button),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			AlertDialog alert1 = builder1.create();
			alert1.setTitle(getString(R.string.license_title));
			alert1.show();
			break;

		}
		return true;
	}

	/* ******** Operators - Sensors ****** */

	private SeekBar.OnSeekBarChangeListener myZoomBarOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		Toast t;

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			float myout = calcZoomLevel();

			getMixViewData().setZoomLevel(String.valueOf(myout));
			getMixViewData().setZoomProgress(getMixViewData().getMyZoomBar()
					.getProgress());

			t.setText("Radius: " + String.valueOf(myout));
			t.show();
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			Context ctx = seekBar.getContext();
			t = Toast.makeText(ctx, "Radius: ", Toast.LENGTH_LONG);
			// zoomChanging= true;
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			/* store the zoom range of the zoom bar selected by the user */
			editor.putInt("zoomLevel", getMixViewData().getMyZoomBar().getProgress());
			editor.apply();
			getMixViewData().getMyZoomBar().setVisibility(View.INVISIBLE);
			// zoomChanging= false;

			getMixViewData().getMyZoomBar().getProgress();

			t.cancel();
			//repaint after zoom level changed.
			repaint();
			setZoomLevel();
		}

	};


	public void onSensorChanged(SensorEvent evt) {
		try {

			if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				getMixViewData().getGrav()[0] = evt.values[0];
				getMixViewData().getGrav()[1] = evt.values[1];
				getMixViewData().getGrav()[2] = evt.values[2];

				augScreen.postInvalidate();
			} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				getMixViewData().getMag()[0] = evt.values[0];
				getMixViewData().getMag()[1] = evt.values[1];
				getMixViewData().getMag()[2] = evt.values[2];

				augScreen.postInvalidate();
			}

			SensorManager.getRotationMatrix(getMixViewData().getRTmp(),
					getMixViewData().getI(), getMixViewData().getGrav(),
					getMixViewData().getMag());

			int rotation = Compatibility.getRotation(this);

			if (rotation == 1) {
				SensorManager.remapCoordinateSystem(getMixViewData().getRTmp(),
						SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z,
						getMixViewData().getRot());
			} else {
				SensorManager.remapCoordinateSystem(getMixViewData().getRTmp(),
						SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z,
						getMixViewData().getRot());
			}
			getMixViewData().getTempR().set(getMixViewData().getRot()[0],
					getMixViewData().getRot()[1], getMixViewData().getRot()[2],
					getMixViewData().getRot()[3], getMixViewData().getRot()[4],
					getMixViewData().getRot()[5], getMixViewData().getRot()[6],
					getMixViewData().getRot()[7], getMixViewData().getRot()[8]);

			getMixViewData().getFinalR().toIdentity();
			getMixViewData().getFinalR().prod(getMixViewData().getM4());
			getMixViewData().getFinalR().prod(getMixViewData().getM1());
			getMixViewData().getFinalR().prod(getMixViewData().getTempR());
			getMixViewData().getFinalR().prod(getMixViewData().getM3());
			getMixViewData().getFinalR().prod(getMixViewData().getM2());
			getMixViewData().getFinalR().invert();

			getMixViewData().getHistR()[getMixViewData().getrHistIdx()].set(getMixViewData()
					.getFinalR());
			getMixViewData().setrHistIdx(getMixViewData().getrHistIdx() + 1);
			if (getMixViewData().getrHistIdx() >= getMixViewData().getHistR().length)
				getMixViewData().setrHistIdx(0);

			getMixViewData().getSmoothR().set(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
			for (int i = 0; i < getMixViewData().getHistR().length; i++) {
				getMixViewData().getSmoothR().add(getMixViewData().getHistR()[i]);
			}
			getMixViewData().getSmoothR().mult(
					1 / (float) getMixViewData().getHistR().length);

			getMixViewData().getMixContext().updateSmoothRotation(getMixViewData().getSmoothR());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		try {
			killOnError();

			float xPress = me.getX();
			float yPress = me.getY();
			if (me.getAction() == MotionEvent.ACTION_UP) {
				getDataView().clickEvent(xPress, yPress);
			}//TODO add gesture events (low)

			return true;
		} catch (Exception ex) {
			// doError(ex);
			ex.printStackTrace();
			return super.onTouchEvent(me);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Context ctx;
		try {
			killOnError();

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (getDataView().isDetailsView()) {
					getDataView().keyEvent(keyCode);
					getDataView().setDetailsView(false);
					return true;
				} else {
					//TODO handle keyback to finish app correctly
					ctx = this;
					startActivity(new Intent(ctx, MenuActivity.class));
					finish();
					return false;
				}
			} else if (keyCode == KeyEvent.KEYCODE_MENU) {
				return super.onKeyDown(keyCode, event);
			} else {
				getDataView().keyEvent(keyCode);
				return false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return super.onKeyDown(keyCode, event);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE
				&& getMixViewData().getCompassErrorDisplayed() == 0) {
			for (int i = 0; i < 2; i++) {
				Toast.makeText(getMixViewData().getMixContext(),
						"Compass data unreliable. Please recalibrate compass.",
						Toast.LENGTH_LONG).show();
			}
			getMixViewData().setCompassErrorDisplayed(getMixViewData()
					.getCompassErrorDisplayed() + 1);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		getDataView().setFrozen(false);
		if (getMixViewData().getSearchNotificationTxt() != null) {
			getMixViewData().getSearchNotificationTxt().setVisibility(View.GONE);
			getMixViewData().setSearchNotificationTxt(null);
		}
		return false;
	}


	/* ************ Handlers *************/

	public void doError(Exception ex1) {
		if (!fError) {
			fError = true;

			setErrorDialog();

			ex1.printStackTrace();
			try {
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}

		try {
			augScreen.invalidate();
		} catch (Exception ignore) {
		}
	}

	public void killOnError() throws Exception {
		if (fError)
			throw new Exception();
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	/* ******* Getter and Setters ********** */

	public boolean isZoombarVisible() {
		return getMixViewData().getMyZoomBar() != null
				&& getMixViewData().getMyZoomBar().getVisibility() == View.VISIBLE;
	}
	
	public String getZoomLevel() {
		return getMixViewData().getZoomLevel();
	}
	
	/**
	 * @return the dWindow
	 */
	static PaintScreen getdWindow() {
		return dWindow;
	}


	/**
	 * @param dWindow
	 *            the dWindow to set
	 */
	static void setdWindow(PaintScreen dWindow) {
		MixView.dWindow = dWindow;
	}


	/**
	 * @return the dataView
	 */
	static DataView getDataView() {
		return dataView;
	}

	/**
	 * @param dataView
	 *            the dataView to set
	 */
	static void setDataView(DataView dataView) {
		MixView.dataView = dataView;
	}


	public int getZoomProgress() {
		return getMixViewData().getZoomProgress();
	}

	private void setZoomLevel() {
		float myout = calcZoomLevel();

		getDataView().setRadius(myout);
		//caller has the to control of zoombar visibility, not setzoom
		//mixViewData.getMyZoomBar().setVisibility(View.INVISIBLE);
		mixViewData.setZoomLevel(String.valueOf(myout));
		//setZoomLevel, caller has to call refreash download if needed.
	};

	/**
     * Ensures compatibility with older and newer versions of the API.
     * See the SDK docs for comments
     *
     * @author daniele
     *
     */
    public static class Compatibility {
        private static Method mParameters_getSupportedPreviewSizes;
        private static Method mDefaultDisplay_getRotation;

        static {
            initCompatibility();
        };

        /** this will fail on older phones (Android version < 2.0) */
        private static void initCompatibility() {
            try {
                mParameters_getSupportedPreviewSizes = Camera.Parameters.class.getMethod(
                        "getSupportedPreviewSizes", new Class[] { } );
                mDefaultDisplay_getRotation = Display.class.getMethod("getRotation", new Class[] { } );

                /* success, this is a newer device */
            } catch (NoSuchMethodException nsme) {
                /* failure, must be older device */
            }
        }

        /** If it's running on a new phone, let's get the supported preview sizes, before it was fixed to 480 x 320*/
        @SuppressWarnings("unchecked")
        public static List<Camera.Size> getSupportedPreviewSizes(Camera.Parameters params) {
            List<Camera.Size> retList = null;

            try {
                Object retObj = mParameters_getSupportedPreviewSizes.invoke(params);
                if (retObj != null) {
                    retList = (List<Camera.Size>)retObj;
                }
            }
            catch (InvocationTargetException ite) {
                /* unpack original exception when possible */
                Throwable cause = ite.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if (cause instanceof Error) {
                    throw (Error) cause;
                } else {
                    /* unexpected checked exception; wrap and re-throw */
                    throw new RuntimeException(ite);
                }
            } catch (IllegalAccessException ie) {
                //System.err.println("unexpected " + ie);
            }
            return retList;
        }

        static public int getRotation(final Activity activity) {
            int result = 1;
            try {
                    Display display = ((WindowManager) activity.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                    Object retObj = mDefaultDisplay_getRotation.invoke(display);
                    if( retObj != null) {
                        result = (Integer) retObj;
                    }
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return result;
        }

    }

	/**
     * Created by nazunamoe on 2017-11-09
     */

	static class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
        MixView app;
        SurfaceHolder holder;
        Camera camera;

        CameraSurface(Context context) {
            super(context);

            try {
                app = (MixView) context;

                holder = getHolder();
                holder.addCallback(this);
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            } catch (Exception ex) {

            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if (camera != null) {
                    try {
                        camera.stopPreview();
                    } catch (Exception ignore) {
                    }
                    try {
                        camera.release();
                    } catch (Exception ignore) {
                    }
                    camera = null;
                }

                camera = Camera.open();
                camera.setPreviewDisplay(holder);
            } catch (Exception ex) {
                try {
                    if (camera != null) {
                        try {
                            camera.stopPreview();
                        } catch (Exception ignore) {
                        }
                        try {
                            camera.release();
                        } catch (Exception ignore) {
                        }
                        camera = null;
                    }
                } catch (Exception ignore) {

                }
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (camera != null) {
                    try {
                        camera.stopPreview();
                    } catch (Exception ignore) {
                    }
                    try {
                        camera.release();
                    } catch (Exception ignore) {
                    }
                    camera = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                try {
                    List<Camera.Size> supportedSizes = null;
                    // On older devices (<1.6) the following will fail
                    // the camera will work nevertheless
                    supportedSizes = Compatibility.getSupportedPreviewSizes(parameters);

                    // preview form factor
                    float ff = (float) w / h;
                    Log.d("Mixare", "Screen res: w:" + w + " h:" + h
                            + " aspect ratio:" + ff);

                    // holder for the best form factor and size
                    float bff = 0;
                    int bestw = 0;
                    int besth = 0;
                    Iterator<Camera.Size> itr = supportedSizes.iterator();

                    // we look for the best preview size, it has to be the closest
                    // to the
                    // screen form factor, and be less wide than the screen itself
                    // the latter requirement is because the HTC Hero with update
                    // 2.1 will
                    // report camera preview sizes larger than the screen, and it
                    // will fail
                    // to initialize the camera
                    // other devices could work with previews larger than the screen
                    // though
                    while (itr.hasNext()) {
                        Camera.Size element = itr.next();
                        // current form factor
                        float cff = (float) element.width / element.height;
                        // check if the current element is a candidate to replace
                        // the best match so far
                        // current form factor should be closer to the bff
                        // preview width should be less than screen width
                        // preview width should be more than current bestw
                        // this combination will ensure that the highest resolution
                        // will win
                        Log.d("Mixare", "Candidate camera element: w:"
                                + element.width + " h:" + element.height
                                + " aspect ratio:" + cff);
                        if ((ff - cff <= ff - bff) && (element.width <= w)
                                && (element.width >= bestw)) {
                            bff = cff;
                            bestw = element.width;
                            besth = element.height;
                        }
                    }
                    Log.d("Mixare", "Chosen camera element: w:" + bestw + " h:"
                            + besth + " aspect ratio:" + bff);
                    // Some Samsung phones will end up with bestw and besth = 0
                    // because their minimum preview size is bigger then the screen
                    // size.
                    // In this case, we use the default values: 480x320
                    if ((bestw == 0) || (besth == 0)) {
                        Log.d("Mixare", "Using default camera parameters!");
                        bestw = 480;
                        besth = 320;
                    }
                    parameters.setPreviewSize(bestw, besth);
                } catch (Exception ex) {
                    parameters.setPreviewSize(480, 320);
                }

                camera.setParameters(parameters);
                camera.startPreview();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	static class Camera2Surface extends CameraSurface {


        private static final int DEFAULT_CAM_WIDTH = 1920;
        private static final int DEFAULT_CAM_HEIGHT = 1080;

        Size[] sizes = null;

        MixView mixViewActivity;
        SurfaceHolder holder;
        CameraDevice camera;
        CameraManager cameraManager;
        private String cameraId;
        private CameraCaptureSession activeSession;


        Camera2Surface(Context context) {
            super(context);
            try {
                mixViewActivity = (MixView) context;

                holder = getHolder();
                holder.addCallback(this);

                cameraManager = (CameraManager) mixViewActivity.getSystemService(CAMERA_SERVICE);
            } catch (Exception ex) {

            }

            cameraId = null;
            sizes = null;
            try {
                String[] ids = cameraManager.getCameraIdList();
                for (String curCameraId : ids) {
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(curCameraId);
                    Integer camLensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                    if (camLensFacing != null && camLensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        cameraId = curCameraId;
                        StreamConfigurationMap configs = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        sizes = configs.getOutputSizes(SurfaceHolder.class);
                        break;
                    }
                }
            } catch (CameraAccessException ex) {
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                if (activeSession != null) {
                    activeSession.close();
                    activeSession = null;
                }
                camera.close();
                camera = null;
            }
            holder.removeCallback(this);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // preview form factor
            float ff = (float) w / h;

            // holder for the best form factor and size
            float bff = 0;
            int bestw = 0;
            int besth = 0;

            // we look for the best preview size, it has to be the closest
            // to the
            // screen form factor, and be less wide than the screen itself
            // the latter requirement is because the HTC Hero with update
            // 2.1 will
            // report camera preview sizes larger than the screen, and it
            // will fail
            // to initialize the camera
            // other devices could work with previews larger than the screen
            // though
            if(sizes!=null) {
                for (Size curSize : sizes) {
                    // current form factor
                    float cff = (float) curSize.getWidth() / curSize.getHeight();
                    // check if the current element is a candidate to replace
                    // the best match so far
                    // current form factor should be closer to the bff
                    // preview width should be less than screen width
                    // preview width should be more than current bestw
                    // this combination will ensure that the highest resolution
                    // will win
                    if ((ff - cff <= ff - bff) && (curSize.getWidth() <= w)
                            && (curSize.getWidth() >= bestw)) {
                        bff = cff;
                        bestw = curSize.getWidth();
                        besth = curSize.getHeight();
                    }
                }
            }
            // Some Samsung phones will end up with bestw and besth = 0
            // because their minimum preview size is bigger then the screen
            // size.
            // In this case, we use the default values: 480x320
            if ((bestw == 0) || (besth == 0)) {
                bestw = 1080;
                besth = 1920;
            }

            holder.setFixedSize(1920,1080);
        }

        private void openCamera() {
            try{
                if (ActivityCompat.checkSelfPermission(this.mixViewActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback(){
                    @Override
                    public void onOpened(@NonNull CameraDevice cameraDevice) {
                        Camera2Surface.this.camera = cameraDevice;
                        createPreviewSession();
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice cameraDevice) {

                    }

                    @Override
                    public void onError(@NonNull CameraDevice cameraDevice, int i) {

                    }
                }, null);
            } catch (CameraAccessException ex) {
            }
        }

        private void createPreviewSession() {
            List<Surface> outputs = new ArrayList<>();
            outputs.add(holder.getSurface());
            try {
                final CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.addTarget(holder.getSurface());
                camera.createCaptureSession(outputs, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        try {
                            cameraCaptureSession.setRepeatingRequest(builder.build(),null,null);
                            Camera2Surface.this.activeSession = cameraCaptureSession;
                        } catch (CameraAccessException ex){
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    }
                }, null);
            } catch (CameraAccessException ex) {
            }
        }
    }

	/**
     * @author daniele
     *
     */

	static class AugmentedView extends View {
        MixView app;
        int xSearch = 200;
        int ySearch = 10;
        int searchObjWidth = 0;
        int searchObjHeight = 0;

        Paint zoomPaint = new Paint();

        public AugmentedView(Context context) {
            super(context);
            try {
                app = (MixView) context;

                app.killOnError();
            } catch (Exception ex) {
                app.doError(ex);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            try {
                app.killOnError();

                getdWindow().setWidth(canvas.getWidth());
                getdWindow().setHeight(canvas.getHeight());

                getdWindow().setCanvas(canvas);

                if (!getDataView().isInited()) {
                    getDataView().init(getdWindow().getWidth(),
                            getdWindow().getHeight());
                }
                if (app.isZoombarVisible()) {
                    zoomPaint.setColor(Color.WHITE);
                    zoomPaint.setTextSize(14);
                    String startKM, endKM ;
                    startKM = "200m";
                    endKM = "800m";

                    canvas.drawText(startKM, canvas.getWidth() / 100 * 4,
                            canvas.getHeight() / 100 * 85, zoomPaint);
                    canvas.drawText(endKM, canvas.getWidth() / 100 * 99 + 25,
                            canvas.getHeight() / 100 * 85, zoomPaint);

                    int height = canvas.getHeight() / 100 * 85;
                    int zoomProgress = app.getZoomProgress();
                    if (zoomProgress > 92 || zoomProgress < 6) {
                        height = canvas.getHeight() / 100 * 80;
                    }
                    canvas.drawText(app.getZoomLevel(), (canvas.getWidth()) / 100
                            * zoomProgress + 20, height, zoomPaint);
                }

                getDataView().draw(getdWindow());
            } catch (Exception ex) {
                app.doError(ex);
            }
        }
    }

	/**
     * Internal class that holds Mixview field Dataclass.
     *
     * @author A B
     */
	static class MixViewDataHolder {
        private final MixContext mixContext;
        private float[] RTmp;
        private float[] Rot;
        private float[] I;
        private float[] grav;
        private float[] mag;
        private SensorManager sensorMgr;
        private List<Sensor> sensors;
        private Sensor sensorGrav;
        private Sensor sensorMag;
        private int rHistIdx;
        private Matrix tempR;
        private Matrix finalR;
        private Matrix smoothR;
        private Matrix[] histR;
        private Matrix m1;
        private Matrix m2;
        private Matrix m3;
        private Matrix m4;
        private SeekBar myZoomBar;
        private PowerManager.WakeLock mWakeLock;
        private int compassErrorDisplayed;
        private String zoomLevel;
        private int zoomProgress;
        private TextView searchNotificationTxt;

        public MixViewDataHolder(MixContext mixContext) {
            this.mixContext=mixContext;
            this.RTmp = new float[9];
            this.Rot = new float[9];
            this.I = new float[9];
            this.grav = new float[3];
            this.mag = new float[3];
            this.rHistIdx = 0;
            this.tempR = new Matrix();
            this.finalR = new Matrix();
            this.smoothR = new Matrix();
            this.histR = new Matrix[60];
            this.m1 = new Matrix();
            this.m2 = new Matrix();
            this.m3 = new Matrix();
            this.m4 = new Matrix();
            this.compassErrorDisplayed = 0;
        }

        /* ******* Getter and Setters ********** */
        public MixContext getMixContext() {
            return mixContext;
        }

        public float[] getRTmp() {
            return RTmp;
        }

        public void setRTmp(float[] rTmp) {
            RTmp = rTmp;
        }

        public float[] getRot() {
            return Rot;
        }

        public void setRot(float[] rot) {
            Rot = rot;
        }

        public float[] getI() {
            return I;
        }

        public void setI(float[] i) {
            I = i;
        }

        public float[] getGrav() {
            return grav;
        }

        public void setGrav(float[] grav) {
            this.grav = grav;
        }

        public float[] getMag() {
            return mag;
        }

        public void setMag(float[] mag) {
            this.mag = mag;
        }

        public SensorManager getSensorMgr() {
            return sensorMgr;
        }

        public void setSensorMgr(SensorManager sensorMgr) {
            this.sensorMgr = sensorMgr;
        }

        public List<Sensor> getSensors() {
            return sensors;
        }

        public void setSensors(List<Sensor> sensors) {
            this.sensors = sensors;
        }

        public Sensor getSensorGrav() {
            return sensorGrav;
        }

        public void setSensorGrav(Sensor sensorGrav) {
            this.sensorGrav = sensorGrav;
        }

        public Sensor getSensorMag() {
            return sensorMag;
        }

        public void setSensorMag(Sensor sensorMag) {
            this.sensorMag = sensorMag;
        }

        public int getrHistIdx() {
            return rHistIdx;
        }

        public void setrHistIdx(int rHistIdx) {
            this.rHistIdx = rHistIdx;
        }

        public Matrix getTempR() {
            return tempR;
        }

        public void setTempR(Matrix tempR) {
            this.tempR = tempR;
        }

        public Matrix getFinalR() {
            return finalR;
        }

        public void setFinalR(Matrix finalR) {
            this.finalR = finalR;
        }

        public Matrix getSmoothR() {
            return smoothR;
        }

        public void setSmoothR(Matrix smoothR) {
            this.smoothR = smoothR;
        }

        public Matrix[] getHistR() {
            return histR;
        }

        public void setHistR(Matrix[] histR) {
            this.histR = histR;
        }

        public Matrix getM1() {
            return m1;
        }

        public void setM1(Matrix m1) {
            this.m1 = m1;
        }

        public Matrix getM2() {
            return m2;
        }

        public void setM2(Matrix m2) {
            this.m2 = m2;
        }

        public Matrix getM3() {
            return m3;
        }

        public void setM3(Matrix m3) {
            this.m3 = m3;
        }

        public Matrix getM4() {
            return m4;
        }

        public void setM4(Matrix m4) {
            this.m4 = m4;
        }

        public SeekBar getMyZoomBar() {
            return myZoomBar;
        }

        public void setMyZoomBar(SeekBar myZoomBar) {
            this.myZoomBar = myZoomBar;
        }

        public PowerManager.WakeLock getmWakeLock() {
            return mWakeLock;
        }

        public void setmWakeLock(PowerManager.WakeLock mWakeLock) {
            this.mWakeLock = mWakeLock;
        }

        public int getCompassErrorDisplayed() {
            return compassErrorDisplayed;
        }

        public void setCompassErrorDisplayed(int compassErrorDisplayed) {
            this.compassErrorDisplayed = compassErrorDisplayed;
        }

        public String getZoomLevel() {
            return zoomLevel;
        }

        public void setZoomLevel(String zoomLevel) {
            this.zoomLevel = zoomLevel;
        }

        public int getZoomProgress() {
            return zoomProgress;
        }

        public void setZoomProgress(int zoomProgress) {
            this.zoomProgress = zoomProgress;
        }

        public TextView getSearchNotificationTxt() {
            return searchNotificationTxt;
        }

        public void setSearchNotificationTxt(TextView searchNotificationTxt) {
            this.searchNotificationTxt = searchNotificationTxt;
        }
    }
}


