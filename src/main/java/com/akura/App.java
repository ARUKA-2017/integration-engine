package com.akura;

import com.akura.logger.FileLogger;
import com.akura.mapping.service.MappingService;
import com.akura.parser.Parser;
import com.akura.retrieval.service.ProductService;
import com.akura.retrieval.service.SparkMiddleware;
import com.google.gson.Gson;

import spark.ModelAndView;
import spark.Spark;
import spark.template.jade.JadeTemplateEngine;

import java.io.File;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Main application startup class with spark
 */
public class App {

    public static void main(String[] args) {
        Gson gson = new Gson();
        ProductService ps = new ProductService();
        MappingService mp = new MappingService();
        Spark.staticFileLocation("/public");

        FileLogger.Log("com.akura.integration-engine Application Started",FileLogger.TYPE_PROCESS, FileLogger.DEST_ENTIRE_PROCESS_LOG);

        get("/json2owl", (req, res) -> new ModelAndView(new HashMap<>(), "j2owl"), new JadeTemplateEngine());

        SparkMiddleware.enableCORS("*", "POST, GET, OPTIONS, PUT, DELETE",
                "Content-Type, x-xsrf-token, content-Type, X-Auth-Token, Origin, Authorization");

        get("/search", (req, res) -> gson.toJson(ps.searchProduct(req.queryParams("search"), res, false, mp)));

        post("/generate-vowl", (req, res) -> {
            String uid = Parser.parseFromJsonString(req.body());
            return uid;
        });

        // logging
        get("/search/:id", (req, res) -> gson.toJson(ps.searchProduct(req.params(":id"), res, true, mp)));

        post("/update-ontology", (req, res) -> gson.toJson(mp.map(req.body(), res)));
        post("/update-ontology-adaptor", (req, res) -> gson.toJson(mp.useAdaptor(req.body(), res)));

//      get("/test", (req, res) -> mp.mongoLoader(res));
    }
}
