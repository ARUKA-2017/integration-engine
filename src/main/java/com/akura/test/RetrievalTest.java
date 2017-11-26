package com.akura.test;

import com.akura.adaptor.resolver.EntityNameResolver;
import com.akura.config.Config;
import com.akura.retrieval.db.DBConnection;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.OntologyReader;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import org.apache.jena.ontology.OntModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class used to the retrieval component.
 */
public class RetrievalTest {

    public static void main(String[] args) {

//        OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);
//        SingleResponse resp = new SingleResponse(m, "Samsung Galaxy S5", false);

//        BasicDBObject whereQuery = new BasicDBObject();
//        whereQuery.put("name", new BasicDBObject("$regex", ".*samsung galaxy s5.*")
//                .append("$options", "i"));
//
//
//        List<String> consList = (ArrayList<String>) DBConnection.Connect().getCollection("phone_pros_and_cons").find(whereQuery).first().get("cons");
//
//        for (String con : consList) {
//            System.out.println(con);
//        }

        // EntityNameResolver.dataExtractionResolve("Apple iPhone 7");

    }
}
