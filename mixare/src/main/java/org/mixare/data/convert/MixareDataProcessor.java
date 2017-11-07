/*
 * Copyright (C) 2012- Peer internet solutions & Finalist IT Group
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
package org.mixare.data.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.MixView;
import org.mixare.POIMarker;
import org.mixare.data.DataHandler;
import org.mixare.lib.HtmlUnescape;
import org.mixare.lib.marker.Marker;

import android.util.Log;

/**
 * A data processor for custom urls or data, Responsible for converting raw data (to json and then) to marker data.
 * @author A. Egal
 */
public class MixareDataProcessor extends DataHandler implements DataProcessor{

	public static final int MAX_JSON_OBJECTS = 1000;
	
	@Override
	public String[] getUrlMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}

	@Override
	public String[] getDataMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}
	
	@Override
	public boolean matchesRequiredType(String type) {
		return true; //this datasources has no required type, it will always match.
	}

	@Override
	public List<Marker> load(String rawData, int taskId, int colour) throws JSONException {
		List<Marker> markers = new ArrayList<Marker>();
		JSONObject root = convertToJSON(rawData);
		JSONArray dataArray = root.getJSONArray("results");
		int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());

		for (int i = 0; i < top; i++) {
			JSONObject jo = dataArray.getJSONObject(i);
			
			Marker ma = null;
			if (jo.has("title") && jo.has("lat") && jo.has("lng")
					&& jo.has("elevation")) {

				String id = "";
				if(jo.has("id"))
						id = jo.getString("id");
				
				Log.v(MixView.TAG, "processing Mixare JSON object");
				String link=null;
		
				if(jo.has("has_detail_page") && jo.getInt("has_detail_page")!=0 && jo.has("webpage"))
					link=jo.getString("webpage");
				
				ma = new POIMarker(
						id,
						HtmlUnescape.unescapeHTML(jo.getString("title"), 0), 
						jo.getDouble("lat"), 
						jo.getDouble("lng"), 
						jo.getDouble("elevation"), 
						link, 
						taskId, colour);
				// 기본적으로 mixare에서 사용하는 마커의 데이터 형식은 위와 같음
				// 각각 id, 이름 (아무래도 하이퍼링크?), 위도 경도, 높이, 링크, taskId, 색을 나타내는데 
				// 이것을 이용해서 우리가 새로 데이터 프로세서를 만들어야할듯 
				// taskid는 아무리봐도 쓸데가 없움, 그냥 0으로 세팅하면 될듯
				// 색상은 나중에 조건문으로 적당히 세팅.
				// 색상을 int형으로 받는 부분은 https://stackoverflow.com/questions/18022364/how-to-convert-rgb-color-to-int-in-java 를 참조하면 될듯함
				
				// 결론 : ID, 이름 (이곳에서는 JSON 데이터 처리 때문에 복잡해보이지만 결론은 이름이다), 위도, 경도, 높이, URL, taskID(0으로 일단 세팅), 컬러(위 링크 참조)
				// 로 데이터 형을 구성해서 목록을 넘겨줄수 있으면 OK, 데이터베이스 없이 구현한다.
				markers.add(ma);
			}
		}
		return markers;
	}
	
	private JSONObject convertToJSON(String rawData){
		try {
			return new JSONObject(rawData);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
