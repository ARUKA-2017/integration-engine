package com.akura.retrieval.service;

import com.akura.adaptor.resolver.EntityNameResolver;
import com.akura.config.Config;
import com.akura.retrieval.db.DBConnection;
import com.akura.retrieval.response.EntityListResponse;
import com.akura.retrieval.response.IRetrievalResponse;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.InstanceChecker;
import com.akura.utility.OntologyReader;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;

import org.apache.jena.ontology.OntModel;

import org.bson.Document;
import spark.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public class ProductService {

    OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

    public IRetrievalResponse searchProduct(String search, Response res, boolean isHash) {

        MongoDatabase database = DBConnection.Connect();

        res.type("Application/JSON");

        String mobileName = EntityNameResolver.getMobileName(search);

        if (mobileName != null) {

            if (InstanceChecker.isEntityExists(m, mobileName, isHash)) {
                return new SingleResponse(m, search, isHash);
            } else {

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

                return new EntityListResponse(m, search);
            }
        } else {
            return new EntityListResponse(m, search);
        }
    }
}
