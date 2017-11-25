package com.gnuarmap.data.convert;

import android.graphics.Bitmap;
import android.graphics.Color;

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

    public void addItem(String name, String url, double Latitude, double Longitude, String type, String Filtering1, String Filtering2, int no){
        int value = 0;
        int number = List.size();
        String num = String.valueOf(number); // ID값은 사이즈에 따라서 결정됨
        SocialMarker item = new SocialMarker(
                num,
                HtmlUnescape.unescapeHTML((name), 0),
                Latitude,
                Longitude,
                0, // 소셜 마커이므로 고도에 구애받지 않는다.
                url,
                1,
                value,
                type);
        List.add(item);
    }

    public int getSize(){
        return List.size();
    }

    public SocialMarker getData(int index){
        return List.get(index);
    }

}
