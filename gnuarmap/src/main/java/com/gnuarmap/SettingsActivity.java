package com.gnuarmap;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.view.KeyEvent;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import com.gnuarmap.State;
import com.gnuarmap.R;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences sharedPref;
    SettingsFragment settings;

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Context ctx;
            ctx = this;
            startActivity(new Intent(ctx, MenuActivity.class));
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        /*actionBar.setBackgroundDrawable(new ColorDrawable(0xFF33B5E5));
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(0xFF33B5E5);
        }*/
        actionBar.setTitle(R.string.title_activity_settings);
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        settings = new SettingsFragment();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, settings);
        trans.commit();

    }


    // 설정 값을 변경할 때 이벤트 처리를 담당한다.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        State state = (State)getApplicationContext();
        FilteringState Filtering_state = FilteringState.getInstance();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if("MMapViewSet".equals(key)){
            switch(state.getNMapState()){
                case 0:{
                    state.setNMapState(1);
                    break;
                }
                case 1:{
                    state.setNMapState(0);
                    break;
                }
            }
        }else if("MoreView".equals(key)){
            switch(state.getMoreView()){
                case 0:{
                    state.setMoreView(1);
                    break;
                }
                case 1:{
                    state.setMoreView(0);
                    break;
                }
            }
            Log.v("mixare","  "+state.getMoreView());
        }else if("All".equals(key)){
            // 필터링 - 모두 보기 핸들러
        }else if("ATM".equals(key)){
            // 필터링 - ATM만 보기 핸들러
        }else if("CVS".equals(key)){
            // 필터링 - 편의점/매점만 보기 핸들러
        }else if("Vending".equals(key)){
            // 필터링 - 자판기만 보기 핸들러
        }else if("Printer".equals(key)){
            // 필터링 - 프린터만 보기 핸들러
        }
        // 각 건물에 맞는 필터링을 사용. 하나의 메소드로 처리한다.
        else if("AllBuilding".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.All);
            if(Filtering_state.All == true){
                Filtering_state.All = false;
            }else if(Filtering_state.All == false){
                Filtering_state.All = true;
            }
        }else if("Business".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Business);
            if(Filtering_state.Business == true){
                Filtering_state.Business = false;
            }else if(Filtering_state.Business == false){
                Filtering_state.Business = true;
            }
        }else if("Engnieering".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Engnieering);
            if(Filtering_state.Engnieering == true){
                Filtering_state.Engnieering = false;
            }else if(Filtering_state.Engnieering == false){
                Filtering_state.Engnieering = true;
            }
        }else if("Dormitory".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Dormitory);
            if(Filtering_state.Dormitory == true){
                Filtering_state.Dormitory = false;
            }else if(Filtering_state.Dormitory == false){
                Filtering_state.Dormitory = true;
            }
        }else if("ETC".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.ETC);
            if(Filtering_state.ETC == true){
                Filtering_state.ETC = false;
            }else if(Filtering_state.ETC == false){
                Filtering_state.ETC = true;
            }
        }else if("Agriculture".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Agriculture);
            if(Filtering_state.Agriculture == true){
                Filtering_state.Agriculture = false;
            }else if(Filtering_state.Agriculture == false){
                Filtering_state.Agriculture = true;
            }
        }else if("University".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.University);
            if(Filtering_state.University == true){
                Filtering_state.University = false;
            }else if(Filtering_state.University == false){
                Filtering_state.University = true;
            }
        }else if("Club".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Club);
            if(Filtering_state.Club == true){
                Filtering_state.Club = false;
            }else if(Filtering_state.Club == false){
                Filtering_state.Club = true;
            }
        }else if("Door".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Door);
            if(Filtering_state.Door == true){
                Filtering_state.Door = false;
            }else if(Filtering_state.Door == false){
                Filtering_state.Door = true;
            }
        }else if("Law".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Law);
            if(Filtering_state.Law == true){
                Filtering_state.Law = false;
            }else if(Filtering_state.Law == false){
                Filtering_state.Law = true;
            }
        }else if("Education".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Education);
            if(Filtering_state.Education == true){
                Filtering_state.Education = false;
            }else if(Filtering_state.Education == false){
                Filtering_state.Education = true;
            }
        }else if("Social".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Social);
            if(Filtering_state.Social == true){
                Filtering_state.Social = false;
            }else if(Filtering_state.Social == false){
                Filtering_state.Social = true;
            }
        }else if("Veterinary".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Veterinary);
            if(Filtering_state.Veterinary == true){
                Filtering_state.Veterinary = false;
            }else if(Filtering_state.Veterinary == false){
                Filtering_state.Veterinary = true;
            }
        }else if("Leisure".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Leisure);
            if(Filtering_state.Leisure == true){
                Filtering_state.Leisure = false;
            }else if(Filtering_state.Leisure == false){
                Filtering_state.Leisure = true;
            }
        }else if("Humanities".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Humanities);
            if(Filtering_state.Humanities == true){
                Filtering_state.Humanities = false;
            }else if(Filtering_state.Humanities == false){
                Filtering_state.Humanities = true;
            }
        }else if("Natrual".equals(key)){
            Log.d("mixare","status changed"+Filtering_state.Science);
            if(Filtering_state.Science == true){
                Filtering_state.Science = false;
            }else if(Filtering_state.Science == false){
                Filtering_state.Science = true;
            }
        }


        editor.commit();
    }

    public void changeStatus(boolean in){
        Log.d("mixare","status changed");
        if(in){
            in = false;
        }else if(!in){
            in = true;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    // 화면 구성을 위해 PreferenceFragment 를 상속받는 SettingsFragment class 를 구현한다.
    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }
}
