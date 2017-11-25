package com.gnuarmap.data.convert;

import android.graphics.Color;
import com.gnuarmap.data.convert.DataClass;
/**
 * Created by nazunamoe on 2017-11-11.
 */

public class SampleData {
    public DataClass data = new DataClass();

    public void Initialize(){
        data.addItem("훼미리아파트","http://m.naver.com",35.263230,128.639500, "GREEN",25,"default");
        data.addItem("유성빌라","http://m.naver.com",35.262838,128.639119, "RED",25,"default");
        data.addItem("남성빌라","http://m.naver.com",35.262552,128.638582, "YELLOW",25,"default");
        data.addItem("상아리더스타운","http://m.naver.com",35.263049, 128.640294, "PINK",25,"default");
        data.addItem("금강목련아파트","http://m.naver.com",35.262593,128.639853, "BLACK",25,"default");
        data.addItem("CU편의점","http://m.naver.com",35.263136,128.639374, "GREEN",25,"default");

        data.addItem("도서관 매점","http://m.naver.com",35.1537364,128.1015611, "GREEN",13,"default");
        data.addItem("야외공연장","http://m.naver.com",35.1521609,128.1029144, "GREEN",13,"CAFE");
        data.addItem("경영대학","http://m.naver.com",35.1538200,128.099861, "GREEN",13,"engine");
        data.addItem("학생회관","http://m.naver.com",35.1538205,128.0973951,"GREEN",12,"student");
        // 샘플 데이터, 위 방식대로 학교의 건물들을 모두 등록한다.
    }
}
