package com.gnuarmap.data;

/**
 * Created by nazunamoe on 2017-12-02.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gnuarmap.R;
import com.gnuarmap.mixare.app.SocialMarker;
import com.gnuarmap.mixare.app.State;

import org.mixare.lib.HtmlUnescape;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class Dataclass {
    public static Bitmap schoolicon; // 학교 아이콘
    public static Bitmap dormicon;
    public static Bitmap dorm2icon;
    public static Bitmap elecicon;
    public static Bitmap forlangicon; // 국제어학원
    public static Bitmap gateicon; // 정문이나 후문
    public static Bitmap libraryicon; // 도서관
    public static Bitmap lifesci; // 농생대
    public static Bitmap manageinticon; // 인문대
    public static Bitmap multi; // 학생회관
    public static Bitmap student; // 따로 만들어야함, 경상대 아이콘
    public static Bitmap viliage; // 기숙사
    public static Bitmap panpacific; // ??
    public static Bitmap basic; // 기본 아이콘
    public static Bitmap engine; // 공대
    public static Bitmap physical; // 체육관, 운동시설

    private String name;
    private String url;

    public static String Building_Type;
    public ArrayList<SocialMarker> WholeList = new ArrayList<SocialMarker>();
    public ArrayList<SocialMarker> List = new ArrayList<SocialMarker>();
    private String Filtering1;
    private String[] Filtering2;
    State state = State.getInstance();

    public int getIntFromColor(float Red, float Green, float Blue) {
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public void addItem(String num, String name, String url, double Latitude, double Longitude, String type, String Filtering1, String[] Filtering2, int no) {
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
                if (Filtering1 == "dormitory") {
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
                HtmlUnescape.unescapeHTML((name), 0),
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
        dormicon = BitmapFactory.decodeResource(res, R.drawable.school_dorm);
        dorm2icon = BitmapFactory.decodeResource(res, R.drawable.school_dorm2);
        elecicon = BitmapFactory.decodeResource(res, R.drawable.school_elecinfo);
        forlangicon = BitmapFactory.decodeResource(res, R.drawable.school_forlang);
        gateicon = BitmapFactory.decodeResource(res, R.drawable.school_gate);
        libraryicon = BitmapFactory.decodeResource(res, R.drawable.school_library);
        lifesci = BitmapFactory.decodeResource(res, R.drawable.school_lifesci);
        manageinticon = BitmapFactory.decodeResource(res, R.drawable.school_manageint);
        multi = BitmapFactory.decodeResource(res, R.drawable.school_multi);
        panpacific = BitmapFactory.decodeResource(res, R.drawable.school_panpacific);
        engine = BitmapFactory.decodeResource(res, R.drawable.school_engine);
        physical = BitmapFactory.decodeResource(res, R.drawable.school_physical);
        viliage = BitmapFactory.decodeResource(res, R.drawable.school_village);
        basic = BitmapFactory.decodeResource(res, R.drawable.school_default);
        // routeIcon;
    }

    public static Bitmap getBitmap(String ds) {
        Bitmap bitmap = null;
        switch (ds) {

            case "dorm":
                bitmap = dormicon;
                break;
            case "dorm2":
                bitmap = dorm2icon;
                break;
            case "elecinfo":
                bitmap = elecicon;
                break;
            case "forlang":
                bitmap = forlangicon;
                break;
            case "gate":
                bitmap = gateicon;
                break;
            case "library":
                bitmap = libraryicon;
                break;
            case "lifesci":
                bitmap = lifesci;
                break;
            case "manageint":
                bitmap = manageinticon;
                break;
            case "student":
                bitmap = multi;
                break;
            case "panpacific":
                bitmap = panpacific;
                break;
            case "engine":
                bitmap = engine;
                break;
            case "physical":
                bitmap = physical;
                break;
            case "default":
                bitmap = basic;
                break;
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
