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
        if("ar_view_settings".equals(key)){
        }
        if("MMapViewSet".equals(key)){
            if(state.getNMapState()==0){
                state.setNMapState(1);
            }else if (state.getNMapState()==1){
                state.setNMapState(0);
            }

        }
        if("naver_map_settings".equals(key)){
        }
        if("filtering_settings".equals(key)){
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
