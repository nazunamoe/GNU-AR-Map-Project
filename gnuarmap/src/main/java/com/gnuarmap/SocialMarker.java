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

package com.gnuarmap;

import org.mixare.lib.gui.PaintScreen;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.gnuarmap.data.DataSource;

/**
 * The SocialMarker class represents a marker, which contains data from
 * sources like twitter etc. Social markers appear at the top of the screen
 * and show a small logo of the source.
 * 
 * @author hannes
 *
 */
public class SocialMarker extends LocalMarker {

	public static final int MAX_OBJECTS=15;

	public String filter1;
	public String filter2[];

	public String getFlag;
	public SocialMarker(String id, String title, double latitude, double longitude,
			double altitude, String URL, int type, int color ,String flag, String filtering1, String filtering2[]) {
		super(id, title, latitude, longitude, altitude, URL, type, color);
		this.getFlag = flag;
		this.filter1 = filtering1;
		this.filter2 = filtering2;
	}

	@Override
	public void update(Location curGPSFix) {

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

			Bitmap bitmap = DataSource.getBitmap(getFlag);

			// 비트맵 파일이 읽혔다면 적절한 위치에 출력
			if(bitmap!=null) {
				Log.v(MixView.TAG, "fuck");
				dw.paintBitmap(bitmap, cMarker.x - maxHeight/1.5f, cMarker.y - maxHeight/0.6f);
			}
			else {	// 비트맵 파일을 갖지 않는 마커의 경우

				dw.setStrokeWidth(maxHeight / 10f);
				dw.setFill(false);
				//dw.setColor(DataSource.getColor(datasource));
				dw.paintCircle(cMarker.x, cMarker.y, maxHeight / 1.5f);
			}
		}
	}


	@Override
	public int getMaxObjects() {
		return MAX_OBJECTS;
	}


}
