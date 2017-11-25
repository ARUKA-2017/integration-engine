package com.akura.retrieval.service;

import com.akura.adaptor.resolver.EntityNameResolver;
import com.akura.retrieval.db.DBConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackgroundService extends Thread {

    private String searchString;

    public BackgroundService(String search) {
        this.searchString = search;
    }

    public void run() {

        MongoDatabase database = DBConnection.Connect();

        String mobileName = EntityNameResolver.getMobileName(searchString);

        if (mobileName != null) {

            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("name", mobileName);

            try {
                long searchCount = database.getCollection("search_registry").count(whereQuery);

                if (searchCount == 0) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Document document = new Document();
                    document.put("name", mobileName);
                    document.put("date", dateFormat.format(date));

                    database.getCollection("search_registry").insertOne(document);
                }
            } catch (Exception e) {
                System.out.println("Exception : " + e);
            }
        }
    }
}
