package com.gnuarmap.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gnuarmap.R;

/**
 * 앱 초기에 실행되서 각종 설정값의 초기화를 수행
 */

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    State state = State.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = this.getApplicationContext();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Initialize();
        Log.d("mixare", "Initialize start");
        startActivity(new Intent(ctx, MenuActivity.class));
        finish();
    }

    // 초기화 메소드, for문으로 모든 세팅값을 돌면서 각자의 원래 값을 찾아서 설정한다
    public void Initialize() {
        for (int i = 0; i < ListSize; i++) {
            if (sharedPref.getBoolean(List[i], false)) {
                setState(List[i], true);
            } else {
                setState(List[i], false);
            }
        }
        if (getString(R.string.NaverWebAPI).equals(sharedPref.getString(List[23], null))) {
            state.api = 0;
        } else if (getString(R.string.KakaoWebAPI).equals(sharedPref.getString(List[23], null))) {
            state.api = 2;
        } else if (getString(R.string.KakaoAppAPI).equals(sharedPref.getString(List[23], null))) {
            state.api = 1;
        }
    }

    public String[] List = {"Camera", "MMapViewSet", "MoreView", "All", "ATM", "CVS", "Vending", "Printer", "AllBuilding", "Business", "Engineering",
            "Dormitory", "ETC", "Agriculture", "University", "Club", "Door", "Law", "Education", "Social", "Veterinary", "Leisure", "Science", "mapAPI"};
    public int ListSize = 23;
    // 초기화 해야할 세팅값 목록

    // 스위치 문을 통해서 각 값들을 설정한다
    private void setState(String key, boolean value) {
        switch (key) {
            case "Camera2": {
                state.Camera2 = value;
                break;
            }
            case "MMapViewSet": {
                state.NMapState = value;
                break;
            }
            case "MoreView": {
                state.MoreView = value;
                break;
            }
            case "All": {
                state.All = value;
                break;
            }
            case "ATM": {
                state.ATM = value;
                break;
            }
            case "CVS": {
                state.CVS = value;
                break;
            }
            case "Vending": {
                state.Vending = value;
                break;
            }
            case "Printer": {
                state.Printer = value;
                break;
            }
            case "AllBuilding": {
                state.AllBuilding = value;
                break;
            }
            case "Business": {
                state.Business = value;
                break;
            }
            case "Engineering": {
                state.Engineering = value;
                break;
            }
            case "Dormitory": {
                state.Dormitory = value;
                break;
            }
            case "ETC": {
                state.ETC = value;
                break;
            }
            case "Agricultire": {
                state.Agriculture = value;
                break;
            }
            case "University": {
                state.University = value;
                break;
            }
            case "Club": {
                state.Club = value;
                break;
            }
            case "Door": {
                state.Door = value;
                break;
            }
            case "Law": {
                state.Law = value;
                break;
            }
            case "Education": {
                state.Education = value;
                break;
            }
            case "Social": {
                state.Social = value;
                break;
            }
            case "Vaterinary": {
                state.Veterinary = value;
                break;
            }
            case "Leisure": {
                state.Leisure = value;
                break;
            }
            case "Science": {
                state.Science = value;
                break;
            }
        }
    }
}