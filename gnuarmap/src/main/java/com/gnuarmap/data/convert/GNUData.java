package com.gnuarmap.data.convert;

import android.graphics.Color;
import com.gnuarmap.data.convert.DataClass;
/**
 * Created by nazunamoe on 2017-11-11.
 */

public class GNUData {
    public DataClass data = new DataClass();

    public void Initialize(){
        data.addItem("0","아파트","http://m.naver.com",35.263230,128.639500,20, "GREEN");
       // 샘플 데이터, 위 방식대로 학교의 건물들을 모두 등록한다.
    }
}
