package com.gnuarmap;

/**
 * Created by nazunamoe on 2017-12-02.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class Dataclass {
    private static Bitmap agriculture;
    private static Bitmap business;
    private static Bitmap drug;
    private static Bitmap education;
    private static Bitmap engineering;
    private static Bitmap law;
    private static Bitmap library;
    private static Bitmap science;
    private static Bitmap vet;

    private String name;
    private String url;

    public ArrayList<SocialMarker> WholeList = new ArrayList<SocialMarker>();
    public ArrayList<SocialMarker> List = new ArrayList<SocialMarker>();
    private String Filtering1;
    private String[] Filtering2;
    State state = State.getInstance();
    Database database = new Database();

    public Dataclass(){
        database.Initialize(this);
    }

    private static HashMap<String, String> htmlEntities;
    static {
        htmlEntities = new HashMap<String, String>();
        htmlEntities.put("&lt;", "<");
        htmlEntities.put("&gt;", ">");
        htmlEntities.put("&amp;", "&");
        htmlEntities.put("&quot;", "\"");
        htmlEntities.put("&agrave;", "à");
        htmlEntities.put("&Agrave;", "À");
        htmlEntities.put("&acirc;", "â");
        htmlEntities.put("&auml;", "ä");
        htmlEntities.put("&Auml;", "Ä");
        htmlEntities.put("&Acirc;", "Â");
        htmlEntities.put("&aring;", "å");
        htmlEntities.put("&Aring;", "Å");
        htmlEntities.put("&aelig;", "æ");
        htmlEntities.put("&AElig;", "Æ");
        htmlEntities.put("&ccedil;", "ç");
        htmlEntities.put("&Ccedil;", "Ç");
        htmlEntities.put("&eacute;", "é");
        htmlEntities.put("&Eacute;", "É");
        htmlEntities.put("&egrave;", "è");
        htmlEntities.put("&Egrave;", "È");
        htmlEntities.put("&ecirc;", "ê");
        htmlEntities.put("&Ecirc;", "Ê");
        htmlEntities.put("&euml;", "ë");
        htmlEntities.put("&Euml;", "Ë");
        htmlEntities.put("&iuml;", "ï");
        htmlEntities.put("&Iuml;", "Ï");
        htmlEntities.put("&ocirc;", "ô");
        htmlEntities.put("&Ocirc;", "Ô");
        htmlEntities.put("&ouml;", "ö");
        htmlEntities.put("&Ouml;", "Ö");
        htmlEntities.put("&oslash;", "ø");
        htmlEntities.put("&Oslash;", "Ø");
        htmlEntities.put("&szlig;", "ß");
        htmlEntities.put("&ugrave;", "ù");
        htmlEntities.put("&Ugrave;", "Ù");
        htmlEntities.put("&ucirc;", "û");
        htmlEntities.put("&Ucirc;", "Û");
        htmlEntities.put("&uuml;", "ü");
        htmlEntities.put("&Uuml;", "Ü");
        htmlEntities.put("&nbsp;", " ");
        htmlEntities.put("&copy;", "\u00a9");
        htmlEntities.put("&reg;", "\u00ae");
        htmlEntities.put("&euro;", "\u20a0");
    }

    public static String unescapeHTML(String source, int start) {
        int i, j;

        i = source.indexOf("&", start);
        if (i > -1) {
            j = source.indexOf(";", i);
            if (j > i) {
                String entityToLookFor = source.substring(i, j + 1);
                String value = (String) htmlEntities.get(entityToLookFor);
                if (value != null) {
                    source = new StringBuffer().append(source.substring(0, i))
                            .append(value).append(source.substring(j + 1))
                            .toString();
                    return unescapeHTML(source, i + 1); // recursive call
                }
            }
        }
        return source;
    }

    public void addItem(String num, String name, String url, double Latitude, double Longitude, String type, String Filtering1, String[] Filtering2, int no) {
        int value = 0;
        this.Filtering1 = Filtering1;
        this.Filtering2 = Filtering2;
        int number = List.size();
        WholeList.add(new SocialMarker(
                num,
                unescapeHTML((name), 0),
                Latitude,
                Longitude,
                0, // 소셜 마커이므로 고도에 구애받지d 않는다.
                url,
                1,
                value,
                type,
                Filtering1,
                Filtering2,
                no)
        );
        if (state.AllBuilding) {
            secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
        } else {
            if (state.Agriculture) {
                if (Filtering1 == "agriculture") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Business) {
                if (Filtering1 == "business") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Engnieering) {
                if (Filtering1 == "engnieering") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Dormitory) {
                if (Filtering1 == "dominatory") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.ETC) {
                if (Filtering1 == "etc") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.University) {
                if (Filtering1 == "university") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Club) {
                if (Filtering1 == "club") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Door) {
                if (Filtering1 == "door") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Law) {
                if (Filtering1 == "law") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Education) {
                if (Filtering1 == "education") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Social) {
                if (Filtering1 == "social") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Veterinary) {
                if (Filtering1 == "veterinary") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Leisure) {
                if (Filtering1 == "leisure") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Humanities) {
                if (Filtering1 == "humanities") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
            if (state.Science) {
                if (Filtering1 == "science") {
                    secondFiltering(num, Latitude, Longitude, url, value, type, name, Filtering2, no);
                }
            }
        }
        // 건물 필터링, 이 다음에 2차 필터링도 넣어야 함.
    }

    public String[] vending = {"vending"};
    public String[] printer = {"printer"};
    public String[] cvs = {"cvs"};
    public String[] atm = {"atm"};

    public String[] printer_cvs = {"printer", "cvs"};
    public String[] printer_atm = {"printer", "atm"};
    public String[] vending_atm = {"vending", "atm"};
    public String[] vending_printer = {"vending", "printer"};
    public String[] cvs_atm = {"cvs", "atm"};

    public String[] vending_cvs_atm = {"vending", "cvs", "atm"};
    public String[] printer_cvs_atm = {"printer", "cvs", "atm"};

    public String[] nothing = {};

    private void secondFiltering(String num, Double Latitude, Double Longitude, String url, int value, String type, String name, String[] Filtering2, int ID) {

        if (state.All) {
            addMarker(num, Latitude, Longitude, url, value, type, name, ID);
        } else if (state.Vending) {
            if (state.ATM) {
                if (Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("atm")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            } else if (state.Printer) {
                if (Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("printer")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            } else if (state.CVS) {
                if (Arrays.asList(Filtering2).contains("vending") && Arrays.asList(Filtering2).contains("cvs")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            } else {
                if (Arrays.asList(Filtering2).contains("vending")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            }
        } else if (state.Printer) {
            if (state.CVS) {
                if (state.ATM) {
                    if (Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("cvs") && Arrays.asList(Filtering2).contains("atm")) {
                        addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                    }
                } else {
                    if (Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("cvs")) {
                        addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                    } else {
                    }
                }
            } else if (state.ATM) {
                if (Arrays.asList(Filtering2).contains("printer") && Arrays.asList(Filtering2).contains("atm")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            } else {
                if (Arrays.asList(Filtering2).contains("printer")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            }
        } else if (state.CVS) {
            if (state.ATM) {
                if (Arrays.asList(Filtering2).contains("cvs") && Arrays.asList(Filtering2).contains("atm")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            } else {
                if (Arrays.asList(Filtering2).contains("cvs")) {
                    addMarker(num, Latitude, Longitude, url, value, type, name, ID);
                }
            }
        } else if (state.ATM) {
            if (Arrays.asList(Filtering2).contains("atm")) {
                addMarker(num, Latitude, Longitude, url, value, type, name, ID);
            }
        } else {
        }
    }


    private void addMarker(String num, Double Latitude, Double Longitude, String url, int value, String type, String name, int ID) {
        SocialMarker item = new SocialMarker(
                num,
                unescapeHTML((name), 0),
                Latitude,
                Longitude,
                0, // 소셜 마커이므로 고도에 구애받지 않는다.
                url,
                1,
                value,
                type,
                Filtering1,
                Filtering2,
                ID);
        List.add(item);
    }


    public static void createIcons(Resources res) {
        agriculture = BitmapFactory.decodeResource(res, R.drawable.icon_agriculture);
        business = BitmapFactory.decodeResource(res, R.drawable.icon_business);
        drug = BitmapFactory.decodeResource(res, R.drawable.icon_drug);
        education = BitmapFactory.decodeResource(res, R.drawable.icon_education);
        engineering = BitmapFactory.decodeResource(res, R.drawable.icon_engineering);
        law = BitmapFactory.decodeResource(res, R.drawable.icon_law);
        library = BitmapFactory.decodeResource(res, R.drawable.icon_library);
        science = BitmapFactory.decodeResource(res, R.drawable.icon_science);
        vet = BitmapFactory.decodeResource(res, R.drawable.icon_vet);
        // routeIcon;
    }

    public static Bitmap getBitmap(String ds) {
        Bitmap bitmap = null;
        switch (ds) {
            case "agriculture":{
                bitmap = agriculture;
            }
            case "business":{
                bitmap = business;
            }
            case "drug":{
                bitmap = drug;
            }
            case "education":{
                bitmap = education;
            }
            case "engineering":{
                bitmap = engineering;
            }
            case "law":{
                bitmap = law;
            }
            case "library":{
                bitmap = library;
            }
            case "science":{
                bitmap = science;
            }
            case "vet":{
                bitmap = vet;
            }
            default:{
                bitmap = vet;
            }
        }
        return bitmap;
    }


    public SocialMarker getMarker(String no) {
        int b = 0;

        for (int a = 0; a < WholeList.size(); a++) {
            if (no.equals(WholeList.get(a).NUM)) {
                b = a;
            }
        }
        return List.get(b);
    }

    public int getSize() {
        return List.size();
    }

    public int getWholeSize() {
        return WholeList.size();
    }

    public SocialMarker getData(int index) {
        return List.get(index);
    }

    public SocialMarker getWholeData(int index) {
        return WholeList.get(index);
    }

    public String getFilter1(int index) {
        return List.get(index).filter1;
    }

    public String[] getFilter2(int index) {
        return List.get(index).filter2;
    }


}
