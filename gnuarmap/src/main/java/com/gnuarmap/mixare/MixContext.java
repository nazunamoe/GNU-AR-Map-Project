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
package com.gnuarmap.mixare;

import org.mixare.lib.MixContextInterface;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.render.Matrix;

import com.gnuarmap.Location.LocationFinder;
import com.gnuarmap.Location.LocationFinderFactory;
import com.gnuarmap.NaverMap.NaverMapActivity;
import com.gnuarmap.mixare.WebContent.WebContentManager;
import com.gnuarmap.mixare.WebContent.WebContentManagerFactory;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Toast;

/**
 * Cares about location management and about the data (source, inputstream)
 */
public class MixContext extends ContextWrapper implements MixContextInterface {

	// TAG for logging
	public static final String TAG = "Mixare";

	private MixView mixView;

	private Matrix rotationM = new Matrix();

	/** Responsible for all location tasks */
	private LocationFinder locationFinder;

	/** Responsible for Web Content */
	private WebContentManager webContentManager;

	public MixContext(MixView appCtx) {
		super(appCtx);
		mixView = appCtx;
		getLocationFinder().switchOn();
		getLocationFinder().findLocation();
	}

	public String getStartUrl() {
		Intent intent = ((Activity) getActualMixView()).getIntent();
		if (intent.getAction() != null
				&& intent.getAction().equals(Intent.ACTION_VIEW)) {
			return intent.getData().toString();
		} else {
			return "";
		}
	}

	public void getRM(Matrix dest) {
		synchronized (rotationM) {
			dest.set(rotationM);
		}
	}

	public void MarkerMenu(Marker marker){
		Context ctx;
		ctx=this;
		Intent intent;
		intent = new Intent(ctx, NaverMapActivity.class);
		intent.putExtra("Return", "True");
		intent.putExtra("set", "True");
		intent.putExtra("num", marker.getNum());

		startActivity(intent);
		/*Intent menu = new Intent(ctx, MarkerActivity.class);
		menu.putExtra("Title",marker.getTitle());
		menu.putExtra("Latitude",marker.getLatitude());
		menu.putExtra("Longitude",marker.getLongitude());
		menu.putExtra("Altitude",marker.getAltitude());
		menu.putExtra("URL",marker.getURL());
		menu.putExtra("num",marker.getNum());
		//menu.putExtra("Filtering - 1",marker.getTitle());
		//menu.putExtra("Filtering - 1",marker.getTitle());
		//필터링 기준을 정해서 마커 액티비티로 넘겨준다.
		startActivity(menu);*/
	}

	/**
	 * Shows a webpage with the given url when clicked on a marker.
	 */
	public void loadMixViewWebPage(String url) throws Exception {
		// TODO: CHECK INTERFACE METHOD
		getWebContentManager().loadWebPage(url, getActualMixView());
	}

	public void doResume(MixView mixView) {
		setActualMixView(mixView);
	}

	public void updateSmoothRotation(Matrix smoothR) {
		synchronized (rotationM) {
			rotationM.set(smoothR);
		}
	}


	public LocationFinder getLocationFinder() {
		if (this.locationFinder == null) {
			locationFinder = LocationFinderFactory.makeLocationFinder(this);
		}
		return locationFinder;
	}

	public WebContentManager getWebContentManager() {
		if (this.webContentManager == null) {
			webContentManager = WebContentManagerFactory
					.makeWebContentManager(this);
		}
		return webContentManager;
	}

	public MixView getActualMixView() {
		synchronized (mixView) {
			return this.mixView;
		}
	}

	private void setActualMixView(MixView mv) {
		synchronized (mixView) {
			this.mixView = mv;
		}
	}

	public ContentResolver getContentResolver() {
		ContentResolver out = super.getContentResolver();
		if (super.getContentResolver() == null) {
			out = getActualMixView().getContentResolver();
		}
		return out;
	}
	
	/**
	 * Toast POPUP notification
	 * 
	 * @param string message
	 */
	public void doPopUp(final String string){
       Toast.makeText(this,string,Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast POPUP notification
	 *
	 */
	public void doPopUp(int RidOfString) {
        doPopUp(this.getString(RidOfString));
	}
}
