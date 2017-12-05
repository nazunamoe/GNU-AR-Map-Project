package com.gnuarmap;

import android.test.ActivityInstrumentationTestCase2;

import com.gnuarmap.app.DataHandler;
import com.gnuarmap.app.DataView;
import com.gnuarmap.app.MixView;
import com.gnuarmap.app.NaverMapActivity;
import com.gnuarmap.app.NaverMapMarker;

import java.util.ArrayList;

public class MapTest extends ActivityInstrumentationTestCase2<NaverMapActivity> {

    public MapTest(){
        super(NaverMapActivity.class);
    }

    public void testForStartNaverMapActivity(){
        assertEquals(1,getActivity().a);
    }

}
