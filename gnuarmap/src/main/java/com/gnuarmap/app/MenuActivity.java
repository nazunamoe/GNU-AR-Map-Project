package com.gnuarmap.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gnuarmap.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public Context getContext() {
        return getApplicationContext();
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //Toast.makeText(MenuActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //Toast.makeText(MenuActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Context ctx;
        int gpsCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int select = (int) parent.getItemIdAtPosition(position);
        switch (select) {
            case 0: { // AR 뷰
                ctx = this;
                LocationManager current = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location currentGPSInfo = current.getLastKnownLocation(NETWORK_PROVIDER);
                if (current.isProviderEnabled(GPS_PROVIDER)) {
                    currentGPSInfo = current.getLastKnownLocation(GPS_PROVIDER);
                }
                if (gpsCheck == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
                } else if (gpsCheck == PackageManager.PERMISSION_GRANTED) {
                    try{
                        double d = currentGPSInfo.getAltitude();
                        startActivity(new Intent(ctx, MixView.class));
                        finish();
                    }catch(NullPointerException e){
                        Toast.makeText(getApplicationContext(), R.string.GPSerror, Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(ctx, MixView.class));
                    finish();
                }
                break;
            }
            case 1: { // 네이버 지도
                ctx = this;
                if (gpsCheck == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
                } else if (gpsCheck == PackageManager.PERMISSION_GRANTED) {
                    LocationManager current = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location currentGPSInfo = current.getLastKnownLocation(NETWORK_PROVIDER);
                    if (current.isProviderEnabled(GPS_PROVIDER)) {
                        currentGPSInfo = current.getLastKnownLocation(GPS_PROVIDER);
                    }
                    try{
                        double d = currentGPSInfo.getAltitude();
                        Intent intent = new Intent(ctx, NaverMapActivity.class);
                        intent.putExtra("return", 0);
                        startActivity(intent);
                        finish();
                    }catch(NullPointerException e){
                        Toast.makeText(getApplicationContext(), R.string.GPSerror, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case 2: { // 검색
                ctx = this;
                startActivity(new Intent(ctx, SearchActivity.class));
                finish();
                break;
            }
            case 3: { // 설정
                ctx = this;
                startActivity(new Intent(ctx, SettingsActivity.class));
                finish();
                break;
            }
            case 4: { // 라이센스
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage(getString(R.string.license));
                builder1.setNegativeButton(getString(R.string.close_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert1 = builder1.create();
                alert1.setTitle(getString(R.string.license_title));
                alert1.show();
                break;
            }
            case 6: { // 이메일
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://anse.gnu.ac.kr"));
                startActivity(intent);
                break;
            }
            case 5: { // 홈페이지
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"jpg3927@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                startActivity(email);
                break;
            }
            case 7: { // 현재 위치
                LocationManager current = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                try {
                    try{
                        Location currentGPSInfo = current.getLastKnownLocation(NETWORK_PROVIDER);
                        if (current.isProviderEnabled(GPS_PROVIDER)) {
                            currentGPSInfo = current.getLastKnownLocation(GPS_PROVIDER);
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(getString(R.string.general_info_text) + "\n\n"
                                + getString(R.string.longitude)
                                + currentGPSInfo.getLongitude() + "\n"
                                + getString(R.string.latitude)
                                + currentGPSInfo.getLatitude() + "\n"
                                + getString(R.string.altitude)
                                + currentGPSInfo.getAltitude() + "m\n"
                                + getString(R.string.speed) + currentGPSInfo.getSpeed()
                                + "km/h\n" + getString(R.string.accuracy)
                                + currentGPSInfo.getAccuracy() + "m\n"
                                + getString(R.string.gps_last_fix)
                                + new Date(currentGPSInfo.getTime()).toString() + "\n");
                        builder.setNegativeButton(getString(R.string.close_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.general_info_title));
                        alert.show();
                    }catch(NullPointerException e){
                        Toast.makeText(getApplicationContext(), R.string.GPSerror, Toast.LENGTH_SHORT).show();
                    }
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 8: { // 종료
                finish();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.permission_rejected)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.MenuList, android.R.layout.simple_list_item_1);
        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
    }
}
