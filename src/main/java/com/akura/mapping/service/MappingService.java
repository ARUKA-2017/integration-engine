package com.akura.mapping.service;

import com.akura.adaptor.AdaptorService;
import com.akura.adaptor.input.NLUOutput;
import com.akura.adaptor.input.NLURequest;
import com.akura.config.Config;
import com.akura.integration.service.IntegrateService;
import com.akura.mapping.models.JsonResponse;
import com.akura.mapping.models.ServiceResponse;
import com.akura.utility.Log;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import com.google.gson.Gson;
import org.apache.jena.ontology.OntModel;
import spark.Response;

import static com.akura.utility.OntologyWriter.fileResourceManager;

public class MappingService {
    Log log = new Log();

    public ServiceResponse map(String body, Response res) {

        log.write("Mapping Request from HTTP");

        res.type("Application/JSON");
        OntModel m = OntologyReader.getOntologyModel(Config.OWL_DYNAMIC_EMPTY_FILENAME);
        try {
            JsonResponse jsonResponse = new Gson().fromJson(body, JsonResponse.class);
            jsonResponse.setAll(m);
        } catch (Exception e) {
            e.printStackTrace();
            log.write("Invalid JSON. There was a parse error. Please check the format again");
            return new ServiceResponse("error", "Invalid JSON. There was a parse error. Please check the format again");
        }


        log.write("Data : " + body);
        log.write("Mapping Completed");

        //merge ontology
        IntegrateService integrateService = new IntegrateService(m);
        Boolean bool = integrateService.integrate();

        if (bool) {
            //TODO save files sepeartedly
            OntologyWriter.writeOntology(m, fileResourceManager.getFilePath("ontology/demo_test_map_ontology_json.owl"));

            res.status(200);
            return new ServiceResponse("success", "Successfully mapped and merged");
        } else {
            res.status(500);
            return new ServiceResponse("error", "Ontology was already merged. Duplicate Data Instance");
        }


    }

    public ServiceResponse useAdaptor(String body, Response res) {
        log.write("Mapping Request from HTTP via the Adaptor");

        res.type("Application/JSON");

        NLURequest nluRequest = null;
        try {
            nluRequest = new Gson().fromJson(body, NLURequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.write("Invalid JSON. There was a parse error. Please check the format again");
            return new ServiceResponse("error", "Invalid JSON. There was a parse error. Please check the format again");
        }

        for (NLUOutput nlu : nluRequest.data) {

            OntModel m = OntologyReader.getOntologyModel(Config.OWL_DYNAMIC_EMPTY_FILENAME);

            nlu.replaceIdentifiers();
            AdaptorService adopt = new AdaptorService(nlu);
            adopt.convert();
            adopt.target.setAll(m);

            log.write("Data : " + body);
            log.write("Mapping Completed");

            //merge ontology
            IntegrateService integrateService = new IntegrateService(m);
            Boolean bool = integrateService.integrate();

            if (bool) {
                //TODO save files sepeartedly
                OntologyWriter.writeOntology(m, fileResourceManager.getFilePath("ontology/demo_test_map_ontology_json.owl"));
                log.write("Successfully mapped and merged");
            } else {
                log.write("Ontology was already merged. Duplicate Data Instance");

            }
        }

        res.status(200);
        return new ServiceResponse("success", "Successfully mapped and merged");
    }
}
