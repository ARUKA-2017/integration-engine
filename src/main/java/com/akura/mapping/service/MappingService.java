package com.akura.mapping.service;

import com.akura.config.Config;
import com.akura.integration.service.IntegrateService;
import com.akura.mapping.models.JsonResponse;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import com.google.gson.Gson;
import org.apache.jena.ontology.OntModel;
import spark.Response;

import static com.akura.utility.OntologyWriter.fileResourceManager;

public class MappingService {

    public String map(String body, Response res) {
        JsonResponse jsonResponse = new Gson().fromJson(body, JsonResponse.class);
        OntModel m = OntologyReader.getOntologyModel(Config.OWL_DYNAMIC_EMPTY_FILENAME);
        jsonResponse.setAll(m);


        System.out.println("Mapping Completed");

        //merge ontology
        IntegrateService integrateService = new IntegrateService(m);
        Boolean bool = integrateService.integrate();

        if (bool) {
            //TODO save files sepeartedly
            OntologyWriter.writeOntology(m, fileResourceManager.getFilePath("ontology/demo_test_map_ontology_json.owl"));

            res.status(200);
            return "{ status: 'success', message : 'Successfully mapped and merged'}";
        } else {
            res.status(500);
            return "{ status: 'error', message : 'Ontology was already merged. Duplicate Data Instance'}";
        }


    }
}
