package com.gnuarmap.data.convert;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.gnuarmap.FilteringState;
import com.gnuarmap.SocialMarker;
import com.gnuarmap.State;
import org.mixare.lib.HtmlUnescape;
import java.util.ArrayList;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class DataClass {

    public ArrayList<SocialMarker> List = new ArrayList<SocialMarker>();
    public static Bitmap basic;
    private String Filtering1;
    private String Filtering2;


    public int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public void addItem(String name, String url, double Latitude, double Longitude, String type, String Filtering1, String Filtering2, int no){
        FilteringState state = FilteringState.getInstance();
        int value = 0;
        this.Filtering1 = Filtering1;
        this.Filtering2 = Filtering2;
        int number = List.size();
        String num = String.valueOf(number); // ID값은 사이즈에 따라서 결정됨
        if(state.All){
            addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
        }else{
            if(state.Agriculture){
                if(Filtering1=="agriculture"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Business){
                if(Filtering1=="business"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Engnieering){
                if(Filtering1=="engnieering"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Dormitory){
                if(Filtering1=="dormitory"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.ETC){
                if(Filtering1=="etc"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.University){
                if(Filtering1=="university"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Club){
                if(Filtering1=="club"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Door){
                if(Filtering1=="door"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Law){
                if(Filtering1=="law"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Education){
                if(Filtering1=="education"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Social){
                if(Filtering1=="social"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Veterinary){
                if(Filtering1=="veterinary"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Leisure){
                if(Filtering1=="leisure"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Humanities){
                if(Filtering1=="humanities"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
            if(state.Science){
                if(Filtering1=="science"){
                    addMarker(Integer.toString(no),Latitude,Longitude,url,value,type,name);
                }
            }
        }
        // 건물 필터링, 이 다음에 2차 필터링도 넣어야 함.
    }

    private void addMarker(String num, Double Latitude, Double Longitude, String url, int value, String type, String name){
        SocialMarker item = new SocialMarker(
                num,
                HtmlUnescape.unescapeHTML((name), 0),
                Latitude,
                Longitude,
                0, // 소셜 마커이므로 고도에 구애받지 않는다.
                url,
                1,
                value,
                type,
                Filtering1,
                Filtering2);
        List.add(item);
    }

    public int getSize(){
        return List.size();
    }

    public SocialMarker getData(int index){
        return List.get(index);
    }

    public String getFilter1(int index){
        return List.get(index).filter1;
    }

    public String getFilter2(int index){
        return List.get(index).filter2;
    }
}
