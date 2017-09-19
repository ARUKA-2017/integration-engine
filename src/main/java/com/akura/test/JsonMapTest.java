package com.akura.test;


import com.akura.config.Config;
import com.akura.mapping.models.JsonResponse;
import com.akura.utility.FileResourceManager;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonMapTest {

    public static void main(String[] args) {
        FileResourceManager fileResourceManager = new FileResourceManager();
        Gson gson = new Gson();
        JsonResponse jsonResponse = null;
        try {
            // Staff staff = gson.fromJson(jsonInString, Staff.class);
            jsonResponse = gson.fromJson(new FileReader(fileResourceManager.getFilePath("dynamic.json")), JsonResponse.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // start ontology mapping insert

        OntModel m = OntologyReader.getOntologyModel(Config.OWL_DYNAMIC_EMPTY_FILENAME);

        jsonResponse.setAll(m);

        OntologyWriter.writeOntology(m,fileResourceManager.getFilePath("ontology/demo_test_map_ontology_json.owl"));

        System.out.println("Done");




    }
}
