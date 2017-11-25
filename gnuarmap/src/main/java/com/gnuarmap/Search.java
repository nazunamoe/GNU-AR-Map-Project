package com.gnuarmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import static com.gnuarmap.NaverMapActivity.db;

public class Search extends AppCompatActivity {

    String[] items = {"대학본부","중앙도서관","중도","중식","학생회관","인재개발원","체육관","GNU어린이집","어린이집","문화회관",
    "국제문화회관","스포츠컴플렉스","정문","남문","후문","북문","교양동","교양학관","창업보육센터","공동실험실습관","BNIT산학협력관","약학대학",
    "약대","국제어학원","컴퓨터과학관","남명학관","학군단","고문헌도서관","박물관","예절교육관","예절교육원","InformationCenter",
            "야외공연장","야공","대운동장","파워플랜트","교직원테니스장","송신탑","기숙사 행정실","기숙사구관","게스트하우스","영캠",
    "영어캠퍼스","LG개척관"};

    public static String s = "";
    public int num=0;
    public String j="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        num = db.Count();

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
                    Toast.makeText(getApplicationContext(),"검색할 건물번호 또는 건물명을 입력하세요",Toast.LENGTH_SHORT).show();
                }else if(j.equals("-1")){
                 Toast.makeText(getApplicationContext(),"정확한 건물명 또는 건물번호를 입력하세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),NaverMapActivity.class);
                    intent.putExtra("num",j);
                    startActivity(intent);
                    s="";
                    finish();
                }
            }
        });
    }

    private String value(){
        for(int i=0;i<num;i++){
            if(s.equals(db.Title(i))){
                j=Integer.toString(i);
            }
        }
        return j;
    }
}
