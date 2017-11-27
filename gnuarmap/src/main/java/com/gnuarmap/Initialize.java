package com.gnuarmap;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

public class Initialize extends AppCompatActivity{

    SharedPreferences sharedPref;
    FilteringState state = FilteringState.getInstance();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        Context ctx = this.getApplicationContext();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Initialize();
        startActivity(new Intent(ctx, MenuActivity.class));
        finish();
    }


    public void Initialize(){
        for(int i=0; i<ListSize; i++){
            if(sharedPref.getBoolean(List[i],false)){
                setState(List[i],true);
            }else{
                setState(List[i],false);
            }
        }
    }

    public String[] List = {"MMapViewSet","MoreView","All","ATM","CVS","Vending","Printer","AllBuilding","Business","Engnieering",
            "Dormitory","ETC","Agriculture","University","Club","Door","Law","Education","Social","Veterinary","Leisure","Science"};
    public int ListSize = 22;


    private void setState(String key, boolean value){
        switch(key){
            case "MMapViewSet":{state.NMapState = value; break;}
            case "MoreView":{state.MoreView = value; break;}
            case "All":{state.All = value; break;}
            case "ATM":{state.ATM = value; break;}
            case "CVS":{state.CVS = value; break;}
            case "Vending":{state.Vending = value; break;}
            case "Printer":{state.Printer = value; break;}
            case "AllBuilding":{state.AllBuilding = value; break;}
            case "Business":{state.Business = value; break;}
            case "Engnieering":{state.Engnieering = value; break;}
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