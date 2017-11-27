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
import android.preference.PreferenceCategory;
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

import com.gnuarmap.R;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences sharedPref;
    SettingsFragment settings;
    FilteringState state = FilteringState.getInstance();
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

    private void pickPreferenceObject(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory cat = (PreferenceCategory) p;
            for (int i = 0; i < cat.getPreferenceCount(); i++) {
                pickPreferenceObject(cat.getPreference(i));
            }
        } else {
        }
    }

    // 설정 값을 변경할 때 이벤트 처리를 담당한다.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if("MMapViewSet".equals(key)){
            if(!sharedPreferences.getBoolean("MMapViewSet",false)){
                state.NMapState = false;
            }else if(sharedPreferences.getBoolean("MMapViewSet",false)){
                state.NMapState = true;
            }
        }else if("MoreView".equals(key)){
            if(!sharedPreferences.getBoolean("MoreView",false)){
                state.MoreView = false;
            }else if(sharedPreferences.getBoolean("MoreView",false)){
                state.MoreView = true;
            }Log.d("mixare",""+state.MoreView);
        }else if("All".equals(key)){
            if(!sharedPreferences.getBoolean("All",false)){
                state.All = false;
            }else if(sharedPreferences.getBoolean("All",false)){
                state.All = true;
            }
        }else if("ATM".equals(key)){
            if(!sharedPreferences.getBoolean("ATM",false)){
                state.ATM = false;
            }else if(sharedPreferences.getBoolean("ATM",false)){
                state.ATM = true;
            }
        }else if("CVS".equals(key)){
            if(!sharedPreferences.getBoolean("CVS",false)){
                state.CVS = false;
            }else if(sharedPreferences.getBoolean("CVS",false)){
                state.CVS = true;
            }
        }else if("Vending".equals(key)){
            if(!sharedPreferences.getBoolean("Vending",false)){
                state.Vending = false;
            }else if(sharedPreferences.getBoolean("Vending",false)){
                state.Vending = true;
            }
        }else if("Printer".equals(key)){
            if(!sharedPreferences.getBoolean("Printer",false)){
                state.Printer = false;
            }else if(sharedPreferences.getBoolean("Printer",false)){
                state.Printer = true;
            }
        }
        // 건물 필터링 시작
        else if("AllBuilding".equals(key)){
            if(!sharedPreferences.getBoolean("AllBuilding",false)){
                state.AllBuilding = false;
            }else if(sharedPreferences.getBoolean("AllBuilding",false)){
                state.AllBuilding = true;
            }
        }else if("Business".equals(key)){
            if(!sharedPreferences.getBoolean("Business",false)){
                state.Business = false;
            }else if(sharedPreferences.getBoolean("Business",false)){
                state.Business = true;
            }
        }else if("Engnieering".equals(key)){
            if(!sharedPreferences.getBoolean("Engnieering",false)){
                state.Engnieering = false;
            }else if(sharedPreferences.getBoolean("Engnieering",false)){
                state.Engnieering = true;
            }
        }else if("Dormitory".equals(key)){
            if(!sharedPreferences.getBoolean("Dormitory",false)){
                state.Dormitory = false;
            }else if(sharedPreferences.getBoolean("Dormitory",false)){
                state.Dormitory = true;
            }
        }else if("ETC".equals(key)){
            if(!sharedPreferences.getBoolean("ETC",false)){
                state.ETC = false;
            }else if(sharedPreferences.getBoolean("ETC",false)){
                state.ETC = true;
            }
        }else if("Agriculture".equals(key)){
            if(!sharedPreferences.getBoolean("Agriculture",false)){
                state.Agriculture = false;
            }else if(sharedPreferences.getBoolean("Agriculture",false)){
                state.Agriculture = true;
            }
        }else if("University".equals(key)){
            if(!sharedPreferences.getBoolean("University",false)){
                state.University = false;
            }else if(sharedPreferences.getBoolean("University",false)){
                state.University = true;
            }
        }else if("Club".equals(key)){
            if(!sharedPreferences.getBoolean("Club",false)){
                state.Club = false;
            }else if(sharedPreferences.getBoolean("Club",false)){
                state.Club = true;
            }
        }else if("Door".equals(key)){
            if(!sharedPreferences.getBoolean("Door",false)){
                state.Door = false;
            }else if(sharedPreferences.getBoolean("Door",false)){
                state.Door = true;
            }
        }else if("Law".equals(key)){
            if(!sharedPreferences.getBoolean("Law",false)){
                state.Law = false;
            }else if(sharedPreferences.getBoolean("Law",false)){
                state.Law = true;
            }
        }else if("Education".equals(key)){
            if(!sharedPreferences.getBoolean("Education",false)){
                state.Education = false;
            }else if(sharedPreferences.getBoolean("Education",false)){
                state.Education = true;
            }
        }else if("Social".equals(key)){
            if(!sharedPreferences.getBoolean("Social",false)){
                state.Social = false;
            }else if(sharedPreferences.getBoolean("Social",false)){
                state.Social = true;
            }
        }else if("Veterinary".equals(key)){
            if(!sharedPreferences.getBoolean("Veterinary",false)){
                state.Veterinary = false;
            }else if(sharedPreferences.getBoolean("Veterinary",false)){
                state.Veterinary = true;
            }
        }else if("Leisure".equals(key)){
            if(!sharedPreferences.getBoolean("Leisure",false)){
                state.Leisure = false;
            }else if(sharedPreferences.getBoolean("Leisure",false)){
                state.Leisure = true;
            }
        }else if("Humanities".equals(key)){
            if(!sharedPreferences.getBoolean("Humanities",false)){
                state.Humanities = false;
            }else if(sharedPreferences.getBoolean("Humanities",false)){
                state.Humanities = true;
            }
        }else if("Science".equals(key)){
            if(!sharedPreferences.getBoolean("Science",false)){
                state.Science = false;
            }else if(sharedPreferences.getBoolean("Science",false)){
                state.Science = true;
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
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {

                pickPreferenceObject(getPreferenceScreen().getPreference(i));
            }
        }
        private void pickPreferenceObject(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory cat = (PreferenceCategory) p;
                for (int i = 0; i < cat.getPreferenceCount(); i++) {
                    pickPreferenceObject(cat.getPreference(i));
                }
            } else {
            }
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
