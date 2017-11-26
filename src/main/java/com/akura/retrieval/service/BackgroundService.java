package com.akura.retrieval.service;

import com.akura.adaptor.resolver.EntityNameResolver;
import com.akura.mapping.service.MappingService;
import com.akura.retrieval.db.DBConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import spark.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackgroundService extends Thread {

    private String searchString;
    private MappingService mappingService;
    private Response response;

    public BackgroundService(String search, MappingService mappingService, Response response) {
        this.searchString = search;
        this.mappingService = mappingService;
        this.response = response;
    }

    public void run() {

        MongoDatabase database = new DBConnection().Connect();

        String mobileName = EntityNameResolver.getMobileName(searchString);

        if (mobileName != null) {

            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("name", mobileName);

            try {
                long searchCount = database.getCollection("search_registry").count(whereQuery);

                if (searchCount == 0) {

                    System.out.println("Request send to data extraction");

                    EntityNameResolver.dataExtractionResolve(mobileName, response, mappingService);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Document document = new Document();
                    document.put("name", mobileName);
                    document.put("date", dateFormat.format(date));

                    database.getCollection("search_registry").insertOne(document);

                    System.out.println("Request finished");
                }
            } catch (Exception e) {
                System.out.println("Exception : " + e);
                e.printStackTrace();
            }
        }
    }
}
