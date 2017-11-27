package com.akura.mapping.service;

import com.akura.adaptor.AdaptorService;
import com.akura.adaptor.input.Entity;
import com.akura.adaptor.input.NLUOutput;
import com.akura.adaptor.input.NLURequest;
import com.akura.config.Config;
import com.akura.integration.service.IntegrateService;
import com.akura.logger.FileLogger;
import com.akura.mapping.models.JsonResponse;
import com.akura.mapping.models.ServiceResponse;
import com.akura.retrieval.db.DBConnection;
import com.akura.utility.Log;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;

import com.google.gson.Gson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.apache.jena.ontology.OntModel;

import spark.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Set;

import static com.akura.utility.OntologyWriter.fileResourceManager;

/**
 * Class representing a MappingService.
 */
public class MappingService {
    Log log = new Log();

    /**
     * Method used to map ontologies.
     *
     * @param body - body of the request containing JSON.
     * @param res  - response of the request.
     * @return - ServiceResponse.
     */
    public ServiceResponse map(String body, Response res) {

        log.write("Mapping Request from HTTP");
        FileLogger.Log("J2OWL Mapping Process Starting",FileLogger.TYPE_TITLE, FileLogger.DEST_J2OWL);

        res.type("Application/JSON");
        OntModel m = OntologyReader.getOntologyModel(Config.OWL_DYNAMIC_EMPTY_FILENAME);
        try {
            JsonResponse jsonResponse = new Gson().fromJson(body, JsonResponse.class);
            jsonResponse.setAll(m);
//            FileLogger.Log(new Gson().toJson(jsonResponse),FileLogger.TYPE_JSON, FileLogger.DEST_J2OWL);
        } catch (Exception e) {
            e.printStackTrace();
            FileLogger.Log("Invalid JSON. There was a parse error",FileLogger.TYPE_CONT, FileLogger.DEST_J2OWL);
            return new ServiceResponse("error", "Invalid JSON. There was a parse error. Please check the format again");
        }



        FileLogger.Log("J2OWL Mapping Completed",FileLogger.TYPE_SUB, FileLogger.DEST_J2OWL);


        log.write("Data : " + body);
        log.write("Mapping Completed");

        //merge ontology
        FileLogger.Log("Merging the new knowledge base with existing knowledge base",FileLogger.TYPE_TITLE, FileLogger.DEST_INTEGRATION);
        IntegrateService integrateService = new IntegrateService(m);
        Boolean bool = integrateService.integrate();

        if (bool) {
            //TODO save files sepeartedly
            OntologyWriter.writeOntology(m, fileResourceManager.getFilePath("ontology/demo_test_map_ontology_json.owl"));

            res.status(200);
            FileLogger.Log("Knowledgebase successfully merged",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            return new ServiceResponse("success", "Successfully mapped and merged");
        } else {
            FileLogger.Log("Knowledgebase was already merged. Duplicate Data Instance",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            res.status(500);
            return new ServiceResponse("error", "Ontology was already merged. Duplicate Data Instance");
        }
    }

    /**
     * Method used to map ontlogies using Adapter.
     *
     * @param body - body of the request.
     * @param res  - response of the request.
     * @return - ServiceResponse.
     */
    public ServiceResponse useAdaptor(String body, Response res) {
        log.write("Mapping Request from HTTP via the Adaptor");

        FileLogger.Log("Mapping Request from  J2OWL", FileLogger.TYPE_TITLE, FileLogger.DEST_J2OWL);

        res.type("Application/JSON");
        HashMap<String, Entity> entityRegistry = new HashMap<>();

        NLURequest nluRequest = null;
        try {
            nluRequest = new Gson().fromJson(body, NLURequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            FileLogger.Log("Invalid JSON", FileLogger.TYPE_SUB, FileLogger.DEST_J2OWL);
            FileLogger.Log(body, FileLogger.TYPE_SUB, FileLogger.DEST_J2OWL);
            log.write("Invalid JSON. There was a parse error. Please check the format again");
            return new ServiceResponse("error", "Invalid JSON. There was a parse error. Please check the format again");
        }

        for (NLUOutput nlu : nluRequest.data) {

            OntModel m = OntologyReader.getOntologyModel(Config.OWL_DYNAMIC_EMPTY_FILENAME);

            nlu.replaceIdentifiers();
            AdaptorService adopt = new AdaptorService(nlu);
            adopt.convert();
            System.out.println(new Gson().toJson(adopt.target));
            FileLogger.Log(new Gson().toJson(adopt.target), FileLogger.TYPE_SUB, FileLogger.DEST_J2OWL);
            if (adopt.mainEntityStatus) {

                adopt.target.setAll(m);

                log.write("Data : " + body);
                log.write("Mapping Completed");
                FileLogger.Log("Dynamic Ontology Mapped and Processed", FileLogger.TYPE_SUB, FileLogger.DEST_J2OWL);

                //merge ontology
                IntegrateService integrateService = new IntegrateService(m);
                Boolean bool = integrateService.integrate();

                if (bool) {
                    //TODO save files sepeartedly
                    FileLogger.Log("Dynamic Ontology Successfully Merged", FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
//                    FileLogger.Log(m.toString(), FileLogger.TYPE_JSON, FileLogger.DEST_J2OWL);
                    OntologyWriter.writeOntology(m, fileResourceManager.getFilePath("ontology/demo_test_map_ontology_json.owl"));

                    // add entities to hashmap to use for mongo extraction
                    for (Entity ent : nlu.specificationDto.relativeEntityList) {

                        if (entityRegistry.get(ent.id) == null) {

                            entityRegistry.put(ent.id, ent);
                        }
                    }
                    log.write("Successfully mapped and merged");
                } else {
                    FileLogger.Log("Dynamic Ontology was already merged. Duplicate data instance", FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
                    System.out.println("Ontology was already merged. Duplicate Data Instance");
                    log.write("Ontology was already merged. Duplicate Data Instance");

                }
            } else {
                FileLogger.Log("Ignoring because Main Entity Not Found", FileLogger.TYPE_SUB, FileLogger.DEST_J2OWL);
                System.out.println("Ignoring because Main Entity Not Found!");
            }
        }

        // after everything, insert data in mongodb as well
        mongoLoader(entityRegistry, res);

        res.status(200);
        return new ServiceResponse("success", "Successfully mapped and merged");
    }

    /**
     * Method used for mongoLoader.
     *
     * @param entityRegistry - entity registry.
     * @param res            - response of the request.
     */
    public void mongoLoader(HashMap<String, Entity> entityRegistry, Response res) {

        Set keys = entityRegistry.keySet();
        for (Object key : keys) {
            mongoLoaderSingleEntity(entityRegistry.get(key.toString()).text, res);
        }
    }

    /**
     * Method used mongo loader single entity.
     *
     * @param name - name of the entity.
     * @param res  - response of the request.
     */
    public void mongoLoaderSingleEntity(String name, Response res) {

        Runnable r = () -> {
            MongoDatabase database = new DBConnection().Connect();

            if (name != null) {

                BasicDBObject whereQuery = new BasicDBObject();
                whereQuery.put("name", name);

                try {
                    long searchCount = database.getCollection("search_registry").count(whereQuery);

                    if (searchCount == 0) {


                        FileLogger.Log("Retrieving Data from Node Data Feeder: "+ name,FileLogger.TYPE_SUB, FileLogger.DEST_RETRIEVAL);

                        URL url = null;
                        try {
                            url = new URL("http://35.198.251.53:3002/phone/" + URLEncoder.encode(name, "UTF-8"));
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
                            FileLogger.Log("Data Retrieval Success from Data Feeder for: "+ name,FileLogger.TYPE_SUB, FileLogger.DEST_RETRIEVAL);

                            //TODO: Commented because this line is huge in log
                            //FileLogger.Log(content.toString(),FileLogger.TYPE_JSON, FileLogger.DEST_RETRIEVAL);


                            map(content.toString(), res);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        FileLogger.Log("Data Extraction already requested for: "+ name,FileLogger.TYPE_CONT, FileLogger.DEST_RETRIEVAL);
                    }
                } catch (Exception e) {
                    System.out.println("Exception : " + e);
                    e.printStackTrace();
                }
            }
        };

        new Thread(r).start();

    }
}
