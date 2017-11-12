package com.gnuarmap.data.convert;

import android.graphics.Color;
import com.gnuarmap.data.convert.DataClass;
/**
 * Created by nazunamoe on 2017-11-11.
 */

public class SampleData {
    public DataClass data = new DataClass();

    public void Initialize(){
        data.addItem("훼미리아파트","http://m.naver.com",35.263230,128.639500, "GREEN",25);
        data.addItem("유성빌라","http://m.naver.com",35.262838,128.639119, "RED",25);
        data.addItem("남성빌라","http://m.naver.com",35.262552,128.638582, "YELLOW",25);
        data.addItem("상아리더스타운","http://m.naver.com",35.263049, 128.640294, "PINK",25);
        data.addItem("금강목련아파트","http://m.naver.com",35.262593,128.639853, "BLACK",25);
        data.addItem("CU편의점","http://m.naver.com",35.263136,128.639374, "GREEN",25);
       // 샘플 데이터, 위 방식대로 학교의 건물들을 모두 등록한다.
    }
}
