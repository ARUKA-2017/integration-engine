package com.akura.parser.models;

import java.util.ArrayList;

public class ClassHeirachy {

    public Integer count;
    public ArrayList<String>  matched;
    public ArrayList<String>  unmatched;

    public ClassHeirachy(){

        matched =  new  ArrayList();
        unmatched =  new  ArrayList();
        count = 0;
    }
}
