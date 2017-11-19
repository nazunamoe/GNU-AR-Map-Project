package com.gnuarmap;

import android.app.Application;

/**
 * Created by nazunamoe on 2017-11-14.
 */

public class State extends Application {
    private int NMapState;
    private int MoreView;

    @Override
    public void onCreate(){
        NMapState=0;
        super.onCreate();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public int getNMapState(){
        return NMapState;
    }

    public void setNMapState(int State){
        this.NMapState = State;
    }

    public int getMoreView(){
        return MoreView;
    }

    public void setMoreView(int moreview){
        this.MoreView = moreview;
    }
}
