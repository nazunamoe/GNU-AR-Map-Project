/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.gnuarmap.app;

import android.util.Log;

import org.mixare.lib.MixContextInterface;
import org.mixare.lib.MixStateInterface;

/**
 * 앱의 각종 상태를 관리하는 클래스
 */
public class State implements MixStateInterface {

    public SocialMarker marker;

    private static State instance;

    // Global variable
    public boolean Business;
    public boolean Engineering;
    public boolean Dormitory;
    public boolean ETC;
    public boolean Agriculture;
    public boolean University;
    public boolean Club;
    public boolean Law;
    public boolean Education;
    public boolean Social;
    public boolean Veterinary;
    public boolean Leisure;
    public boolean Humanities;
    public boolean Science;
    public boolean Door;
    public boolean AllBuilding;

    public boolean Printer;
    public boolean ATM;
    public boolean CVS;
    public boolean Vending;
    public boolean All;

    public boolean NMapState;
    public boolean MoreView;
    public boolean Camera2;

    private State() {
    }

    public static synchronized State getInstance() {
        if (instance == null) {
            instance = new State();
        }
        return instance;
    }

    public static int NOT_STARTED = 0;
    public static int PROCESSING = 1;
    public static int READY = 2;
    public static int DONE = 3;

    int nextLStatus = State.NOT_STARTED;

    private boolean detailsView;

    public boolean handleEvent(MixContextInterface ctx, String onPress, org.mixare.lib.marker.Marker marker) {
        if (onPress != null && onPress.startsWith("webpage")) {
            try {
                this.detailsView = true;
                Log.d("mixare", "Clicked Marker");
                ctx.MarkerMenu(marker);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean handleEvent(MixContextInterface ctx, String onPress) {
        if (onPress != null && onPress.startsWith("webpage")) {
            try {
                this.detailsView = true;
                Log.d("mixare", "Clicked Marker");
                //ctx.MarkerMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public boolean isDetailsView() {
        return detailsView;
    }

    public void setDetailsView(boolean detailsView) {
        this.detailsView = detailsView;
    }
}
