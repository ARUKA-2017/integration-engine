package com.akura.parser.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Entity {

    public Map simpleTypes;
    public String name;
    public HashMap<String,ArrayList> complexTypes;


    public Entity(String _key){
        simpleTypes = new HashMap();
        complexTypes = new HashMap<>();
        name = _key;
    }

    public void addComplexType(String key, Entity ent){

        if(complexTypes.get(key) != null){
            complexTypes.get(key).add(ent);
        }else{
            ArrayList arr = new ArrayList();
            arr.add(ent);
            complexTypes.put(key,arr );
        }
    }


    public void addSimpleType(String key, Object obj){
        simpleTypes.put(key,obj);
    }


    public void saveToOntology(){

        System.out.println(name);
    }
}
