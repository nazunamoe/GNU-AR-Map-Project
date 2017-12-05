package com.gnuarmap;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.gnuarmap.app.DataHandler;
import com.gnuarmap.app.DataView;
import com.gnuarmap.app.Database;
import com.gnuarmap.app.Dataclass;
import com.gnuarmap.app.MainActivity;
import com.gnuarmap.app.Marker;
import com.gnuarmap.app.MenuActivity;
import com.gnuarmap.app.MixContext;
import com.gnuarmap.app.MixView;
import com.gnuarmap.app.NaverMapActivity;
import com.gnuarmap.app.NaverMapMarker;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SW10 on 2017-12-06.
 */

public class MainTest extends ActivityInstrumentationTestCase2<MixView> {

    public MainTest(){
        super(MixView.class);
    }

    public void testForGetMarkerFromDataBase(){
        DataView view = getActivity().getDataView();
        DataHandler handler = view.getDataHandler();
        ArrayList<org.mixare.lib.marker.Marker> a= new ArrayList<org.mixare.lib.marker.Marker>();
        handler.addMarkers(a);
        assertEquals(1,handler.fd);
    }

    public void testForStartMixViewActivity(){
        assertEquals(1,getActivity().tss);
    }
}
