package com.gnuarmap;

/**
 * Created by nazunamoe on 2017-11-25.
 */

public class FilteringState {
    private static FilteringState instance;

    // Global variable
    public boolean Business;
    public boolean Engnieering;
    public boolean Dormitory;
    public boolean ETC;
    public boolean Agriculture;
    public boolean University;
    public boolean Club;
    public boolean Law;
    public boolean Education;
    public boolean Social;
    public boolean Veterinary;
    public boolean Leisure;
    public boolean Humanities;
    public boolean Science;
    public boolean Door;

    private FilteringState(){}

    public static synchronized FilteringState getInstance(){
        if(instance==null){
            instance=new FilteringState();
        }
        return instance;
    }
}
