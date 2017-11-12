package com.gnuarmap;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.gnuarmap.Fragments.AR_View_Fragment;
import com.gnuarmap.Fragments.Naver_Map_Fragment;
import com.gnuarmap.Fragments.Filtering_Fragment;

import org.gnuarmap.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.settings_arview:
                    setTitle(R.string.settings_drawer_arview);
                    AR_View_Fragment ARView = new AR_View_Fragment();
                    android.support.v4.app.FragmentTransaction transaction_ar = getSupportFragmentManager().beginTransaction();
                    transaction_ar.replace(R.id.fram,ARView,"AR View");
                    transaction_ar.commit();
                    return true;
                case R.id.settings_navermap:
                    setTitle(R.string.settings_drawer_navermap);
                    Naver_Map_Fragment NaverMap = new Naver_Map_Fragment();
                    android.support.v4.app.FragmentTransaction transaction_naver = getSupportFragmentManager().beginTransaction();
                    transaction_naver.replace(R.id.fram,NaverMap,"AR View");
                    transaction_naver.commit();
                    return true;
                case R.id.settings_filtering :
                    setTitle(R.string.settings_drawer_filtering);
                    Filtering_Fragment Filtering = new Filtering_Fragment();
                    android.support.v4.app.FragmentTransaction transaction_filter = getSupportFragmentManager().beginTransaction();
                    transaction_filter.replace(R.id.fram,Filtering,"AR View");
                    transaction_filter.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        Context ctx;
        ctx = this;
        startActivity(new Intent(ctx, MenuActivity.class));
        finish();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle("AR View");
        AR_View_Fragment ARView = new AR_View_Fragment();
        android.support.v4.app.FragmentTransaction transaction_ar = getSupportFragmentManager().beginTransaction();
        transaction_ar.commit();

    }

}
