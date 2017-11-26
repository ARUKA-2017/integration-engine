package com.akura.parser;

import com.akura.parser.service.EntityService;
import com.akura.parser.vowl.VowlConverter;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Class representing a Parser.
 */
public class Parser {

    /**
     * Method used to parse from JSON string.
     *
     * @param json - JSON to be parsed.
     * @return - string value.
     */
    public static String parseFromJsonString(String json) {

        Gson gson = new Gson();
        Map jsonObj = gson.fromJson(json, (Type) Object.class);
        EntityService ent = new EntityService(jsonObj);
        ent.saveOntology();

        // vowl convert
        return VowlConverter.convert();
    }
}
