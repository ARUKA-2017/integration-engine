package com.akura.mapping.service;

import com.akura.adaptor.AdaptorService;
import com.akura.adaptor.input.Entity;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Set;

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
        HashMap<String,Entity> entityRegistry = new HashMap<>();

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

                // add entities to hashmap to use for mongo extraction
                for(Entity ent: nlu.specificationDto.relativeEntityList){

                   if(entityRegistry.get(ent.id) == null){

                       entityRegistry.put(ent.id,ent);
                   }
                }
                log.write("Successfully mapped and merged");
            } else {
                System.out.println("Ontology was already merged. Duplicate Data Instance");
                log.write("Ontology was already merged. Duplicate Data Instance");

            }
        }



        // after everything, insert data in mongodb as well
        mongoLoader(entityRegistry,res);



        res.status(200);
        return new ServiceResponse("success", "Successfully mapped and merged");
    }

    public void mongoLoader( HashMap<String,Entity> entityRegistry,Response res){

        Set keys = entityRegistry.keySet();
        for(Object key : keys) {
            mongoLoaderSingleEntity(entityRegistry.get(key.toString()).text,res);
        }
    }



    public void mongoLoaderSingleEntity(String name, Response res){

        System.out.println("retrieving for "+ name);
        URL url = null;
        try {
            url = new URL("http://localhost:3000/phone/"+  URLEncoder.encode(name, "UTF-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            String contentType = con.getHeaderField("Content-Type");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(status);
            System.out.println(content.toString());
            in.close();
            map(content.toString(),res);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
