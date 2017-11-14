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

import com.gnuarmap.R;

import android.content.res.Resources;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The DataSource class is able to create the URL where the information about a
 * place can be found.
 * 
 * @author hannes
 * 
 */
 
 // 이건 마커 정보에 따라서 URL을 생성하는 부분
public class DataSource extends Activity {

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

	private boolean enabled;
	private TYPE type;
	private DISPLAY display;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datasourcedetails);
		final EditText nameField = (EditText) findViewById(R.id.name);
		final EditText urlField = (EditText) findViewById(R.id.url);
		final Spinner typeSpinner = (Spinner) findViewById(R.id.type);
		final Spinner displaySpinner = (Spinner) findViewById(R.id.displaytype);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey("DataSourceId")) {
				String fields[] = DataSourceStorage.getInstance().getFields(
						extras.getInt("DataSourceId"));
				nameField.setText(fields[0], TextView.BufferType.EDITABLE);
				urlField.setText(fields[1], TextView.BufferType.EDITABLE);
				typeSpinner.setSelection(Integer.parseInt(fields[2]) - 3);
				displaySpinner.setSelection(Integer.parseInt(fields[3]));
			}
		}

	}

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


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			final EditText nameField = (EditText) findViewById(R.id.name);
			String name = nameField.getText().toString();
			final EditText urlField = (EditText) findViewById(R.id.url);
			String url = urlField.getText().toString();
			final Spinner typeSpinner = (Spinner) findViewById(R.id.type);
			int typeId = (int) typeSpinner.getItemIdAtPosition(typeSpinner
					.getSelectedItemPosition());
			final Spinner displaySpinner = (Spinner) findViewById(R.id.displaytype);
			int displayId = (int) displaySpinner
					.getItemIdAtPosition(displaySpinner
							.getSelectedItemPosition());


			DataSource newDS = new DataSource(name, url, typeId + 3, displayId,
					true);

			int index = DataSourceStorage.getInstance().getSize();
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				if (extras.containsKey("DataSourceId")) {
					index = extras.getInt("DataSourceId");
				}
			}
			DataSourceStorage.getInstance().add("DataSource" + index,
					newDS.serialize());
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int base = Menu.FIRST;
		menu.add(base, base, base, R.string.cancel);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public DataSource() {

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


	public int getColor() {
		int ret;
		switch (this.type) {
		case BUZZ:
			ret = Color.rgb(4, 228, 20);
			break;
		case TWITTER:
			ret = Color.rgb(50, 204, 255);
			break;
		case WIKIPEDIA:
			ret = Color.RED;
			break;
		case ARENA:
			ret = Color.RED;
			break;
		default:
			ret = Color.WHITE;
			break;
		}
		return ret;
	}

	public int getDataSourceIcon() {
		int ret;
		switch (this.type) {
		case BUZZ:
			ret = R.drawable.buzz;
			break;
		case TWITTER:
			ret = R.drawable.twitter;
			break;
		case OSM:
			ret = R.drawable.osm;
			break;
		case WIKIPEDIA:
			ret = R.drawable.wikipedia;
			break;
		case ARENA:
			ret = R.drawable.arena;
			break;
		default:
			ret = R.drawable.ic_launcher;
			break;
		}
		return ret;
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
