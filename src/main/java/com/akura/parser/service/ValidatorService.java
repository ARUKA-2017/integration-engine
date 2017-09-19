package com.akura.parser.service;



import com.akura.parser.config.Config;

import java.util.Map;

public class ValidatorService {
    public static Boolean validateJSON(Map json) {

        Boolean relatioship = false;
        Boolean entity = false;

        if(json.get(Config.entities) != null){
            entity = true;
        }

        if(json.get(Config.relationships) != null){
            relatioship = true;
        }
        return (relatioship && entity);
    }




    public static Map getEntities(Map json){
        return (Map) json.get(Config.entities);
    }
}
