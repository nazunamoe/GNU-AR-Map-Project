package com.gnuarmap.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gnuarmap.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    SharedPreferences sharedPref;
    State state = State.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = this.getApplicationContext();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Initialize();
        Log.d("mixare","Initialize start");
        startActivity(new Intent(ctx, MenuActivity.class));
        finish();
    }

    // 초기화 메소드, for문으로 모든 세팅값을 돌면서 각자의 원래 값을 찾아서 설정한다
    public void Initialize(){
        for(int i=0; i<ListSize; i++){
            if(sharedPref.getBoolean(List[i],false)){
                setState(List[i],true);
            }else{
                setState(List[i],false);
            }
        }
        if(sharedPref.getString(List[23],null).equals(getString(R.string.NaverWebAPI))){
            state.api = 0;
        }else if(sharedPref.getString(List[23],null).equals(getString(R.string.KakaoAppAPI))){
            state.api = 1;
        }else if(sharedPref.getString(List[23],null).equals(getString(R.string.KakaoWebAPI))){
            state.api = 2;
        }
    }

    public String[] List = {"Camera","MMapViewSet","MoreView","All","ATM","CVS","Vending","Printer","AllBuilding","Business","Engineering",
            "Dormitory","ETC","Agriculture","University","Club","Door","Law","Education","Social","Veterinary","Leisure","Science","mapAPI"};
    public int ListSize = 23;
    // 초기화 해야할 세팅값 목록

    // 스위치 문을 통해서 각 값들을 설정한다
    private void setState(String key, boolean value){
        switch(key){
            case "Camera2":{state.Camera2 = value; break;}
            case "MMapViewSet":{state.NMapState = value; break;}
            case "MoreView":{state.MoreView = value; break;}
            case "All":{state.All = value; break;}
            case "ATM":{state.ATM = value; break;}
            case "CVS":{state.CVS = value; break;}
            case "Vending":{state.Vending = value; break;}
            case "Printer":{state.Printer = value; break;}
            case "AllBuilding":{state.AllBuilding = value; break;}
            case "Business":{state.Business = value; break;}
            case "Engineering":{state.Engineering = value; break;}
            case "Dormitory":{state.Dormitory = value; break;}
            case "ETC":{state.ETC = value; break;}
            case "Agricultire":{state.Agriculture = value; break;}
            case "University":{state.University = value; break;}
            case "Club":{state.Club = value; break;}
            case "Door":{state.Door = value; break;}
            case "Law":{state.Law = value; break;}
            case "Education":{state.Education = value; break;}
            case "Social":{state.Social = value; break;}
            case "Vaterinary":{state.Veterinary = value; break;}
            case "Leisure":{state.Leisure = value; break;}
            case "Science":{state.Science = value; break;}
        }
    }
}