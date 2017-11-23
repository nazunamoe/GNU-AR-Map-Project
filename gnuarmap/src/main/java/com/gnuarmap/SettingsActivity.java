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

        }else if("Business".equals(key)){

        }else if("Engnieering".equals(key)){

        }else if("Dormitory".equals(key)){

        }else if("ETC".equals(key)){

        }else if("Agriculture".equals(key)){

        }else if("University".equals(key)){

        }else if("Club".equals(key)){

        }else if("Door".equals(key)){

        }else if("Law".equals(key)){

        }else if("Education".equals(key)){

        }else if("Social".equals(key)){

        }else if("Veterinary".equals(key)){

        }else if("Leisure".equals(key)){

        }else if("Humanities".equals(key)){

        }else if("Natrual".equals(key)){

        }

        editor.commit();
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
