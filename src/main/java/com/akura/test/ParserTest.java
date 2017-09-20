package com.akura.test;


import com.akura.parser.models.Ontology;
import com.akura.parser.service.EntityService;
import com.akura.parser.service.ValidatorService;
import com.akura.utility.FileResourceManager;
import com.google.gson.Gson;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class ParserTest {

    public static void main(String[] args) {

        FileResourceManager fileResourceManager = new FileResourceManager();
        Gson gson = new Gson();
        Map json = null;
        try {
            json = gson.fromJson((Reader) new FileReader(fileResourceManager.getFilePath("parse-test.json")), (Type) Object.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // validate the format
        if (ValidatorService.validateJSON(json)) {

            new EntityService(ValidatorService.getEntities(json));
            Ontology.saveOntologyFile();

        } else {
            // todo make this more generic later on
            System.out.println("ERROR: Invalid JSON TYPE");
        }


        /**
         *
         *  1. validate the json format --> should have entities and relationships
         *  2. get entities
         *  3. for each entity ->
         *      3.1 get complex properties --> iterate for 3
         *      3.2 get the atomic properties list
         *      3.3 check whether a class exists for that set of properties --> for this, we need to maintain a class registry
         *      3.4 if no class exists, create a new class and put the object in @ID sessionID + hash of object path
         *      3.5 if the class exists, push the object to the existing class @ID sessionID + hash of object path
         *      3.5 create a registry on where each object was saved in ontology along with its object path to be used with relatioships
         *
         */


    }
}
