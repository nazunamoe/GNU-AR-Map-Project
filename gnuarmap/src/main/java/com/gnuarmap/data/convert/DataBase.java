package com.gnuarmap.data.convert;

import android.graphics.Color;
import com.gnuarmap.data.convert.DataClass;
/**
 * Created by nazunamoe on 2017-11-11.
 */

public class DataBase {
    public DataClass data = new DataClass();

    public String Engnieering = "http://ce.gnu.ac.kr";
    public String Dormitory = "http://dorm.gnu.ac.kr";
    public String Base = "http://gnu.ac.kr";
    public String Business = "http://business.gnu.ac.kr";
    public String Agriculture = "http://cals.gnu.ac.kr/";
    public String Library = "https://lib.gnu.ac.kr";
    public String Law = "http://law.gnu.ac.kr";
    public String Education = "http://sadae.gnu.ac.kr/main/";
    public String Science = "http://cns.gnu.ac.kr";
    public String Humanities = "http://inmun.gnu.ac.kr";
    public String Veterinary = "http://vet.gnu.ac.kr";
    public String Social = "http://css.gnu.ac.kr/main/";


    public String[] vending_printer = {"vending","printer"};
    public String[] cvs_atm = {"cvs","atm"};
    public String[] printer_cvs_atm = {"printer","cvs","atm"};
    public String[] vending = {"vending"};
    public String[] printer = {"printer"};
    public String[] cvs = {"cvs"};
    public String[] atm = {"atm"};
    public String[] printer_cvs = {"printer","cvs"};
    public String[] vending_cvs_atm = {"vending","cvs","atm"};
    public String[] printer_atm = {"printer","atm"};
    public String[] vending_atm = {"vending","atm"};
    public String[] nothing = {};


    public DataBase(){
        this.Initialize();
    }

    public void getFiltering1(DataClass in, int index){
        in.getData(index);
    }

    public void Initialize(){
        /* 필터링 알고리즘

        String Filtering 1으로 건물별 필터링, String Filering 2로 Switch문 돌려서 시설 필터링

        건물 번호 0은 모르거나 건물 번호가 없거나 건물이 아님을 나타냄.

        시설 필터링 개요
        - cvs : 매점 또는 편의점
        - printer : 프린터
        - atm : atm
        - vending : 자판기

        - 자판기 - 프린터 - 편의점 - ATM 순으로 정렬하여 나타낸다.
        존재하는 데이터 유형 개요

        vending,printer
        cvs,atm
        printer,cvs,atm
        vending
        printer
        cvs
        printer,cvs
        atm
        printer,atm
        vending,cvs,atm
        vending,atm

        */
        // 샘플 데이터
        data.addItem("샘플",
                Engnieering,
                35.262957,128.639452,
                "dorm",
                "engnieering",
                nothing,
                401
        );
        // 공대 데이터
        data.addItem("공대 1호관",
                    Engnieering,
                    35.15382,128.099861,
                    "engine",
                "engnieering",
                nothing,
                401
                );
        data.addItem("공대 2호관",
                Engnieering,
                35.154728,128.094193,
                "engine",
                "engnieering",
                nothing,
                402
        );
        data.addItem("공대 3호관",
                Engnieering,
                35.155825,128.094035,
                "engine",
                "engnieering",
                nothing,
                403
        );
        data.addItem("공대 4호관",
                Engnieering,
                35.155408,128.094235,
                "engine",
                "engnieering",
                cvs_atm,
                404
        );
        data.addItem("공대 5호관",
                Engnieering,
                35.154863,128.094563,
                "engine",
                "engnieering",
                nothing,
                405
        );
        data.addItem("공대 6호관",
                Engnieering,
                35.1542783,128.0941758,
                "engine",
                "engnieering",
                nothing,
                406
        );
        data.addItem("공대 7호관",
                Engnieering,
                35.154354,128.093236,
                "engine",
                "engnieering",
                nothing,
                407
        );
        data.addItem("공대부속공장",
                Engnieering,
                35.156122,128.0945,
                "engine",
                "engnieering",
                nothing,
                416
        );

        // 경영대학
        data.addItem("경영대학",
                Business,
                35.15382,128.099861,
                "business",
                "business",
                nothing,
                201
        );

        // 기숙사
        data.addItem("기숙사행정실",
                Dormitory,
                35.157377,128.100519,
                "dominatory",
                "dominatory",
                printer_cvs_atm,
                416
        );
        data.addItem("게스트하우스",
                Dormitory,
                35.1577176,128.0991493,
                "dominatory",
                "dominatory",
                nothing,
                68
        );
        data.addItem("LG개척관",
                Dormitory,
                35.158045,128.098891,
                "dominatory",
                "dominatory",
                nothing,
                70
        );
        data.addItem("기숙사 1동",
                Dormitory,
                35.157377,128.100519,
                "dominatory",
                "dominatory",
                nothing,
                61
        );
        data.addItem("기숙사 2동",
                Dormitory,
                35.157735,128.100078,
                "dominatory",
                "dominatory",
                nothing,
                62
        );
        data.addItem("기숙사 3동",
                Dormitory,
                35.156122,128.0945,
                "dominatory",
                "dominatory",
                nothing,
                416
        );
        data.addItem("기숙사 4동",
                Dormitory,
                35.157941,128.100439,
                "dominatory",
                "dominatory",
                nothing,
                64
        );
        data.addItem("기숙사 5동",
                Dormitory,
                35.158456,128.098934,
                "dominatory",
                "dominatory",
                vending,
                65
        );
        data.addItem("기숙사 6동",
                Dormitory,
                35.157541,128.101319,
                "dominatory",
                "dominatory",
                nothing,
                66
        );
        data.addItem("기숙사 7동",
                Dormitory,
                35.1574650,128.1015570,
                "dominatory",
                "dominatory",
                nothing,
                67
        );
        data.addItem("ENGLISH ONLY ZONE",
                Dormitory,
                35.1577648,128.1008716,
                "dominatory",
                "dominatory",
                nothing,
                416
        );
        data.addItem("기숙사 8동",
                Dormitory,
                35.156735,128.101429,
                "dominatory",
                "dominatory",
                printer,
                71
        );
        data.addItem("기숙사 9동",
                Dormitory,
                35.15672,128.101799,
                "dominatory",
                "dominatory",
                cvs,
                72
        );
        data.addItem("기숙사 10동",
                Dormitory,
                35.156738,128.1006885,
                "dominatory",
                "dominatory",
                printer_cvs,
                73
        );
        data.addItem("기숙사 11동",
                Dormitory,
                35.157065,128.100451,
                "dominatory",
                "dominatory",
                nothing,
                74
        );
        data.addItem("아람관",
                Dormitory,
                35.1573572,128.1008773,
                "dominatory",
                "dominatory",
                nothing,
                74
        );

        // 기타 건물
        data.addItem("통학버스승강장",
                Base,
                35.152723,128.100083,
                "etc",
                "etc",
                nothing,
                74
        );
        data.addItem("남문주차장",
                Base,
                35.1518122,128.1017564,
                "etc",
                "etc",
                nothing,
                74
        );

        // 농생대 건물
        data.addItem("공동실험실습관",
                Agriculture,
                35.15332,128.094787,
                "agriculture",
                "agriculture",
                nothing,
                27
        );
        data.addItem("농생대 1호관",
                Agriculture,
                35.151932,128.098099,
                "agriculture",
                "agriculture",
                nothing,
                451
        );
        data.addItem("농생대 2호관",
                Agriculture,
                35.152891,128.09758,
                "agriculture",
                "agriculture",
                vending,
                452
        );
        data.addItem("농생대 3호관",
                Agriculture,
                35.153362,128.098328,
                "agriculture",
                "agriculture",
                nothing,
                453
        );
        data.addItem("농생대 4호관",
                Agriculture,
                35.151089,128.097168,
                "agriculture",
                "agriculture",
                vending,
                454
        );
        data.addItem("농생대 5호관",
                Agriculture,
                35.151859,128.096268,
                "agriculture",
                "agriculture",
                vending,
                455
        );
        data.addItem("농생대 6호관",
                Agriculture,
                35.153266,128.096192,
                "agriculture",
                "agriculture",
                nothing,
                456
        );
        data.addItem("농생대 7호관",
                Agriculture,
                35.152103,128.096176,
                "agriculture",
                "agriculture",
                nothing,
                457
        );
        data.addItem("농생대 8호관",
                Agriculture,
                35.152115,128.096313,
                "agriculture",
                "agriculture",
                nothing,
                458
        );
        data.addItem("농생대 9호관",
                Agriculture,
                35.152386,128.0961,
                "agriculture",
                "agriculture",
                nothing,
                459
        );
        data.addItem("농대부속농장",
                Agriculture,
                35.150478,128.098755,
                "agriculture",
                "agriculture",
                nothing,
                0
        );
        data.addItem("농대 카페테리아",
                Agriculture,
                35.150505,128.09874,
                "agriculture",
                "agriculture",
                nothing,
                0
        );

        // 대학직속 건물
        data.addItem("NH농협",
                Base,
                35.1538058,128.1012258,
                "university",
                "university",
                nothing,
                1
        );
        data.addItem("대학본부",
                Base,
                35.1537364,128.1015611,
                "university",
                "university",
                nothing,
                1
        );
        data.addItem("중앙도서관",
                Library,
                35.153057,128.099869,
                "university",
                "university",
                printer_cvs,
                2
        );
        data.addItem("학생회관",
                Base,
                35.153587,128.097427,
                "university",
                "university",
                nothing,
                3
        );
        data.addItem("학술정보관",
                Base,
                35.153305,128.096603,
                "university",
                "university",
                nothing,
                1
        );
        data.addItem("GNU어린이집",
                Base,
                35.156085,128.103174,
                "university",
                "university",
                nothing,
                0
        );
        data.addItem("국제문화회관",
                Base,
                35.154518,128.101817,
                "university",
                "university",
                nothing,
                0
        );
        data.addItem("교양동",
                Base,
                35.151986,128.099173,
                "university",
                "university",
                vending_printer,
                24
        );
        data.addItem("창업보육센터",
                Base,
                35.151787,128.099991,
                "university",
                "university",
                nothing,
                25
        );
        data.addItem("BNIT관/약대",
                Base,
                35.1537802,128.0949872,
                "university",
                "university",
                atm,
                28
        );
        data.addItem("국제어학원",
                Base,
                35.154055,128.098787,
                "university",
                "university",
                nothing,
                29
        );
        data.addItem("남명학관",
                Base,
                35.156117,128.099965,
                "university",
                "university",
                nothing,
                31
        );
        data.addItem("학군단",
                Base,
                35.15745,128.092433,
                "university",
                "university",
                nothing,
                32
        );
        data.addItem("박물관",
                Base,
                35.155313,128.102129,
                "university",
                "university",
                nothing,
                33
        );
        data.addItem("예절교육원",
                Base,
                35.153349,128.103188,
                "university",
                "university",
                nothing,
                34
        );
        data.addItem("경비실",
                Base,
                35.152751,128.103959,
                "university",
                "university",
                nothing,
                0
        );


        // 동아리
        data.addItem("남문 동아리방",
                Base,
                35.151151,128.0995362,
                "club",
                "club",
                nothing,
                303
        );

        // 문
        data.addItem("정문",
                Base,
                35.152538,128.104164,
                "door",
                "door",
                nothing,
                0
        );
        data.addItem("남문",
                Base,
                35.149809,128.099459,
                "door",
                "door",
                nothing,
                0
        );
        data.addItem("북문",
                Base,
                35.155902,128.104297,
                "door",
                "door",
                nothing,
                0
        );

        // 법대
        data.addItem("법과대학",
                Law,
                35.154416,128.09973,
                "law",
                "law",
                vending,
                251
        );
        data.addItem("대경학술관",
                Law,
                35.1541728,128.1008419,
                "law",
                "law",
                nothing,
                252
        );

        // 사범대
        data.addItem("사범대 1호관",
                Education,
                35.1542261,128.0975008,
                "education",
                "education",
                nothing,
                301
        );
        data.addItem("사범대 2호관",
                Education,
                35.1547425,128.0973952,
                "education",
                "education",
                printer,
                302
        );
        data.addItem("평생교육원",
                Education,
                35.1549003,128.0985308,
                "education",
                "education",
                nothing,
                303
        );
        data.addItem("교육문화센터",
                Education,
                35.1547403,128.0993619,
                "education",
                "education",
                nothing,
                303
        );
        data.addItem("예술관",
                Education,
                35.15608,128.101117,
                "education",
                "education",
                vending,
                304
        );

        // 사회과학대학
        data.addItem("사회과학대학",
                Social,
                35.1548353,128.1004319,
                "social",
                "social",
                vending,
                151
        );

        // 수의대
        data.addItem("수의대 1호관",
                Veterinary,
                35.1548353,128.1004319,
                "veterinary",
                "veterinary",
                vending_atm,
                501
        );
        data.addItem("수의대 2호관",
                Veterinary,
                35.150898,128.09787,
                "veterinary",
                "veterinary",
                nothing,
                502
        );
        data.addItem("수의대 3호관",
                Veterinary,
                35.1501,128.096923,
                "veterinary",
                "veterinary",
                nothing,
                503
        );
        data.addItem("수의대 4호관",
                Veterinary,
                35.150391,128.096721,
                "veterinary",
                "veterinary",
                nothing,
                504
        );
        data.addItem("수의대 5호관",
                Veterinary,
                35.150982,128.097137,
                "veterinary",
                "veterinary",
                nothing,
                505
        );

        // 여가시설
        data.addItem("체육관",
                Base,
                35.155083,128.102976,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("스포츠컴플렉스",
                Base,
                35.154663,128.1055,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("남문운동장",
                Base,
                35.1513994,128.1008149,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("스포츠컴플렉스",
                Base,
                35.154663,128.1055,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("야외공연장",
                Base,
                35.1521609,128.1029144,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("대운동장",
                Base,
                35.154856,128.103712,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("도서관매점",
                Base,
                35.153279,128.099226,
                "leisure",
                "leisure",
                cvs,
                0
        );

        // 인문대학
        data.addItem("인문 1호관",
                Humanities,
                35.1551261,128.1001703,
                "humanities",
                "humanities",
                printer,
                101
        );
        data.addItem("인문 2호관",
                Humanities,
                35.1549278,128.1000108,
                "humanities",
                "humanities",
                printer,
                102
        );

        // 자연과학대학
        data.addItem("자연 1호관",
                Science,
                35.1552561,128.0955716,
                "science",
                "science",
                vending,
                351
        );
        data.addItem("자연 2호관",
                Science,
                35.1554908,128.0964877,
                "science",
                "science",
                vending,
                352
        );
        data.addItem("자연 3호관",
                Science,
                35.1549003,128.0985308,
                "science",
                "science",
                vending,
                353
        );
        data.addItem("자연 4호관",
                Science,
                35.1559556,128.0968994,
                "science",
                "science",
                vending,
                102
        );
        data.addItem("컴퓨터과학관",
                Science,
                35.154373,128.098434,
                "science",
                "science",
                printer,
                30
        );
        data.addItem("지진관측소",
                Science,
                35.1551678,128.0971216,
                "science",
                "science",
                printer,
                0
        );
        data.addItem("과학영재교육관",
                Science,
                35.1550508,128.0964877,
                "science",
                "science",
                printer,
                352
        );
    }
}
