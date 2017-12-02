package com.gnuarmap.data;

/**
 * Created by nazunamoe on 2017-12-02.
 */

/**
 * Created by nazunamoe on 2017-11-11.
 */

import com.gnuarmap.data.Dataclass;

public class Database {

    private String Engnieering = "http://ce.gnu.ac.kr";
    private String Dormitory = "http://dorm.gnu.ac.kr";
    private String Base = "http://gnu.ac.kr";
    private String Business = "http://business.gnu.ac.kr";
    private String Agriculture = "http://cals.gnu.ac.kr/";
    private String Library = "https://lib.gnu.ac.kr";
    private String Law = "http://law.gnu.ac.kr";
    private String Education = "http://sadae.gnu.ac.kr/main/";
    private String Science = "http://cns.gnu.ac.kr";
    private String Humanities = "http://inmun.gnu.ac.kr";
    private String Veterinary = "http://vet.gnu.ac.kr";
    private String Social = "http://css.gnu.ac.kr/main/";


    private String[] vending_printer = {"vending","printer"};
    private String[] cvs_atm = {"cvs","atm"};
    private String[] printer_cvs_atm = {"printer","cvs","atm"};
    private String[] vending = {"vending"};
    private String[] printer = {"printer"};
    private String[] cvs = {"cvs"};
    private String[] atm = {"atm"};
    private String[] printer_cvs = {"printer","cvs"};
    private String[] vending_cvs_atm = {"vending","cvs","atm"};
    private String[] printer_atm = {"printer","atm"};
    private String[] vending_atm = {"vending","atm"};
    private String[] nothing = {};


    public void getFiltering1(Dataclass in, int index){
        in.getData(index);
    }

    public void Initialize(Dataclass data){
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
        // 공대 데이터
        data.addItem("0","공대 1호관",
                Engnieering,
                35.154782,128.093720,
                "dorm",
                "engnieering",
                nothing,
                401
        );
        data.addItem("1","공대 2호관",
                Engnieering,
                35.155352,128.093763,
                "dorm",
                "engnieering",
                nothing,
                402
        );
        data.addItem("2","공대 3호관",
                Engnieering,
                35.155887,128.093849,
                "dorm",
                "engnieering",
                nothing,
                403
        );
        data.addItem("3","공대 4호관",
                Engnieering,
                35.155747,128.094825,
                "engine",
                "engnieering",
                cvs_atm,
                404
        );
        data.addItem("4","공대 5호관",
                Engnieering,
                35.155229,128.094718,
                "engine",
                "engnieering",
                nothing,
                405
        );
        data.addItem("5","공대 6호관",
                Engnieering,
                35.154141,128.094053,
                "engine",
                "engnieering",
                nothing,
                406
        );
        data.addItem("6","공대 7호관",
                Engnieering,
                35.154177,128.092926,
                "engine",
                "engnieering",
                nothing,
                407
        );
        data.addItem("7","공대부속공장",
                Engnieering,
                35.156326,128.094882,
                "engine",
                "engnieering",
                nothing,
                416
        );

        // 경영대학
        data.addItem("33","경영대학",
                Business,
                35.153946, 128.099808,
                "business",
                "business",
                vending_printer,
                201
        );

        //35.153786, 128.095466

        // 약학대학, BNIT 건물, 둘이 같은 건물을 공유하기 때문에 아이콘은 약대, 필터링은 대학 건물로 등록한다.
        data.addItem("39","BNIT/약학대학",
                Business,
                35.153786, 128.095466,
                "drug",
                "university",
                atm,
                201
        );

        // 기숙사
        data.addItem("70","게스트하우스",
                Dormitory,
                35.157687,128.099124,
                "dominatory",
                "dominatory",
                nothing,
                68
        );
        data.addItem("72","LG개척관",
                Dormitory,
                35.158058,128.098808,
                "dominatory",
                "dominatory",
                nothing,
                70
        );
        data.addItem("68","기숙사 1동",
                Dormitory,
                35.157430,128.100245,
                "dominatory",
                "dominatory",
                nothing,
                61
        );
        data.addItem("69","기숙사 2동",
                Dormitory,
                35.157637,128.100027,
                "dominatory",
                "dominatory",
                nothing,
                62
        );
        data.addItem("91","기숙사 3동",
                Dormitory,
                35.157882,128.099818,
                "dominatory",
                "dominatory",
                nothing,
                64
        );
        data.addItem("85","기숙사 4동",
                Dormitory,
                35.157969, 128.100623,
                "dominatory",
                "dominatory",
                nothing,
                64
        );
        data.addItem("86","기숙사 5동",
                Dormitory,
                35.158447,128.098869,
                "dominatory",
                "dominatory",
                vending,
                65
        );
        data.addItem("71","기숙사 6동",
                Dormitory,
                35.157401,128.101258,
                "dominatory",
                "dominatory",
                nothing,
                66
        );
        data.addItem("92","기숙사 관리동",
                Dormitory,
                35.158368,128.099583,
                "dominatory",
                "dominatory",
                printer_cvs_atm,
                67
        );
        data.addItem("93","영어 전용 강의동",
                Dormitory,
                35.157827,128.100946,
                "dominatory",
                "dominatory",
                nothing,
                0
        );
        data.addItem("73","기숙사 8동",
                Dormitory,
                35.156679,128.101334,
                "dominatory",
                "dominatory",
                printer,
                71
        );
        data.addItem("74","기숙사 9동",
                Dormitory,
                35.156770,128.101905,
                "dominatory",
                "dominatory",
                cvs,
                72
        );
        data.addItem("75","기숙사 10동",
                Dormitory,
                35.156882,128.100903,
                "dominatory",
                "dominatory",
                printer_cvs,
                73
        );
        data.addItem("76","기숙사 11동",
                Dormitory,
                35.157065,128.100451,
                "dominatory",
                "dominatory",
                nothing,
                74
        );
        data.addItem("87","아람관",
                Dormitory,
                35.157398,128.101216,
                "dominatory",
                "dominatory",
                nothing,
                74
        );

        // 기타 건물
        data.addItem("81","통학버스승강장",
                Base,
                35.152723,128.100083,
                "etc",
                "etc",
                nothing,
                74
        );
        data.addItem("84","남문주차장",
                Base,
                35.1518122,128.1017564,
                "etc",
                "etc",
                nothing,
                74
        );

        // 농생대 건물
        data.addItem("55","공동실험실습관",
                Agriculture,
                35.153219,128.094733,
                "agriculture",
                "agriculture",
                nothing,
                27
        );
        data.addItem("22","농생대 1호관",
                Agriculture,
                35.152195,128.096858,
                "agriculture",
                "agriculture",
                nothing,
                451
        );
        data.addItem("23","농생대 2호관",
                Agriculture,
                35.152829,128.097558,
                "agriculture",
                "agriculture",
                vending,
                452
        );
        data.addItem("24","농생대 3호관",
                Agriculture,
                35.152094, 128.097282,
                "agriculture",
                "agriculture",
                nothing,
                453
        );
        data.addItem("25","농생대 4호관",
                Agriculture,
                35.151446,128.096936,
                "agriculture",
                "agriculture",
                vending,
                454
        );
        data.addItem("26","농생대 5호관",
                Agriculture,
                35.151340,128.097532,
                "agriculture",
                "agriculture",
                vending,
                455
        );
        data.addItem("27","농생대 6호관",
                Agriculture,
                35.153065,128.095837,
                "agriculture",
                "agriculture",
                nothing,
                456
        );
        data.addItem("28","농생대 7호관",
                Agriculture,
                35.152144,128.095410,
                "agriculture",
                "agriculture",
                nothing,
                457
        );
        data.addItem("29","농생대 8호관",
                Agriculture,
                35.151669,128.095711,
                "agriculture",
                "agriculture",
                nothing,
                458
        );
        data.addItem("30","농생대 9호관",
                Agriculture,
                35.152085,128.095928,
                "agriculture",
                "agriculture",
                nothing,
                459
        );
        data.addItem("32","농대부속농장",
                Agriculture,
                35.149973,128.098858,
                "agriculture",
                "agriculture",
                nothing,
                0
        );
        data.addItem("31","농대 카페테리아",
                Agriculture,
                35.150704,128.098038,
                "agriculture",
                "leisure",
                cvs,
                0
        );

        // 대학직속 건물
        data.addItem("83","NH농협",
                Base,
                35.153936,128.101363,
                "university",
                "university",
                atm,
                1
        );
        data.addItem("41","대학본부",
                Base,
                35.153883,128.101629,
                "university",
                "university",
                nothing,
                1
        );
        data.addItem("90","남명학관",
                Base,
                35.156210,128.099874,
                "university",
                "university",
                nothing,
                310
        );
        data.addItem("42","중앙도서관",
                Library,
                35.153188,128.099485,
                "university",
                "university",
                printer_cvs,
                2
        );
        data.addItem("43","학생회관",
                Base,
                35.153580,128.097367,
                "university",
                "university",
                cvs,
                3
        );
        data.addItem("44","학술정보관",
                Base,
                35.153726,128.096132,
                "university",
                "university",
                nothing,
                1
        );
        data.addItem("46","GNU어린이집",
                Base,
                35.156238,128.103403,
                "university",
                "university",
                nothing,
                0
        );
        data.addItem("47","국제문화회관",
                Base,
                35.154499,128.101783,
                "university",
                "university",
                nothing,
                0
        );
        data.addItem("52","교양동",
                Base,
                35.151986,128.099173,
                "university",
                "university",
                vending_printer,
                24
        );
        data.addItem("53","창업보육센터",
                Base,
                35.151482,128.100109,
                "university",
                "university",
                nothing,
                25
        );
        data.addItem("60","박물관",
                Base,
                35.155313,128.102129,
                "university",
                "university",
                nothing,
                33
        );
        data.addItem("61","예절교육원",
                Base,
                35.153349,128.103188,
                "university",
                "university",
                nothing,
                34
        );
        data.addItem("62","경비실",
                Base,
                35.152751,128.103959,
                "university",
                "university",
                nothing,
                0
        );


        // 동아리
        data.addItem("64","남문 동아리방",
                Base,
                35.151151,128.0995362,
                "club",
                "club",
                vending,
                303
        );

        // 문
        data.addItem("49","정문",
                Base,
                35.152538,128.104164,
                "door",
                "door",
                nothing,
                0
        );
        data.addItem("50","남문",
                Base,
                35.149809,128.099459,
                "door",
                "door",
                nothing,
                0
        );
        data.addItem("51","북문",
                Base,
                35.155902,128.104297,
                "door",
                "door",
                nothing,
                0
        );

        // 법대
        data.addItem("16","법과대학",
                Law,
                35.154373,128.099919,
                "law",
                "law",
                vending,
                251
        );
        data.addItem("17","대경학술관",
                Law,
                35.154251,128.100665,
                "law",
                "law",
                nothing,
                252
        );

        // 사범대
        data.addItem("18","사범대 1호관",
                Education,
                35.154369,128.097381,
                "education",
                "education",
                printer,
                301
        );
        data.addItem("19","사범대 2호관",
                Education,
                35.154831,128.097468,
                "education",
                "education",
                nothing,
                302
        );
        data.addItem("20","교육문화센터",
                Education,
                35.155068,128.099352,
                "education",
                "education",
                nothing,
                303
        );
        data.addItem("80","예술관",
                Education,
                35.155964,128.101193,
                "education",
                "education",
                vending,
                304
        );
        // 사회과학대학
        data.addItem("40","사회과학대학",
                Social,
                35.1544687,128.100379,
                "social",
                "social",
                vending,
                151
        );

        // 수의대
        data.addItem("34","수의대 1호관",
                Veterinary,
                35.150438,128.097276,
                "veterinary",
                "veterinary",
                vending_atm,
                501
        );
        data.addItem("35","수의대 2호관",
                Veterinary,
                35.150898,128.09787,
                "veterinary",
                "veterinary",
                nothing,
                502
        );
        data.addItem("36","수의대 3호관",
                Veterinary,
                35.1501,128.096923,
                "veterinary",
                "veterinary",
                nothing,
                503
        );
        data.addItem("37","수의대 4호관",
                Veterinary,
                35.150391,128.096721,
                "veterinary",
                "veterinary",
                nothing,
                504
        );
        data.addItem("38","수의대 5호관",
                Veterinary,
                35.150982,128.097137,
                "veterinary",
                "veterinary",
                nothing,
                505
        );

        // 여가시설
        data.addItem("88","체육관",
                Base,
                35.155383,128.103038,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("48","스포츠컴플렉스",
                Base,
                35.154663,128.1055,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("54","남문운동장",
                Base,
                35.1513994,128.1008149,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("63","야외공연장",
                Base,
                35.1521609,128.1029144,
                "leisure",
                "leisure",
                nothing,
                0
        );
        data.addItem("89","대운동장",
                Base,
                35.154856,128.103712,
                "leisure",
                "leisure",
                vending,
                0
        );


        // 인문대학
        data.addItem("14","인문 1호관",
                Humanities,
                35.155175,128.100516,
                "humanities",
                "humanities",
                printer,
                101
        );
        data.addItem("15","인문 2호관",
                Humanities,
                35.155091,128.099820,
                "humanities",
                "humanities",
                vending,
                102
        );

        // 자연과학대학
        data.addItem("22","자연 1호관",
                Science,
                35.155177,128.095471,
                "science",
                "science",
                vending_printer,
                351
        );
        data.addItem("23","자연 2호관",
                Science,
                35.155431,128.096279,
                "science",
                "science",
                vending,
                352
        );
        data.addItem("24","자연 3호관",
                Science,
                35.155022,128.098579,
                "science",
                "science",
                vending,
                353
        );
        data.addItem("25","자연 4호관",
                Science,
                35.156058,128.096919,
                "science",
                "science",
                vending,
                354
        );
        data.addItem("57","컴퓨터과학관",
                Science,
                35.154498,128.098414,
                "science",
                "science",
                printer,
                30
        );
        data.addItem("12","지진관측소",
                Science,
                35.155157,128.097204,
                "science",
                "science",
                printer,
                0
        );

    }
}