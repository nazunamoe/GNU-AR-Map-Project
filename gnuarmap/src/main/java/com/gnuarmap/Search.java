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

import com.gnuarmap.data.convert.DataBase;

public class Search extends AppCompatActivity {

    String[] items = {"대학본부","중앙도서관","중도","중식","학생회관","인재개발원","체육관","GNU어린이집","어린이집","문화회관",
    "국제문화회관","스포츠컴플렉스","정문","남문","후문","북문","교양동","교양학관","창업보육센터","공동실험실습관","BNIT산학협력관","약학대학",
    "약대","국제어학원","컴퓨터과학관","남명학관","학군단","고문헌도서관","박물관","예절교육관","예절교육원","InformationCenter",
            "야외공연장","야공","대운동장","파워플랜트","교직원테니스장","송신탑","기숙사 행정실","기숙사구관","게스트하우스","영캠",
    "영어캠퍼스","LG개척관"};
    public DataBase database = new DataBase();
    public static String s = "";
    public int num=0;
    public String j="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //database.Initialize();

        setContentView(R.layout.activity_search);
        num = database.data.getSize();

        final AutoCompleteTextView edit = (AutoCompleteTextView) findViewById(R.id.edit);

        edit.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, items));

        Button sbutton = (Button)findViewById(R.id.SearchButton);
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = edit.getText().toString();
                Log.d("mixare",s);
                value();
                if(s.equals("")) {
                    Toast.makeText(getApplicationContext(), "검색할 건물번호 또는 건물명을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),NaverMapActivity.class);
                    intent.putExtra("num",s);
                    startActivity(intent);
                    s="";
                    finish();
                }
            }
        });
    }

    private String value(){
        for(int i=0;i<num;i++){
            if(s.equals(database.data.getData(i).getTitle())){
                j=Integer.toString(i);
            }
        }
        return j;
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Context ctx;
            ctx = this;
            startActivity(new Intent(ctx, MainActivity.class));
            finish();
        }
        return false;
    }

}
