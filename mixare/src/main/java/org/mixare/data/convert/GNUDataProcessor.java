package org.mixare.data.convert;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.MixView;
import org.mixare.POIMarker;
import org.mixare.data.DataHandler;
import org.mixare.lib.HtmlUnescape;
import org.mixare.lib.marker.Marker;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazunamoe on 2017-11-09.
 */

public class GNUDataProcessor extends DataHandler implements DataProcessor {

    @Override
    public String[] getUrlMatch() {
        String[] str = new String[0]; //only use this data source if all the others don't match
        return str;
    }

    @Override
    public String[] getDataMatch() {
        String[] str = new String[0]; //only use this data source if all the others don't match
        return str;
    }

    @Override
    public boolean matchesRequiredType(String type) {
        return true; //this datasources has no required type, it will always match.
    }

    @Override
    public List<Marker> load(String rawData, int taskId, int colour) throws JSONException {
        List<Marker> markers = new ArrayList<Marker>();
        int max = 1;
        for (int i = 0; i < max; i++) {

            Marker ma = null;
                String id = "0";
                String link="http://anse.gnu.ac.kr/anse/main.do";


                ma = new POIMarker(
                        id,
                        HtmlUnescape.unescapeHTML(("디버그"), 0),
                        35.262898,
                        128.639501,
                        21,
                        link,
                        0, android.R.color.black);

                markers.add(ma);
        }
        return markers;
    }



}
