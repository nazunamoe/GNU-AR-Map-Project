package com.gnuarmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.gnuarmap.data.DataBase;
import com.gnuarmap.NaverMap.NaverMapActivity;
import com.gnuarmap.data.DataClass;

public class Search extends AppCompatActivity {

    String[] items = {
            "대학본부","중앙도서관","학생회관/인재개발원","학술정보관/교육정보전산원",
            "체육관","GNU어린이집","국제문화회관","스포츠컴플렉스","정문","남문","북문","교양학관",
            "창업보육센터","남문운동장","공동실험실습관","BNIT산학협력관/약학대학","국제어학원",
            "컴퓨터과학관","남명학관","학군단","고문헌도서관박물관","예절교육원","InformationCenter",
            "야외공연장","대운동장","파워플랜트","교직원테니스장","산-송신탑",
            "게스트하우스","LG개척관",
            "기숙사 8동","기숙사 9동","기숙사 10동","기숙사 11동","아람관","부설중학교","부설고등학교",
            "101동","102동","151동","201동","251동","252동","301동","310동",
            "302동","303동","예술관","351동","352동",
            "353동","354동","401동","402동","403동","404동","405동",
            "406동","407동","416동","451동","452동",
            "453동","454동","455동","456동","457동",
            "458동","459동","501동","502동","503동","504동",
            "505동","남문동아리방","농대카페테리아","통학버스승강장","도서관매점","NH농협","지진관측소",
            "과학영재교육관","남문주차장","농대부속농장",
            "식품영양학과","의류학과",  "국어국문학과","독어독문학과","러시아학과","불어불문학과","사학과","영어영문학과",
            "중어중문학과","철학과","한문학과","민속무용학과","인문대도서관","인문학연구소",
            "해외지역연구센터","법학과","법학연구소","법률상담실",
            "법학도서관","대경학술관",  "교육학과","영어교육과","일어교육과","수학교육과",
            "교육대학원","사범대학부설교육연수원","교육연구원","중등교육연구센터","국어문화원","사범대학 실습동",
            "학생식당","교직원식당","윤리교육과","국어교육과","일반사회교육과","역사교육과",
            "지리교육과","유아교육과","체육교육과","산림환경자원학과",
            "식품자원경제학과","농학과","응용생물학과","식품공학과","환경생명화학과","동물생명과학과",
            "축산학과","원예학과","농생6호관","공작동","두레홀","누리홀",
            "농대부속농장",  "경영학과","경영정보학과","국제통상학과","회계학과",
            "산업경영학과","경영대학원","수의1호관","동물병원1","수의3호관","수의4호관","동물병원2",
            "경제학과","사회복지학과","사회학과","심리학과","정치외교학과",
            "행정학과","행정대학원","사회과학연구원","총장실","교무처","학생처","기획처","사무국",
            "대외협력본부","입학본부","대학원","도서관","매점","박물관(신설)",
            "출판부","전산원","체육교육과","생활체육연구센터","농업생명공동실험실습관",
            "원장실","파이오니어오디토리엄","일본문화연구센터","컴퓨터과학과","도서관학습과","제1열람실",
            "제2열람실","학생상담센터","기록관","남명홀","경남문화연구원","경남문화연구센터","남명학연구소",
            "국제지역연구원","통일평화연구센터","ROTC","박물관(중앙도서관)","고문헌도서관","예절교육원 본관",
            "정문-InformationCenter","정도회","K.T.C","일레븐","화랑회","농업대학 실습동",
            "청학","맥","FC BB","대금소리","팀레이지","V.E.C","점보","바로바로서비스센터","목공실",
            "교직원테니스장","송신탑","기숙사행정실","총동아리방","음악교육과","미술교육과","자연과학대학 학장실"
            ,"자연과학대학 행정실","기숙사영어캠퍼스","기숙사 4동","기숙사 5동","기숙사 2동","산학협력단","공학연구원","교육연구원(다문화교육센터","기초과학연구소","농업생명과학연구원"
            ,"사회과학연구원","약학연구소","여성연구소","인권사회발전연구소","EU연구소","국책사업단",
            "약학대학","혁신연구실","장비실","식물배양실","제약실습공장","나노신소재공학부(고분자,생명화학)","고분자공학과","생명화학공학과","항공기부품기술연구소",
            "기계공학부","공학교육혁신센터",
            "수학과","정보통계학과","지구환경과학과","지질전시실","자생식물보존실습장및온실",
            "생화학과","화학과","화학교육과",
            "물리학과","미생물학과","생물학과","물리교육과","생물교육과",
            "건축학과","도시공학과","토목공학과",
            "반도체공학과","전기공학과","전자공학과","제어계측공학과",
            "나노신소재공학부(금속재료,세라믹)","그린에너지융합연구소",
            "공과대학 본부","융합과학기술대학원","토지주택대학원","어울마루","산업시스템공학부","산업공학과","산업정보공학과",
            "건축공학과","기계항공정보융합공학부","항공우주 및 소프트웨어공학과",
            "교육문화센터","인문대식당","평생교육원","남명학관","기숙사 3동","기숙사 관리동","영어 전용 강의동"
    };
    DataBase data = new DataBase();
    DataClass data2 = new DataClass();
    public static String s = "";
    public int num=0;
    public String j="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        num = data.data.getWholeSize();
        Log.d("mixare",""+num);
        final AutoCompleteTextView edit = (AutoCompleteTextView) findViewById(R.id.edit);
        edit.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, items));

        Button sbutton = (Button)findViewById(R.id.SearchButton);
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = edit.getText().toString();
                value();
                if(s.equals("")){
                    Toast.makeText(getApplicationContext(),R.string.Search_Error,Toast.LENGTH_SHORT).show();
                }else if(j.equals("-1")){
                    Toast.makeText(getApplicationContext(),R.string.Search_Error,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),NaverMapActivity.class);
                    intent.putExtra("num",j);
                    intent.putExtra("return",2);
                    s="";
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void numbersearch(String input, int i){
        int s2 = Integer.parseInt(input);
        if(data.data.WholeList.get(i).id != 0) {
            //Log.d("FNC", "" + String.valueOf(data.data.WholeList.get(i).id) + "," + s);
            if (input.equals(String.valueOf(data.data.WholeList.get(i).id))) {
                Log.d("FAL", "" + data.data.WholeList.get(i).id + "," + s);
                j = data.data.WholeList.get(i).NUM;
            }
        }
    }

    private String value() {
        data.Initialize();
        for (int i = 0; i < num; i++) {
            try{
                numbersearch(s,i);
            }catch (NumberFormatException e){
                if (s.equals("101동")||s.equals("102동")||s.equals("151동")||s.equals("201동")||s.equals("251동")||s.equals("252동")||s.equals("301동")||s.equals(
                        "302동")||s.equals("303동")||s.equals("351동")||s.equals("352동")||s.equals(
                        "353동")||s.equals("354동")||s.equals("401동")||s.equals("402동")||s.equals("403동")||s.equals("404동")||s.equals("405동")||s.equals(
                        "406동")||s.equals("407동")||s.equals("416동")||s.equals("451동")||s.equals("452동")||s.equals(
                        "453동")||s.equals("454동")||s.equals("455동")||s.equals("456동")||s.equals("457동")||s.equals(
                        "458동")||s.equals("459동")||s.equals("501동")||s.equals("502동")||s.equals("503동")||s.equals("504동")||s.equals(
                        "505동")||s.equals("310동")){
                    int a = s.indexOf('동');
                    String s2 = s.substring(0,a);
                    numbersearch(s2,i);
                } else if (s.equals("공과대학 본부") || s.equals("융합과학기술대학원") || s.equals("토지주택대학원") || s.equals("어울마루") || s.equals("산업정보공학과") || s.equals("산업공학과") || s.equals("산업시스템공학부")) {
                    j = "0"; //401
                } else if (s.equals("나노신소재공학부(금속재료,세라믹)") || s.equals("그린에너지융합연구소")) {
                    j = "1"; //402
                } else if (s.equals("기계공학부") || s.equals("공학교육혁신센터")) {
                    j = "2"; //403
                } else if (s.equals("나노신소재공학부(고분자,생명화학)") || s.equals("고분자공학과") || s.equals("생명화학공학과") || s.equals("항공기부품기술연구소")) {
                    j = "3"; //404
                } else if (s.equals("반도체공학과") || s.equals("전기공학과") || s.equals("전자공학과") || s.equals("제어계측공학과")) {
                    j = "4"; //405
                } else if (s.equals("건축학과") || s.equals("도시공학과") || s.equals("토목공학과")) {
                    j = "5"; //406
                } else if (s.equals("건축공학과") || s.equals("기계항공정보융합공학부") || s.equals("항공우주 및 소프트웨어공학과")) {
                    j = "6"; //407
                } else if (s.equals("공대부속공장")) {
                    j = "7"; //416
                } else if (s.equals("물리학과") || s.equals("미생물학과") || s.equals("생물학과") || s.equals("물리교육과") || s.equals("생물교육과")) {
                    j = "8"; //351
                } else if (s.equals("생화학과") || s.equals("화학과") || s.equals("화학교육과")) {
                    j = "9"; //352
                } else if (s.equals("식품영양학과“)") || s.equals("의류학과")) {
                    j = "10"; //353
                } else if (s.equals("수학과") || s.equals("정보통계학과") || s.equals("지구환경과학과") || s.equals("지질전시실") || s.equals("자생식물보존실습장및온실")) {
                    j = "11"; //354
                } else if (s.equals("지진관측소")) {
                    j = "12"; //지진관측소
                } else if (s.equals("과학영재교육관")) {
                    j = "13"; //과학영재교육관
                } else if (s.equals("국어국문학과") || s.equals("독어독문학과") || s.equals("러시아학과") || s.equals("불어불문학과") || s.equals("사학과") || s.equals("영어영문학과") || s.equals("중어중문학과") || s.equals("철학과") || s.equals("한문학과") || s.equals("민속무용학과") || s.equals("인문대도서관") || s.equals("인문학연구소") || s.equals("해외지역연구센터")) {
                    j = "14"; //101
                } else if (s.equals("민속무용학과")) {
                    j = "15"; //102
                } else if (s.equals("법학과") || s.equals("법학연구소") || s.equals("법률상담실") || s.equals("법학도서관")) {
                    j = "16"; //251
                } else if (s.equals("대경학술관")) {
                    j = "17"; //252
                } else if (s.equals("교육학과") || s.equals("영어교육과") || s.equals("일어교육과") || s.equals("수학교육과") || s.equals("교육대학원") || s.equals("사범대학부설교육연수원") || s.equals("교육연구원") || s.equals("중등교육연구센터") || s.equals("국어문화원")) {
                    j = "18"; //301
                } else if (s.equals("사범대학 실습동")) {
                    j = "19"; //302
                } else if (s.equals("학생식당") || s.equals("교직원식당")|| s.equals("교육문화센터")|| s.equals("인문대식당")|| s.equals("평생교육원")) {
                    j = "20"; //303
                } else if (s.equals("윤리교육과") || s.equals("국어교육과") || s.equals("일반사회교육과") || s.equals("역사교육과") || s.equals("지리교육과") || s.equals("유아교육과") || s.equals("체육교육과")) {
                    j = "21"; //303
                } else if (s.equals("자연과학대학 학장실") || s.equals("자연과학대학 행정실")) {
                    j = "22"; //451
                } else if (s.equals("산림환경자원학과") || s.equals("식품자원경제학과") || s.equals("농학과") || s.equals("응용생물학과")) {
                    j = "23"; //452
                } else if (s.equals("식품공학과") || s.equals("환경생명화학과")) {
                    j = "24"; //453
                } else if (s.equals("동물생명과학과") || s.equals("축산학과")) {
                    j = "25"; //454
                } else if (s.equals("원예학과")) {
                    j = "26"; //455
                } else if (s.equals("농생6호관")) {
                    j = "27"; //456
                } else if (s.equals("공작동")) {
                    j = "28"; //457
                } else if (s.equals("농업대학 실습동")) {
                    j = "29"; //458
                } else if (s.equals("두레홀") || s.equals("누리홀")) {
                    j = "30"; //459
                } else if (s.equals("농대카페테리아")) {
                    j = "31";
                } else if (s.equals("농대부속농장")) {
                    j = "32";
                } else if (s.equals("경영학과") || s.equals("경영정보학과") || s.equals("국제통상학과") || s.equals("회계학과") || s.equals("산업경영학과") || s.equals("경영대학원")) {
                    j = "33"; //201
                } else if (s.equals("수의1호관")) {
                    j = "34"; //501
                } else if (s.equals("동물병원1")) {
                    j = "35"; //502
                } else if (s.equals("수의3호관")) {
                    j = "36"; //503
                } else if (s.equals("수의4호관")) {
                    j = "37"; //504
                } else if (s.equals("동물병원2")) {
                    j = "38"; //505
                } else if (s.equals("BNIT산학협력관/약학대학") || s.equals("산학협력단") || s.equals("공학연구원") || s.equals("교육연구원(다문화교육센터)") || s.equals("기초과학연구소") || s.equals("농업생명과학연구원") || s.equals("사회과학연구원") || s.equals("약학연구소") || s.equals("여성연구소") || s.equals("인권사회발전연구소") || s.equals("EU연구소") || s.equals("국책사업단") || s.equals("약학대학") || s.equals("혁신연구실") || s.equals("장비실") || s.equals("식물배양실") || s.equals("제약실습공장")) {
                    j = "39"; //28동
                } else if (s.equals("경제학과") || s.equals("사회복지학과") || s.equals("사회학과") || s.equals("심리학과") || s.equals("정치외교학과") || s.equals("행정학과") || s.equals("행정대학원") || s.equals("사회과학연구원")) {
                    j = "40"; //151동
                } else if (s.equals("총장실") || s.equals("교무처") || s.equals("학생처") || s.equals("기획처") || s.equals("사무국") || s.equals("대외협력본부") || s.equals("입학본부") || s.equals("대학원")) {
                    j = "41"; //1동
                } else if (s.equals("도서관") || s.equals("박물관") || s.equals("매점")||s.equals("도서관매점")) {
                    j = "42"; //2
                } else if (s.equals("학생회관") || s.equals("인재개발원")) {
                    j = "43"; //3
                } else if (s.equals("출판부") || s.equals("전산원")) {
                    j = "44"; //4
                } else if (s.equals("체육교육과") || s.equals("생활체육연구센터")) {
                    j = "45"; //5
                } else if (s.equals("GNU어린이집")) {
                    j = "46"; //6
                } else if (s.equals("국제문화회관")) {
                    j = "47"; //7
                } else if (s.equals("스포츠컴플렉스")) {
                    j = "48"; //8
                } else if (s.equals("정문")) {
                    j = "49"; //21
                } else if (s.equals("남문")) {
                    j = "50"; //22
                } else if (s.equals("북문")) {
                    j = "51"; //23
                } else if (s.equals("교양학관")) {
                    j = "52"; //24
                } else if (s.equals("창업보육센터")) {
                    j = "53"; //25
                } else if (s.equals("남문운동장")) {
                    j = "54"; //26
                } else if (s.equals("농업생명공동실험실습관")) {
                    j = "55"; //27
                } else if (s.equals("원장실") || s.equals("파이오니어오디토리엄") || s.equals("일본문화연구센터") || s.equals("국제어학원")) {
                    j = "56"; //29
                } else if (s.equals("컴퓨터과학과") || s.equals("도서관학습과") || s.equals("제1열람실") || s.equals("제2열람실") || s.equals("학생상담센터")) {
                    j = "57"; //30
                } else if (s.equals("기록관") || s.equals("남명홀") || s.equals("경남문화연구원") || s.equals("경남문화연구센터") || s.equals("남명학연구소") || s.equals("국제지역연구원") || s.equals("통일평화연구센터") || s.equals("인문학연구소")) {
                    j = "58"; //31
                } else if (s.equals("학군단") || s.equals("ROTC")) {
                    j = "59"; //32
                } else if (s.equals("박물관") || s.equals("고문헌도서관")) {
                    j = "60"; //33
                } else if (s.equals("예절교육원 본관")) {
                    j = "61"; //34
                } else if (s.equals("정문-InformationCenter")) {
                    j = "62"; //41
                } else if (s.equals("야외공연장")) {
                    j = "63"; //42
                } else if (s.equals("정도회") || s.equals("K.T.C") || s.equals("일레븐") || s.equals("화랑회") || s.equals("청학") || s.equals("맥") || s.equals("FC BB") || s.equals("대금소리") || s.equals("팀레이지") || s.equals("V.E.C") || s.equals("점보")) {
                    j = "64"; //43
                } else if (s.equals("바로바로서비스센터") || s.equals("목공실")) {
                    j = "65"; //44
                } else if (s.equals("교직원테니스장")) {
                    j = "66"; //49
                } else if (s.equals("송신탑")) {
                    j = "67"; //50
                } else if (s.equals("기숙사행정실")) {
                    j = "68"; //61
                } else if (s.equals("기숙사구관")) {
                    j = "69"; //62
                } else if (s.equals("게스트하우스")) {
                    j = "70"; //68
                } else if (s.equals("기숙사영어캠퍼스")) {
                    j = "71"; //69
                } else if (s.equals("LG개척관")) {
                    j = "72"; //70
                } else if (s.equals("기숙사 8동")) {
                    j = "73"; //71
                } else if (s.equals("기숙사 9동")) {
                    j = "74"; //72
                } else if (s.equals("기숙사 10동")) {
                    j = "75"; //73
                } else if (s.equals("기숙사 11동")) {
                    j = "76"; //74
                } else if (s.equals("부설중학교")) {
                    j = "77"; //81
                } else if (s.equals("부설고등학교")) {
                    j = "78"; //82
                } else if (s.equals("총동아리방")) {
                    j = "79"; //남문동아리방
                } else if (s.equals("음악교육과") || s.equals("미술교육과")) {
                    j = "80"; //304동
                } else if (s.equals("통학버스승강장")) {
                    j = "81";
                } else if (s.equals("NH농협")) {
                    j = "83";
                } else if (s.equals("남문주차장")) {
                    j = "84";
                } else if (s.equals("기숙사 4동")) {
                    j = "85";
                } else if (s.equals("기숙사 5동")) {
                    j = "86";
                } else if (s.equals("아람관")) {
                    j = "87";
                } else if (s.equals("체육관")) {
                    j = "88";
                } else if (s.equals("대운동장")) {
                    j = "89";
                } else if (s.equals("남명학관")) {
                    j = "90";
                } else if (s.equals("기숙사 3동")) {
                    j = "91";
                } else if (s.equals("기숙사 관리동")) {
                    j = "92";
                } else if (s.equals("영어 전용 강의동")) {
                    j = "93";
                }

            }
        }return j;
    }
    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Context ctx;
            ctx = this;
            startActivity(new Intent(ctx, MenuActivity.class));
            finish();
        }
        return false;
    }

}
