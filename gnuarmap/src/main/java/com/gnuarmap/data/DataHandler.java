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

package com.gnuarmap.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.gnuarmap.MixContext;

import org.mixare.lib.marker.Marker;

import android.location.Location;

/**
 * DataHandler is the model which provides the Marker Objects with its data.
 * DataHandler is also the Factory for new Marker objects.
 */
public class DataHandler {

	// 완성된 마커 리스트
	private List<Marker> markerList = new ArrayList<Marker>();

	public void addMarkers(List<Marker> markers) {
	//	DataBase data = new DataBase();
		Sample data = new Sample();
		data.Initialize();

		for(int i=0; i<data.data.getSize(); i++){
			markerList.add(data.data.List.get(i));
		}
		/**
		 * 독립된 데이터 프로세서 대신 이 부분에 marker 배열에 필요한 마커 데이터를 직접 등록한다.
		 */

		//Log.d(ARView.TAG, "Marker count: "+markerList.size());
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
