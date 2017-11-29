package com.gnuarmap;

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
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.gnuarmap.NaverMap.NaverMapActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context ctx;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.permission_rejected)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int gpsCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        switch(id){
            case R.id.nav_camera:{
                ctx = this;
                if(gpsCheck == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(getApplicationContext(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
                }
                else if(gpsCheck == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(ctx, ARView.class));
                    finish();
                }
                break;
            }
            case R.id.design_navigation_view:{
                ctx = this;
                if(gpsCheck == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(getApplicationContext(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
                }
                else if(gpsCheck == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(ctx,NaverMapActivity.class);
                    intent.putExtra("return",0);
                    startActivity(intent);
                    finish();
                }
                break;
            }
            case R.id.design_search:{
                ctx = this;
                startActivity(new Intent(ctx, SearchActivity.class));
                finish();
                break;
            }
            case R.id.nav_settings:{
                ctx = this;
                startActivity(new Intent(ctx, SettingsActivity.class));
                finish();
                break;
            }
            case R.id.nav_license:{
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
            case R.id.nav_homepage:{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://anse.gnu.ac.kr"));
                startActivity(intent);
                break;
            }
            case R.id.nav_email:{
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"jpg3927@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                startActivity(email);
                break;
            }
            case R.id.nav_information:{
                LocationManager current = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                int gps = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                try{
                    Location currentGPSInfo = current.getLastKnownLocation(NETWORK_PROVIDER);
                    if(current.isProviderEnabled(GPS_PROVIDER)){
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
                }catch(SecurityException e){
                    Toast.makeText(getApplicationContext(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
                }

			break;
			}
        }

        return true;
    }
}
