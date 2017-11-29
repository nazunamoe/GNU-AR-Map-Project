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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.gnuarmap.NaverMap.NaverMapActivity;

import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Context ctx;
        int gpsCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int select = (int)parent.getItemIdAtPosition(position);
        switch(select){
            case 0:{
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
            case 1:{
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
            case 2:{
                ctx = this;
                startActivity(new Intent(ctx, SearchActivity.class));
                finish();
                break;
            }
            case 3:{
                ctx = this;
                startActivity(new Intent(ctx, SettingsActivity.class));
                finish();
                break;
            }
            case 4:{
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
            case 5:{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://anse.gnu.ac.kr"));
                startActivity(intent);
                break;
            }
            case 6:{
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"jpg3927@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                startActivity(email);
                break;
            }
            case 7:{
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
            case 8:{
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
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.MenuList, android.R.layout.simple_list_item_1);
        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
    }
}
