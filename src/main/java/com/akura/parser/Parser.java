package com.akura.parser;

import com.akura.parser.models.Ontology;
import com.akura.parser.service.EntityService;
import com.akura.parser.service.ValidatorService;
import com.akura.parser.vowl.VowlConverter;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class Parser {

    public static void parseFromJsonString(String json){

        Gson gson = new Gson();
        Map jsonObj = gson.fromJson( json , (Type) Object.class);
        new EntityService(jsonObj);
        Ontology.saveOntologyFile();
        // todo make this non static
        Ontology.m = null;

        // vowl convert
        VowlConverter.convert();

    }
}
