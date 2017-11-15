package com.gnuarmap.data.convert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;

import com.gnuarmap.LocalMarker;
import com.gnuarmap.MixView;
import com.gnuarmap.POIMarker;
import com.gnuarmap.R;
import com.gnuarmap.SocialMarker;

import org.mixare.lib.HtmlUnescape;
import java.util.ArrayList;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class DataClass {

    public ArrayList<SocialMarker> List = new ArrayList<SocialMarker>();
    public static Bitmap basic;
    public int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    //TODO : 데이터 추가하는 부분, 이부분에 이미지도 삽입해야함, POIMarker도 수정해야할 필요가 있음
    public void addItem( String name, String url, double Latitude, double Longitude, String color,int Height){
        int value = 0;
        switch(color){
            case "BLACK":{
                value = Color.BLACK;
                break;
            }
            case "RED":{
                value = Color.RED;
                break;
            }
            case "GREEN":{
                value = Color.GREEN;
                break;
            }
            case "BLUE":{
                value = Color.BLUE;
                break;
            }
            case "YELLOW":{
                value = Color.YELLOW;
                break;
            }
            case "PINK":{
                value = getIntFromColor(255,144,190);
                break;
            }

            default:{
                value = Color.BLACK;
                break;
            }
        }
        int number = List.size();
        String num = String.valueOf(number); // ID값은 사이즈에 따라서 결정됨
        SocialMarker item = new SocialMarker(
                num,
                HtmlUnescape.unescapeHTML((name), 0),
                Latitude,
                Longitude,
                Height,
                url,
                1,
                value);
        List.add(item);
    }

    public int getSize(){
        return List.size();
    }

    public SocialMarker getData(int index){
        return List.get(index);
    }

}
