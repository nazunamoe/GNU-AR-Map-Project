package com.gnuarmap.data.convert;

import android.graphics.Color;

import com.gnuarmap.POIMarker;
import org.mixare.lib.HtmlUnescape;
import java.util.ArrayList;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class DataClass {

    public ArrayList<POIMarker> List = new ArrayList<POIMarker>();


    public void addItem(String number, String name, String url, double Latitude, double Longitude, int Height, String color){
        int value=0;
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
            default:{
                value = Color.BLACK;
                break;
            }
        }

        POIMarker item = new POIMarker(
                number,
                HtmlUnescape.unescapeHTML((name), 0),
                Latitude,
                Longitude,
                Height,
                url,
                0,
                value);
        List.add(item);
    }

    public int getSize(){
        return List.size();
    }

    public POIMarker getData(int index){
        return List.get(index);
    }

}
