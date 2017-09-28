package com.akura.retrieval.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


public class DBConnection {


    public static MongoDatabase Connect() {

        MongoClientURI connectionString = new MongoClientURI("mongodb://nilesh:akura@ds147544.mlab.com:47544/akura");
        MongoClient mongoClient = new MongoClient(connectionString);

        MongoDatabase database = mongoClient.getDatabase("akura");

        return  database;
    }
}
