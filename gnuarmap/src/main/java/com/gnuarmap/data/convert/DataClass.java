package com.gnuarmap.data.convert;

import android.graphics.Bitmap;

import com.gnuarmap.FilteringState;
import com.gnuarmap.SocialMarker;

import org.mixare.lib.HtmlUnescape;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class DataClass {
    public ArrayList<SocialMarker> WholeList = new ArrayList<SocialMarker>();
    public ArrayList<SocialMarker> List = new ArrayList<SocialMarker>();
    public static Bitmap basic;
    private String Filtering1;
    private String[] Filtering2;
    FilteringState state = FilteringState.getInstance();

    public int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public void addItem(String num, String name, String url, double Latitude, double Longitude, String type, String Filtering1, String[] Filtering2, int no){
        int value = 0;
        this.Filtering1 = Filtering1;
        this.Filtering2 = Filtering2;
        int number = List.size();
        WholeList.add(new SocialMarker(
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
                Filtering2)
        );
        if(state.AllBuilding){
            secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
        }else{
            if(state.Agriculture){
                if(Filtering1=="agriculture"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Business){
                if(Filtering1=="business"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Engnieering){
                if(Filtering1=="engnieering"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Dormitory){
                if(Filtering1=="dormitory"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.ETC){
                if(Filtering1=="etc"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.University){
                if(Filtering1=="university"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Club){
                if(Filtering1=="club"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Door){
                if(Filtering1=="door"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Law){
                if(Filtering1=="law"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Education){
                if(Filtering1=="education"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Social){
                if(Filtering1=="social"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Veterinary){
                if(Filtering1=="veterinary"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Leisure){
                if(Filtering1=="leisure"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Humanities){
                if(Filtering1=="humanities"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
            if(state.Science){
                if(Filtering1=="science"){
                    secondFiltering(num,Latitude,Longitude,url,value,type,name,Filtering2);
                }
            }
        }
        // 건물 필터링, 이 다음에 2차 필터링도 넣어야 함.
    }

    public String[] vending = {"vending"};
    public String[] printer = {"printer"};
    public String[] cvs = {"cvs"};
    public String[] atm = {"atm"};

    public String[] printer_cvs = {"printer","cvs"};
    public String[] printer_atm = {"printer","atm"};
    public String[] vending_atm = {"vending","atm"};
    public String[] vending_printer = {"vending","printer"};
    public String[] cvs_atm = {"cvs","atm"};

    public String[] vending_cvs_atm = {"vending","cvs","atm"};
    public String[] printer_cvs_atm = {"printer","cvs","atm"};

    public String[] nothing = {};

    private void secondFiltering(String num, Double Latitude, Double Longitude, String url, int value, String type, String name, String[] Filtering2){

        if(state.All){
            addMarker(num,Latitude,Longitude,url,value,type,name);}
        else if(state.Vending){
            if(state.ATM){
                if(Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("atm")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }else if(state.Printer){
                if(Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("printer")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }else if(state.CVS) {
                if(Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("cvs")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }else{
                if(Arrays.asList(Filtering2).contains("vending")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }
        }else if(state.Printer){
            if(state.CVS){
                if(state.ATM){
                    if(Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("cvs") && Arrays.asList(Filtering2).contains("atm")) {
                        addMarker(num,Latitude,Longitude,url,value,type,name);
                    }
                }else{
                    if(Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("cvs")){
                        addMarker(num,Latitude,Longitude,url,value,type,name);
                    }else{
                    }
                }
            }else if(state.ATM){
                if(Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("atm")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }else{
                if(Arrays.asList(Filtering2).contains("printer")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }
        }else if(state.CVS){
            if(state.ATM){
                if(Arrays.asList(Filtering2).contains("cvs") && Arrays.asList(Filtering2).contains("atm")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }else{
                if(Arrays.asList(Filtering2).contains("cvs")){
                    addMarker(num,Latitude,Longitude,url,value,type,name);
                }
            }
        }else if(state.ATM){
            if(Arrays.asList(Filtering2).contains("atm")){
                addMarker(num,Latitude,Longitude,url,value,type,name);
            }
        }else{
        }
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

    public SocialMarker getMarker(String no){
        int b=0;

        for(int a = 0; a<WholeList.size(); a++){
            Log.d("FAL",""+WholeList.get(a).NUM+","+no);
            if(no.equals(WholeList.get(a).NUM)){
                Log.d("FNC",""+WholeList.get(a).NUM+","+a);
                b = a;
            }
        }
        return List.get(b);
    }

    public int getSize(){
        return List.size();
    }

    public int getWholeSize() { return WholeList.size();}

    public SocialMarker getData(int index){
        return List.get(index);
    }

    public String getFilter1(int index){
        return List.get(index).filter1;
    }

    public String[] getFilter2(int index){
        return List.get(index).filter2;
    }
}
