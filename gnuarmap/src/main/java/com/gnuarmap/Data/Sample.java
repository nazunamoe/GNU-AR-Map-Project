package com.gnuarmap.Data;

import com.gnuarmap.mixare.data.DataClass;

/**
 * Created by nazunamoe on 2017-11-11.
 */

public class Sample {
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


    public Sample(){
        this.Initialize();
    }

    public void getFiltering1(DataClass in, int index){
        in.getData(index);
    }

    public void Initialize(){

        // 샘플 데이터
        data.addItem("0","샘플",
                Engnieering,
                35.262957,128.639452,
                "engine",
                "engnieering",
                nothing,
                401
        );

    }
}
